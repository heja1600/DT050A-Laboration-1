# Laboration 1


Netbeans project 


# Laboration 1

In this first laboration you are expected to learn how to program UDP broadcast socket programming in Java, as well as understand the basis for this whole laboratory work.

You will be given a basic skeleton for the program which you will need to extend to also do the following:

[x] Choose your own port, so your program will not collide with other students programs on the same network.
[x] Implement a Join message, that is sent from a client when the client starts.
[x] When another client receives the Join message, it shall add the user to its list of active clients.
[x] Implement a Leave message, that is sent from a client when the client starts. (probably means leaves)
[x] When another client receives the Leave message, it shall remove the user fromt its list of active clients.
[x] Remember that the newly joined client should also get a list of all active client from the older clients.
[x] Adjust the user interface according to your own taste.



# Laboration 2

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