package task4.implement;

import task4.abstracts.Broker;
import task1.implement.CircularBuffer;
import task4.abstracts.Channel;

public class EChannel extends Channel {
	
	int port;
	boolean dangling;
	boolean disconnect;
	CircularBuffer in; // read
	CircularBuffer out; // write
	String rname;
	Channel rch; // remote channel
	Broker b;
	
	public EChannel(Broker b,CircularBuffer read, CircularBuffer write) {
		this.b = b;
		this.disconnect = false;
		this.in = read;
		this.out = write;
	}

	@Override
	public boolean write(byte[] bytes, int offset, int length, WriteListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int read(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean disconnected() {
		return this.disconnect;
	}

	@Override
	public void disconnect(DisconnectListener listener) {
		// TODO Auto-generated method stub
		
	}

}
