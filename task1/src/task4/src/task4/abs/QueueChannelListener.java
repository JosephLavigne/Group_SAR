package task4.abs;

import task4.implementation.messages.Message;

public interface QueueChannelListener {
	void received(byte[] bytes);

	void sent(Message message);
	
	void isQueueChannelClosed();
}
