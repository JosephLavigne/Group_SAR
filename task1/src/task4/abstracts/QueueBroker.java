package task4.abstracts;

import task4.abstracts.MessageQueue;


public abstract class QueueBroker {
	String name;
	protected QueueBroker(String name) {
		this.name = name;
	}
	public interface AcceptListener {
			public void accepted(MessageQueue q);

	}
	public abstract boolean bind(int port, AcceptListener listener);
	public abstract boolean unbind(int port);
	public interface ConnectListener {
		public void connected(MessageQueue q);
		public void refused();
	}
	public abstract boolean connect(String name, int port, ConnectListener listener);
}
