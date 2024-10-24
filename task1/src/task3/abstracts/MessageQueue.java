package task3.abstracts;

import java.util.LinkedList;

import task1.abstracts.Channel;

public abstract class MessageQueue {
	protected MessageListener l;
	protected Channel channel;
	
	public MessageQueue(Channel channel) {
		this.channel = channel;
	}
	
	public interface MessageListener{
		abstract void receive(Message msg);
		abstract void sent(Message msg);
		void closed();
	}
	public abstract void setListener(MessageListener l);
	public abstract void send(Message msg);
	public abstract void close();
	public abstract boolean closed();
	public abstract void send(byte[] bytes);
	public abstract void send(byte[] bytes, int offset, int length);

}
