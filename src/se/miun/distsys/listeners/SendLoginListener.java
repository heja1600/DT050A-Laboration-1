/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.distsys.listeners;

import se.miun.models.User;

/**
 * class to send to specific new inlogged user all users from every host logged in
 * @author herja
 */
public interface SendLoginListener {
    public void onSendLoginListener(User user);
}
