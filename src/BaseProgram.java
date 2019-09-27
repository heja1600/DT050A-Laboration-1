import java.util.ArrayList;
import java.util.List;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.Listeners;
import se.miun.distsys.messages.Message;
import se.miun.models.OutOfOrder;
import se.miun.models.User;

abstract public class BaseProgram implements Listeners {

	protected GroupCommuncation gc = null; 
  List<BotProgram> bots = new ArrayList<>();
  private int botInstances = 0;
  protected Logger logger = new Logger();

  protected OutOfOrder outOfOrder = new OutOfOrder();
  
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
		for(int i = 0; i < botInstances; i++, addBot()); 
  }
  protected void terminateBots() {
    for(BotProgram bot: bots)  bot.botIsRunning = false;
  }

  protected <T extends Message> void addOutOfOrder(T message) {
    if(!outOfOrder.containsKey(message.user.userId))
    {
      outOfOrder.put(message.user.userId, 0);
    }
    outOfOrder.put(message.user.userId, outOfOrder.get(message.user.userId) + 1);
  }
  protected void addBot() {
     bots.add(new BotProgram());
     botInstances++;
  }
  protected void removeBot() {
    bots.remove(bots.size() - 1);
  }
}