import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.models.User;

import javax.annotation.PreDestroy;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.JScrollPane;


public class WindowProgram implements Listeners{

	private JFrame frame;
	private JTextPane txtpnChat = new JTextPane();
	private JTextPane txtpnMessage = new JTextPane();
	private DefaultListModel<Set<String>> model;
	private JList<Set<String>> loggedInUsers;
	
	GroupCommuncation gc = null;	
	

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
		gc = new GroupCommuncation();	
		gc.setListeners(this);	
		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
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



		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            gc.shutdown();
	        }
		});
		
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.broadcastMessage(new ChatMessage(txtpnMessage.getText()));
		}		
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {	
		txtpnChat.setText(chatMessage.chat + "\n" + txtpnChat.getText());				
	}


	@Override
	public void onUserLogout(User user) {
		System.out.println("User left the groupchat:" + user.getAddress());
		setUsers();
	}

	@Override
	public void onUserLogin(User user) {
		System.out.println("User joined groupchat:" + user.getAddress());
		setUsers();
	}

	@Override
	public void onSendLoginListener(User user) {
		// TODO Auto-generated method stub

	}
	private void setUsers()
	{
		if(loggedInUsers != null) frame.remove(loggedInUsers);
		model = new DefaultListModel<Set<String>>();
		model.addElement(gc.getUsers().keySet());
		loggedInUsers = new JList<Set<String>>(model);
		System.out.println("currently" + model.size() + " users.");
		frame.add(loggedInUsers);
	}	

}
