package task4.implement;

import task3.abstracts.EventPump;
import task3.implement.EventPumpImplementation;
import task4.abstracts.NewTask;

public class ETask extends NewTask {

	public EventPump pump;
	public boolean killed; // True = task is killed
	private Runnable runnable;
	
	public ETask() {
		this.pump = EventPumpImplementation.getinstance();
		this.killed = false;
		this.runnable = null;
		
	}

	@Override
	public void post(Runnable r) {
		if(this.killed) {
			return;
		}
		this.runnable = r;
		this.pump.post(r);
		
	}

	@Override
	public void kill() {
		this.killed = true;
		
	}

	@Override
	public boolean killed() {
		return this.killed;
	}

}
