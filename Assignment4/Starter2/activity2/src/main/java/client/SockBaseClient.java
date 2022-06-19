package client;

import buffers.RequestProtos.Request;
import buffers.ResponseProtos.Entry;
import buffers.ResponseProtos.Response;

import java.io.*;
import java.net.Socket;

/**
 * This is a simple multi-player game client that uses Protobuf as protocol.
 *
 * @auther  Ser321 teaching team, Babiker Babiker
 */
class SockBaseClient {

    /**
     * this is a helper method that check if whether the user entry is a String or an Integer
     * @param string the string to be examined
     * @return  true if the input is an Integer, otherwise returns false
     */
    public static boolean isNumeric(String string) {
        int intValue;

        if (string == null || string.equals("")) {

            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }



    public static void main(String[] args) throws Exception {
        Socket serverSock = null;
        OutputStream out = null;
        InputStream in = null;
        Request op = null;
        Response response;

        boolean isFirstGame =false;//flag to indicate if the game is the first game (true if it'isNameEntered the first game)
        boolean isNameEntered =false;//flag to indicate if the user already entered her/his name (true if name was not entered)


        int i1 = 0, i2 = 0;
        int port = 9099; // default port

        // Make sure two arguments are given
        if (args.length != 2) {
            System.out.println("Expected arguments: <host(String)> <port(int)>");
            System.exit(1);
        }
        String host = args[0];
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be integer");
            System.exit(2);
        }


        // Ask user for username
        System.out.println("Please provide your name for the server. ( ͡❛ ͜ʖ ͡❛)");
        try {

            while (true) {//continue to run

                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

                String  strToSend = stdin.readLine();//read user inputs


                // connect to the server
                serverSock = new Socket(host, port);

                // write to the server
                out = serverSock.getOutputStream();
                in = serverSock.getInputStream();


                int option;//option the user chooses

                if (isNumeric(strToSend)) { //if the input is a number
                    option = Integer.parseInt(strToSend);//read the input as a number
                    if (option == 1) { //shows the leader board

                        op = Request.newBuilder()//build a request for the leader board and send it to the server
                                .setOperationType(Request.OperationType.LEADER)
                                .build();
                    }
                    if (option == 2) {//request to enter a new game
                        isFirstGame = true;//turn first game flag off to indicates it the start of the game

                        op = Request.newBuilder()//build a request to launch the game.
                                .setOperationType(Request.OperationType.NEW)
                                .build();
                    }

                    if (option == 3) {//quit the game

                        op = Request.newBuilder()//inform the server the user wants to quit
                                .setOperationType(Request.OperationType.QUIT)
                                .build();

                    }


                } else {//otherwise the input is a string

                    //if name wasn't entered previously and it's at the begining of the game
                    if (isNameEntered == false&& isFirstGame==false) {

                        op = Request.newBuilder()//build request to send the user'with name
                                .setOperationType(Request.OperationType.NAME)
                                .setName(strToSend).build();
                        isNameEntered = true;//set the name flag to true
                    } else {

                        op = Request.newBuilder()
                                .setOperationType(Request.OperationType.ANSWER)
                                .setAnswer(strToSend)
                                .build();

                    }

                }


                op.writeDelimitedTo(out);//write and send the request to the server



                response = Response.parseDelimitedFrom(in);//read the server response

                //if the respons is GREETING
                if (response.getResponseType() == Response.ResponseType.GREETING) {

                    System.out.println(response.getMessage());//retrieve and prints the greeting message
                    //display the menu to the user
                    System.out.println("What would you like to do? \n" +
                            " 1 - to see the leader board \n 2 - to enter a game\n 3 - quit");
                }

                //if the response is LEADER
                if (response.getResponseType() == Response.ResponseType.LEADER) {

                    //parse the response and prints all users and their score
                    for (Entry lead : response.getLeaderList()) {
                        System.out.println(lead.getName() + ": " + lead.getWins());
                    }

                    //display the menu to the user
                    System.out.println("What would you like to do? \n" +
                            " 1 - to see the leader board \n 2 - to enter a game\n 3 - quit");
                }

                //if the response is TASK
                if (response.getResponseType() == Response.ResponseType.TASK ) {

                    if (isFirstGame ==false) {//indicates the game has already started

                        //retrive and prints the image, answer evaluation, and the task
                        System.out.println(response.getTask() + "\n");
                        System.out.println("Image: \n" + response.getImage());
                        System.out.println("Eval: \n" + response.getEval());
                        System.out.println("Task: \n" + response.getTask());


                    }

                    if(isFirstGame ==true) {//if the game flag is true, then it's the beginning of the game

                        //display the first image and task
                        System.out.println("Image: \n" + response.getImage());
                        System.out.println("Task: \n" + response.getTask());


                        isFirstGame =false;//turn first game flag off (false) to indicate  the game already started

                    }
                }
                //if the response is WON, the  player have won the game
                if (response.getResponseType() == Response.ResponseType.WON) {

                    isFirstGame = true;//turn first game flag off to make ready for a new game
                    System.out.println(response.getImage());//prints the finale image

                    System.out.println("##### YOU WON #####");//inform the user with a wining message

                    //display the menu
                    System.out.println("What would you like to do? \n" +
                            " 1 - to see the leader board \n 2 - to enter a game\n 3 - quit");


                }


                //if the response is BYE, the  player have requested the server to he/she wishes to quit the game
                if (response.getResponseType() == Response.ResponseType.BYE) {
                    System.out.println(response.getMessage());//prints a good bye message

                    //close all the connections
                    in.close();
                    out.close();
                    serverSock.close();
                    break;
                }

                //if the user enter 'Exit' at any point, the game will gracefully terminate
                if (strToSend.equalsIgnoreCase("exit")) {

                    //close all the connections
                    in.close();
                    out.close();
                    serverSock.close();
                    break;

                }

                op.writeDelimitedTo(out);//send the user request to the server



                continue;

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (serverSock != null) serverSock.close();
        }


    }

}


