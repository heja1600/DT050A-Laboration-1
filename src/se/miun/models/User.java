
package se.miun.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;


public class User implements Serializable {

    public SocketAddress socketAddress;
    public InetAddress address; 
    public int userId;
    public Users users = new Users();
    public boolean bully = false;
    public int sentMessages = 0;
    public User() { this(null, null); }
    
    public User(SocketAddress socketAddress, InetAddress inetAddress)
    {
        this.socketAddress = socketAddress;
        this.address = inetAddress;
        this.userId = (int) (Math.random() * 10000); // for now XDD
        users.put(userId, this);
    }
}
