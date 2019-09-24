package se.miun.distsys.messages;

import se.miun.models.User;

public class ChatMessage extends Message {

	public String chat = "";	
	
	public ChatMessage(User user, String chat) {
		super(user);
		this.chat = chat;
	}
}
