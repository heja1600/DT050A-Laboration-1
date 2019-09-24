import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.models.User;

//Skeleton code for Distributed systems 9hp, DT050A

public class Program extends BaseProgram implements Listeners {
    private boolean runProgram = true;

	public static void main(String[] args) {
		Program program = new Program();
	}



	public Program() {
		initGroupCommunication();


		
		addBots();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (runProgram) {
			try {

				System.out.println("Write message to send: ");
				String chat = br.readLine();
				gc.broadcastChatMessage(chat);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		gc.shutdown();
	}


	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {
		System.out.println(getUserInfo(chatMessage.user)+ ":"+ chatMessage.chat);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void onUserLogout(User user) {
		System.out.println("User left the groupchat:" + getUserInfo(user));
	}

	@Override
	public void onUserLogin(User user) {
		System.out.println("User joined groupchat:" + getUserInfo(user));
	}

	@Override
	public void onSendLoginListener(User user) {
		// TODO Auto-generated method stub

	}	
}