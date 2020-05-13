package cs455.overlay.wireformats;
import java.io.*;

public class NodeReportsOverlaySetupStatus {

	public int type;
	public int successStatus;
	public int infoLength;
	public String infoMessage;
	
	
	public NodeReportsOverlaySetupStatus(int type, int successStatus, int length, String infoMessage) {
		this.type = type;
		this.successStatus = successStatus;
		this.infoLength = length;
		this.infoMessage = infoMessage;
		
	}
	
	public NodeReportsOverlaySetupStatus() {
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(successStatus);
		dout.writeInt(infoLength);
		
		byte[] identifierBytes = infoMessage.getBytes();
		int elementLength = identifierBytes.length;
		dout.writeInt(elementLength);
		dout.write(identifierBytes);
		
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
		successStatus = din.readInt();
		infoLength = din.readInt();
		
		int messageLen = din.readInt();
		byte[] info = new byte[messageLen];
		din.readFully(info);
		
		infoMessage = new String(info);
		
		baIS.close();
		din.close();
		
	}
}
