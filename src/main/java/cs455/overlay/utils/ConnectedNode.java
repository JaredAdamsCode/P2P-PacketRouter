package cs455.overlay.utils;
import java.net.Socket;

import java.io.*;

public class ConnectedNode {
	public int portNumber;
	public String ipAddress;
	private Socket socket;
	public int nodeID;
	
	private DataInputStream din;
	private DataOutputStream dout;
	
	public ConnectedNode(int pn, String addr, int id) {
		this.portNumber = pn;
		this.ipAddress = addr;
		this.nodeID = id;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.din = new DataInputStream(socket.getInputStream());
		this.dout = new DataOutputStream(socket.getOutputStream());
	}

	public DataInputStream getDin() {
		return din;
	}

	public void setDin(DataInputStream din) {
		this.din = din;
	}

	public DataOutputStream getDout() {
		return dout;
	}

	public void setDout(DataOutputStream dout) {
		this.dout = dout;
	}
	
	
	
	
}
