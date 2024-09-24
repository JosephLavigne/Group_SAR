package task1.implementation;

import task1.Broker;
import task1.Channel;
import task1.Task;

public class ChannelManager {
	private Channel client;
	private Channel serveur;
	
	public ChannelManager() {
		this.client = null;
		this.serveur = null;
	}
	
	public void setClient(Channel client) {
		this.client = client;
	}
	
	public void setServeur(Channel serveur) {
		this.serveur = serveur;
	}
	
	public void disconnect(Channel disconnectedChannel) {
		if(disconnectedChannel == client) {
			this.serveur.disconnect();
		}
		else {
			this.client.disconnect();
		}
	}
}
