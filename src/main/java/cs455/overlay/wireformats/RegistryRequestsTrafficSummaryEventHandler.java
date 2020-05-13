package cs455.overlay.wireformats;
import java.io.*;
import java.net.*;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class RegistryRequestsTrafficSummaryEventHandler {
	
	private TrackingData trackingData;
	private NodeID nodeID;
	private byte[] outputByteArray;
	private Socket registrySocket;
	
	private DataOutputStream dout;
	
	public RegistryRequestsTrafficSummaryEventHandler(TrackingData td, NodeID nodeID, Socket regSocket,
			DataOutputStream dout) {
		
		this.trackingData = td;
		this.nodeID = nodeID;
		this.registrySocket = regSocket;
		this.dout = dout;
	}
	
	private void createSummaryMessage() throws IOException{
		
		OverlayNodeReportsTrafficSummary onrts = new OverlayNodeReportsTrafficSummary(Protocols.OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY,
				nodeID.nodeID, trackingData.sendTracker.get(), trackingData.relayTracker.get(), trackingData.sendSummation.get(),
				trackingData.receiveTracker.get(), trackingData.receiveSummation.get());
		
		outputByteArray = onrts.getBytes();
		
	}
	
	public void sendSummary() throws IOException {
		
		createSummaryMessage();
				
		dout.writeInt(outputByteArray.length);
		dout.write(outputByteArray, 0, outputByteArray.length);
		
		dout.flush();
				
	}

}
