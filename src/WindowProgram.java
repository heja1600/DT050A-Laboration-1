import java.awt.EventQueue;

import javax.swing.JFrame;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.models.User;
import se.miun.models.Users;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;


public class WindowProgram extends BaseProgram implements Listeners{

	private JFrame frame;
	private JTextPane txtpnChat = new JTextPane();
	private JTextPane txtpnMessage = new JTextPane();
	private JTextPane userList = new JTextPane();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {	
				try {
					WindowProgram window = new WindowProgram();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WindowProgram() {
		initializeFrame();
		initGroupCommunication();
		addBots();
	}
 
	private void initializeFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(txtpnChat);
		txtpnChat.setEditable(false);	
		txtpnChat.setText("--== Group Chat ==--");



		
		txtpnMessage.setText("Message");
		frame.getContentPane().add(txtpnMessage);
		
		JButton btnSendChatMessage = new JButton("Send Chat Message");
		btnSendChatMessage.addActionListener(this);
		btnSendChatMessage.setActionCommand("send");
		frame.getContentPane().add(btnSendChatMessage);

		JButton btnAddBot = new JButton("Add Bot");
		btnAddBot.addActionListener(this);
		btnAddBot.setActionCommand("addBot");
		frame.getContentPane().add(btnAddBot);

		JButton btnRemoveBot = new JButton("Remove Bot");
		btnRemoveBot.addActionListener(this);
		btnRemoveBot.setActionCommand("removeBot");
		frame.getContentPane().add(btnRemoveBot);
		
		JButton btnElection = new JButton("new election");
		btnElection.addActionListener(this);
		btnElection.setActionCommand("election");
		frame.getContentPane().add(btnElection);


		JScrollPane userListScrollPane = new JScrollPane();
		frame.getContentPane().add(userListScrollPane);
		userListScrollPane.setViewportView(userList);
		userList.setEditable(false);
		userList.setText("--== User List ==--");

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
				gc.shutdown();
				terminateBots();
	        }
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
            gc.broadcastChatMessage(txtpnMessage.getText());
		}	
		else if (event.getActionCommand().equalsIgnoreCase("addBot")) {
			addBot();
		}
		else if (event.getActionCommand().equalsIgnoreCase("removeBot")) {
			removeBot();
		}
		else if (event.getActionCommand().equalsIgnoreCase("election")) {
			gc.onLookForNewElectionTimeout();
		}
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {	
		appendToTextChat(getUserInfo(chatMessage.user) + "("+ chatMessage.sequenceNumber + "): " + chatMessage.chat );
		logger.log(chatMessage);
		setUsers();
	}

	@Override
	public void onUserLogout(User user) {
		appendToTextChat("User left the groupchat:" + user.userId);
		setUsers();
	}

	@Override
	public void onUserLogin(User user) {
		appendToTextChat("User joined groupchat:" + user.userId);
		setUsers();
	}

	private void setUsers()
	{
		String tmp = "";
		for (Users.Entry<Integer, User> entry : gc.getUser().users.entrySet()) {
			tmp += "user:";
			tmp += entry.getKey();
			tmp += ", sent (" + gc.getUser().users.get(entry.getKey()).sentMessages + ")";
			tmp += ", bully(" + (gc.getUser().users.get(entry.getKey()).bully ? "true" : "false") + ")";
			tmp += "\n";
		}
		userList.setText(tmp);
		
	}
	private void appendToTextChat(String chat) {
		txtpnChat.setText(txtpnChat.getText() +  "\n" + chat);	
		txtpnChat.setCaretPosition(txtpnChat.getDocument().getLength());
	}

	@Override
	public void onElectionChange(User user) {
		appendToTextChat("user: " + user.userId + "is the new elected leader");
		setUsers();
	}

	@Override
	public void onShutdown(User user) {
		terminateBots();
		
	}

}