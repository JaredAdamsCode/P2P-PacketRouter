package cs455.overlay.transport;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;


import cs455.overlay.node.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;

public class OverLayToRegisterListener implements Runnable {

	private Socket socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private EventFactory eventFactory;
	
	public OverLayToRegisterListener(Socket socket, EventFactory ef, 
			DataInputStream din, DataOutputStream dout) throws IOException {
		this.socket = socket;
		this.din = din;
		this.dout = dout;
		this.eventFactory = ef;
		

	}
	
	@Override
	public void run() {
		try {
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void listen() throws IOException {
		int dataLength;

		while(socket != null) {
			
			try {
				dataLength = din.readInt();
				byte[] data = new byte[dataLength];
				
				din.readFully(data, 0, dataLength);
				
				ByteArrayInputStream baIS = new ByteArrayInputStream(data);
				DataInputStream din2 = new DataInputStream(new BufferedInputStream(baIS));
				
				int type = din2.readInt();
				
				eventFactory.processEvent(data, type, socket, din, dout);
								
			} catch (SocketException se) {
				System.out.println(se.getMessage());
				break;
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
				break;
			}
		}	
	}
}
