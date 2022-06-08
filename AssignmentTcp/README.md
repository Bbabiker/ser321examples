# Client/Server communication via TCP custom portocol.




## Description of the project

### NOTE: kindely note, to implement this program, i have used some codes from the provided course codes examples which is developed by the teaching team.


this is a simple program to simulate a client/server communication over custom TCP portocol.
this communication includes sending and reciving String texts and image in which are converted into byte befor being sent over the wire, the reciepient converts the bytes back to the corresponding data format.

In the example we are actually converting all the data to a byte[] and not just sending over the String and letting Java do the rest

the client connects to server and the server greet the client and request the client name.
the client provides her/his name and the server, and the server greet the client with her/his name.
the server gives the client two options:
1- view the leader board.
2- start the game.

the client can view the leader borad, or continues and start the game.
the client enter 'START', then the server will send first image. The client respond with the name of the character in which the image belongs to. if the client geuss is correct, the server respond with another image fro the same character.
if the client  geuss is incorrect, the server respond with a message informing the client and ask to try again.
if the timer ends or the client trials reaches 3 trial, then the server ends the game.




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

unfortonately, i wasnt able to integrate my program into the provided UI. I have spent ennormous time trying to figure out the best way to integrate my code and wasnt succssful.
i have developed the program to run from command line.
i have used branching (if statement) to check for errors and provide the propper action. i created JsonArray to store the images for each character,then  
