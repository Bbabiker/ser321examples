
# Client/Server communication via UDP custom protocol.

## Description of the project

### NOTE: kindly please note, to implement this program, I have utilized codes from the codes examples provided by the teaching team.


this is a simple program to simulate a client/server communication over custom UDP protocol.
this communication is simplified version of the custom TCP protocol. This program starts with the cleint sending an intital handshake message to the server, and server send a greeting message asking for the client’s name, and the client respond with her/his name. The server sends another greeting message with the client’s name. The client then enter 'START' and the server respond with an image.

again, like the TCP protocol, we are  converting all the data to a byte[] and not just sending over the String and letting Java do the rest

the client connects to server and the server greet the client and request the client’s name.
the client provides her/his name and the server, and the server greet the client with her/his name.
the server gives the client two options:
1- view the leader board.
2- start the game.

In this program, per the assignment requirements, the client can only 'START' the game.


## Checklist of the requirements 

1. The asking and sending of the user’s name
2. Sending an image from the server to the client


## How to run the program:
for this program you must run the client first, then the server.
 since the server will start sending the packet once it connects. and UDP is connectionless, hence the only way to receive the packet if the client is connected to the server when the data transfer over the wire takes place.


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

since UDP is a datagram communication portocol, it's connectionless, meaning that every time we send data we have to specify the connection meta-data such as local socket descriptor and receiving socket address. we also have to decide on other metrics such as the size of the packets as well as  we  have to incorporate a mechanism to serialize the packets to enable the recipient to properly re-assemble the packets back in order. As we can see there are more burden on the developer to ensure the transfer is carried properly. whereas TCP is a stream communication protocol, unlike UDP i's connection-oriented protocol, meaning that a connection has to be established first between two sockets before any transfer can takes place.    
  

