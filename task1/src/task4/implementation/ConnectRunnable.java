package task4.implementation;

import task4.Broker;
import task4.implementation.BrokerImplementation.ConnectListener;

public class ConnectRunnable implements Runnable{
	
	private ConnectListener connectListener;
	private Broker brokerToConnect;
	private int port;

	public ConnectRunnable(int port, ConnectListener connectListener, Broker brokerToConnect) {
		this.connectListener = connectListener;
		this.port = port;
		this.brokerToConnect = brokerToConnect;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
