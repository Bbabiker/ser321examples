# Assignment 4 Activity 1
## Description
This activity is about practicing threads. In this activity, i have added more functionalities as per the assignment instruction to minapulate a list bu utlizing a Json oreinted custom protocol.

## Screen cast: https://youtu.be/2WHYxGocpUo

## Protocol

### Requests
request: { "selected": <int: 1=add, 2=pop, 3=display, 4=count, 5=switch,
0=quit>, "data": <thing to send>}

  data <string> add
  data <> pop
  data <> display
  data <> count
  data <int> <int> switch return a string

### Responses

sucess response: {"type": <"add",
"pop", "display", "count", "switch", "quit"> "data": <thing to return> }

type <String>: echoes original selected from request
data <string>: add = new list, pop = new list, display = current list, count = num elements, switch = current list


error response: {"type": "error", "message"": <error string> }
Should give good error message if something goes wrong


## How to run the program


### Task 1
Client: 
gradle runClient -Phost=localhost -Pport=9099 
Default: gradle runClient 
        Default port:8000
        Default hos: local host
        
Server:
    gradle runTask1 -Pport=9099
    Default: gradle runTask1 
             Default port:8000
         
### Task 2
Client: 
gradle runClient -Phost=localhost -Pport=9099 
Default: gradle runClient 
        Default port:8000
        Default hos: local host
        
Server:
    gradle runtask2 -Pport='9000'
    Default: gradle runTask2 
             Default port:8000
         
### Task 3
Client: 
gradle runClient -Phost=localhost -Pport=9099
Default: gradle runClient 
        Default port:8000
        Default hos: local host
        
Server:
     gradle runtask3 -Pport='9000' -Pclient='4'
     Default: gradle runTask2 
             Default port:8000
             Default nuber of client =3

         
         
## Checklist of fulfilled  requirements:

##Task 1:

1. add <string> - adds a string to the list (presently what it does by default now) and displays the list (strings will be added to the end) – already implemented

2. pop - removes the top element of the list and displays it. If the list is empty return "null"

3. display - displays the current list

4. count - returns the number of elements in your list and displays the number

5.  switch the elements at the given indexes. If one of the indexes is invalid return "null" (list does not change)


##Task 2:

1. Server named "ThreadedServer"

2. Allow unbounded incoming connections to the server.

3. No client should block.

4. The shared state of the string list is properly managed.


##Task 3: 

Made the multi-threaded server bounded, Task 1 and 2 still run as is.

1. server named "ThreadPoolServer".

2. allows a set number of incoming connections at any given time.  

3. Server named "ThreadPoolServer".


##Gradle 

1.  There are 3 Gradle tasks that run the different servers

2. Gradle uses default values for each task, per default host=localhost, port=8000

3. Run "gradle runClient" also use the default values.

4. Running your different servers:
a) One for running Task 1: gradle runTask1
b) One for running Task 2: gradle runTask2 
c) One for running Task 3: gradle runTask3


