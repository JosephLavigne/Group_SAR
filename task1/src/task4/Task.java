package task4;

public abstract class Task extends Thread{
	public Broker b;
	public Runnable r;
	
	public Task(Broker b, Runnable r) {
		this.b = b;
		this.r = r;
	}
	
	public static abstract Broker getBroker();
}
