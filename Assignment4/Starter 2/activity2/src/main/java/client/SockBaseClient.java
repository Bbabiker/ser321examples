package client;

import buffers.RequestProtos.Request;
import buffers.ResponseProtos.Entry;
import buffers.ResponseProtos.Response;

import java.io.*;
import java.net.Socket;

class SockBaseClient {

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

    public static void readResponse(Response response) {

        if (response.getResponseType() == Response.ResponseType.GREETING) {

            System.out.println(response.getMessage());
        }
        if (response.getResponseType() == Response.ResponseType.LEADER) {

            for (Entry lead : response.getLeaderList()) {
                System.out.println(lead.getName() + ": " + lead.getWins());
            }
        }

        if (response.getResponseType() == Response.ResponseType.TASK) {

            System.out.println("Task: " + response.getResponseType());
            System.out.println("Image: \n" + response.getImage());
            System.out.println("Task: \n" + response.getTask());

        }

    }

    public static Request makeRequest(String str) throws IOException {

        Request req = null;
        int option;
        if (isNumeric(str)) {
            option = Integer.parseInt(str);
            if (option == 1) {
                req = Request.newBuilder()
                        .setOperationType(Request.OperationType.LEADER)
                        .build();
            }
            if (option == 2) {
                req = Request.newBuilder()
                        .setOperationType(Request.OperationType.NEW)
                        .build();
            }

            if (option == 3) {
                req = Request.newBuilder()
                        .setOperationType(Request.OperationType.QUIT)
                        .build();
            }
        } else {

            req = Request.newBuilder()
                    .setOperationType(Request.OperationType.NAME)
                    .setName(str).build();

        }


        return req;
    }

    public static void main(String args[]) throws Exception {
        Socket serverSock = null;
        OutputStream out = null;
        InputStream in = null;
        Request op = null;
        Response response;
        String  temp="";


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
            while (true) {///continue to run
                temp="";

                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                String strToSend = stdin.readLine();

                if (strToSend.equalsIgnoreCase("exit")) {

                    in.close();
                    out.close();
                    serverSock.close();
                    break;

                }

                // connect to the server
                serverSock = new Socket(host, port);

                // write to the server
                out = serverSock.getOutputStream();
                in = serverSock.getInputStream();


                int option;
                if (isNumeric(strToSend)) {
                    option = Integer.parseInt(strToSend);
                    if (option == 1) {
                        temp="leader";
                        op = Request.newBuilder()
                                .setOperationType(Request.OperationType.LEADER)
                                .build();
                    }
                    if (option == 2) {
                        temp="new";
                        op = Request.newBuilder()
                                .setOperationType(Request.OperationType.NEW)
                                .build();
                    }

                    if (option == 3) {
                        temp="quit";
                        op = Request.newBuilder()
                                .setOperationType(Request.OperationType.QUIT)
                                .build();

                    }
                    op.writeDelimitedTo(out);
                } else {

                    if (op == null) {
                        temp="name";
                        op = Request.newBuilder()
                                .setOperationType(Request.OperationType.NAME)
                                .setName(strToSend).build();
                    }
                    op.writeDelimitedTo(out);
                }

                response = Response.parseDelimitedFrom(in);


                if (response.getResponseType() == Response.ResponseType.GREETING) {

                    System.out.println(response.getMessage());
                    System.out.println("What would you like to do? \n" +
                            " 1 - to see the leader board \n 2 - to enter a game\n 3 - quit");
                }

                if (response.getResponseType() == Response.ResponseType.LEADER) {

                    for (Entry lead : response.getLeaderList()) {
                        System.out.println(lead.getName() + ": " + lead.getWins());
                    }
                }

                if (response.getResponseType() == Response.ResponseType.TASK ) {

                        if (temp.equalsIgnoreCase("new")) {

                            System.out.println(response.getTask() + "\n");
                            System.out.println("Image: \n" + response.getImage());
                            System.out.println("Eval: \n" + response.getEval());
                            System.out.println("Task: \n" + response.getTask());
                        }else {


                            temp = "new";

                            System.out.println("Image: \n" + response.getImage());
                            System.out.println("Task: \n" + response.getTask());
                            //System.out.println(strToSend);

                             op = Request.newBuilder()
                                    .setOperationType(Request.OperationType.ANSWER)
                                    .setAnswer(strToSend).build();

                        }
                        temp="";


                }

                if (response.getResponseType() == Response.ResponseType.BYE) {
                    System.out.println(response.getMessage());

                    in.close();
                    out.close();
                    serverSock.close();
                    break;
                }

                op.writeDelimitedTo(out);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (serverSock != null) serverSock.close();
        }


    }

    public static boolean check(Response r) {
        if (r.getEval() == true) {
            return true;
        }
        if (r.getEval() == false) {
            return true;
        }
        return false;
    }

    public static boolean check2(Response r) {
        if (r.getEval() == true) {
            return true;
        }
        if (r.getEval() == false) {
            return true;
        }
        return false;
    }
}


