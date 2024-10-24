package task2.abstracts;

import task1.abstracts.Broker;

public abstract class QueueBroker {
	public abstract String getName();
	public abstract MessageQueue accept(int port);
	public abstract MessageQueue connect(String name, int port);
}
