package se.miun.distsys;

import java.util.ArrayList;
import java.util.List;

import se.miun.distsys.listeners.MessageAccepted;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;

public class MessageBuffer{
	MessageAccepted messageAccepted = null;
	List<ChatMessage> list;
	public int sequenceNumber;
	public MessageBuffer(MessageAccepted messageAccepted) {
		this.messageAccepted = messageAccepted;
		reset();
	}
	public void addMessage(ChatMessage message) {
		list.add(message);
		checkForMessage();
	}
	public void checkForMessage() {
		
		for(int i = 0; i < list.size(); i++) {
			Message m = list.get(i);
			if(m.sequenceNumber == this.sequenceNumber) {
				list.remove(i);
				sequenceNumber++;
				messageAccepted.onMessageAccepted(m);
				break;
			}
			else if(m.sequenceNumber < this.sequenceNumber) {
				list.remove(i);
				i--;
			}
		}

	}
	public void reset() {
		sequenceNumber = 0;
		list = new ArrayList<> ();
	}
}
