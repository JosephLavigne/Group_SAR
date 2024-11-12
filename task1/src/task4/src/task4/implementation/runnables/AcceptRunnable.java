package task4.implementation.runnables;

import task4.implementation.RDV;
import task4.implementation.TaskImplementation;

public class AcceptRunnable implements Runnable {
	
	private RDV rdv;

	public AcceptRunnable(RDV rdv) {
		this.rdv = rdv;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!this.rdv.hasAcceptArrived) {
			TaskImplementation.getTask().postRunnable(this);
			return;
		}
		this.rdv.accept();
	}

}
