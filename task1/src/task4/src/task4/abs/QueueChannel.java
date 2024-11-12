package task4.abs;

import task4.implementation.messages.Message;

public abstract class QueueChannel {

	public abstract boolean send(Message message);

	public abstract void close();

	public abstract boolean isClosed();
	
	public abstract void setListener(QueueChannelListener listener);
}
