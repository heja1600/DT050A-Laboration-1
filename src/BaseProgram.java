import java.util.ArrayList;
import java.util.List;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.Listeners;
import se.miun.models.User;

abstract public class BaseProgram implements Listeners {

	protected GroupCommuncation gc = null; 
  List<BotProgram> bots = new ArrayList<>();
  private int botInstances = 5;
  protected Logger logger = new Logger();
  
  public void initGroupCommunication() {
    gc = new GroupCommuncation();
    gc.setListeners(this);
    System.out.println("Group Communcation Started");
  }
  protected String getUserInfo(User user) 
  {
    //return "userId:" + user.userId + ", adress:" + user.address;
    return "userId:" + user.userId;
  }
  protected void addBots() { 
		for(int i = 0; i < botInstances; i++, bots.add(new BotProgram())); 
  }
  protected void terminateBots() {
    for(BotProgram bot: bots)  bot.botIsRunning = false;
  }
}