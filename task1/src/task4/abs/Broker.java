package task4.abs;

public abstract class Broker {
	
	public interface AcceptListener {
		void accepted(Channel queue);
	}
	public interface ConnectListener {
		void refused();
		void connected(Channel queue);
	}
	
	public String name;

	public Broker(String name) {
		this.name = name;
	}
	
	public abstract boolean unbind(int port);
	
	public abstract boolean bind(int port, AcceptListener listener);
	
	public abstract boolean connect(String name, int port, ConnectListener listener);
}
