package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


import javax.annotation.PreDestroy;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.listeners.MessageAccepted;
import se.miun.distsys.messages.BullyElected;
import se.miun.distsys.messages.BullyMessage;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.ConfirmLoginMessage;
import se.miun.distsys.messages.DenyElectionMessage;
import se.miun.distsys.messages.ElectionMessage;
import se.miun.distsys.messages.LoginMessage;
import se.miun.distsys.messages.LogoutMessage;
import se.miun.distsys.messages.Message;
import se.miun.distsys.messages.MessageSerializer;
import se.miun.models.Constants;
import se.miun.models.User;
import se.miun.models.Users;




public class GroupCommuncation implements MessageAccepted{
	private int datagramSocketPort = Constants.groupChatPort;
	DatagramSocket datagramSocket = null;	
	User user;
	boolean runGroupCommuncation = true;	
	MessageSerializer messageSerializer = new MessageSerializer();
	//Listeners
	Listeners listeners = null;

	private MessageBuffer messageBuffer = new MessageBuffer(this);
	
	private Thread electionTimeout = null;
	private Thread lookForTimeout = null;
	
	private Integer bullyMessages = null;
	
	private int totalMessagesSent = 0;
	private int totalMessagesRecieved = 0;

	private boolean higherCandidateFound = false;

	private final int electionDelay = 300;
	private final int lookForDelay = 1000;

