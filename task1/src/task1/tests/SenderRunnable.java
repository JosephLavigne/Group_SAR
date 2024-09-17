package task1.tests;

import task1.Channel;

public class SenderRunnable implements Runnable{
	private Channel channel;
	private byte[] data;
	
	public SenderRunnable(Channel channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}
	
	public void run() {
		int sendedBytes = 0;
		while(sendedBytes < this.data.length) {
			sendedBytes = this.channel.write(this.data, sendedBytes, this.data.length);
		}
		System.out.println("Message : " + this.data.toString() + " sended");
		this.channel.disconnect();
	}
}
