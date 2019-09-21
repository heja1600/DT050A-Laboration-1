/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.models;

import java.net.InetAddress;
import java.net.SocketAddress;

/**
 *
 * @author herja
 */


public class User {

    private SocketAddress socketAddress;
    private InetAddress address; 
    private String userId;
    private VectorClock vectorClock;

    public User(SocketAddress socketAddress, InetAddress inetAddress)
    {
        this.socketAddress = socketAddress;
        this.address = inetAddress;
    }
    public User(SocketAddress socketAddress, InetAddress inetAddress, String userId)
    {
        this.socketAddress = socketAddress;
        this.address = inetAddress;
        this.userId = userId;
    }
    public SocketAddress getSocketAddress() { return this.socketAddress; }

    public void setSocketAddress(SocketAddress socketAddress) { this.socketAddress = socketAddress; }

    public InetAddress getAddress() { return this.address; }

    public void setAddress(InetAddress inetAddress) { this.address = inetAddress; }

    public void setUserId(final String userId) { this.userId = userId; }

    public String getUserId() { return this.userId; }

    public VectorClock getVectorClock() { return this.vectorClock; };
}
