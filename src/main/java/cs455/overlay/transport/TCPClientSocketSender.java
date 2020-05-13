package cs455.overlay.transport;

import java.io.*;
import java.net.*;

public class TCPClientSocketSender {
	
	private Socket socket;
	private DataOutputStream dout;
	
	public TCPClientSocketSender(Socket socket) throws IOException {
		this.socket = socket;
		this.dout = new DataOutputStream(socket.getOutputStream());
		
	}
	
	public void sendData(byte[] dataToSend) throws IOException {
		int dataLength = dataToSend.length;
		dout.writeInt(dataLength);
		dout.write(dataToSend, 0, dataLength);
		dout.flush();
		
	}

}
