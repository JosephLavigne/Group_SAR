package task1.abstracts;

import java.util.HashMap;

import task1.implement.RDV;

public abstract class Broker {
	public String name;
	public HashMap<Integer, RDV> portRDV;
	public Broker(String name) {
		this.name = name;
	}
	public abstract Channel accept(int port);
	public abstract Channel connect(String name, int port) ;
	public abstract String getname();
	
}
