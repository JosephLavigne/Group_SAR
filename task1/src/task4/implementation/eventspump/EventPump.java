package task4.implementation.eventspump;

import java.util.LinkedList;
import java.util.Queue;

import task4.abs.Task;
import task4.implementation.TaskImplementation;

public class EventPump extends Thread{
	Event currentEvent;
	public boolean isActive;
	private final Queue<Event> eventList;
    public static EventPump instance;

	private EventPump() {
		super("EventPump");
		this.currentEvent = null;
		this.isActive = false;
		this.eventList = new LinkedList<>();
	}
	
	public static EventPump getEventPump() {
	    Thread t = Thread.currentThread();
		if (t instanceof EventPump) {
			return (EventPump) t;
		}
		return null;
	}
	

	static {
		instance = new EventPump();
	}
	
	public static EventPump getInstance() {
        if (instance == null) {
            instance = new EventPump();
        }
        return instance;
    }

	public synchronized void post(Event event) {
		this.eventList.add(event);
		notify();
	}

	public void kill() {
        this.isActive = false;		
	}
	
	public boolean killed() {
		return !this.isActive;
	}
	
	@Override
	public synchronized void run() {
		this.isActive = true;
		while (this.isActive) {
			if (this.eventList.isEmpty()) {
				synchronized(this) {
					try{
						wait();
					} catch(InterruptedException e) {
						// nothing
					}
				}
			} else {
				this.currentEvent = this.eventList.poll();
				TaskImplementation.currentTask = currentEvent.TaskImplementation;
				this.currentEvent.run();
				this.currentEvent = null;
			}
		}
	}
	
	public boolean remove(Event event) {
        return this.eventList.remove(event);
    }
}