	public GroupCommuncation()
	{
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
	

	class RecieveThread extends Thread {
 
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
					
					handleMessage(recievedMessage);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		private void handleMessage (Message message) {
			if(message instanceof BullyElected) {
				higherCandidateFound = false;
				System.out.println("bully elected" + message.user.userId);
				fixUserNewBully(message.user);
				messageBuffer.reset();
				listeners.onElectionChange(message.user);
				cancelElectionTimeout();
			}
			else if(message instanceof LoginMessage) {

				if(!user.users.containsKey(message.user.userId)) {
					addUser(message.user);
					listeners.onUserLogin(message.user);
				}
				ConfirmLoginMessage cmsg = new ConfirmLoginMessage(user);
				if(user.bully) {
					cmsg.sequenceNumber = bullyMessages;
				}
				broadcastMessage(cmsg);			
			}
			else if(message instanceof ConfirmLoginMessage) {
				if(!user.users.containsKey(message.user.userId)) {
					addUser(message.user);
					listeners.onUserLogin(message.user);
				}
				if(message.user.bully) {
					messageBuffer.reset();
					messageBuffer.sequenceNumber = message.sequenceNumber + 1;
				}
			}
			else if(message instanceof LogoutMessage) {
				if(user.users.containsKey(message.user.userId)) {
					removeUser(message.user);
					listeners.onUserLogout(message.user);
				}
			}
			else if(message instanceof ElectionMessage && !isSelf(message.user) && !higherCandidateFound) {
				if(message.user.userId < user.userId) {
					broadcastMessage(new DenyElectionMessage(user));
					sendElectionMessage(user);
				} else {
					cancelElectionTimeout();
				}
			}
			else if(message instanceof DenyElectionMessage && !isSelf(message.user)) {
				if(message.user.userId > user.userId) {
					higherCandidateFound = true;
					cancelElectionTimeout();
				}
			}
			else if(message.fromBully) {
				this.handleMessageFromBully(message);
			}
			else if(message instanceof ChatMessage && user.bully) {
				System.out.println("user:" + user.userId +" is bully");
				handleMessageByBully(message);
			}
		}
		
		private void handleMessageByBully(Message message) {
			message.fromBully = true;
			if(message instanceof ChatMessage) {
				message.sequenceNumber = bullyMessages;
				broadcastMessage((ChatMessage) message);
				bullyMessages++;
			}
		}

		private void handleMessageFromBully(Message message) {
			if(isSelf(message.user))
			{
				cancelLookTimeout();
			}
			if(message instanceof ChatMessage) {
				messageBuffer.addMessage((ChatMessage)message);
			}
		}
	}

	public void fixUserNewBully(User newBully) {
		for(Users.Entry<Integer, User>  entry : user.users.entrySet()) {
			if(newBully.userId == entry.getKey())
			{
				User user = entry.getValue();
				user.bully = true;
				user.users.put(user.userId, user);
			}
			else {
				User user = entry.getValue();
				user.bully = false;
				user.users.put(user.userId, user);
			}
		}
		if(isSelf(newBully)) {
			user.bully = true;
			bullyMessages = 0;
		}
	}
	public Thread setTimeout(Runnable runnable, int delay){
	        return new Thread(() -> {
	            try {
	                Thread.sleep(delay);
	                runnable.run();
	            }
	            catch (Exception e){
	                System.err.println(e);
	            }
	        });
	}

	public void broadcastChatMessage(String chat) {
		setLookForNewElectionTimeout();
		totalMessagesSent++;
		ChatMessage chatMessage = new ChatMessage(user, chat);
		broadcastMessage(chatMessage);
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
	private boolean bullyFail() {
		return (boolean) ((int) (Math.random() * 10000) < 1000);
	}
	private void onElectionTimeout()  {
		if(!higherCandidateFound) {
			System.out.println("new Election timeout" + user.userId);
			BullyElected bullyElected = new BullyElected(user);
			bullyElected.fromBully = true;
			broadcastMessage(bullyElected);
		} 
		cancelElectionTimeout();
	}
	
	private void sendElectionMessage(User user) {
		System.out.println("sendElectionMessage:" + user.userId);
		electionTimeout = setTimeout(() -> onElectionTimeout(), electionDelay);
		electionTimeout.start();
		broadcastMessage(new ElectionMessage(user));
	}
	private void setLookForNewElectionTimeout() {
		if(	lookForTimeout == null) {
			cancelLookTimeout();
			lookForTimeout = setTimeout(() -> onLookForNewElectionTimeout(), lookForDelay);
			lookForTimeout.start();
		}
		else {

		}
	}
	public void onLookForNewElectionTimeout() {

		System.out.println("onLookForNewElectionTimeout success" + user.userId);
		sendElectionMessage(user);
		cancelLookTimeout();
	}
	
	private void login() 
	{	
		initUser();
		broadcastMessage(new LoginMessage(user));
	}

	@PreDestroy
	private void logout()
	{
		System.out.println("broadcastning logoutMessage before exiting");
		broadcastMessage(new LogoutMessage(user));
	}
	public void setListeners(Listeners listeners)
	{
		this.listeners = listeners;
	}

	private void initUser() { 
		user = new User(datagramSocket.getLocalSocketAddress(), datagramSocket.getInetAddress()); 
		listeners.onUserLogin(user);
	}

	private void addUser(User user)
	{
		GroupCommuncation.this.user.users.put(user.userId, user.users.get(user.userId));
	}
	
	private void removeUser(User user)
	{
		GroupCommuncation.this.user.users.remove(user.userId);
	}

	public boolean isSelf(User user) { return user.userId == GroupCommuncation.this.user.userId; }

	public User getUser() { return this.user; }
	
	@SuppressWarnings("deprecation")
	public void cancelElectionTimeout() {
		if(electionTimeout != null) {
			electionTimeout.stop();
		}
		electionTimeout = null;
	}
	@SuppressWarnings("deprecation")
	public void cancelLookTimeout(){
		if(lookForTimeout!= null) {
			lookForTimeout.stop();
		}
		lookForTimeout = null;
	}

	@Override
	public void onMessageAccepted(Message message) {

		totalMessagesRecieved++;
		if(user.users.containsKey(message.user.userId)) {
			user.users.get(message.user.userId).sentMessages++;
			
			listeners.onIncomingChatMessage((ChatMessage) message);
		}
		if( totalMessagesSent - 5 > totalMessagesRecieved) {
			totalMessagesSent = totalMessagesRecieved = 0;
			this.onLookForNewElectionTimeout();
		}
		messageBuffer.checkForMessage();
	}
}
