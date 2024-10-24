package task3.implement;

import java.util.HashMap;

import task1.abstracts.Broker;
import task1.abstracts.Channel;
import task1.implement.BrokerImplementation;
import task1.implement.BrokerManager;
import task3.abstracts.QueueBroker;

public class QueueBrokerImplementation extends QueueBroker {
	
	Broker b;
	private HashMap<Integer, Thread> acceptport;
	BrokerManager bm;

	public QueueBrokerImplementation(String name, BrokerManager bm) {
		super(name);
		this.bm = bm;
		b = new BrokerImplementation(name, bm);
		this.acceptport = new HashMap<Integer, Thread>();
		
		
	}

	@Override
	public synchronized boolean bind(int port, AcceptListener listener) {
		if(!this.acceptport.containsKey(port)) {
			Thread t = new Thread(()-> {
				while(true) {
					Channel c = this.b.accept(port);
					MessageQueueImplementation q = new MessageQueueImplementation(c);
					TaskEventImplementation task = new TaskEventImplementation();
					task.post(()-> listener.accepted(q));
				}
			});
			acceptport.put(port, t);
			t.start();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public synchronized boolean unbind(int port) {
		if(this.acceptport.containsKey(port)) {
			Thread t = acceptport.get(port);
			t.interrupt();
			acceptport.remove(port);
			return true;
			
		}
		return false;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		BrokerManager bm = this.bm;
		Broker b = bm.getBroker(name);
		if(b != null) {
			Thread t = new Thread(() -> {
				Channel c = (Channel) b.connect(name, port);
				MessageQueueImplementation q = new MessageQueueImplementation(c);
				TaskEventImplementation task = new TaskEventImplementation();
				task.post(()-> listener.connected(q));
			});
			t.start();
			return true;
		}
		return false;
	}
	
	
	public String name() {
		return this.b.getname();
	}
	


}
