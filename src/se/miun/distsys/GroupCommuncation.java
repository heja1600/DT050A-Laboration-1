package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

import se.miun.models.User;
import java.util.Map;

import javax.annotation.PreDestroy;

import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;
import se.miun.distsys.messages.MessageSerializer;
import se.miun.distsys.messages.SendLoginMessage;
import se.miun.distsys.messages.LoginMessage;
import se.miun.distsys.messages.LogoutMessage;

public class GroupCommuncation {
	Map<String, User> loggedInUsers = new HashMap<String, User>();
	
	private int datagramSocketPort = 2525; //You need to change this!
	// private int loggedInSocketPort = 80;		
	DatagramSocket datagramSocket = null;	
	boolean runGroupCommuncation = true;	
	MessageSerializer messageSerializer = new MessageSerializer();
	
	//Listeners
	Listeners listeners = null;
	
	
	public GroupCommuncation() {			
		try {
			runGroupCommuncation = true;				
			datagramSocket = new MulticastSocket(datagramSocketPort);
			RecieveThread rt = new RecieveThread();
			rt.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
			runGroupCommuncation = false;	
			logout();	
	}
	

	class RecieveThread extends Thread{
		
		@Override
		public void run() {
			byte[] buffer = new byte[65536];		
			DatagramPacket recievePacket = new DatagramPacket(buffer, buffer.length);

			login();

			while(runGroupCommuncation) {
				try {
					datagramSocket.receive(recievePacket);									
					byte[] packetData = recievePacket.getData();	
                                        

					Message recievedMessage = messageSerializer.deserializeMessage(packetData);	

					handleMessage(recievedMessage, new User(recievePacket.getSocketAddress(), recievePacket.getAddress()));
				

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		private boolean addUser(User user)
		{
			if(!loggedInUsers.containsValue(user.getAddress().toString())) 
			{
				loggedInUsers.put(user.getAddress().toString(), user);
				return true;
			}
			else return false;
		}
		
		private void handleMessage (Message message, User user) {
			if(message instanceof LoginMessage)
			{
				listeners.onUserLogin(user);

				addUser(user);

				sendMessage(new SendLoginMessage(), user.getAddress());
			}
			else if(message instanceof SendLoginMessage)
			{
				if(addUser(user))
				{
					System.out.println(loggedInUsers.size());
				}
				listeners.onSendLoginListener(user);
			}
			else if(message instanceof LogoutMessage)
			{
				loggedInUsers.remove(user.getAddress().toString());

				listeners.onUserLogout(user);
			}
			else if(message instanceof ChatMessage) {				
				ChatMessage chatMessage = (ChatMessage) message;				
				if(listeners != null){
					listeners.onIncomingChatMessage(chatMessage);
				}
			} else {				
				System.out.println("Unknown message type");
			}			
		}		
	}	
	
	public <T extends Message> void broadcastMessage(T message) 
    {
		try {
			sendMessage(message, InetAddress.getByName("255.255.255.255"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public <T extends Message> void sendMessage(T message, InetAddress inetAddress)
	{
		try {
			byte[] sendData = messageSerializer.serializeMessage(message);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	private void login() 
	{	
		broadcastMessage(new LoginMessage());
	}
	@PreDestroy
	private void logout()
	{
		System.out.println("broadcastning logoutMessage before exiting");
		broadcastMessage(new LogoutMessage());
	}
	public void setListeners(Listeners listeners)
	{
		this.listeners = listeners;
	}

	public Map<String, User> getUsers()
	{
		return this.loggedInUsers;
	}
}
