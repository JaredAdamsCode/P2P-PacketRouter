package cs455.overlay.wireformats;
import java.io.DataInputStream;
import java.io.IOException;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class RegistrationResponseEventHandler {

	private NodeID nodeID;
	
	public RegistrationResponseEventHandler(NodeID nodeID) {
		this.nodeID = nodeID;
	}
	
	
	public void registrationStatusReceiver(byte[] data) {
		RegistryReportsRegistrationStatus rrrs = new RegistryReportsRegistrationStatus();
		try {
			rrrs.readBytes(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		nodeID.nodeID = rrrs.successStatus;
		
		System.out.println("Message: " + rrrs.infoMessage);
		System.out.println("Enter a command:");
	}
}
