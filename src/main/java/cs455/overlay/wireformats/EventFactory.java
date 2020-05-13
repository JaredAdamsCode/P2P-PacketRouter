package cs455.overlay.wireformats;

import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

import java.io.*;
import java.net.*;

public class EventFactory {
	
	private RegisteredNode[] nodeRegistry;
	private RegisteredNodeCount registeredNodeCount;
	
	public Socket registrySocket;
	private NodeID nodeID;
	
	private RoutingTable routingTable;
	private PortNumber messagingNodeServerPortNumber;
	
	private NumberOfPackets numberOfPackets;
	private TrackingData trackingData;
	
	public EventFactory(RegisteredNode[] nodeRegistry, RegisteredNodeCount registeredNodeCount) {
		this.nodeRegistry = nodeRegistry;
		this.registeredNodeCount = registeredNodeCount;
	}
	
	public EventFactory(NodeID nodeID, RoutingTable rt, PortNumber pn, NumberOfPackets numPacks, TrackingData td) {
		this.nodeID = nodeID;
		this.routingTable = rt;
		this.messagingNodeServerPortNumber = pn;
		this.numberOfPackets = numPacks;
		this.trackingData = td;
	}
	
	public void processEvent(byte[] data, int type, Socket socket, DataInputStream din, 
			DataOutputStream dout) throws IOException {
		
		switch(type) {
		
			case 1:
				OverlayNodeRegistrationEventHandler onreh = new OverlayNodeRegistrationEventHandler(socket, 
						this, messagingNodeServerPortNumber, din, dout);
				onreh.register();
				
				break;
		
			case 2:
				RegistrationEventHandler reh = new RegistrationEventHandler(nodeRegistry,
						registeredNodeCount, socket, din, dout);
				reh.registerNode(data);
				
				break;
				
			case 3:
				RegistrationResponseEventHandler rreh = new RegistrationResponseEventHandler(nodeID);
				rreh.registrationStatusReceiver(data);
				registrySocket = socket;
				
				break;
				
			case 4:
				DeregistrationEventHandler deh = new DeregistrationEventHandler(nodeRegistry, 
						registeredNodeCount, socket, din, dout);
				deh.deregister(data);
				deh.closeSocket();
				break;
				
			case 5:
				DeregistrationResponseEventHandler dreh = new DeregistrationResponseEventHandler(socket, data); 
				socket.close();
				break;
				
			case 6:
				OverlayNodeReceivesManifestEventHandler onrmeh = new OverlayNodeReceivesManifestEventHandler(routingTable, 
						socket, data, nodeID, dout);
				onrmeh.createMessagingNodeRoutingTable();
				onrmeh.createConnections();
				break;
				
			case 7:
				NodeReportsOverlaySetupStatus nross = new NodeReportsOverlaySetupStatus();
				nross.readBytes(data);
				
				if(nross.successStatus != -1) {
					nodeRegistry[nross.successStatus].setAllConnectionsMade(1);
				}
	
				break;
				
			case 8:
				RegistryRequestsTaskInitiate rrti = new RegistryRequestsTaskInitiate();
				rrti.readBytes(data);
								
				numberOfPackets.numberOfPackets = rrti.numberOfPacketsToSend;

				OverlayNodeInitiatesTaskEventHandler ontieh = new OverlayNodeInitiatesTaskEventHandler(rrti.numberOfPacketsToSend,
						routingTable, nodeID, socket.getLocalAddress().getHostAddress(), socket.getLocalPort(), 
						registrySocket, trackingData, dout);
				
				ontieh.sendPackets();
						
				break;
			
			case 9:
				OverlayNodeSendsData onsd = new OverlayNodeSendsData();
				onsd.readBytes(data);

				OverlayNodeReceivesDataEventHandler onrdeh = new OverlayNodeReceivesDataEventHandler(routingTable,
						numberOfPackets, nodeID, onsd.destinationID, onsd.sourceID, onsd.payload, 
						onsd.numberOfHops, trackingData, onsd.trace);
				
				onrdeh.processReceivedData();
				
				break;
				
			case 10:
				OverlayNodeReportsTaskFinished onrtf = new OverlayNodeReportsTaskFinished();
				onrtf.readBytes(data);
				nodeRegistry[onrtf.nodeID].setSummaryReceived(1);
				break;
				
			case 11:
				RegistryRequestsTrafficSummaryEventHandler rrtseh = new RegistryRequestsTrafficSummaryEventHandler(trackingData,
						nodeID, registrySocket, dout);
				
				rrtseh.sendSummary();
				trackingData.zeroAllValues();
				break;
				
			case 12:
				OverlayNodeReportsTrafficSummary onrts = new OverlayNodeReportsTrafficSummary();
				onrts.readBytes(data);
				
				nodeRegistry[onrts.nodeID].setPacketsSent(onrts.packetsSent);
				nodeRegistry[onrts.nodeID].setPacketsReceived(onrts.packetsReceived);
				nodeRegistry[onrts.nodeID].setPacketsRelayed(onrts.packetsRelayed);
				nodeRegistry[onrts.nodeID].setSendSummation(onrts.sumSent);
				nodeRegistry[onrts.nodeID].setReceiveSummation(onrts.sumReceived);

				break;

			default:
				System.out.println("Invalid type sent to EventFactory. Type received: " + type);
				
			
		}
	}

}
