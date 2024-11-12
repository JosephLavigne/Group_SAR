package task4.implementation.eventspump;

import task4.implementation.TaskImplementation;

public class Event implements Runnable {
	TaskImplementation TaskImplementation;
	Runnable runnable;

	public Event(TaskImplementation TaskImplementation, Runnable runnable) {
		this.TaskImplementation = TaskImplementation;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		if (this.TaskImplementation != null && this.TaskImplementation.isAlive()) {
			runnable.run();
		}
	}
}