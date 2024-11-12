package task4.implementation.messages;

import task4.abs.Broker;
import task4.abs.Broker.AcceptListener;
import task4.abs.Broker.ConnectListener;
import task4.abs.Channel;
import task4.abs.QueueBroker;
import task4.implementation.BrokerImplementation;
import task4.implementation.BrokerManager;

public class QueueBrokerImplementation extends QueueBroker {
	private String name;
	private Broker broker;

	public QueueBrokerImplementation(String string, BrokerManager brokerManager) {
		this.name = string;
		this.broker = new BrokerImplementation(string, brokerManager);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public boolean unbind(int port) {
		// TODO Auto-generated method stub
		return broker.unbind(port);
	}

	@Override
	public boolean bind(int port, QueueAcceptListener listener) {
		// TODO Auto-generated method stub
		QueueAcceptListenerImplementation acceptListener = new QueueAcceptListenerImplementation(listener);
		return broker.bind(port, acceptListener);
	}

	@Override
	public boolean connect(String name, int port, QueueConnectListener listener) {
		// TODO Auto-generated method stub
		QueueConnectListenerImplementation connectListener = new QueueConnectListenerImplementation(listener);
		return broker.connect(name, port, connectListener);
	}

	private class QueueAcceptListenerImplementation implements AcceptListener {
		private QueueAcceptListener listener;

		public QueueAcceptListenerImplementation(QueueAcceptListener listener) {
			this.listener = listener;
		}

		@Override
		public void accepted(Channel channel) {
			// TODO Auto-generated method stub
			listener.accepted(new QueueChannelImplementation(channel));
		}
	}

	private class QueueConnectListenerImplementation implements ConnectListener {
		private QueueConnectListener listener;

		public QueueConnectListenerImplementation(QueueConnectListener listener) {
			this.listener = listener;
		}

		@Override
		public void connected(Channel channel) {
			// TODO Auto-generated method stub
			listener.connected(new QueueChannelImplementation(channel));
		}

		@Override
		public void refused() {
			// TODO Auto-generated method stub
			listener.refused();
		}
	}

}
