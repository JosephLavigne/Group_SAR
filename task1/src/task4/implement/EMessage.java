package task4.implement;

import task4.abstracts.Message;

public class EMessage extends Message {

	public EMessage(byte[] bytes, int offset, int length) {
		super(bytes, offset, length);
	}
	
	public EMessage(byte[] bytes) {
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
