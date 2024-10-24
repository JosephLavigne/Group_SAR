package task3.abstracts;

import task3.implement.MessageQueueImplementation;
import task3.implement.MessageImplementation;

public abstract class QueueBroker {
	String name;
	protected QueueBroker(String name) {
		this.name = name;
	}
	public interface AcceptListener {
			public void accepted(MessageQueueImplementation q);

	}
	public abstract boolean bind(int port, AcceptListener listener);
	public abstract boolean unbind(int port);
	public interface ConnectListener {
		public void connected(MessageQueueImplementation q);
		public void refused();
	}
	public abstract boolean connect(String name, int port, ConnectListener listener);
}
