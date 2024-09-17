package task1.tests;
import task1.*;

public class Test {
	public static void main(String[] args) {
		testEchoServer();
	}
	
	public static void testEchoServer() {
		Broker broker = new ConcreateBroker("localhost");
		Channel server = broker.connect("localhost", 5555);
		Channel client = broker.accept(5555);
		byte[] data1 = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
		Runnable readerRunnable = new ReaderRunnable(server);
		Runnable senderRunnable = new SenderRunnable(client, data1);
		Task taskReader = new ConcreateTask(broker, readerRunnable);
		taskReader.run();
		
		Task taskSender = new ConcreateTask(broker, senderRunnable);
		taskSender.run();
	}
}
