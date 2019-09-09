Introduktion
9 lectures 
written exam
2 laborations 6 occasions 
Read chapter 1 & 2

Choose study group
maximum 2 students
Choose topic
Distributed Anonymity (Onion routing, TOR)


Major issues for distributed system
-   unreliable network communication
-   no global clock ??? ( ticking in different speed)
concurrency can cause information space inconsistent
naming ( name of addresses etc)
security 

Major Challenges
heterogeneity
openness
concurrency
scalability
security
fault tolerance
transparency (make the system look like a single one (single system, “single thing”))

Heterogeneity
networks ( different protocols)
computer hardware 
operating systems
programming languages
implementations by different developers

Openness 
The system can be extended and re-implemented in various ways
An open system offers services according to standard rules that describe the syntax and semantics of those service.

Concurrency - Controlling concurrent access to shared resource.
Example of Techniques:
locks 
transactions

Scalability
Scalability of a system can be measured along at least three different dimensions. 
Size ( amount of clients)
Geographically 
Administratively

SCALABILITY AND OPENNESS IS NOT THE GODDAMN SAME tingTING 

Key techniques
Asynchronous communication
reduce the overall communication
replicating and caching
distribution
Quorum/consensus
enable a group to reach agreement

Fault tolerant

Transparency
Access transparency, Local and remote is used the same way
Location transparency, Ip address 
Concurrency transparency, 
Replication transparency, redundancy is often used, save things in different places
failure transparency, save the fail from failing
migration transparency, 
performance transparency, 
scaling transparency

Operating Systems for distributed computers 
Network operating system
Loosely-coupled operating system for ...
Distributed operating system
Tightly coupled operating system for multiprocessors and homogeneous multi-computers


BitTorrent unstructured P2P 










## CLOCKS

The importance of time in distributed system; 

-   Time is quantity we often



## Synchronized physical closk

-    the problems synchronizing multiple  physical clocks

    1. different clook frequency difference clock.

    2. Very high speed of processor

        * High requirement: down to milliseconds or below

    3. Message transmission in network. 

## compensating for clock drift

* if the time provided by a  time service, such as unervsial coordinated time singlan, is geater than the time at a computer C, then C's clock can be set to the time service time. 

* If the computer is quicker than theservices time mthen c's clock can be slowed down.




## Extra
Tround = time it takes for something to send and for it to come back.

min = tround/ 2;

