package task4.implementation;

import task4.Broker;
import task4.implementation.BrokerImplementation.AcceptListener;

public class AcceptRunnable implements Runnable{
	
	private AcceptListener connectListener;
	private Broker brokerToAccept;
	private int port;

	public AcceptRunnable(int port, AcceptListener connectListener, Broker brokerToAccept) {
		this.connectListener = connectListener;
		this.port = port;
		this.brokerToAccept = brokerToAccept;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
