package se.miun.distsys.listeners;

import se.miun.distsys.messages.ChatMessage;

public interface MessageAccepted {
    public void onMessageAccepted(ChatMessage chatMessage);
}
