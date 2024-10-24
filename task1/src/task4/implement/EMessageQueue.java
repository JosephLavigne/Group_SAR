package task4.implement;

import task4.abstracts.Channel;
import task4.abstracts.Message;
import task4.abstracts.MessageQueue;

public class EMessageQueue extends MessageQueue {

	public EMessageQueue(Channel channel) {
		super(channel);
	}

	@Override
	public void setListener(MessageListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean closed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void send(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		
	}

}
