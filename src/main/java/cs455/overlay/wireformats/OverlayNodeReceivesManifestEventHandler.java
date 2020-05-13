package cs455.overlay.wireformats;

import java.io.*;
import java.net.*;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class OverlayNodeReceivesManifestEventHandler {

	public RoutingTable routingTable;
	private Socket registrySocket;
	private byte[] data;
	private NodeID nodeID;
	private DataOutputStream dout;
	
	
	public OverlayNodeReceivesManifestEventHandler(RoutingTable rt, Socket socket, byte[] data, NodeID nodeID,
			DataOutputStream dout){
		this.routingTable = rt;
		this.registrySocket = socket;
		this.data = data;
		this.nodeID = nodeID;
		
		this.dout = dout;
	}
	
	public void createMessagingNodeRoutingTable() throws IOException {
		
		RegistrySendsNodeManifest rsnm = new RegistrySendsNodeManifest(routingTable);
		rsnm.readBytes(data);
		
//		System.out.println("We should now have a correct Routing Table. Here is the info: ");
//		System.out.println("Routing Table Size: " + routingTable.routingTableSize);

//		System.out.println("Routing Table OverlaySize: " + routingTable.overlaySize);
//		for(ConnectedNode cn : routingTable.connectedNodes) {
//			System.out.println("Connected Node: " + cn.nodeID);
//		}
//		for(Integer i : routingTable.nodeIDs) {
//			System.out.println("Routing Table Node IDs: " + i);
//
//		}

	}
	
	public void createConnections() throws UnknownHostException, IOException {
		
		boolean successFlag = true;
		byte[] outputByteArray = new byte[] {};


		for(ConnectedNode cn : routingTable.connectedNodes) {
			Socket socket = new Socket(cn.ipAddress, cn.portNumber);
			cn.setSocket(socket);
			
			
			// Note: isConnected returns true forever once connected even if later disconnected
			if(!socket.isConnected()) {
				successFlag = false;
				System.out.println("failed to make connection to setup overlay");
			}
			
			

		}
		
		if(successFlag) {
			outputByteArray = createOverlaySetupStatusMessage(nodeID.nodeID);
		}
		else {
			outputByteArray = createOverlaySetupStatusMessage(-1);

		}
		sendOverlaySetupStatusMessage(registrySocket, outputByteArray, dout);

	}
	
	private byte[] createOverlaySetupStatusMessage(int status) throws IOException {
		
		String infoMessage = "";
		
		if(status == -1) {
			infoMessage = "Overlay setup failed. Could not make connections to all nodes.";
		}
//		else {
//			infoMessage = "Overlay setup succeeded!";
//		}
		
		NodeReportsOverlaySetupStatus nross = new NodeReportsOverlaySetupStatus(Protocols.NODE_REPORTS_OVERLAY_SETUP_STATUS,
				status, infoMessage.length(), infoMessage);
		
		return nross.getBytes();
	}
	
	private void sendOverlaySetupStatusMessage(Socket socket, byte[] data, DataOutputStream dout) throws IOException {
		
//		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
		dout.writeInt(data.length);
		dout.write(data, 0, data.length);
		dout.flush();
		
//		dout.close();
		
	}
	
	
}
