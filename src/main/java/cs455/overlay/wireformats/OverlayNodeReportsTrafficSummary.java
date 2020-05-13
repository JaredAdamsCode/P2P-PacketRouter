package cs455.overlay.wireformats;

import java.io.*;

public class OverlayNodeReportsTrafficSummary {
	
	public int type;
	public int nodeID;
	
	public int packetsSent;
	public int packetsRelayed;
	public long sumSent;
	
	public int packetsReceived;
	public long sumReceived;
	

	
	public OverlayNodeReportsTrafficSummary(int type, int nodeID, int pSent, int pRelayed, long sumSent,
			int pReceived, long sumReceived) {
		
		this.type = type;
		this.nodeID = nodeID;
		this.packetsSent = pSent;
		this.packetsRelayed = pRelayed;
		this.sumSent = sumSent;
		this.packetsReceived = pReceived;
		this.sumReceived = sumReceived;

	}
	
	public OverlayNodeReportsTrafficSummary() {
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(nodeID);
		
		dout.writeInt(packetsSent);
		dout.writeInt(packetsRelayed);
		
		dout.writeLong(sumSent);
		
		dout.writeInt(packetsReceived);
		
		dout.writeLong(sumReceived);
		
		dout.flush();
		marshalledBytes = baOS.toByteArray();
		
		baOS.close();
		dout.close();
		
		return marshalledBytes;
	} 
	
	public void readBytes(byte[] marshalledBytes) throws IOException {
		ByteArrayInputStream baIS = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baIS));
		
		type = din.readInt();
		
		nodeID = din.readInt();
		packetsSent = din.readInt();
		packetsRelayed = din.readInt();
		sumSent = din.readLong();
		packetsReceived = din.readInt();
		sumReceived = din.readLong();
		
		baIS.close();
		din.close();
		
	}

}
