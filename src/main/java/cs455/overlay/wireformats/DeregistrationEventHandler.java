package cs455.overlay.wireformats;
import java.io.DataOutputStream;

import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;

public class DeregistrationEventHandler {
	
	private RegisteredNode[] nodeRegistry;
	private Socket socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private RegisteredNodeCount registeredNodeCount;
	private int failureReason = 0;
	
	public DeregistrationEventHandler(RegisteredNode[] nodeRegistry,
			RegisteredNodeCount registeredNodeCount, Socket socket,
			DataInputStream din, DataOutputStream dout) {
		
		this.nodeRegistry = nodeRegistry;
		this.din = din;
		this.dout = dout;
		this.registeredNodeCount = registeredNodeCount;
		this.socket = socket;
	}
	
	public void deregister(byte[] data) throws IOException {
		OverlayNodeSendsDeregistration onsd = new OverlayNodeSendsDeregistration();
		
		try {
			onsd.readBytes(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		boolean noError = deregistrationErrorCheck(onsd);
		 
		if(noError) {
			nodeRegistry[onsd.nodeID] = null;
			registeredNodeCount.registeredNodeCount -= 1;
			sendDeregistrationStatus(noError, infoMessageBuilder(noError), onsd);
		}
		else {
			nodeRegistry[onsd.nodeID] = null;
			sendDeregistrationStatus(noError, infoMessageBuilder(noError), onsd);
		}
		
		
	}
	
	private boolean deregistrationErrorCheck(OverlayNodeSendsDeregistration onsd) {
		
		InetAddress ia = socket.getInetAddress();
		String inetAddress = ia.getHostAddress();
		int isPortNum = socket.getPort();
		
		if(nodeRegistry[onsd.nodeID] == null) {
			failureReason = 1;
			return false;
		}
		
		if(!inetAddress.equals(onsd.address)) {
			failureReason = 2;
			return false;
		}
	
		return true;
	}
	
	private void sendDeregistrationStatus(boolean b, String message, OverlayNodeSendsDeregistration onsd) throws IOException {
		
		RegistryReportsDeregistrationStatus rrds;
		
		if(b) {
			rrds = new RegistryReportsDeregistrationStatus(
					Protocols.REGISTRY_REPORTS_DEREGISTRATION_STATUS, onsd.nodeID, message.length(), message);
			
		}
		else {
			rrds = new RegistryReportsDeregistrationStatus(
				Protocols.REGISTRY_REPORTS_DEREGISTRATION_STATUS, Protocols.REGISTRATION_FAILURE,
				message.length(), message);
		}
		
		byte[] sendStatus = rrds.getBytes();
				
		dout.writeInt(sendStatus.length);
		dout.write(sendStatus, 0, sendStatus.length);
		dout.flush();
				
	}
	
	private String infoMessageBuilder(boolean b) {
		String str;
		String reason = "";
		if(failureReason == 1) {
			reason = "Node is already deregistered.";
		}
		else {
			reason = "Mismatch with socket IP and/or port.";
		}
		if(b) {
			str = "Deregistratioin request successful. The number of nodes currently constituting"
					+ " the overlay is (" + registeredNodeCount.registeredNodeCount + ")";
					
		}
		else {
			str = "Registration unsuccessful. Reason: " + reason;
		}
		return str;
	}
	
	public void closeSocket() throws IOException {
		socket.close();
	}

}
