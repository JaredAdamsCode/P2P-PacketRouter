package cs455.overlay.transport;


import java.io.*;
import java.net.*;

import cs455.overlay.node.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;



public class RegistryServer implements Runnable {
	
	private ServerSocket serverSocket;
	private EventFactory eventFactory;
	
	
	public RegistryServer(EventFactory eventFactory, int port) throws IOException {
		this.serverSocket = new ServerSocket(port, 10000);
		this.eventFactory = eventFactory;
	}
	
	public void listen() {
		
		while(true) {
			try {
				
//				System.out.println("REGISTRY Local Host is: " + InetAddress.getLocalHost());
//				System.out.println("REGISTRY Server created using port: " + this.serverSocket.getLocalPort() + "\n");
//				System.out.println("Waiting for connections...\n");
				Socket clientSocket = serverSocket.accept();
//				System.out.println("New connection is made. Attempting to read data...\n");
//				DataInputStream din = new DataInputStream(clientSocket.getInputStream());
//				din.read(inputByteArray);
//				writeBytes(inputByteArray);
				
				DataInputStream din = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
				
				TCPRegisterReceiver receiver = new TCPRegisterReceiver(eventFactory, clientSocket, din, dout);
				Thread receiverThread = new Thread(receiver);
				receiverThread.start();
				
//				clientSocket.close();
//				System.out.println("client is now closed \n");
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		listen();
		
	}
	
}
