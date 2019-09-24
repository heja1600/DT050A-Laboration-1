package se.miun.distsys.listeners;

import se.miun.distsys.messages.ChatMessage;

public interface MessageOutOfOrder {
    public void onOutOfOrder(ChatMessage chatMessage);
}
