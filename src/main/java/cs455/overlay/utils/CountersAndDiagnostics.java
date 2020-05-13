// Used to Save Data so it can be displayed when command is issued from Messagning Node
// to print counters and diagnostics. 

package cs455.overlay.utils;


import cs455.overlay.transport.*;
import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;

public class CountersAndDiagnostics {
	
	public int packetsSent;
	public int packetsReceived;
	public int packetsRelayed;
	public long SumSent;
	public long SumReceived;
	
	public CountersAndDiagnostics() {
		this.packetsSent = 0;
		this.packetsReceived = 0;
		this.packetsRelayed = 0;
		this.SumReceived = 0;
		this.SumSent = 0;
	}

}
