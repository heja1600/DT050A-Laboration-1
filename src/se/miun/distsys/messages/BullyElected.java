package se.miun.distsys.messages;

import se.miun.models.User;

public class BullyElected extends Message {
	public BullyElected(User user) { super(user); }
}