package se.miun.distsys.listeners;

import se.miun.models.User;

public interface ElectionListener {
	public void onElectionChange(User user);
}
