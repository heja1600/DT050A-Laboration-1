
# Laboration 2


```
Laboration 2
In the second laboration you are expected to implement causal ordring in the chat system. Basically, you should use your basic implementation from Laboration 1, but now implement causal ordering (vector clocks) for your clients. Making so each client individually keeps track of the clock of each other client using a vector style clock.

Implement causal ordering using vector clocks.
Never display a message on the screen that is ahead of or out of order.
```

UDP chat with implemented Casual Order VectorClock 
- Only display message that is in order
- Remove messages that is out of order

Number of bots could be choosen in BaseProgram.java. 

You should use the WindowProgram.java not the Program.java

Messages from each users and the failed messages (messages out of order) is displayed in WindowProgra.java
https://i.gyazo.com/383b85d773bd9b330946498f48f1db1f.mp4
![image](https://user-images.githubusercontent.com/43444902/65540436-af49e280-df0b-11e9-9a96-71d8e1da19d1.png)


Log of all events from current Program can be seen in logger.txt while program is running
![image](https://user-images.githubusercontent.com/43444902/65540534-e4eecb80-df0b-11e9-9324-e12354a44d2c.png)