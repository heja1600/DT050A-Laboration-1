package se.miun.distsys.messages;

import se.miun.models.User;

public class BullyMessage extends Message {
    public Message message;
	public BullyMessage(User user, Message message) { super(user); }
}