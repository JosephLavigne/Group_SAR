package task1.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import task1.Broker;
import task1.Channel;

public class ConcreateBroker extends Broker{
	private BrokerManager brokerManager;
	private Map<Integer, RDV> rdvMap;

	public ConcreateBroker(String name, BrokerManager brokerManager) {
		super(name);
		this.brokerManager = brokerManager;
		this.rdvMap = new HashMap<Integer, RDV>();
	}

	@Override
	public synchronized Channel accept(int port) {
		if(this.rdvMap.containsKey(port)) {
			return this.rdvMap.get(port).accept(this);
		}
		else {
			RDV createdRDV = new RDV();
			this.rdvMap.put(port, createdRDV);
			return createdRDV.accept(this);
		}
	}

	@Override
	public synchronized Channel connect(String name, int port) {
		// TODO Auto-generated method stub
		if(this.rdvMap.containsKey(port)) {
			return this.rdvMap.get(port).connect(this);
		}
		else {
			RDV createdRDV = new RDV();
			this.rdvMap.put(port, createdRDV);
			return createdRDV.connect(this);
		}
	}
	
	public synchronized void disconnectPort(int port) {
		this.rdvMap.remove(port);
		notifyAll();
	}
}
