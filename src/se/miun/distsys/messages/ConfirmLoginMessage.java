package se.miun.distsys.messages;

import se.miun.models.User;

public class ConfirmLoginMessage extends Message {
	public ConfirmLoginMessage(User user) { super(user); }
}