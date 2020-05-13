package cs455.overlay.wireformats;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;

public class RegistrationEventHandler {
	
	private RegisteredNode[] nodeRegistry;
	private Socket socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private RegisteredNodeCount registeredNodeCount;
	private int failureReason = 0;
	
	public RegistrationEventHandler(RegisteredNode[] nodeRegistry,
			RegisteredNodeCount registeredNodeCount, Socket socket, DataInputStream din, DataOutputStream dout) {
		
		this.nodeRegistry = nodeRegistry;
		this.din = din;
		this.dout = dout;
		this.registeredNodeCount = registeredNodeCount;
		this.socket = socket;
	}
	
	public void registerNode(byte[] data) throws IOException {

		OverlayNodeSendsRegistration onsr = new OverlayNodeSendsRegistration();
		try {
			onsr.readBytes(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean noError = registrationErrorCheck(onsr);
		int identifier = Protocols.REGISTRATION_FAILURE;
		RegisteredNode rn = new RegisteredNode(identifier, onsr.portNumber, onsr.address, socket);

		
		if(noError) {
			identifier = getIdentifier();
			rn.setIndetifier(identifier);
			nodeRegistry[identifier] = rn;
			registeredNodeCount.registeredNodeCount += 1;
			
			String message = infoMessageBuilder(noError);
			sendRegistrationStatus(rn, message, noError);
		}
		else {
			String message = infoMessageBuilder(noError);
			sendRegistrationStatus(rn, message, noError);
		}
		

	
	}
	
	private int getIdentifier() {
		Random rand = new Random();
		int idNum = rand.nextInt(128);
		while (nodeRegistry[idNum] != null) {
			idNum = rand.nextInt(128);
		}
		return idNum;
	}
	
	private boolean registrationErrorCheck(OverlayNodeSendsRegistration onsr) {
		InetAddress ia = socket.getInetAddress();
		String inetAddress = ia.getHostAddress();
		int isPortNum = socket.getPort();
		
		// First check is previous registration
		for(RegisteredNode rn : nodeRegistry) {
			if(rn == null) {
				continue;
			}
			if(rn.getAddress().equals(inetAddress) && rn.getPortNumber() == isPortNum) {
				failureReason = 1;

				return false;
			}
		}
		
		// check that socket IP matches IP in message
		if(!inetAddress.equals(onsr.address)) {
			failureReason = 2;
			
			return false;
		}	
		// We are good. Return true. 
		return true;
		
	}

	private void sendRegistrationStatus(RegisteredNode rn, String message, boolean b) throws IOException {
		RegistryReportsRegistrationStatus rrrs;
		if(b) {
			rrrs = new RegistryReportsRegistrationStatus(Protocols.REGISTRY_REPORTS_REGISTRATION_STATUS,
					rn.getIdentifier(), message.length(), message);
		}
		else {
			rrrs = new RegistryReportsRegistrationStatus(Protocols.REGISTRY_REPORTS_REGISTRATION_STATUS,
					rn.getIdentifier(), message.length(), message);
		}

		
		byte[] sendStatus = rrrs.getBytes();
		
		
		dout.writeInt(sendStatus.length);
		dout.write(sendStatus, 0, sendStatus.length);
		dout.flush();
		
	}
	
	public String infoMessageBuilder(boolean b) {
		String str;
		String reason = "";
		if(failureReason == 1) {
			reason = "Node is already registered.";
		}
		else {
			reason = "Mismatch with socket IP and/or port.";
		}
		if(b) {
			str = "Registratioin request successful. The number of nodes currently constituting"
					+ " the overlay is (" + registeredNodeCount.registeredNodeCount + ")";
					
		}
		else {
			str = "Registration unsuccessful. Reason: " + reason;
		}
		return str;
	}

}
