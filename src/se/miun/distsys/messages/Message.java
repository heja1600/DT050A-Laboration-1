package se.miun.distsys.messages;

import java.io.Serializable;

import se.miun.models.User;

public class Message implements Serializable {
    public User user;
    public Message(User user) { this.user = user; }
}
