package se.miun.distsys.messages;

import se.miun.models.User;

public class DenyElectionMessage extends Message {
	public DenyElectionMessage(User user) { super(user); }
}