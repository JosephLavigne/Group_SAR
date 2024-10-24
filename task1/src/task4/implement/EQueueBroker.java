package task4.implement;

import task4.abstracts.QueueBroker;

public class EQueueBroker extends QueueBroker{

	protected EQueueBroker(String name) {
		super(name);
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

}
