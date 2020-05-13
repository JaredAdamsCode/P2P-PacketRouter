package cs455.overlay.wireformats;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class RegistrySendsNodeManifest {

	public int type;
	public int routingTableSize;
	public int overlaySize;
	public Manifest manifest;
	public ArrayList<Integer> nodeIDs;
	public int nodeID;
	
	public RoutingTable routingTable;
	
	
	public RegistrySendsNodeManifest(RoutingTable rt) {
		this.routingTable = rt;
	}
	
	public RegistrySendsNodeManifest(int type, int rtSize, int oSize, Manifest mf, ArrayList<Integer> nodeIDs) {
		this.type = type;
		this.routingTableSize = rtSize;
		this.overlaySize = oSize;
		this.manifest = mf;
		this.nodeIDs = nodeIDs;
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(routingTableSize);
		
		for(int i = 0; i < routingTableSize; i++) {
			dout.writeInt(manifest.connectedNodes.get(i).getIdentifier());
			byte[] ipAddress = manifest.connectedNodes.get(i).getAddress().getBytes();
			int ipAddressLength = ipAddress.length;
			dout.writeInt(ipAddressLength);
			dout.write(ipAddress);
			
			dout.writeInt(manifest.connectedNodes.get(i).getPortNumber());
		}
		
		dout.writeInt(overlaySize);
		
		for(int i = 0; i < overlaySize; i++) {
			dout.writeInt(nodeIDs.get(i));
		}
		
		dout.flush();
		marshalledBytes = baOS.toByteArray();
		
		baOS.close();
		dout.close();
		
		return marshalledBytes;
	} 
	
	public void readBytes(byte[] marshalledBytes) throws IOException {
		ByteArrayInputStream baIS = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baIS));
		
		type = din.readInt();

		routingTableSize = din.readInt();
		
		routingTable.routingTableSize = routingTableSize;
		
		for(int i = 0; i < routingTableSize; i++) {
			int tempID = din.readInt();

			int idLength = din.readInt();
			byte[] ids = new byte[idLength];
			din.readFully(ids);
			String address = new String(ids);
			int portNumber = din.readInt();
			

			ConnectedNode connectedNode = new ConnectedNode(portNumber, address, tempID);
			
			routingTable.connectedNodes.add(connectedNode);

		}
		
		overlaySize = din.readInt();
		
		routingTable.overlaySize = overlaySize;
		
		for(int i = 0; i < overlaySize; i++) {
			routingTable.nodeIDs.add(din.readInt());
		}
		
		baIS.close();
		din.close();
		
	}

}
