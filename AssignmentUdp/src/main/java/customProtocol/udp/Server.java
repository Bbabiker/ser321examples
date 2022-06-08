package customProtocol.udp;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Base64;

public class Server {
    /*
     *this class serves as a server for a simple game implemented using custom UDP protocol
     *for data  exchange with the server.

     * request: { "selected": <String:captain, darth, homer, jack, joker, tony, wolverine > }
     *
     * response: {"datatype": <string: character>,"data": [images] }
     *
     * error response: {"error": <error string> }
     * message :{"datatype": <string: message>, "reply": <message string>}
     *
     * @author Babiker, SER321 teaching team
     */


    /**
     * Captain_America character object
     *
     * @return json object with all images of Captain_America.
     */
    public static JSONObject captain() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Captain_America/quote1.png");
        array.put("img/Captain_America/quote2.png");
        array.put("img/Captain_America/quote3.png");
        array.put("img/Captain_America/quote4.png");
        json.put("datatype", "captain");
        json.put("data", array);
        return json;
    }

    /**
     * Darth_Vader character object
     *
     * @return json object with all images of Darth_Vader.
     */
    public static JSONObject darth() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Darth_Vader/quote1.png");
        array.put("img/Darth_Vader/quote2.png");
        array.put("img/Darth_Vader/quote3.png");
        array.put("img/Darth_Vader/quote4.png");
        json.put("datatype", "darth");
        json.put("data", array);
        return json;
    }

    /**
     * Homer_Simpson character object
     *
     * @return json object with all images of Homer_Simpson.
     */
    public static JSONObject homer() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Homer_Simpson/quote1.png");
        array.put("img/Homer_Simpson/quote2.png");
        array.put("img/Homer_Simpson/quote3.png");
        array.put("img/Homer_Simpson/quote4.png");
        json.put("datatype", "homer");
        json.put("data", array);
        return json;
    }

    /**
     * Jack_Sparrow character object
     *
     * @return json object with all images of Jack_sparrow.
     */
    public static JSONObject jack() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Jack_Sparrow/quote1.png");
        array.put("img/Jack_Sparrow/quote2.png");
        array.put("img/Jack_Sparrow/quote3.png");
        array.put("img/Jack_Sparrow/quote4.png");
        json.put("datatype", "jack");
        json.put("data", array);
        return json;
    }

    /**
     * Joker character object
     *
     * @return json object with all images of Joker.
     */
    public static JSONObject joker() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Jocker/quote1.png");
        array.put("img/Jocker/quote2.png");
        array.put("img/Jocker/quote3.png");
        array.put("img/Jocker/quote4.png");
        json.put("datatype", "joker");
        json.put("data", array);
        return json;
    }

    /**
     * Tony_stark character object
     *
     * @return json object with all images of Tony_stark.
     */
    public static JSONObject tony() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Tony_Stark/quote1.png");
        array.put("img/Tony_Stark/quote2.png");
        array.put("img/Tony_Stark/quote3.png");
        array.put("img/Tony_Stark/quote4.png");
        json.put("datatype", "tony");
        json.put("data", array);
        return json;
    }

    /**
     * Wolverine character object
     *
     * @return json object with all images of Wolverine.
     */
    public static JSONObject wolverine() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("img/Wolverine/quote1.png");
        array.put("img/Wolverine/quote2.png");
        array.put("img/Wolverine/quote3.png");
        array.put("img/Wolverine/quote4.png");
        json.put("datatype", "wolvrine");
        json.put("data", array);
        return json;
    }

    /**
     * helper method to construct an image, the method uses another helper method from NetworkUtils.
     *
     * @param js json object containing the image
     * @param i  index if the image within the json object
     * @return json object containing an image data
     * @throws IOException
     */
    public static JSONObject imageMake(JSONObject js, int i) throws IOException {
        JSONObject json = new JSONObject();
        json.put("datatype", js.getString("datatype"));


        File file = new File(js.getJSONArray("data").get(i).toString());
        System.out.println(js.getJSONArray("data").get(i).toString());

        if (!file.exists()) {
            System.err.println("Cannot find file: " + file.getAbsolutePath());
            System.exit(-1);
        }
        // Read in image
        BufferedImage img = ImageIO.read(file);
        byte[] bytes = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            bytes = out.toByteArray();
        }
        if (bytes != null) {
            Base64.Encoder encoder = Base64.getEncoder();
            json.put("data", encoder.encodeToString(bytes));
            return json;
        }
        return error("Unable to save image to byte array");
    }


    /**
     * error object
     *
     * @param err error message
     * @return json object containing an error message.
     */
    public static JSONObject error(String err) {
        JSONObject json = new JSONObject();
        json.put("error", err);
        return json;
    }

    /**
     * message object
     *
     * @param m message to show
     * @return json object containing a message.
     */
    public static JSONObject message(String m) {
        JSONObject json = new JSONObject();
        json.put("datatype", "message");
        json.put("reply", m);
        return json;
    }

    /**
     * helper method that creates a list with all the game's characters
     *
     * @return JsonArray of all characters.
     */
    public static JSONArray charList() {
        JSONArray list = new JSONArray();
        list.put(captain());
        list.put(darth());
        list.put(homer());
        list.put(jack());
        list.put(joker());
        list.put(tony());
        list.put(wolverine());

        return list;


    }

    /**
     * helper method to match the client's answer with the provided image
     *
     * @param js list containing the current displayed character's image
     * @param s  client's input
     * @return
     */
    public static Boolean checkAnswer(ArrayList<JSONObject> js, String s) {
        for (int i = 0; i < js.size(); i++) {

            if (js.get(i).getString("datatype").equalsIgnoreCase(s)) {

                return true;


            }
        }
        return false;

    }

    /**
     * helper method to remove a charcter from the list if client's response
     * is correct.
     *
     * @param js array list of Json object
     * @param s  string key of the object to be removed
     */
    public static void removChar(ArrayList<JSONObject> js, String s) {
        for (int i = 0; i < js.size(); i++) {

            if (js.get(i).getString("datatype").equalsIgnoreCase(s)) {

                js.remove(i);
                break;


            }
        }


    }

    public static void main(String[] args) throws IOException {
        DatagramSocket sock = null;//data gram socket


        int image = 0;// counter for the images
        int correct = 0;//counter for the correct answers
        int wrong = 0;// counter for the incorrect answers
        int charCount = 0;// counter for the characters
        JSONObject temp = null;//temproray JsonObject to hold current image in process
        JSONObject returnMessage = null;//message returned to the client
        ArrayList list = new ArrayList<JSONObject>();//list contain characters already processed.
        JSONArray listC = charList();//JsonArray contains all game's characters.
        InetAddress address = InetAddress.getByName("localhost");//host
        int port;// port
        NetworkUtils.Tuple messageTuple;//helper object from NetworkUtil that hold the packet data meta-date
        // (port,host, and payload)
        try {
            port = Integer.parseInt(args[0]);// set the port
            sock = new DatagramSocket();// set the UDP socket

            //helper method from NetworkUtils for sending a packet
            NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(message("hello and welcome" + " to the game, what is your name ?.")));
            while (true) {


                try {

                    while (true) {

                        messageTuple = NetworkUtils.Receive(sock);//get data from the client
                        //convert the received data from bytes int json object
                        //with a helper method from NetworkUtils
                        JSONObject message = JsonUtils.fromByteArray(messageTuple.Payload);

                        //if the received object contains 'selected' then it's valid for processing.
                        if (message.has("selected")) {
                            //if the input isn't a string, then returns a message to the client
                            if (message.get("selected") instanceof String) {

                                //get the client data (object 'key')
                                String clientInput = message.getString("selected");


                                if (clientInput.equalsIgnoreCase("name")) {//if name 'header' received
                                    //return greeting message with the player name
                                    returnMessage = message("hello " + message.getString("name") + "\nenter: 'LEADER' to see the leader board.\nenter: 'START' to start the game");

                                }
                                if (clientInput.equalsIgnoreCase("start")) {//if started

                                    //start the game with the first image of the first character in the list
                                    returnMessage = imageMake(listC.getJSONObject(charCount), image);
                                    temp = listC.getJSONObject(charCount);//store sent character for later checking
                                    list.add(temp);//store  sent object for checking answer later
                                    System.out.println(returnMessage.getString("datatype"));//print to the server terminal
                                    NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(returnMessage));

                                }
                                if (clientInput.equalsIgnoreCase("quit")) {//if 'quit', then terminate and exit
                                    //close streams and socket,then exit
                                    sock.close();
                                    System.exit(0);
                                    break;
                                }

                                NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(returnMessage));

                            } else {//otherwise the entry is not a string, inform the client
                                returnMessage = error("Selection must be String");
                            }
                        } else {
                            returnMessage = error("Invalid message received");//otherwise the entry is invalid. Inform the user
                        }


                    }

                } catch (Exception e) {
                    System.out.println("Client disconnect");
                    System.out.println(e.getMessage());
                    continue;
                } finally {
                    if (sock != null) {
                        sock.close();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sock != null) {
                sock.close();
            }
        }

    }


}

