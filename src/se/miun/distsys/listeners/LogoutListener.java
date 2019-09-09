package se.miun.distsys.listeners;

import se.miun.models.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author herja
 */
public interface LogoutListener {
    public void onUserLogout(User user);
}
