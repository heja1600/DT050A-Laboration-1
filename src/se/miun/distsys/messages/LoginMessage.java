package se.miun.distsys.messages;

import se.miun.models.User;
import se.miun.models.VectorClock;

public class LoginMessage extends Message {
	public LoginMessage(User user) { super(user); }
}
