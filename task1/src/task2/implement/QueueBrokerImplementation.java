package task2.implement;

import task1.abstracts.Broker;
import task1.abstracts.Channel;
import task1.implement.BrokerImplementation;
import task1.implement.BrokerManager;
import task2.abstracts.MessageQueue;
import task2.abstracts.QueueBroker;

public class QueueBrokerImplementation extends QueueBroker{
	private String name;
	private BrokerManager brokerManager;
	private Broker broker;
	
	public QueueBrokerImplementation(String name, BrokerManager brokerManager) {
		this.name = name;
		this.brokerManager = brokerManager;
		this.broker = new BrokerImplementation(name, brokerManager);
	}
	
	@Override
	public MessageQueue accept(int port) {
    	Channel serverChannel = this.broker.accept(port);
    	return new MessageQueueImplementation(serverChannel);
    }

	@Override
    public MessageQueue connect(String name, int port) {
    	Channel clientChannel = this.broker.connect(name, port);
    	return new MessageQueueImplementation(clientChannel);
    }
    
	@Override
    public String getName() {
    	return this.name;
    }

}
