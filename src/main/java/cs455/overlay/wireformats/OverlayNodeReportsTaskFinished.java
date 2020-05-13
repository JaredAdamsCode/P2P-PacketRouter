package cs455.overlay.wireformats;

import java.io.*;

public class OverlayNodeReportsTaskFinished {

	
	public int type;
	public int IPLength;
	public String address;
	public int portNumber;
	public int nodeID;
	
	public OverlayNodeReportsTaskFinished(int type, int IPLength, String addr, int portNum, int nodeID) {
		this.type = type;
		this.IPLength = IPLength;
		this.address = addr;
		this.portNumber = portNum;
		this.nodeID = nodeID;
	}
	
	public OverlayNodeReportsTaskFinished() {
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(IPLength);
		
		byte[] identifierBytes = address.getBytes();
		int elementLength = identifierBytes.length;
		dout.writeInt(elementLength);
		dout.write(identifierBytes);

		
		dout.writeInt(portNumber);
		dout.writeInt(nodeID);
		
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
		IPLength = din.readInt();
		
		int idLength = din.readInt();
		byte[] ids = new byte[idLength];
		din.readFully(ids);
		
		address = new String(ids);

		portNumber = din.readInt();
		
		nodeID = din.readInt();
		
		baIS.close();
		din.close();
		
	}
}
