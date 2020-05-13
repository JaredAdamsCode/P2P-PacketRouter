package cs455.overlay.utils;
import java.util.ArrayList;

public class Manifest {

	public int routingTableSize;
	public ArrayList<RegisteredNode> connectedNodes;
	public int overlaySize;
	public int indentifier; // id the node this manifest belongs to
	
	public Manifest(int overlaySize, int rtSize) {
		this.overlaySize = overlaySize;
		this.routingTableSize = rtSize;
		this.connectedNodes = new ArrayList<RegisteredNode>();
	}
	
	public Manifest() {
		
	}
}
