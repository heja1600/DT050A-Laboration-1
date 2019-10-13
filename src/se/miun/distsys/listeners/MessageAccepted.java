package se.miun.distsys.listeners;

import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;

public interface MessageAccepted {
    public void onMessageAccepted(Message message);
}
