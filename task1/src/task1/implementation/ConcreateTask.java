package task1.implementation;

import task1.Broker;
import task1.Task;

public class ConcreateTask extends Task{

	public ConcreateTask(Broker b, Runnable r) {
		super(b, r);
	}
	
	public static Broker getBroker() {
		Thread currentThread = Thread.currentThread();
		Task task = (Task) currentThread;
		return task.b;
	}
}
