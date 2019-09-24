/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;

/**
 *
 * @author herja
 */


public class User implements Serializable {

    public SocketAddress socketAddress;
    public InetAddress address; 
    public int userId;
    public VectorClock vectorClock;
    
    public User() { this(null, null); }

    public User(SocketAddress socketAddress, InetAddress inetAddress)
    {
        this.socketAddress = socketAddress;
        this.address = inetAddress;
        this.userId = (int) (Math.random() * 10000); // for now XDD
        this.vectorClock = new VectorClock();
        vectorClock.put(userId, 0);
    }
}
