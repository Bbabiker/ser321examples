# Client/Server communication via TCP cystom portocol.




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
  
## Issues in the code that were not included on purpose
The code is basically to show you how you can use a TCP connection to send over different data and interpret it on either side. It focuses on this alone and not on error handling and some nicer features.
It is suggested that you play with this and try to include some of the below for your own practice. 

- Not very robust, e.g. user enters String
- Second client can connect to socket but will not be informed that there is already a connection from other client thus the server will not response
	- More than one thread can solve this
	- can consider that client always connects with each new request
		- drawback if server is working with client A then client B still cannot connect, not very robust
- Protocol is very simple no header and payload, here we just used data and type to simplify things
- Error handling is very basic and not complete
- Always send the same joke, quote and picture. Having more of each and randomly selecting with also making sure to not duplicate things would improve things



# UDP

The main differences can be seen in NetworkUtils.java. In there the sending and reading of messages happen. For UDP the max buffer length is assumed to be 1024 bytes. So if the package is bigger it is split up into multiple packages. Ever package holds the information about the following data
     *   totalPackets(4-byte int),  -- number of total packages
     *   currentPacket#(4-byte int),  -- number of current package
     *   payloadLength(4-byte int), -- length of the payload for this package
     *   payload(byte[]) -- payload

Client and server are very similar to the TCP example just the connection of course is UDP instead of TCP. The UDP version has the same issues as the TCP example and that is again on purpose. 

## Description of the project

### NOTE: kindely note, to implement this program, i have utlized codes from the codes examples provided by the teaching team.


this is a simple program to simulate a client/server communication over custom UDP portocol.
this communication is simplfied version of the custom TCP portocol. This program starts with the server sending a greeting message asking for the cleint name, and the client respond with her/his name. The server send another greeting message with the client name. The cleint then enter 'START' and the server respond with an image.

again, similar to the TCP portocol, we are  converting all the data to a byte[] and not just sending over the String and letting Java do the rest

the client connects to server and the server greet the client and request the client name.
the client provides her/his name and the server, and the server greet the client with her/his name.
the server gives the client two options:
1- view the leader board.
2- start the game.

In this program, per the assignment requirements,the client can only 'START' the game.


## How to run the program:
for this program you have to run the client first, then the server.
 since the server will start sending the packet once it connect and UDP is connectionless, the only way to receive the packet if the client is connected to the server whent the the data transfer over the wire takes place.


with parameters:
gradle UDPClient -Pport= port number of choice
gradle UDPServer -Phost= host of choice -Pport= port number of choice

default:
gradle UDPServer will run on localhost and port '8080'
gradle UDPClient  will run on port '8080'


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

since UDP is a datagram communication portocol, it's connectionless, meaning that everytime we send data we have to specify the connection meta-data such as local socket discriptor and recieving socket address. we also have to decide on othe metrics such as the size of the packets aswell as  we  have to incorporate a mechanism to serelize the packets to enable the reciepient to propperly re-assemble the packets back in order.As we can see there are more burden on the developer to ensure the transfer is carried propperly. whereas TCP is a stream communication protocol, unlike UDP i's connection oriented protocol, meaning that a connection has to be established first between two sockets before any transfere can takes place.    
  
