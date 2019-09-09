package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import se.miun.models.User;
import java.util.List;

import com.sun.javafx.collections.MappingChange.Map;

import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.LoginListener;
import se.miun.distsys.listeners.LogoutListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;
import se.miun.distsys.messages.MessageSerializer;
import se.miun.distsys.messages.LoginMessage;
import se.miun.distsys.messages.LogoutMessage;

public class GroupCommuncation {
	Map<String, User> loggedInUsers;
	
	private int datagramSocketPort = 9999; //You need to change this!
	// private int loggedInSocketPort = 80;		
	DatagramSocket datagramSocket = null;	
	boolean runGroupCommuncation = true;	
	MessageSerializer messageSerializer = new MessageSerializer();
	
	//Listeners
	ChatMessageListener chatMessageListener = null;
	LoginListener loginListener = null;
	LogoutListener logoutListener = null;
	
	
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
		
		
		private void handleMessage (Message message, User user) {
			if(message instanceof LoginMessage)
			{
				loginListener.onUserLogin(user);
			}
			else if(message instanceof LogoutMessage)
			{
				logoutListener.onUserLogout(user);
			}
			else if(message instanceof ChatMessage) {				
				ChatMessage chatMessage = (ChatMessage) message;				
				if(chatMessageListener != null){
					chatMessageListener.onIncomingChatMessage(chatMessage);
				}
			} else {				
				System.out.println("Unknown message type");
			}			
		}		
	}	
	
	public <T extends Message> void broadcastMessage(T message) 
    {
		try {
			byte[] sendData = messageSerializer.serializeMessage(message);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setLoginListener(LoginListener listener)
    {
		this.loginListener = listener;
	}

	public void setLogoutListenerListener(LogoutListener listener)
    {
		this.logoutListener = listener;
	}
	
	public void setChatMessageListener(ChatMessageListener listener)
    {
		this.chatMessageListener = listener;
	}

	private void login() 
	{	
		broadcastMessage(new LoginMessage());
	}
	private void logout()
	{
		broadcastMessage(new LogoutMessage());
	}
	
}
