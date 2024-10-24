package task4.abstracts;

public abstract class Channel {
	
	public abstract boolean write(byte[] bytes, int offset, int length, WriteListener listener);
	
	public abstract int read(byte[] bytes, int offset, int length);
	
	public interface ReadListener {
		void available();
	}
	
	public interface WriteListener {
		void written(int bytesWritten);
	}
	
	public interface DisconnectListener {
		void disconnected();
	}

	public abstract void setReadListener(ReadListener listener);
	
	public abstract boolean disconnected();

	public abstract void disconnect(DisconnectListener listener);

}
