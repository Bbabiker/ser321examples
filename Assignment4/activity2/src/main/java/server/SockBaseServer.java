package server;

import buffers.RequestProtos.Logs;
import buffers.RequestProtos.Message;
import buffers.RequestProtos.Request;
import buffers.ResponseProtos.Entry;
import buffers.ResponseProtos.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * This is a simple multi-player game server that uses Protobuf as protocol.
 *
 * @auther  Ser321 teaching team, Babiker Babiker
 */
class SockBaseServer {
    static String logFilename = "logs.txt";
    static String img;//  variable to temporary store the image
    ServerSocket serv = null;
    InputStream in = null;
    OutputStream out = null;
    Socket clientSocket = null;
    int port = 9099; // default port
    Game game;
    static String name;// variable to store the player name
    static int score=0 ;// variable to store the player score
    JSONArray lead;// Json array to store leaderboard  entries


    public SockBaseServer(Socket sock, Game game) {
        this.clientSocket = sock;
        this.game = game;


        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (Exception e) {
            System.out.println("Error in constructor: " + e);
        }
    }


    /**
     * Writing a new entry to our log
     *
     * @param name    - Name of the person logging in
     * @param message - type Message from Protobuf which is the message to be written in the log (e.g. Connect)
     * @return String of the new hidden image
     */
    public static void writeToLog(String name, Message message) {
        try {
            // read old log file
            Logs.Builder logs = readLogFile();

            // get current time and data
            Date date = java.util.Calendar.getInstance().getTime();

            // we are writing a new log entry to our log
            // add a new log entry to the log list of the Protobuf object
            logs.addLog(date + ": " + name + " - " + message);

            // open log file
            FileOutputStream output = new FileOutputStream(logFilename);
            Logs logsObj = logs.build();

            // This is only to show how you can iterate through a Logs object which is a protobuf object
            // which has a repeated field "log"

            for (String log : logsObj.getLogList()) {

                System.out.println(log);
            }

            // write to log file
            logsObj.writeTo(output);
        } catch (Exception e) {
            System.out.println("Issue while trying to save");
        }
    }

    /**
     * Reading the current log file
     *
     * @return Logs.Builder a builder of a logs entry from protobuf
     */
    public static Logs.Builder readLogFile() throws Exception {
        Logs.Builder logs = Logs.newBuilder();

        try {
            // just read the file and put what is in it into the logs object
            return logs.mergeFrom(new FileInputStream(logFilename));
        } catch (FileNotFoundException e) {
            System.out.println(logFilename + ": File not found.  Creating a new file.");
            return logs;
        }
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();

        if (args.length != 2) {
            System.out.println("Expected arguments: <port(int)> <delay(int)>");
            System.exit(1);
        }
        int port = 9099; // default port
        int sleepDelay = 10000; // default delay
        Socket clientSocket = null;
        ServerSocket serv = null;

        try {
            port = Integer.parseInt(args[0]);
            sleepDelay = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port|sleepDelay] must be an integer");
            System.exit(2);
        }
        try {
            serv = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        while (true) {// here where the threads start, every client is able to log in and share the game state.
                        //the server will continue to run (unbounded)
            clientSocket = serv.accept();
            SockBaseServer server = new SockBaseServer(clientSocket, game);
            server.start();
        }
    }

