package cs455.overlay.utils;
import java.util.ArrayList;

import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;

public class CreateRoutingTable {

	public RegisteredNode[] registeredNodes;
	public int overlaySize;
	public ArrayList<Manifest> manifestList;
	public ArrayList<RegisteredNode> registeredNodeList;
	public int routingTableSize;
	
	private ArrayList<Integer> nodeIDs;

	public CreateRoutingTable(RegisteredNode[] rn, int rtSize, ArrayList<Manifest> manifestList, ArrayList<Integer> nodeIDs){
		this.registeredNodes = rn;
		
		this.routingTableSize = rtSize;
		this.manifestList = manifestList;
		this.nodeIDs = nodeIDs;
		
		this.registeredNodeList = new ArrayList<RegisteredNode>();
	}
	
	public void createRegisteredNodeList() {
		for(RegisteredNode rn : registeredNodes) {
			if (rn == null) {
				continue;
			}
			
			registeredNodeList.add(rn);
			
		}
	
		setOverlaySize();
	}
	
	private void setOverlaySize() {
		overlaySize = registeredNodeList.size();
	}
	
	public void createManifests() {
		
		for(int i = 0; i < overlaySize; i++) {
			Manifest manifest = new Manifest(overlaySize, routingTableSize);
			manifest.indentifier = registeredNodeList.get(i).getIdentifier();
			// First add the nodes it can connect to
			for(int j = 0; j < routingTableSize; j++) {

				RegisteredNode tempNode = registeredNodeList.get((int) ((Math.pow(2, j) + i) % overlaySize));
				if((tempNode.getIdentifier() != manifest.indentifier) && !manifest.connectedNodes.contains(tempNode)) {
					manifest.connectedNodes.add(tempNode);
				}
			}
			
			manifestList.add(manifest);
		}
		
	}
	
	public void createNodeIDs() {
		for(int j = 0; j < overlaySize; j++) {
			nodeIDs.add(registeredNodeList.get(j).getIdentifier());
		}
	}
	
	
	
	
	
	
}
