package task1;

public abstract class Task extends Thread{
	protected Broker b;
	protected Runnable r;
	
	public Task(Broker b, Runnable r) {
		this.b = b;
		this.r = r;
	}
	
	public abstract Broker getBroker();
}
