package cs455.overlay.wireformats;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.net.*;

import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

import java.io.*;

public class OverlayNodeInitiatesTaskEventHandler {
	
	
	private int numberOfPackets;
	private RoutingTable routingTable;
	private NodeID nodeID;
	
	private int destinationID;
	private int payload;
	
	private Socket relaySocket;
	private DataOutputStream relayOutput;
	
	private String ipAddress;
	private int portNumber;
	
	private Socket registrySocket;
	
	private TrackingData trackingData;
	
	private DataOutputStream registryOut;
	
	public OverlayNodeInitiatesTaskEventHandler(int numPackets, RoutingTable rt, NodeID nodeID, String addr, 
			int pn, Socket socket, TrackingData td, DataOutputStream dout){
		
		this.numberOfPackets = numPackets;
		this.routingTable = rt;
		this.nodeID = nodeID;
		this.ipAddress = addr;
		this.portNumber = pn;
		this.registrySocket = socket;
		this.trackingData = td;
		this.registryOut = dout;
	}
	
	private void randomDestination() {
		Random rand = new Random();
		int range = routingTable.nodeIDs.size();
		
		int randInt = rand.nextInt(range);
		
		while(routingTable.nodeIDs.get(randInt) == nodeID.nodeID) {
			randInt = rand.nextInt(range);
		}
		
		destinationID = routingTable.nodeIDs.get(randInt);
//		System.out.println("Destination is: " + destinationID);
	}
	
	private void randomPayload() {
		Random rand = new Random();
		payload = rand.nextInt();
//		System.out.println("Payload is: " + payload);

	}
	
	private byte[] createPacket() throws IOException {
		int initialNumberOfHops = 0;
		int[] startingTraceArray = new int[initialNumberOfHops];
		
		OverlayNodeSendsData onsd = new OverlayNodeSendsData(Protocols.OVERLAY_NODE_SENDS_DATA, destinationID, 
				nodeID.nodeID, payload, initialNumberOfHops, startingTraceArray);
		
		byte[] packet = onsd.getBytes();
		
		return packet;
	
	}
	
	
	private void findRelaySocket() {
		
		//case 1: destination is in routing table
		for(ConnectedNode cn : routingTable.connectedNodes) {
			if(destinationID == cn.nodeID) {
				relaySocket = cn.getSocket();
				relayOutput = cn.getDout();
//				System.out.println("relaySock is: " + cn.nodeID);

				return;
			}
		}
		
		//case 2: desination NOT in routingTable
		int shortestDistance = 200; // No distance should exceed 128 so this should be good
		
		for(ConnectedNode cn : routingTable.connectedNodes) {
			int distance = destinationID - cn.nodeID;
			if(distance < 0) {
				distance += 128; // add max size of RT
			}
			if(distance < shortestDistance) {
				shortestDistance = distance;
				relaySocket = cn.getSocket();
				relayOutput = cn.getDout();
			}
		}
		
	}
	
	public void sendPackets() throws IOException {
		
		for(int i = 0; i < numberOfPackets; i++) {
			
			randomDestination();
			randomPayload();
			
			byte[] outputByteArray = createPacket();
			
			findRelaySocket();
			
			synchronized(relayOutput) {
				relayOutput.writeInt(outputByteArray.length);
				relayOutput.write(outputByteArray, 0, outputByteArray.length);
				relayOutput.flush();
			}
			
			trackingData.sendTracker.getAndIncrement();
			trackingData.sendSummation.getAndAdd(payload);
			
		}
				
		OverlayNodeReportsTaskFinished onrtf = new OverlayNodeReportsTaskFinished(Protocols.OVERLAY_NODE_REPORTS_TASK_FINISHED, 
				ipAddress.length(), ipAddress, portNumber, nodeID.nodeID);
		
		byte[] finishedMessage = onrtf.getBytes();
				
		registryOut.writeInt(finishedMessage.length);
		registryOut.write(finishedMessage, 0, finishedMessage.length);
		registryOut.flush();
						
		
	}
	
	

}
