
## Laboration 3

Not very well coded, not at all actually. But i was in a hurry to do the last laboration, i might work it out better later. Doesnt work for alot of bots lel :D



```
In this last laboration you are expected to implement a distributed election to process to elect a leader (Bully algorithm). As well as implement total ordering, managed by this leader. This laboration is optional, but you must do it in order to be eligible to get an A or B on the course. You must finish this laboration before the course ends.

Use the basic program you coded in Laboration 1, which you now will need to extend to also do the following:

Implement the bully algorithm to elect a leader
Send election message when nodes join, leave, or if the leader is disconnected.
Everyone answers the election message.
A winner is announced.
Implement total ordering, managed by the elected leader
Before each chat message is sent. Get a sequence number from the leader.
When receiving a chat message, check if it has the correct sequence number.
Never display a message on the screen that is ahead of or out of order.
```