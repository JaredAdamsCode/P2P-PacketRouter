package cs455.overlay.utils;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrackingData {
	
	public AtomicInteger sendTracker = new AtomicInteger();
	public AtomicInteger receiveTracker = new AtomicInteger();
	public AtomicInteger relayTracker = new AtomicInteger();
	
	public AtomicLong sendSummation = new AtomicLong();
	public AtomicLong receiveSummation = new AtomicLong();
	
	public CountersAndDiagnostics countersAndDiagnostics;
	
	public TrackingData(CountersAndDiagnostics cad) {
		
		this.countersAndDiagnostics = cad;
		
	}
	
	public void zeroAllValues() {
		
		countersAndDiagnostics.packetsSent = sendTracker.get();
		countersAndDiagnostics.packetsReceived = receiveTracker.get();
		countersAndDiagnostics.packetsRelayed = relayTracker.get();
		countersAndDiagnostics.SumReceived = receiveSummation.get();
		countersAndDiagnostics.SumSent = sendSummation.get();
		
		sendTracker.getAndSet(0);
		receiveTracker.getAndSet(0);
		relayTracker.getAndSet(0);
		sendSummation.getAndSet(0);
		receiveSummation.getAndSet(0);
	}


}
