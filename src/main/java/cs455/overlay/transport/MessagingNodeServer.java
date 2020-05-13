package cs455.overlay.transport;

import java.io.*;
import java.net.*;

import cs455.overlay.node.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;

public class MessagingNodeServer implements Runnable {

	private ServerSocket serverSocket;
	private PortNumber portNumber;
	private EventFactory eventFactory;
	
	public MessagingNodeServer(PortNumber portNumber, EventFactory eventFactory) throws UnknownHostException, IOException {
		this.serverSocket = new ServerSocket(0, 10000, InetAddress.getLocalHost());
		this.portNumber = portNumber;
		this.portNumber.portNumber = serverSocket.getLocalPort();
		this.eventFactory = eventFactory;
		
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				
				Socket clientSocket = serverSocket.accept();
				DataInputStream din = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
				
				MessagingNodeReceiver receiver = new MessagingNodeReceiver(clientSocket, eventFactory, din, dout);
				Thread receiverThread = new Thread(receiver);
				receiverThread.start();
								
			} catch (IOException e) {
				e.printStackTrace();
				
			}

		}
			
	}
	

}
