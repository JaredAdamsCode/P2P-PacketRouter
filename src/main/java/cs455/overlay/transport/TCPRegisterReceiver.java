package cs455.overlay.transport;

import java.io.*;
import java.net.*;
import java.util.*;

import cs455.overlay.node.*;
import cs455.overlay.utils.*;
import cs455.overlay.wireformats.*;

public class TCPRegisterReceiver implements Runnable{

	
	private Socket socket;
	private DataInputStream din;
	private EventFactory eventFactory;
	
	private DataOutputStream dout;
	
	
	public TCPRegisterReceiver(EventFactory eventFactory, Socket socket,
			DataInputStream din, DataOutputStream dout) throws IOException {
		
		this.din = din;
		this.dout = dout;
		this.socket = socket;
		this.eventFactory = eventFactory;
		
	}
	
	
	public void run() {
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
				din2.close();
				baIS.close();
				
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
