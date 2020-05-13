package cs455.overlay.node;

import cs455.overlay.transport.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Registry {
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		int port = Integer.parseInt(args[0]);
		
		RegisteredNodeCount registeredNodeCount = new RegisteredNodeCount();
		RegisteredNode[] nodeRegistry = new RegisteredNode[128];
		
		EventFactory eventFactory = new EventFactory(nodeRegistry, registeredNodeCount);
		RegistryServer registryServer = new RegistryServer(eventFactory, port);
		Thread registryServerThread = new Thread(registryServer);
		registryServerThread.start();
		
		ArrayList<Manifest> manifestList = new ArrayList<Manifest>();
		ArrayList<Integer> nodeIDs = new ArrayList<Integer>();
		
		
		RoutingTableSize routingTableSize = new RoutingTableSize();
		NumberOfPackets numberOfPacketsToSend = new NumberOfPackets();
		
		RegistryHelper registryHelper = new RegistryHelper(nodeRegistry, registeredNodeCount, eventFactory, registryServer,
				routingTableSize, manifestList, nodeIDs, numberOfPacketsToSend);
		
		System.out.println("Welcome to CS455 Homework 1!");
		String command;
		String[] commandWords;
		Scanner scanner = new Scanner(System.in);

		
		do {
			System.out.println("Enter a command: ");
			command = scanner.nextLine();
			commandWords = command.split(" ");

			
			if(commandWords[0].equals("list-messaging-nodes")) {
				registryHelper.printNodeRegistry();
			}

			if(commandWords[0].equals("setup-overlay")) {
				if(commandWords.length > 1) {
					routingTableSize.routingTableSize = Integer.parseInt(commandWords[1]);
					int overlaySize = 0;
					for(RegisteredNode rn : nodeRegistry) {
						if(rn == null) {
							continue;
						}
						overlaySize++;
					}
					routingTableSize.setOverlaySize(overlaySize);
					routingTableSize.setRoutingTableSize();

				}
				registryHelper.registrySendAllManifests();
			}
			if(commandWords[0].equals("start")) {
				if(registryHelper.checkSetup()) {
					numberOfPacketsToSend.numberOfPackets = Integer.parseInt(commandWords[1]);
					registryHelper.registrySendPackets();
					System.out.println("Sending packets. Please stand by - this should take about 20 seconds.");
					Thread.sleep(15000);
					registryHelper.checkTaskFinished();
					registryHelper.requestTrafficSummary();
					Thread.sleep(5000);
					registryHelper.calculateTotals();			

				}
			}
			if(commandWords[0].equals("list-routing-tables")) {
				registryHelper.listRoutingTables();
			}

			
		} while (!command.equals("exit"));
		
		scanner.close();
		
		// Close the program
		System.exit(0);
		
	}
 
}
