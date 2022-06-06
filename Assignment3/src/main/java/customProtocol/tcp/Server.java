package customProtocol.tcp;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;

public class Server {
    /*
     * request: { "selected": <String: user input from system> }
     *
     * response: {"datatype": <string: character>,"data": [images] }
     *
     * error response: {"error": <error string> }
     * message :{"datatype": <string: message>, "reply": <message string>}
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
        json.put("datatype", "cap");
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
        json.put("datatype", "dar");
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
        json.put("datatype", "wolv");
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
    public static JSONObject mesage(String m) {
        JSONObject json = new JSONObject();
        json.put("datatype", "message");
        json.put("reply", m);
        return json;
    }


    /**
     * helper method to read input stream; it uses another helper method from
     * JsonUtils class to converts bytes into string
     *
     * @param in input stream object
     * @return json object with data type of string.
     * @throws IOException
     */
    public static JSONObject read(InputStream in) throws IOException {
        byte[] messageBytes = NetworkUtils.Receive(in);

        return JsonUtils.fromByteArray(messageBytes);
    }


    /**
     * helper method to write out stream; it uses another helper method from
     * JsonUtils class to convert string into bytes.
     *
     * @param out out put stream object
     * @throws IOException
     */
    public static void write(OutputStream out, JSONObject js) throws IOException {
        byte[] output = JsonUtils.toByteArray(js);
        NetworkUtils.Send(out, output);


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
     * helper method to remove a character from the list if client's response
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
        ServerSocket serv = null;
        boolean b = false;
        int image = 0;// counter for the images
        int correct = 0;//counter for the correct answers
        int wrong = 0;// counter for the incorrect answers
        int charCount = 0;// counter for the characters
        JSONObject temp = null;//temproray JsonObject to hold current image in process
        JSONObject returnMessage = null;//message returned to the client
        ArrayList list = new ArrayList<JSONObject>();//list contain characters already processed.
        JSONArray listC = charList();//JsonArray contains all game's characters.
        int port;
        try {
            if (args.length > 0) {//if arguments exist, then port and host are user inputs
                port = Integer.parseInt(args[0]);// get the port number
            } else {
                port = 8080;// otherwise use a default port.
            }
            serv = new ServerSocket(port);

            while (true) {
                Socket sock = null;
                try {
                    sock = serv.accept(); // blocking wait
                    OutputStream out = sock.getOutputStream();
                    InputStream in = sock.getInputStream();

                    write(out, mesage("hello and welcome to the game, what is your name ?."));

                    while (true) {


                        JSONObject message = read(in);//read byte stream from the server and convert it into string


                        //if the received object contains 'selected' then it's valid for processing.
                        if (message.has("selected")) {
                            //if the input isn't a string, then returns a message to the client
                            if (message.get("selected") instanceof String || message.get("selected") instanceof Character) {

                                //get the client data (object 'key')
                                String choice = message.getString("selected");


                                if (choice.equalsIgnoreCase("name")) {//if name header recieved
                                    //return greeting message with the player name
                                    returnMessage = mesage("hello " + message.getString("name") + "\nenter: 'LEADER' to see the leader board.\nenter: 'START' to start the game");

                                }
                                if (choice.equalsIgnoreCase("start")) {//if started

                                    //start the game with the first image of the first character in the list
                                    returnMessage = imageMake(listC.getJSONObject(charCount), image);
                                    temp = listC.getJSONObject(charCount);//store the sent character for later checking
                                    list.add(temp);//store the sent object for checking answer later
                                    System.out.println(returnMessage.getString("datatype"));//print to the server terminal
                                    //i++;// advance to next image

                                }
                                if (choice.equalsIgnoreCase("end")) {//if end, then terminate and exit
                                    //close streams and socket,the exit
                                    sock.close();
                                    out.close();
                                    in.close();
                                    System.exit(0);
                                    break;
                                }
                                if (checkAnswer(list, choice) == true) {//check the client response
                                    //if true, display a message

                                    write(out, mesage("Correct, nice work, press 'Enter' to continue."));
                                    removChar(list, choice);//remove the current character from the list
                                    image = 0;//reset image counter
                                    correct++;//increment number of correct answer
                                    charCount++;//increment list index

                                    if (charCount < listC.length()) { //if not the end of the list
                                        temp = listC.getJSONObject(charCount);//reset temp for another image type
                                        returnMessage = imageMake(temp, image);//return new character
                                        list.add(temp);//add the new character to the list
                                        System.out.println(temp.getString("datatype"));

                                    } else { //otherwise we reached the end of the list and we terminate the game and display the score
                                        returnMessage = mesage("end of the game.\n you scored: " + correct + " point");

                                    }

                                }

                                if (choice.equalsIgnoreCase("more")) {//if the user ask for more image
                                    if (temp != null) {//make sure an image exist


                                        image++;
                                        if (image < 4) {//makes sure within the number of images
                                            // write(out, imageMake(temp, i));
                                            returnMessage = imageMake(temp, image);// send the new character image imageMake(temp, i);
                                            image++;//point to the next image

                                        } else {//otherwise all images for this character have been displayed
                                            //  write(out, mesage("Sorry, this is the final image of this character"));
                                            // i=0;
                                            returnMessage = mesage("Sorry, this is the final image of this character");
                                        }
                                    }
                                    wrong++;

                                }

                                if (choice.equalsIgnoreCase("next")) {//if the user ask for more image
                                    if (temp != null) {//make sure an image exist

                                        image = 0;
                                        charCount++;
                                        if (charCount < 7) { //if not the end of the list
                                            temp = listC.getJSONObject(charCount);//reset temp for another image type
                                            returnMessage = imageMake(temp, image);//return new character
                                            //  list.add(temp);//add the new character to the list
                                            System.out.println(temp.getString("datatype"));

                                        } else { //otherwise we reached the end of the list and we terminate the game and display the score

                                            returnMessage = mesage("Sorry ! no more character available");

                                            charCount = 0;
                                        }

                                    }
                                    wrong++;

                                }
                                write(out, returnMessage);

                            } else {
                                returnMessage = error("Selection must be String");
                            }
                        } else {
                            returnMessage = error("Invalid message received");
                        }


                    }

                } catch (Exception e) {
                    System.out.println("Client disconnect");
                    System.out.println(e.getMessage());
                } finally {
                    if (sock != null) {
                        sock.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serv != null) {
                serv.close();
            }
        }
    }


}

