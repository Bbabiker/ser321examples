 
## Description of the project

this is a simple program to simulate a simple multi-player game using Protobuf as protocol.

## Screen cast: https://youtu.be/PZeCvHjwKCE


### The procotol
You will see a response.proto and a request.proto file. You should implement these in your program. 
Protocol description
Request:
- NAME: a name is sent to the server, fields
    - name -- name of the player
    Response: GREETING, fields 
            - message -- greeting text from the server
- LEADER: client wants to get leader board
    - no further data
    Response: LEADER, fields 
            - leader -- repeated fields of Entry
- NEW: client wants to enter a game
    - no further data
    Response: TASK, fields
            - image -- current image as string
            - task -- current task for the cilent to solve
- ANSWER: client sent an answer to a server task
    - answer -- answer the client sent as string
    Response: TASK, fields 
            - image -- current image as string
            - task -- current task for the cilent to solve
            - eval -- true/false depending if the answer was correct
    OR
    Response: WON, fields
            - image -- competed image as string
- QUIT: clients wants to quit connection
    - no further data
    Response: BYE, fields 
        - message -- bye message from the server

Response ERROR: anytime there is an error you should send the ERROR response and give an appropriate message. Client should act appropriately
    - message


##  How I designed the program:

i have utilized the given protobuf protocol as per instrucction. 
-The game starts by the client sending her/his name to the server (build protobuf object)
-the server respond by greeting the player with her/his name.
-a menu with choices is presented to the player.
- when a player start a game, the server send a hidden image and a simple task to solve.
-if the player perform the task correctly, the 1/3 of the image is revealed. 
- i have design the game so the each time a task is fullfilled, one third if the image size will be revealed. The goal is to have the image revealed completely in three correct trials regardless of the image size.
- the player is finally informed of her/his wining status and leader board list is updated.
- the player can choose to continue to play, or quit the game.
- if the player decided to quit the game at any given time, the client will terminate gracefully and the server continue to run.


## Checklist of the requirements 


1. The project run through Gradle 


2.  implemented the given protocol (Protobuf)

3. The main menu gives the user 3 options: 1: leaderboard, 2 play game, 3 quit. After a game is done the menu pops up again. The menu implemented on the Client side.

4. When the user chooses the option 1, a leader board will be shown.

5. The leader board is the same for all users, take care of multiple users not overwriting it wrong and the leader board persists even if the server crashes and is re-started (Json file)

6.  User chooses option 2 (the game) a new image/text (with xxxx is sent to the client).


 7.  Multiple users can enter the SAME game.
 
 
8.  Users win when finishing an image and get back to main menu, multiple clients can win together.

 9. Server sends a task and check if it is done correctly.
 
 
10. Client presents the information well and the task are small and fast to answer (one word answers).

11. Game quits gracefully when option 3 is chosen.

12. Server does not crash when the client just disconnect (without choosing option 3).

13.   server running on your AWS instance.

14. Your game has a good setup for how many tiles each task reveals (the image is fully revealed in three correct trials)

15.  tested  3 other servers

16. If user types in "exit" while in the game, the client exit gracefully.



## How to run it (optional)
The proto file can be compiled using

``gradle generateProto``

This will also be done when building the project. 

You should see the compiled proto file in Java under build/generated/source/proto/main/java/buffers

Now you can run the client and server 

#### Default 
Server is Java
Per default on 9099
runServer

You have one example client in Java using the Protobuf protocol

Clients runs per default on 
host localhost, port 9099
Run Java:
    runClient


#### With parameters:
Java
gradle runClient -Pport=9099 -Phost='localhost'
gradle runServer -Pport=9099








   


