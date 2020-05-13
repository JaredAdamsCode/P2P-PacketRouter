package cs455.overlay.wireformats;
import java.io.*;
import java.util.ArrayList;

public class OverlayNodeSendsData {

	public int type;
	public int destinationID;
	public int sourceID;
	public int payload;
	public int numberOfHops;
	public int[] trace;
	
	
	public OverlayNodeSendsData(int type, int dest, int source, int payload, int numHops, int[] trace) {
		this.type = type;
		this.destinationID = dest;
		this.sourceID = source;
		this.payload = payload;
		this.numberOfHops = numHops;
		this.trace = trace;
		
	}
	
	public OverlayNodeSendsData() {
		
	}
	
	public byte[] getBytes() throws IOException {
		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOS));
		
		dout.writeInt(type);
		dout.writeInt(destinationID);
		dout.writeInt(sourceID);
		dout.writeInt(payload);
		dout.writeInt(numberOfHops);
		
		for(int i = 0; i < trace.length; i++) {
			dout.writeInt(trace[i]);
		}
				
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
		destinationID = din.readInt();
		sourceID = din.readInt();
		payload = din.readInt();
		numberOfHops = din.readInt();
		
		trace = new int[numberOfHops];
		
		for(int i = 0; i < numberOfHops; i++) {
			trace[i] = din.readInt();
		}
		
		baIS.close();
		din.close();
		
	}
}
