package task4.implementation.messages;

public class Message {
	
	public final static int MSG_LENGTH_SIZE = 4;

	private byte[] bytes;
	private int offset;
	private int length;
	
	public Message() {
		this.bytes = null;
    	this.offset = 0;
    	this.length = 0;
	}

	public Message(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}

	public Message(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}
	
	public Message(String message) {
		this.bytes = message.getBytes();
    	this.offset = 0;
    	this.length = this.bytes.length;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public void updateOffset(int offset) {
		this.offset += offset;
	}
	
	public boolean isMessageTotallySend() {
		return length == offset;
	}
}