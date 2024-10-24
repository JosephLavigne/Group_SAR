package task1.implement;

import task1.abstracts.Channel;

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
