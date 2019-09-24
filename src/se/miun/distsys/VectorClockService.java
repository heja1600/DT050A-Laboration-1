package se.miun.distsys;

import java.util.ArrayList;
import java.util.List;

import se.miun.distsys.listeners.MessageAccepted;
import se.miun.distsys.listeners.MessageOutOfOrder;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;
import se.miun.models.User;
import se.miun.models.VectorClock;

public class VectorClockService {
    private MessageAccepted messageAccepted = null;
    private MessageOutOfOrder messageOutOfOrder = null;
    List<ChatMessage> chatMessages = new ArrayList<>();
    
    public VectorClockService(MessageAccepted messageAccepted, MessageOutOfOrder messageOutOfOrder) {
        this.messageAccepted = messageAccepted;
        this.messageOutOfOrder = messageOutOfOrder;
     }


    // should probably compare each userId instead and then calculate total differences
	private int calculateVectorOrder(VectorClock vectorClock)
	{
		int value = 0;
		for (VectorClock.Entry<Integer, Integer> entry : vectorClock.entrySet()) {
		    value += vectorClock.get(entry.getKey());
		}
		return value;
    }

    public void addMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }

    public void checkChatMessages(User user) {
        System.out.println("Currently " + chatMessages.size() + " in queue");
        int expectedVectorOrder = calculateVectorOrder(user.vectorClock) + 1;
        
        for(int i = 0; i < chatMessages.size(); i++) {
            int chatVectorOrder = calculateVectorOrder(chatMessages.get(i).user.vectorClock);
            if(chatVectorOrder == expectedVectorOrder){
                ChatMessage chatMessage = chatMessages.get(i);
                chatMessages.remove(i);
                messageAccepted.onMessageAccepted(chatMessage);
                return;
            }
            // if out of order;
            else if(chatVectorOrder < expectedVectorOrder - 1) {
                ChatMessage outOfOrder = chatMessages.get(i);
                chatMessages.remove(i);
                messageOutOfOrder.onOutOfOrder(outOfOrder);
                i--;
            }
        }
    }

    public static <T extends Message> void incremenetMessageIndex(T message) {
        int increment = message.user.vectorClock.get(message.user.userId) + 1;
        message.user.vectorClock.put(message.user.userId, increment);
    }

    public static void printVectorOrder(User user) {
       System.out.println(getVectorOrder(user));
    }
    
    public static String getVectorOrder(User user) {
        String tmp = "";
        for (VectorClock.Entry<Integer, Integer> entry : user.vectorClock.entrySet()) {
		    tmp += "\n" + "user:" + entry.getKey() + " has:" + user.vectorClock.get(entry.getKey());
        }
        return tmp;
    }
}