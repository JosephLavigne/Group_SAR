package task1.abstracts;

import task1.implement.ChannelDisconnectedException;

public abstract class Channel {
	public abstract int read(byte[] bytes, int offset, int length) throws ChannelDisconnectedException;
	public abstract int write(byte[] bytes, int offset, int length) throws ChannelDisconnectedException;
	public abstract void disconnect();
	public abstract boolean disconnected();
}
