package cs455.overlay.node;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cs455.overlay.transport.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;



public class RegistryHelper {
	
	private RegisteredNode[] nodeRegistry;
	
	private RegisteredNodeCount registeredNodeCount;
	
	private EventFactory eventFactory;
	
	private RegistryServer registryServer;
	
	private RoutingTableSize routingTableSize;
	
	private ArrayList<Manifest> manifestList;
	
	private ArrayList<Integer> nodeIDs;
	
	private NumberOfPackets numberOfPacketsToSend;
	
	private int totalSent;
	private int totalReceived;
	private int totalRelayed;
	private long totalSumSent;
	private long totalSumReceived;
	
	private DataOutputStream dout;
	
	
	public RegistryHelper(RegisteredNode[] nodeReg, RegisteredNodeCount rnc, EventFactory ef, RegistryServer rs,
			RoutingTableSize rts, ArrayList<Manifest> manifestList, ArrayList<Integer> nodeIDs, 
			NumberOfPackets nopts) {
		
		this.nodeRegistry = nodeReg;
		this.registeredNodeCount = rnc;
		this.eventFactory = ef;
		this.registryServer = rs;
		this.routingTableSize = rts;
		this.manifestList = manifestList;
		this.nodeIDs = nodeIDs;
		this.numberOfPacketsToSend = nopts;
		
		
	}
	
	public void printNodeRegistry() {
		System.out.println("Printing Node Registry:\n");
		for(RegisteredNode rn : nodeRegistry) {
			if (rn == null) {
				continue;
			}
			System.out.println("Identifier: " + rn.getIdentifier());
			System.out.println("IP Address: " + rn.getAddress());
			System.out.println("Hostname: " + rn.getSocket().getInetAddress().getHostName());
			System.out.println("Port Number: " + rn.getPortNumber());
			System.out.println("\n");
			
		}
	}
	
	public void printRegistryCount() {
		System.out.println("Number of registered nodes is: " + registeredNodeCount.registeredNodeCount);
	}
	
	public void registrySendAllManifests() throws IOException {

		CreateRoutingTable createRoutingTable = new CreateRoutingTable(nodeRegistry, routingTableSize.routingTableSize, manifestList, nodeIDs);
		createRoutingTable.createRegisteredNodeList();
		createRoutingTable.createNodeIDs();
		createRoutingTable.createManifests();
		
		for(RegisteredNode rn : nodeRegistry) {
			if(rn == null) {
				continue;
			}
			
			// Get correct Manifest
			Manifest tempManifest = new Manifest();
			for(Manifest mf : manifestList) {
				if(rn.getIdentifier() == mf.indentifier) {
					tempManifest = mf;
					break;
				}
				else {
				}
			}
			
			RegistrySendsNodeManifest rsnm = new RegistrySendsNodeManifest(Protocols.REGISTRY_SENDS_NODE_MANIFEST,
					routingTableSize.routingTableSize, nodeIDs.size(), tempManifest, nodeIDs);
			
			byte[] manifestBytes = rsnm.getBytes();
			
			dout = rn.getDout();
			dout.writeInt(manifestBytes.length);
			dout.write(manifestBytes, 0, manifestBytes.length);
			dout.flush();
						
		}
		
		boolean temp = confirmOverlaySetup();
		
	}
	
	
	public void registrySendPackets() throws IOException {
		
		RegistryRequestsTaskInitiate rrti = new RegistryRequestsTaskInitiate(Protocols.REGISTRY_REQUESTS_TASK_INITIATE,
				numberOfPacketsToSend.numberOfPackets);
		
		byte[] outputByteArray = rrti.getBytes();
		
		for(RegisteredNode rn : nodeRegistry) {
			
			if(rn == null) {
				continue;
			}
			dout = rn.getDout();
			dout.writeInt(outputByteArray.length);
			dout.write(outputByteArray, 0, outputByteArray.length);
			dout.flush();
			
		}
	}
	
	public boolean confirmOverlaySetup() {
		boolean success = true;
		
		long time = System.currentTimeMillis();
		long endTime = time+10000;
		
		do {
			success = true;
			for(RegisteredNode rn : nodeRegistry) {
				if(rn == null) {
					continue;
				}
				if(rn.getAllConnectionsMade() == -1) {
					success = false;
				}
			}
			
		} while(!success && System.currentTimeMillis() < endTime);
		
		
		if(success) {
			System.out.println("Registry now ready to initiate tasks!");
			return true;
		}
		else {
			System.out.println("Overlay setup failed. Please try again.");
		}
		return false;
		
	}
	
