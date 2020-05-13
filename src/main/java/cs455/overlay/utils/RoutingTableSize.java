package cs455.overlay.utils;


import cs455.overlay.node.*;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;

public class RoutingTableSize {
	public int routingTableSize;
	public int overlaySize;
	
	public RoutingTableSize() {
		this.routingTableSize = 3;
	}
	
	public void setOverlaySize(int os) {
		this.overlaySize = os;
	}
	
	public void setRoutingTableSize() {
		while(Math.pow(2, routingTableSize) >= overlaySize) {
			routingTableSize -= 1;
		}
	}
}
