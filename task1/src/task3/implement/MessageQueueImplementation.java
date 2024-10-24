package task3.implement;

import java.util.LinkedList;

import task1.abstracts.Channel;
import task1.implement.ChannelDisconnectedException;
import task3.abstracts.Message;
import task3.abstracts.MessageQueue;

public class MessageQueueImplementation extends MessageQueue {
	
	private MessageListener l;
	private LinkedList<Message> attmessage; // Message en attente d'envoie
	private Thread readthread;
	private Thread writethread;

	public MessageQueueImplementation(Channel channel) {
		super(channel);
		this.l = null;
		this.attmessage = new LinkedList<Message>();
		this.readthread = readthreadcreation();
		this.writethread = writethreadcreation();
	}
	
	private Thread readthreadcreation() {
		Thread t = new Thread(()-> {
			while(!this.channel.disconnected()) {
				byte[] length = new byte[4];
				try {
					//Recuperation de la taille du message
					int reponse = 0;
					while(reponse < 4) {
						reponse += this.channel.read(length, reponse, 4-reponse);
					}
					// CONVERSION BYTES en INT
					int len = (length[0] << 24) | ((length[1] & 0xFF) << 16) | ((length[2] & 0xFF) << 8) | ((length[3] & 0xFF));
					
					//Lecture du Message
					int lecture = 0;
					byte[] message = new byte[len];
					while(lecture < len) {
						lecture += this.channel.read(message, lecture, len - lecture);
					}
					
					new TaskEventImplementation().post(() -> l.receive((Message) new MessageImplementation(message)));
				}catch (ChannelDisconnectedException e) {
						break;
					}
			}
		});
		t.start();
		return t;
		
	}
	
	
	private Thread writethreadcreation() {
		Thread t = new Thread(()-> {
			while(!this.channel.disconnected()) {
				try {
					Message msg = null;
					synchronized(attmessage) {
						if(attmessage.isEmpty() == false) {
							msg = attmessage.poll();
						}
					}
					
					int length = msg.getLength();
					//envoie du Message
					byte[] len = new byte[4]; // taille d'un int
					// CONVERSION INT EN BYTE
					len[0] = (byte) (length >> 24);
					len[1] = (byte) (length >> 16);
					len[2] = (byte) (length >> 8);
					len[3] = (byte) (length);
					
					byte[] message = new byte[length + 4];
					message[0] = len[0];
					message[1] = len[1];
					message[2] = len[2];
					message[3] = len[3];
					
					System.arraycopy(msg.getBytes(), msg.getOffset(), message, 4, length);
					
					int ecriture = 0;
					while(ecriture != length) {
						ecriture += this.channel.write(message, ecriture, length - ecriture);
					}
					
					new TaskEventImplementation().post(()-> l.sent((Message) new MessageImplementation(message)));
				} catch (ChannelDisconnectedException e) {
					break;
				}
			}
			
		});
		t.start();
		return t;
		
	}

	@Override
	public void setListener(MessageListener l) {
		this.l = l ;
		
	}

	@Override
	public void send(Message msg) {
		synchronized(this.attmessage) {
			this.attmessage.add(msg);
		}
	}

	@Override
	public void send(byte[] bytes) {
		send((Message) new MessageImplementation(bytes));
	}
	
	@Override
	public void send(byte[] bytes, int offset, int length) {
		send((Message) new MessageImplementation(bytes, offset, length));
		
	}

	@Override
	public void close() {
		this.channel.disconnect();
	}

	@Override
	public boolean closed() {
		return this.channel.disconnected();
	}

	

}
