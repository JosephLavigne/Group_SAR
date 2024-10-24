package task4.abstracts;

public abstract class NewTask {
	
		public NewTask() {
			
		}
		public abstract void post(Runnable r);
		private static NewTask task() {
			return null;
		}
		public abstract void kill();
		public abstract boolean killed();


}
