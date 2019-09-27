import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.VectorClockService;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.Message;
import se.miun.models.User;
import se.miun.models.VectorClock;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Set;

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
		if (event.getActionCommand().equalsIgnoreCase("addBot")) {
			addBot();
		}
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {	
		appendToTextChat(getUserInfo(chatMessage.user) + ": " + chatMessage.chat );
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

	@Override
	public void onSendLoginListener(User user) {
		appendToTextChat("User excists in groupchat:" + user.userId);
		setUsers();
	}
	private void setUsers()
	{
		String tmp = "";
		for (VectorClock.Entry<Integer, Integer> entry : gc.getUser().vectorClock.entrySet()) {
			tmp += "user:";
			tmp += entry.getKey();
			tmp += ", sent (" + gc.getUser().vectorClock.get(entry.getKey()) + ")";
			tmp += outOfOrder.containsKey(entry.getKey()) ? ", failed (" + outOfOrder.get(entry.getKey()) + ")" : "";
			tmp += "\n";
		}
		userList.setText(tmp);
		
	}
	private void appendToTextChat(String chat) {
		txtpnChat.setText(txtpnChat.getText() +  "\n" + chat);	
		txtpnChat.setCaretPosition(txtpnChat.getDocument().getLength());
	}

	@Override
	public void onOutOfOrder(ChatMessage chatMessage) {
		addOutOfOrder(chatMessage);
		setUsers();
	}
}