    // Handles the communication right now it just accepts one input and then is done you should make sure the server stays open
    // can handle multiple requests and does not crash when the server crashes
    // you can use this server as based or start a new one if you prefer.
    public synchronized void start() throws IOException {


        game.newGame();//generate an image
        img = game.getImage();//store the image temporary


        System.out.println("Ready...");
        try {
            Request request;//protobuf request (from client to server)
            Response response = null;//protobuf response (from server to client)
            JSONObject task = task();// generate task randomly ( there are a total of 3 task)

            request = Request.parseDelimitedFrom(in);//read a request from the client

            String fileName = "../activity2/leader.json";//read the leader board from a json file
            File f = new File(fileName);
            FileInputStream is = new FileInputStream(f);

             lead = new JSONArray(new JSONTokener(is));
            // if the operation is NAME ( greet the client)
            if (request.getOperationType() == Request.OperationType.NAME) {

                name = request.getName();//read th player name from the client

                // temporary json object, player is added to the leader board if she/he doesn't exist
                JSONObject player = new JSONObject();
                player.put("name", name);
                player.put("win", score);




                    if (isPlayer(lead, name) == true) {//if the player doesn't exist in the list
                        lead.put(player);//add it to the list
                    }else{
                        score=getScore(lead,name);//if the player exist, then retrieve the score from the list
                        System.out.println("score: " + score);
                    }


                // writing a connect message to the log with name and CONNENCT
                writeToLog(name, Message.CONNECT);
                System.out.println("Got a connection and a name: " + name);

                response = Response.newBuilder()//response with greeting
                        .setResponseType(Response.ResponseType.GREETING)
                        .setMessage("Hello " + name + " and welcome.")
                        .build();

                //update the json file in case a new user is added to the leader board list
                FileWriter writer = new FileWriter("../activity2/leader.json");
                writer.write(lead.toString());
                writer.close();


            }

            //if the request for leader board
            if (request.getOperationType() == Request.OperationType.LEADER) {

                Response.Builder res = Response.newBuilder()
                        .setResponseType(Response.ResponseType.LEADER);

                // building and Entry for all existing player in the list and add it to the builder
                for (int i = 1; i < lead.length(); i++) {
                    Entry leader = Entry.newBuilder()
                            .setName(lead.getJSONObject(i)
                                    .getString("name"))
                            .setWins(lead.getJSONObject(i)
                                    .getInt("win"))
                            .setLogins(0)
                            .build();

                    res.addLeader(leader);
                }
                response = res.build();

                System.out.println(lead);

            }


            //if the request for playing a game
            if (request.getOperationType() == Request.OperationType.NEW) {

                //print the task in its answer to the server terminal
                System.out.println("Task: " + task.getString("task") + "\nanswer: " + task.getString("answer"));

                response = Response.newBuilder()//build a respnse with a task and a hidden image.
                        .setResponseType(Response.ResponseType.TASK)
                        .setImage(img)
                        .setTask(task.getString("task"))
                        .build();
            }

            //if the request is an answer (user answer)
            if (request.getOperationType() == Request.OperationType.ANSWER) {


               //check if the user's answered if correct and the image is not fully revealed
                if (request.getAnswer().equalsIgnoreCase(task.getString("answer")) && checkImage(img) == false) {

                //if correct, then the image is updated and revealed partially
                    img = gameX(img);

                    //build a response and send over to the client
                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.TASK)
                            .setImage(img)
                            .setTask(task.getString("task"))
                            .setEval(true)
                            .build();

                }
                    //if the user's answer is incorrect and the image is not fully revealed
                if (!request.getAnswer().equalsIgnoreCase(task.getString("answer")) && checkImage(img) == false) {
                //build a response with the same task and unchanged image
                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.TASK)
                            .setImage(img).setTask(task.getString("task"))
                            .setEval(false)
                            .build();
                }

                //if the image is fully revealed, then
                if (checkImage(img) == true) {

                   //build a win response
                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.WON)
                            .setImage(img)
                            .build();

                    game.setWon();//set game won to true, this way we can update the image

                    score++;//increment the score
                   lead= updateScore(lead, name,score);//update the player score

                    //update the json file
                    FileWriter writer = new FileWriter("../activity2/leader.json");
                    writer.write(lead.toString());
                    writer.close();

                }
            }
            //if the request is to quit
            if (request.getOperationType() == Request.OperationType.QUIT) {

                // a message is sent to the user
                response = Response.newBuilder()
                        .setResponseType(Response.ResponseType.BYE)
                        .setMessage("Thank you for playing " + name + " and hope to see again, Bye.")
                        .build();
            }


            response.writeDelimitedTo(out);//write the response and send it over to the user.


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();


        }
    }

    /**
     * Replaces num characters in the image. I used it to turn more than one x when the task is fulfilled
     *
     * @param num -- number of x to be turned
     * @return String of the new hidden image
     */
    public String replace(int num) {
        for (int i = 0; i < num; i++) {
            if (game.getIdx() < game.getIdxMax()) game.replaceOneCharacter();
        }
        return game.getImage();
    }

    /**
     * this is a helper method that ckeck whether the image is fully revealed.
     * @param str is the image to be checked.
     * @return return true if the image was fully revealed, and false if not.
     */
    public boolean checkImage(String str) {
        boolean b = true;
        for (int i = 0; i < str.length(); i++) {
            //if either character found, then return false.
            if (str.charAt(i) == 'X' || str.charAt(i) == 'x') {

                b = false;
                break;
            }
        }
        return b;
    }

    /**
     * this helper method used to turn the tiles to reveal the image.
     * it uses an offset that is 1/3 proportional to the image length.
     * the goal is to have all tiles revealed in 3 trials.
     * @param str the image to modify
     * @return return an image with some tiles revealed.
     */
    public String gameX(String str) {
        int ofset = str.length() / 3;//assign an offset to be 1/3
        String s = "";

        s = replace(ofset);//reveal the tiles

        return s;//return the image with some tiles revealed.
    }

    /**
     * this is a helper method that creates three tasks, then generate a random task.
     * the method uses a json array to store the tasks.
     * @return a generated random task.
     */
    public JSONObject task() {
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        JSONObject a = new JSONObject();
        a.put("task", "enter the color of the sky ?");
        a.put("answer", "blue");
        arr.add(a);
        JSONObject b = new JSONObject();
        b.put("task", "enter the car class type of a 'Jeep' ");
        b.put("answer", "suv");
        arr.add(b);

        JSONObject c = new JSONObject();
        c.put("task", "enter the name of the 'evergreen state' ");
        c.put("answer", "wa");
        arr.add(c);
        int roll = roll(2);

        JSONObject json = arr.get(0);
        return json;
    }

    /**
     * this helper method is used to pares the list obtained from the json file
     * and check if a user already exist.
     * @param js  the list to be parsed
     * @param name name of the player to look for
     * @return  returns true if the player is found, otherwise returns false.
     */
    public boolean isPlayer(JSONArray js, String name) {

        boolean b = true;

        for (int i = 0; i < js.length(); i++) {
            if (js.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {

                b = false;
                break;
            }
        }
        return b;
    }

    /**
     * this is a helper method that returns the player score if the player exist in the list(json file)
     * @param js the array to be parsed (Json)
     * @param name  name of the player to look for
     * @return  returns the current player score (if found)
     */
    public int getScore(JSONArray js, String name) {

        int score = 0;
        for (int i = 0; i < js.length(); i++) {
            if (js.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {

                score = js.getJSONObject(i).getInt("win");
                break;
            }
        }
        return score;
    }

    /**
     *  this is a helper method to update the player score in the list
     *
     * @param js  the list containing the array
     * @param playerName name of the player
     * @param score score to assign to the player
     * @return  returns a new updated list
     */
    public JSONArray updateScore(JSONArray js, String playerName, int score) {
        int sc=0;
        for (int i = 0; i < js.length(); i++) {

            if (js.getJSONObject(i).getString("name").equalsIgnoreCase(playerName)) {

                js.getJSONObject(i).put("win",score);//if player exist, update the score
                break;
            }
        }
        return js; //returns a new updated list
    }

    /**
     * this is a helper method to generate a random number
     * @param max  upper limit
     * @return  returns a random number within the specified limit.
     */
    public int roll(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

}

