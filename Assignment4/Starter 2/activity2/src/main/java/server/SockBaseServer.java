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

class SockBaseServer {
    static String logFilename = "logs.txt";

    ServerSocket serv = null;
    InputStream in = null;
    OutputStream out = null;
    Socket clientSocket = null;
    int port = 9099; // default port
    Game game;
    JSONArray lead;


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
        // JSONArray lead = new JSONArray();

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
        while (true) {//////continue running
            clientSocket = serv.accept();
            SockBaseServer server = new SockBaseServer(clientSocket, game);
            server.start();
        }
    }

    // Handles the communication right now it just accepts one input and then is done you should make sure the server stays open
    // can handle multiple requests and does not crash when the server crashes
    // you can use this server as based or start a new one if you prefer.
    public synchronized void start() throws IOException {
        String name = "";
        int score = 0;
        Request request;
        Response response = null;
        int counter = 0;
        String result = "";
        game.newGame();


        System.out.println("Ready...");
        try {

            JSONObject task = task();
            request = Request.parseDelimitedFrom(in);
            //String result = null;


            String fileName = "../activity2/leader.json";
            File f = new File(fileName);
            FileInputStream is = new FileInputStream(f);
            JSONArray lis = new JSONArray(new JSONTokener(is));

            // if the operation is NAME (so the beginning then say there is a commention and greet the client)
            if (request.getOperationType() == Request.OperationType.NAME) {

                    name = request.getName();

                    JSONObject player = new JSONObject();
                    player.put("name", name);
                    player.put("win", score);

                    if (!lis.isEmpty()) {

                        lead = lis;
                        lead.put(player);
                    } else {

                        lead = new JSONArray();
                        lead.put(player);
                    }

                // writing a connect message to the log with name and CONNENCT
                    writeToLog(name, Message.CONNECT);
                    System.out.println("Got a connection and a name: " + name);
                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.GREETING)
                            .setMessage("Hello " + name + " and welcome.")
                            .build();


                    FileWriter writer = new FileWriter("../activity2/leader.json");
                    writer.write(lead.toString());
                    writer.close();

                response.writeDelimitedTo(out);
            }


            if (request.getOperationType() == Request.OperationType.LEADER) {

                    Response.Builder res = Response.newBuilder()
                            .setResponseType(Response.ResponseType.LEADER);

                    // building and Entry
                    for (int i = 1; i < lis.length(); i++) {
                            Entry leader = Entry.newBuilder()
                                    .setName(lis.getJSONObject(i)
                                            .getString("name"))
                                    .setWins(lis.getJSONObject(i)
                                            .getInt("win")).setLogins(0).build();

                            res.addLeader(leader);
                    }
                    response = res.build();
                    System.out.println(lis);
                    response.writeDelimitedTo(out);
            }


            // adding the String of the game to
            if (request.getOperationType() == Request.OperationType.NEW) {

                    System.out.println("Task: " + task.getString("task") + "\nanswer: " + task.getString("answer"));
                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.TASK)
                            .setImage(game.getImage())
                            .setTask(task.getString("task"))
                            .build();
                    response.writeDelimitedTo(out);
            }

            if (request.getOperationType() == Request.OperationType.ANSWER) {

                String s = new String(request.getAnswer());
                boolean b;

                if (request.getAnswer().equalsIgnoreCase(task.getString("answer"))) {
                        counter++;
                        b = true;

                        response = Response.newBuilder()
                                .setResponseType(Response.ResponseType.TASK)
                                .setImage(replace(50))
                                .setEval(b)
                                .build();

                } else {

                        b = false;
                        response = Response.newBuilder()
                                .setResponseType(Response.ResponseType.TASK)
                                .setImage(game.getImage())
                                .setEval(b)
                                .build();
                }
                response.writeDelimitedTo(out);
            }

            if (request.getOperationType() == Request.OperationType.QUIT) {

                    response = Response.newBuilder()
                            .setResponseType(Response.ResponseType.BYE)
                            .setMessage("Thank you for playing " + name + " and hope to see again, Bye.")
                            .build();
                    response.writeDelimitedTo(out);
            }

            response.writeDelimitedTo(out);


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


    public JSONObject task() {
        ArrayList<JSONObject> arr = new ArrayList<>();
        JSONObject a = new JSONObject();
        a.put("task", "what color is the sky ?");
        a.put("answer", "blue");
        arr.add(a);
        JSONObject b = new JSONObject();
        b.put("task", "what class of car a jeep is ? ");
        b.put("answer", "suv");
        arr.add(b);

        JSONObject c = new JSONObject();
        c.put("task", "what state is 'evergreen state' ");
        c.put("answer", "wa");
        arr.add(c);
        int roll = roll(3);

        JSONObject json = arr.get(roll);
        return json;
    }

    public int roll(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}

