package task4.implementation;

import java.util.HashSet;

import task4.abs.Broker;
import task4.abs.Task;
import task4.implementation.eventspump.Event;
import task4.implementation.eventspump.EventPump;

public class TaskImplementation extends Task{
	public static TaskImplementation currentTask = null;
	
	private boolean isActive;
	public HashSet<Event> events;
	public EventPump eventPump;
	public String name;
	
	public TaskImplementation(String name) {
		this.name = name;
		this.events = new HashSet<>();
		this.isActive = true;
		eventPump = EventPump.getInstance();
	}

	public void postRunnable(Runnable r) {
		if (!this.isActive) {
			return;
		}
		Event e = new Event(this, r);
		events.add(e);
		eventPump.post(e);
	}
	
	public static TaskImplementation getTask() {
		return currentTask;
	}
	
	public void kill() {
		this.isActive = false;
		for (Event e : events) {
			eventPump.remove(e);
		}
	}
	
	public boolean isActive() {
		return this.isActive;
	}
}
