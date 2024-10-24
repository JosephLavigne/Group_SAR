package task3.implement;

import task3.abstracts.Message;

public class MessageImplementation extends Message{
	
	public MessageImplementation(byte[] bytes, int offset, int length) {
		super(bytes, offset, length);
	}
	
	public MessageImplementation(byte[] bytes) {
		super(bytes);
	}

	@Override
	public int getLength() {
		return this.length;
	}

	@Override
	public int getOffset() {
		return this.offset;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}


}
