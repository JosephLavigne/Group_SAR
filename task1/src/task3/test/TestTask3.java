package task3.test;

import java.util.concurrent.Semaphore;

import task1.implement.BrokerManager;
import task3.abstracts.Message;
import task3.abstracts.MessageQueue.MessageListener;
import task3.abstracts.QueueBroker;
import task3.abstracts.QueueBroker.AcceptListener;
import task3.abstracts.QueueBroker.ConnectListener;
import task3.implement.MessageQueueImplementation;
import task3.implement.QueueBrokerImplementation;

public class TestTask3 {
	
	
	public TestTask3() {
		launchTest();
	}
	
	private void launchTest() {
		try {
			this.test1();
			this.test2(2, 2);
		} catch (InterruptedException e) {
			System.out.println("Task 3 failed");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Task 3 failed");
			e.printStackTrace();
		}
		System.out.println("Task 3 succes");
	}
	
	public static void test1() throws Exception {
		
		BrokerManager bm = new BrokerManager();
		
		Semaphore sm = new Semaphore(0);
		
		QueueBroker client = new QueueBrokerImplementation("client", bm);
		QueueBroker server = new QueueBrokerImplementation("server", bm);
		int connection_port = 6969;
		client.connect("server", connection_port, new ConnectListener() {
			@Override
			public void connected(MessageQueueImplementation queue) {
				queue.setListener(new MessageListener() {
					@Override
					public void receive(Message msg) {
						queue.close();
					}
	
					@Override
					public void closed() {
						sm.release();
					}

					@Override
					public void sent(Message msg) {
						// nothing
						
					}
				});
	
				queue.send("Hello world!".getBytes());
			}
	
			@Override
			public void refused() {
			}

		});
		
		
		server.bind(connection_port, new AcceptListener() {
			@Override
			public void accepted(MessageQueueImplementation queue) {
				queue.setListener(new MessageListener() {

					@Override
					public void closed() {
						server.unbind(connection_port);
						sm.release();
					}

					@Override
					public void receive(Message msg) {
						queue.send(msg);
						queue.close();
						
					}

					@Override
					public void sent(Message msg) {
						// nothing
						
					}
				});
			}
		});
		sm.acquire();
		//test 1 terminer
	
	}
	
	private static void echo_client(QueueBroker client, int connection_port, Semaphore sm) {
		client.connect("server", connection_port, new ConnectListener() {
			@Override
			public void connected(MessageQueueImplementation queue) {
				queue.setListener(new MessageListener() {
					@Override
					public void receive(Message msg) {
						queue.close();
					}

					@Override
					public void closed() {
						sm.release();
					}

					@Override
					public void sent(Message msg) {
						// TODO Auto-generated method stub
						
					}
				});

				queue.send("Hello world!".getBytes());
			}

			@Override
			public void refused() {
			}

		});
	}
	
	private static void echo_server(QueueBroker server, int connection_port) {
		server.bind(connection_port, new AcceptListener() {
			@Override
			public void accepted(MessageQueueImplementation queue) {
				queue.setListener(new MessageListener() {
					@Override
					public void receive(Message msg) {
						queue.send(msg);
						queue.close();
					}

					@Override
					public void closed() {
						server.unbind(connection_port);
					}

					@Override
					public void sent(Message msg) {
						//nothing
						
					}
				});
			}
		});
	}
	
	public static void test2(int nbre_clients, int test_number) throws InterruptedException {
		
		BrokerManager bm2 = new BrokerManager();
		
		Semaphore sm = new Semaphore(1 - nbre_clients); // Allows to block the execution until the echo message

		int connection_port = 6969;
		QueueBroker server = new QueueBrokerImplementation("server", bm2);

		for (int i = 0; i < nbre_clients; i++) {
			QueueBroker client = new QueueBrokerImplementation("client" + i, bm2);
			echo_client(client, connection_port, sm);
			echo_server(server, connection_port);
		}

		sm.acquire(); // Waits the end of the test
	}

}
