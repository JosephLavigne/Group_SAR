package task4.implementation;

import task4.Task;

public class AcceptRunnable implements Runnable{
	
	private RDV rdv;

	public AcceptRunnable(RDV rdv) {
		this.rdv = rdv;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!this.rdv.hasAcceptArrived) {
			Task.getTask().postRunnable(this);
			return;
		}
		this.rdv.accept();
	}

}
