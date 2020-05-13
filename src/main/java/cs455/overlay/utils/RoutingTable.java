package cs455.overlay.utils;
import java.util.ArrayList;

public class RoutingTable {

	public int routingTableSize;
	public int overlaySize;
	public ArrayList<ConnectedNode> connectedNodes;
	public ArrayList<Integer> nodeIDs;
	
	public RoutingTable(int rt, int os) {
		this.routingTableSize = rt;
		this.overlaySize = os;
		this.nodeIDs = new ArrayList<Integer>();
		this.connectedNodes = new ArrayList<ConnectedNode>();
	}
	
	public RoutingTable() {
		this.nodeIDs = new ArrayList<Integer>();
		this.connectedNodes = new ArrayList<ConnectedNode>();

	}
}
