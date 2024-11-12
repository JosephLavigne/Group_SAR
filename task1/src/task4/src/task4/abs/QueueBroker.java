package task4.abs;

import task4.abs.Broker.AcceptListener;
import task4.abs.Broker.ConnectListener;

public abstract class QueueBroker {
	
	public interface QueueAcceptListener {
		void accepted(QueueChannel queue);
	}
	
	public interface QueueConnectListener {
		void connected(QueueChannel queue);
		void refused();
	}
	
	public abstract String getName();
	
	public abstract boolean unbind(int port);
	public abstract boolean bind(int port, QueueAcceptListener listener);
	public abstract boolean connect(String name, int port, QueueConnectListener listener);
}
