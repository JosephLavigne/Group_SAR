package task4.implement;

import java.util.concurrent.LinkedBlockingQueue;

import task4.abstracts.EventPump;


// Tout les synchronized ne sont plus nécessaire étant donner qu'on ne fonctionne plus avec plusieurs threads

public class EEventPump extends EventPump {

	static EEventPump self;
	static LinkedBlockingQueue<Runnable> queue;
	static boolean alive; // true = pump alive / false = pump is dead
	
	static {
		self = new EEventPump();
		self.alive = true;
		self.start();
	}
	
	private EEventPump() {
		this.queue = new LinkedBlockingQueue<Runnable>(); 
	}
	
	public synchronized static EEventPump getinstance() {
		return EEventPump.self;
	}

	@Override
	public void post(Runnable r) { // Car Post va etre appeler par le monde Threadé
		if(!this.alive) {
			return;
		}
		try {
			self.queue.put(r);
			notify();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean remove(Runnable r) { // supprimer l'event dans la queue
		if(!this.alive) {
			return false;
		}
		return this.queue.remove(r);
	}
	

	@Override
	public void run() {
		Runnable r;
		while(alive) {
			r = queue.poll();
			while (r!=null) {
				r.run();
				r = queue.poll();
			}
			this.sleep();
			}
		
	}
	
	
	private void sleep() {
		try {
			wait();
		} catch (InterruptedException ex){
		// nothing to do here.
		}
	}

	@Override
	public void kill() {
		this.alive = false;
	}

}
