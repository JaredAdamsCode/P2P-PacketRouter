package cs455.overlay.node;

import java.io.*;



import java.net.*;
import java.util.*;

import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;


public class MessagingNode {
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		NumberOfPackets numberOfPackets = new NumberOfPackets();
		CountersAndDiagnostics countersAndDiagnostics = new CountersAndDiagnostics();

		TrackingData trackingData = new TrackingData(countersAndDiagnostics);
		
		
		RoutingTable routingTable = new RoutingTable();
		
		NodeID nodeID = new NodeID();
		int registryPort = Integer.parseInt(args[1]);
		String registryServerAddress = args[0];
		
		PortNumber serverPortNumber = new PortNumber();
		
		EventFactory eventFactory = new EventFactory(nodeID, routingTable, serverPortNumber, numberOfPackets, trackingData);
		
		MessagingNodeServer messagingNodeServer = new MessagingNodeServer(serverPortNumber, eventFactory);
		
		
		Thread serverThread = new Thread(messagingNodeServer);
		serverThread.start();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to CS455 Homework 1!");
		String command;
		
		// Register the node
		Socket registerSocket = new Socket(registryServerAddress, registryPort);
		int type = 1;
		byte[] outputByteArray = new byte[] {};
		
		DataInputStream din = new DataInputStream(registerSocket.getInputStream());
		DataOutputStream dout = new DataOutputStream(registerSocket.getOutputStream());
		
		eventFactory.processEvent(outputByteArray, type, registerSocket, din, dout);
		
		
		do {
			System.out.println("Enter a command: ");
			command = scanner.nextLine();
			
			if(command.equals("exit-overlay")) {
				
				OverlayNodeSendsDeregistration onsd = new OverlayNodeSendsDeregistration(Protocols.OVERLAY_NODE_SENDS_DEREGISTRATION,
						registerSocket.getInetAddress().getLocalHost().getHostAddress().length(), 
						registerSocket.getInetAddress().getLocalHost().getHostAddress(), serverPortNumber.portNumber, nodeID.nodeID);
				
				outputByteArray = onsd.getBytes();
				
				dout.writeInt(outputByteArray.length);
				dout.write(outputByteArray, 0, outputByteArray.length);
				dout.flush();
								
			}
			
			if(command.equals("print-counters-and-diagnostics")) {
				System.out.println("\nPrinting Counters and Diagnostics:\n");
				System.out.println("Packets Sent: " + countersAndDiagnostics.packetsSent);
				System.out.println("Packets Received: " + countersAndDiagnostics.packetsReceived);
				System.out.println("Packets Relayed: " + countersAndDiagnostics.packetsRelayed);
				System.out.println("Sum Sent: " + countersAndDiagnostics.SumSent);
				System.out.println("Sum Received: " + countersAndDiagnostics.SumReceived);
				System.out.println("\n");
			}
			
			
		} while (!command.equals("exit"));
		
		scanner.close();
		
		// Close the program
		System.exit(0);
		
	}
		

}
