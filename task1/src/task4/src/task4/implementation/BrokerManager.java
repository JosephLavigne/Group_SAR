package task4.implementation;

import java.util.HashMap;
import java.util.Map;

import task4.abs.Broker;

public class BrokerManager {
	private Map<String, Broker> brokerMap;
	
	public BrokerManager() {
		this.brokerMap = new HashMap<String, Broker>();
	}
	
	public synchronized void addBroker(String name, Broker broker) {
		Broker brokerWithSameName = this.brokerMap.get(name);
		if(brokerWithSameName != null) {
			throw new IllegalArgumentException("Broker with the name " + name + " already exists in this broker manager.");
		}
		else {
			synchronized(this.brokerMap) {
				this.brokerMap.put(name, broker);
			}
		}
	}
	
	public synchronized Broker removeBroker(String name) {
		synchronized(this.brokerMap) {
			return this.brokerMap.remove(name);
		}
	}
	
	public synchronized Broker getBroker(String name) {
		return this.brokerMap.get(name);
	}
}
