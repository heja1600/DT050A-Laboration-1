/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.distsys.listeners;

import se.miun.models.User;

/**
 *
 * @author herja
 */
public interface LoginListener {
    public void onUserLogin(User user);
}