	public void checkTaskFinished() {
		boolean success = true;
		
		long time = System.currentTimeMillis();
		long endTime = time+10000;
		
		do {
			success = true;
			for(RegisteredNode rn : nodeRegistry) {
				if(rn == null) {
					continue;
				}
				if(rn.getSummaryReceived() == -1) {
					success = false;
				}
			}
			
		} while(!success && System.currentTimeMillis() < endTime);
		
		if(success) {
			System.out.println("\nAll nodes have reported task finished. Retriving data - please stand by... \n");
		}
		else {
			System.out.println("\nFailed to receive all task finished after waiting for 10 an additional seconds. We will still request the summaries, but please try again.\n");
			
		}
	}
	
	
	
	public boolean checkSetup() {
		for(RegisteredNode rn : nodeRegistry) {
			if(rn == null) {
				continue;
			}
			if(rn.getAllConnectionsMade() ==  -1) {
				System.out.println("Overlay is not setup. Please try again.");
				return false;
			}
		}
		return true;
	}
	
	public void requestTrafficSummary() throws IOException {
				
		RegistryRequestsTrafficSummary rrts = new RegistryRequestsTrafficSummary(Protocols.REGISTRY_REQUESTS_TRAFFIC_SUMMARY);
		byte[] outputByteArray = rrts.getBytes();
		
		for(RegisteredNode rn : nodeRegistry) {
			if(rn == null) {
				continue;
			}
			
			DataOutputStream dout = rn.getDout();
			dout.writeInt(outputByteArray.length);
			dout.write(outputByteArray, 0, outputByteArray.length);
			dout.flush();
			
		}
	}
	
	public void calculateTotals() {
		for(RegisteredNode rn : nodeRegistry) {
			if(rn == null) {
				continue;
			}
			totalSent += rn.getPacketsSent();
			totalReceived += rn.getPacketsReceived();
			totalRelayed += rn.getPacketsRelayed();
			totalSumSent += rn.getSendSummation();
			totalSumReceived += rn.getReceiveSummation();
		}
		
		printSummary();
		resetData();


	}
	
	public void listRoutingTables() {
		System.out.println("Here are all of the routing tables: \n");
		
		for(Manifest mf : manifestList) {
			System.out.println("Node ID: " + mf.indentifier);
			System.out.println("IP Address: " + nodeRegistry[mf.indentifier].getAddress());
			System.out.println("Port Number: " + nodeRegistry[mf.indentifier].getPortNumber());
			System.out.println("Connected Nodes: ");
			for(int i = 0; i < mf.connectedNodes.size(); i++) {
				System.out.println(mf.connectedNodes.get(i).getIdentifier());
			}
			System.out.println("\n\n");
			
		}
	}
	
	public void resetData() {
		totalSent = 0;
		totalReceived = 0;
		totalRelayed = 0;
		totalSumSent = 0;
		totalSumReceived = 0;
	}
	
	public void printSummary() {
		String Header0 = "Node ID";
		String Header1 = "Packets Sent";
		String Header2 = "Packets Received";
		String Header3 = "Packets Relayed";
		String Header4 = "Sum Values Sent";
		String Header5 = "Sum Values Received";
				
		System.out.printf("%5.5s %20.20s %20.20s %20.20s %20.20s %20.20s %n",
				Header0, Header1, Header2, Header3, Header4, Header5);
		
		for(int i = 0; i < nodeRegistry.length; i++) {
			if(nodeRegistry[i] == null) {
				continue;
			}

			int row = nodeRegistry[i].getIdentifier();
			int psent = nodeRegistry[i].getPacketsSent();
			int prec = nodeRegistry[i].getPacketsReceived();
			int prel = nodeRegistry[i].getPacketsRelayed();
			long sumSent = nodeRegistry[i].getSendSummation();
			long sumRec = nodeRegistry[i].getReceiveSummation();
			
			System.out.printf("%5.5s %20.20s %20.20s %20.20s %20.20s %20.20s %n",
					row, psent, prec, prel, sumSent, sumRec);
		}
		
		System.out.printf("%5.5s %20.20s %20.20s %20.20s %20.20s %20.20s %n",
					"Sum", totalSent, totalReceived, totalRelayed, 
					totalSumSent, totalSumReceived);
			
	}

}
