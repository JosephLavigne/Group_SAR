package task4.abs;

import task4.implementation.BrokerImplementation.AcceptListener;
import task4.implementation.BrokerImplementation.ConnectListener;

public abstract class Broker {
	
	public String name;

	public Broker(String name) {
		this.name = name;
	}
	
	public abstract boolean unbind(int port);
	
	public abstract boolean bind(int port, AcceptListener listener);
	
	public abstract boolean connect(String name, int port, ConnectListener listener);
}
