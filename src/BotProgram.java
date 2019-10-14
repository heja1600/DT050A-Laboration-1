import java.awt.event.ActionEvent;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.ChatMessage;
import se.miun.models.User;

public class BotProgram extends Thread implements Listeners {
    boolean botIsRunning = false;
    GroupCommuncation gc = null;

    public BotProgram() {
        gc = new GroupCommuncation();
        gc.setListeners(this);
        
        botIsRunning = true;
        start();
    }

    public static void main(String[] args) {
        BotProgram botProgram = new BotProgram();
    }

    @Override
    public void run() {
        while (botIsRunning)
        {
            try {
                sleep((int) (Math.random() * 1000));
                gc.broadcastChatMessage("bot message");
            } catch (Exception e) {
                System.out.println("Thread failed to sleep, stacktrace: " + e.getStackTrace());
            }
        }
        gc.shutdown();
    }


    @Override
    public void onIncomingChatMessage(ChatMessage chatMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserLogin(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserLogout(User user) {
        // TODO Auto-generated method stub

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

	@Override
	public void onElectionChange(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdown(User user) {
        botIsRunning = false;
	}

}