package task1.implementation;

import java.util.HashMap;
import java.util.Map;

import task1.Broker;
import task1.Channel;

public class BrokerImplementation extends Broker{
	private BrokerManager brokerManager;
	private Map<Integer, RDV> acceptMap;

	public BrokerImplementation(String name, BrokerManager brokerManager) {
		super(name);
		this.brokerManager = brokerManager;
		this.acceptMap = new HashMap<Integer, RDV>();
	}

	@Override
	public Channel accept(int port) {
		RDV rendezVous = null;
		synchronized(this.acceptMap) {
			rendezVous = this.acceptMap.get(port);
			if(rendezVous != null) {
				throw new IllegalStateException("Le port : " + port + " est déjà en train d'accepter une connexion.");
			}
			rendezVous = new RDV();
			this.acceptMap.put(port, rendezVous);
			this.acceptMap.notifyAll();
		}
		Channel serverChannel = rendezVous.accept(this, port);
		return serverChannel;
	}

	@Override
	public Channel connect(String name, int port) {
		// TODO Auto-generated method stub
		Broker brokerToConnect = this.brokerManager.getBroker(name);
		if (brokerToConnect == null) {
			return null;
		}
		BrokerImplementation broker = (BrokerImplementation) brokerToConnect;
		Channel clientChannel = broker.connect(name, port);
		return clientChannel;
	}
	
	public Channel connecting(BrokerImplementation connectingBroker, int port) {
		RDV rendezVous = null;
		synchronized(this.acceptMap) {
			rendezVous = this.acceptMap.get(port);
			while(rendezVous != null) {
				try {
					this.acceptMap.wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				rendezVous = this.acceptMap.get(port);
			}
			this.acceptMap.remove(port);
		}
		Channel clientChannel = rendezVous.connect(connectingBroker, port);
		return clientChannel;
	}
}
