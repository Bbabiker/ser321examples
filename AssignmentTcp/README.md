# Client/Server communication via TCP custom portocol.




## Description of the project

### NOTE: kindly note, to implement this program, I have used some codes from the provided course codes examples which is developed by the teaching team.


this is a simple program to simulate a client/server communication over custom TCP protocol.
this communication includes sending and receiving String texts and image in which are converted into byte before being sent over the wire, the recipient converts the bytes back to the corresponding data format.

In the example we are converting all the data to a byte[] and not just sending over the String and letting Java do the rest

the client connects to server and the server greet the client and request the client’s name.
the client provides her/his name and the server, and the server greet the client with her/his name.
the server gives the client two options:
1- view the leader board.
2- start the game.

the client can view the leader board, or continues and start the game.
the client enters 'START', then the server will send first image. The client responds with the name of the character in which the image belongs to. if the client guess is correct, the server responds with another image from the same character.
if the client  guess is incorrect, the server responds with a message informing the client and ask to try again.
if the timer ends or the client trials reaches 3 trials, then the server ends the game.


## Checklist of the requirements 


1.  When the user starts up it should connect to the server. The server will reply by asking for the name of the player.

2.  The user should send their name and the server should receive it and greet the user by name.

3.  If the user chooses to start the game, the server will then send over a first quote of a character – you need to print the intended answer in the server terminal to simplify grading for us (this will be worth some points).

4.  The user can then do one of three things; enter a guess, e.g. "Jack Sparrow", type "more", or type "next". See what the more and next options do in the later contraints respectively.

5.  The user enters a guess and the server must check the guess and respond accordingly. If the answer is correct then they will get a new picture with a new quote (or they might win - see later). If the answer is incorrect they will be informed that the answer was incorrect and can try again.

6. If the user enters "more" then they will get another quote from the same movie character. However, If they enter "more" when the final unique image was already displayed for this character, then they need to be informed that there are no more pictures (quotes) for this character and the image should not change.

 7.  Users can always enter "next" which will make the server send a new quote for a new character. If there are no more characters available you can show one of the old ones or inform the user and quit the round. You may implement other options but do not let things crash.
 
 
8.  If the server receives 3 correct guesses and the timer did not run out (1 minute), then the server will send a "winner" image (display in UI or open frame when using terminal).

 9. If the server receives a guess and the timer ran out the user lost and will get a "loser" image and message (display in UI or open frame when using terminal).
 
 
10. At the end of a game (if lost or won) display how many points the client got. If the user lost, the leader board does not change. If they won add their new points to their old points on the leader board. You can assume that their name always identifies them.


## Partially completed requirements

1.  The leader board will show all players that have played since the frist start of the server with their name and points. The server will maintain the leader board and send it to the client when requested.

2.  Your protocol must be robust. If a command that is not understood is sent to the server or an incorrect parameterization of the command, then the protocol should define how these are indicated. Your protocol must have headers and optionally payloads. This means in the context of one logical message the receiver of the message has to be able to read a header, understand the metadata it provides, and use it to assist with processing a payload (if one is even present). This protocol needs to be described in detail in the README.md.


 3.  Your programs must be robust. If errors occur on either the client or server or if there is a network problem, you have to consider how these should be handled in the most recoverable and informative way possible. Implement good general error handling and output. Your client/server should not crash even when invalid inputs are provided by the user.
 
 4.  After the player wins/loses they can start a new game by entering their name again or they can quit by typing "quit". After entering their name they can choose start or the leader board again.


## How to run the program

with parameters:
gradle TCPServer -Pport= port number of choice
gradle TCPClient -Phost= host of choice -Pport= port number of choice
default:
gradle TCPServer  will run on port '8080'
gradle TCPClient  will run on localhost and port '8080'


## Description of the custom protocol

the client can only send string using System input in real time

{
 
    "selected": <String:captain, darth, homer, jack, joker, tony, wolverine > 
    
}
    
   
the server will respond based on the client request by sending back either an image, string message, or both (optionally)
   

{
   "datatype": <String: captain, darth, homer, jack, joker, tony, wolverine, lose, win>, 
   "data": <array: images> 
   "message":<optional message>
}

   
Server sends error if something goes wrong


{
    "error": <error string> 
}

   
##  How I designed the program:

unfortunately, I couldn’t integrate my program into the provided UI. I have spent enormous time trying to figure out the best way to integrate my code and wasn’t successful.
I have developed the program to run from command line.
I have used branching and (if statement) and exceptions to check for errors and provide the proper action. I used JsonArray to store the images for each character.  


