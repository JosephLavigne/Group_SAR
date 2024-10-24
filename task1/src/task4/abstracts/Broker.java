package task4.abstracts;

public abstract class Broker {
	
	public abstract boolean unbind(int port);
	public abstract boolean bind(int port, AcceptListener listener);
	public abstract boolean connect(String name, int port, ConnectListener listener);
	
	public interface AcceptListener {
		void accepted(Channel queue);
	}
	
	public interface ConnectListener {
		void connected(Channel queue);
		void refused();
	}

	public abstract String getname();

}
