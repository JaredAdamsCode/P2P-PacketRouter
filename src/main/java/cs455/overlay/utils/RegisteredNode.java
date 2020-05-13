package cs455.overlay.utils;

import java.io.*;
import java.net.*;

public class RegisteredNode {
	
	private int identifier;
	private int portNumber;
	private String address;
	private Socket socket;
	
	private int allConnectionsMade;
	private int summaryReceived;
	
	private int packetsSent = 0;
	private int packetsReceived = 0;
	private int packetsRelayed = 0;
	private long sendSummation = 0;
	private long receiveSummation = 0;
	
	private DataOutputStream dout;
	private DataInputStream din;
	
	public RegisteredNode(int id, int portNum, String addr, Socket socket) throws IOException {
		this.identifier = id;
		this.portNumber = portNum;
		this.address = addr;
		this.socket = socket;
		
		this.din = new DataInputStream(this.socket.getInputStream());
		this.dout = new DataOutputStream(this.socket.getOutputStream());
		
		this.allConnectionsMade = -1;
		this.summaryReceived = -1;
	}
	
	public int getSummaryReceived() {
		return summaryReceived;
	}

	public void setSummaryReceived(int summaryReceived) {
		this.summaryReceived = summaryReceived;
	}

	public DataOutputStream getDout() {
		return dout;
	}

	public void setDout(DataOutputStream dout) {
		this.dout = dout;
	}

	public DataInputStream getDin() {
		return din;
	}

	public void setDin(DataInputStream din) {
		this.din = din;
	}

	public RegisteredNode() {
		this.identifier = -1;
	}
	
	public int getPacketsSent() {
		return packetsSent;
	}

	public void setPacketsSent(int packetsSent) {
		this.packetsSent = packetsSent;
	}

	public int getPacketsReceived() {
		return packetsReceived;
	}

	public void setPacketsReceived(int packetsReceived) {
		this.packetsReceived = packetsReceived;
	}

	public int getPacketsRelayed() {
		return packetsRelayed;
	}

	public void setPacketsRelayed(int packetsRelayed) {
		this.packetsRelayed = packetsRelayed;
	}

	public long getSendSummation() {
		return sendSummation;
	}

	public void setSendSummation(long sendSummation) {
		this.sendSummation = sendSummation;
	}

	public long getReceiveSummation() {
		return receiveSummation;
	}

	public void setReceiveSummation(long receiveSummation) {
		this.receiveSummation = receiveSummation;
	}



	public int getIdentifier() {
		return identifier;
	}


	public int getPortNumber() {
		return portNumber;
	}


	public String getAddress() {
		return address;
	}
	
	public Socket getSocket() {
		return socket;
	}


	public void setIndetifier(int identifier) {
		this.identifier = identifier;
	}
	
	public void setAllConnectionsMade(int status) {
		this.allConnectionsMade = status;
	}
	
	public int getAllConnectionsMade() {
		return allConnectionsMade;
	}

	

}
