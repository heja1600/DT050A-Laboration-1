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
    public User(SocketAddress socketAddress, InetAddress inetAddress)
    {
        this.socketAddress = socketAddress;
        this.address = inetAddress;
    }
    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress)
    {
         this.socketAddress = socketAddress;
    }
    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress inetAddress)
    {
         this.address = inetAddress;
    }
}
