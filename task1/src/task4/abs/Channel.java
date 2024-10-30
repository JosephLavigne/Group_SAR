package task4.abs;

import task4.implementation.ChannelDisconnectedException;

public abstract class Channel {
	public ReadListener readListener; 
	
	public interface ReadListener {
		void available();
	}

	public interface WriteListener {
		void writtenBytes(int bytesWritten);
	}

	public interface DisconnectListener {
		void disconnected();
	}
	
	public abstract int read(byte[] bytes, int offset, int length) throws ChannelDisconnectedException;
	
	public abstract boolean write(byte[] bytes, int offset, int length, WriteListener writeListener) throws ChannelDisconnectedException;
	
	public abstract void disconnect(DisconnectListener disconnectListener);
	
	public abstract boolean disconnected();
	
	public void setReadListener(ReadListener readListener) {
		this.readListener = readListener;
	}
}
