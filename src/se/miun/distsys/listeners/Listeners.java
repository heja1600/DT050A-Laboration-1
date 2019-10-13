package se.miun.distsys.listeners;

import java.awt.event.ActionListener;

public interface Listeners extends LoginListener, 
                                    LogoutListener, 
                                    ChatMessageListener, 
                                    ActionListener,
                                    ElectionListener,
                                    Shutdown{ }