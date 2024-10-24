package task3.abstracts;

public abstract class Message {
	protected byte[] bytes;
	protected int offset;
	protected int length;
	
	public Message(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}
	
	public Message(byte[] bytes) {
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}
	
	public abstract int getLength();
	public abstract int getOffset();
	public abstract byte[] getBytes();
	
}
