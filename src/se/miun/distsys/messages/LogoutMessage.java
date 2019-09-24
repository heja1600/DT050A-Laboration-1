package se.miun.distsys.messages;

import se.miun.models.User;

public class LogoutMessage extends Message {	
	public LogoutMessage(User user) { super(user); }
}
