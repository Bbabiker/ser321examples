
# Client/Server communication via UDP custom portocol.

## Description of the project

### NOTE: kindely please note, to implement this program, i have utlized codes from the codes examples provided by the teaching team.


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
  
