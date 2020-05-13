package cs455.overlay.wireformats;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class OverlayNodeReceivesDataEventHandler {

	private NumberOfPackets numberOfPackets;
	private RoutingTable routingTable;
	private NodeID nodeID;
	
	private int destinationID;
	private int source;
	private int payload;
	private int numberOfHops;
	private int[] trace;
	
	private Socket relaySocket;
	
//	private byte[] packet;
	
	private TrackingData trackingData;
	
	private DataOutputStream dout;
	
	OverlayNodeReceivesDataEventHandler(RoutingTable rt, NumberOfPackets numPacks, NodeID nodeID, int dest, int source, 
			int payload, int numHops, TrackingData td, int[] trace){
		
		this.routingTable = rt;
		this.numberOfPackets = numPacks;
		this.nodeID = nodeID;
		this.destinationID = dest;
		this.source = source;
		this.payload = payload;
		this.numberOfHops = numHops;
		this.trace = trace;
		this.trackingData = td;
		
	}
	
	public void processReceivedData() throws IOException {
		
		// Check if this is the sink
		if(destinationID == nodeID.nodeID) {
			
			//TODO: increment received count
			// TODO: increment receiveSummation
			trackingData.receiveTracker.getAndIncrement();
			trackingData.receiveSummation.getAndAdd(payload);
			
			return;
		}
		
		trackingData.relayTracker.getAndIncrement();
		findRelaySocket();
		updateTrace();
//		createPacket();
		sendPacket();
		
		
	}
	
	private void findRelaySocket() {
		
		//case 1: destination is in routing table
		for(ConnectedNode cn : routingTable.connectedNodes) {
			if(destinationID == cn.nodeID) {
				relaySocket = cn.getSocket();
				dout = cn.getDout();
//				System.out.println("relaySock is: " + cn.nodeID);

				return;
			}
		}
		
		//case 2: destination NOT in routingTable
		int shortestDistance = 200; // No distance should exceed 128 so this should be good
		
		for(ConnectedNode cn : routingTable.connectedNodes) {
			int distance = destinationID - cn.nodeID;
			if(distance < 0) {
				distance += 128; // add max size of RT
			}
			if(distance < shortestDistance) {
				shortestDistance = distance;
//				relaySocket = cn.getSocket();
				dout = cn.getDout();
			}
		}
		
	}
	
	private void updateTrace() {
		numberOfHops += 1;

		int[] newTrace = new int[numberOfHops];
		for(int i = 0; i < trace.length; i++) {
			newTrace[i] = trace[i];
		}
		newTrace[numberOfHops - 1] = nodeID.nodeID;
		trace = newTrace;


	}
	
	private byte[] createPacket() throws IOException {
		
		OverlayNodeSendsData onsd = new OverlayNodeSendsData(Protocols.OVERLAY_NODE_SENDS_DATA, destinationID, 
				source, payload, numberOfHops, trace);
		
//		System.out.println("We are creating a packet to relay. Here is what it has: ");
//		System.out.println("Type: " + onsd.type);
//		System.out.println("destination: " + onsd.destinationID);
//		System.out.println("source: " + onsd.sourceID);
//		System.out.println("payload: " + onsd.payload);
//		System.out.println("hops: " + onsd.numberOfHops);
//		System.out.println("Trace Elements: ");
//
//		for(int x : trace) {
//			System.out.println("Trace: " + x);
//
//		}
//		System.out.println("END OF PACKET\n\n");
//

		byte[] packet = onsd.getBytes();
		return packet;
			
	}
	
	private void sendPacket() throws IOException {
//		DataOutputStream dout = new DataOutputStream(relaySocket.getOutputStream());
//		System.out.println("Packet Len: " + packet.length);
		
		byte[] outputByteArray = createPacket();
		synchronized(dout) {
			dout.writeInt(outputByteArray.length);
			dout.write(outputByteArray, 0, outputByteArray.length);
			dout.flush();
		}

		
//		dout.close();
	}
}
