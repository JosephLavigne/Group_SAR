/*package task4.implementation.messages;

import java.util.LinkedList;

import task4.abs.Channel.WriteListener;
import task4.abs.QueueChannelListener;
import task4.implementation.ChannelImplementation;
import task4.implementation.TaskImplementation;

public class MsgWriteListener implements WriteListener{
	
	private int msgOffset;
	private byte[] msgLengthBuffer;
	private ChannelImplementation channel;
	private LinkedList<Message> messageList;
	public Message currentMessage;

	public QueueChannelListener queueChannelListener;
	
	private ListenerState state;
	private TaskImplementation sendingTask;
	private Runnable taskRunnable;

	public MsgWriteListener(LinkedList<Message> messageList, Message currentMessage, TaskImplementation sendingTask, Runnable taskRunnable) {
		this.msgLengthBuffer = new byte[4];
		this.msgOffset = 0;
		this.messageList = messageList;
		this.currentMessage = currentMessage;
		this.queueChannelListener = null;
		this.state = ListenerState.WRITING_SIZE;
		this.sendingTask = sendingTask;
		this.taskRunnable = taskRunnable;
	}

	public void writtenBytes(int bytesWritten) {
		if (channel.disconnected()) {
			return;
		}
		
		this.msgOffset += bytesWritten;
		if (this.state == ListenerState.WRITING_SIZE) {
            if (this.msgOffset == 4) {
            	this.msgOffset = 0;
                this.state = ListenerState.WRITING_MSG;
                channel.write(this.currentMessage.getBytes(), 0, this.currentMessage.getLength(), this);
            } else {
                channel.write(this.msgLengthBuffer, this.msgOffset, Message.MSG_LENGTH_SIZE - this.msgOffset, this);
            }
        }
		
		if(this.state == ListenerState.WRITING_MSG) {
                if (this.msgOffset == this.currentMessage.getLength()) {
                	this.msgOffset = 0;
                	this.state = ListenerState.WRITING_SIZE;
                    this.queueChannelListener.sent(this.currentMessage);
                    this.currentMessage = null;
                    if (!this.messageList.isEmpty()) {
                    	this.messageList.poll();
                        this.sendingTask.postRunnable(this.taskRunnable);
                    }
                } else {
                    channel.write(this.currentMessage.getBytes(), this.msgOffset, this.currentMessage.getLength() - this.msgOffset, this);
                }
            }
		}
}
*/