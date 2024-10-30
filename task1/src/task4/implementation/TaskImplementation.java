package task4.implementation;

import task4.Broker;
import task4.Task;

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
