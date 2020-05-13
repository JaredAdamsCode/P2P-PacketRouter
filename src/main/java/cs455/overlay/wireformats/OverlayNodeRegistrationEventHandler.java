package cs455.overlay.wireformats;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.utils.*;
import java.io.*;

public class OverlayNodeRegistrationEventHandler {

	
	private byte[] outputByteArray;
	private Socket socket;
	private EventFactory eventFactory;
	
	private PortNumber messagingNodeServerPortNumber;
	
	private DataOutputStream dout;
	private DataInputStream din;
	
	public OverlayNodeRegistrationEventHandler(Socket socket, EventFactory ef, PortNumber pn,
			DataInputStream din, DataOutputStream dout) throws IOException {
		this.dout = dout;
		this.din = din;
		this.socket = socket;
		this.eventFactory = ef;
		OverLayToRegisterListener otrl = new OverLayToRegisterListener(socket, eventFactory, din, dout);
		Thread otrlThread = new Thread(otrl);
		otrlThread.start();
		
		this.messagingNodeServerPortNumber = pn;
		
	}
	
	private void createRegistrationMessage(Socket client) throws IOException {
		
		OverlayNodeSendsRegistration onsr = new OverlayNodeSendsRegistration(Protocols.OVERLAY_NODE_SENDS_REGISTRATION, 
				socket.getInetAddress().getLocalHost().getHostAddress().length(), socket.getInetAddress().getLocalHost().getHostAddress(),
				messagingNodeServerPortNumber.portNumber);
		
		outputByteArray = onsr.getBytes();
	}
	
	private void sendRegistrationMessage(Socket socket) throws IOException {
		
		dout.writeInt(outputByteArray.length);
		
		dout.write(outputByteArray, 0, outputByteArray.length);
		dout.flush();
		
	}
	
	public void register() throws IOException {
		
		createRegistrationMessage(socket);
		
		sendRegistrationMessage(socket);
		
	}
}
