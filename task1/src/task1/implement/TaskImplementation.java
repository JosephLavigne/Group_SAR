package task1.implement;

import task1.abstracts.Broker;
import task1.abstracts.Task;

public class TaskImplementation extends Task{
	private String id;
	private Broker broker;
	private Runnable runnable;
	private Thread thread;
	

	public TaskImplementation(Broker b, Runnable r) {
		this.runnable = r;
		this.broker = b;
		this.id = java.util.UUID.randomUUID().toString();
		this.thread = new Thread(this.runnable, this.id);
		this.thread.start();
	}
	
	public static Broker getBroker() {
		Thread currentThread = Thread.currentThread();
		TaskImplementation task = (TaskImplementation) currentThread;
		return task.broker;
	}
}
