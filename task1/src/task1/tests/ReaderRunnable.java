package task1.tests;

import task1.Channel;

public class ReaderRunnable implements Runnable{
	private Channel channel;
	
	public ReaderRunnable(Channel channel) {
		this.channel = channel;
	}
	
	public void run() {
		int readedBytes = 0;
		byte[] readedData = new byte[50];
		while(!this.channel.disconnected()) {
			readedBytes = this.channel.read(readedData, 0, readedData.length);
			System.out.println("Message : " + readedData.toString() + " readed");
		}
	}
}
