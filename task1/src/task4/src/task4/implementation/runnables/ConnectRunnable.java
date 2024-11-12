package task4.implementation.runnables;

import task4.abs.Broker.ConnectListener;
import task4.implementation.BrokerImplementation;

public class ConnectRunnable implements Runnable{
	
	private ConnectListener connectListener;
	private BrokerImplementation brokerToConnect;
	private int port;

	public ConnectRunnable(int port, ConnectListener connectListener, BrokerImplementation brokerToConnect) {
		this.connectListener = connectListener;
		this.port = port;
		this.brokerToConnect = brokerToConnect;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.brokerToConnect.connecting(connectListener, port);
	}

}
