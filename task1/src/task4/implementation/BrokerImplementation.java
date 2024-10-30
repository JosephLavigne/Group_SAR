package task4.implementation;

import java.util.HashMap;
import java.util.Map;

import task4.Broker;
import task4.Channel;
import task4.Task;
import task4.implementation.BrokerImplementation.ConnectListener;

public class BrokerImplementation extends Broker{
	
	public interface AcceptListener {
		void accepted(Channel queue);
	}
	public interface ConnectListener {
		void refused();
		void connected(Channel queue);
	}
	private BrokerManager brokerManager;
	private Map<Integer, RDV> bindMap;
	
	public BrokerImplementation(String name, BrokerManager brokerManager) {
		super(name);
		this.brokerManager = brokerManager;
		this.bindMap = new HashMap<Integer, RDV>();
	}
	
	@Override
	public boolean unbind(int port) {
		RDV rdv = this.bindMap.get(port);
		if (rdv == null) {
			return false;
		}
		rdv.disconnect();
		this.bindMap.remove(port);
		return true;
	}
	
	@Override
	public boolean bind(int port, AcceptListener listener) {
		RDV rdv = this.bindMap.get(port);
		if (rdv != null) {
			return false;
		}

		rdv = new RDV(listener, port);
		this.bindMap.put(port, rdv);
		rdv.bind();

		return true;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener connectlistener) {
		// TODO Auto-generated method stub
		BrokerImplementation brokerToConnect = (BrokerImplementation) this.brokerManager.getBroker(name);
		if (brokerToConnect == null) {
			return false;
		}
		TaskImplementation task = new TaskImplementation("");
		task.postRunnable(new ConnectRunnable(port, connectlistener, brokerToConnect));
		return true;
	}
	
	public void connecting(ConnectListener connectListener, int port) {
		RDV rendezVous = this.bindMap.get(port);
		if (rendezVous == null) {
			connectListener.refused();
			return;
		}
		rendezVous.acceptConnect(connectListener);
	}
}
