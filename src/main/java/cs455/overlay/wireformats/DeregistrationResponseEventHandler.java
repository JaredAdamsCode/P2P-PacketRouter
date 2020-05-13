package cs455.overlay.wireformats;
import java.io.IOException;
import java.net.Socket;

public class DeregistrationResponseEventHandler {
	
	private Socket socket;
	private RegistryReportsDeregistrationStatus rrds;
	private byte[] data;
	
	DeregistrationResponseEventHandler(Socket socket, byte[] data) throws IOException{
		this.socket = socket;
		this.data = data;
		this.rrds = new RegistryReportsDeregistrationStatus();
		rrds.readBytes(data);
		
		System.out.println("Deregistration message: " + rrds.infoMessage);
		System.out.println("Enter a command: ");
		
		
	}
	
	public void closeSocket() throws IOException {
		socket.close();
	}
	
	
	
	
}
