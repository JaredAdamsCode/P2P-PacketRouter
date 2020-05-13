package cs455.overlay.wireformats;

import java.io.*;

public class RegistryRequestsTaskInitiate {

	public int type;
	public int numberOfPacketsToSend;
	
	
	public RegistryRequestsTaskInitiate(int type, int numPackets) {
		this.type = type;
		this.numberOfPacketsToSend = numPackets;
		
	}
	
	public RegistryRequestsTaskInitiate() {
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(numberOfPacketsToSend);

		
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
		numberOfPacketsToSend = din.readInt();
		
		baIS.close();
		din.close();
		
	}
}
