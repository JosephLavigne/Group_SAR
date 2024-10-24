package task3.abstracts;

public abstract class TaskEvent {
	
		public TaskEvent() {
			
		}
		public abstract void post(Runnable r);
		private static TaskEvent task() {
			return null;
		}
		public abstract void kill();
		public abstract boolean killed();


}
