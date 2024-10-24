package task4.implement;

import task4.abstracts.Broker;

public class EBroker extends Broker {
	
	String name;
	
	public EBroker(String name) {
		this.name = name;
	}

	@Override
	public boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getname() {
		return this.name;
	}

}
