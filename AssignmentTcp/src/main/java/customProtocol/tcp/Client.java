package customProtocol.tcp;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    /*
     *this class serves as a client for a simple game implemented using custom TCP portocol
     *for data  exchannge with the server.

     * request: { "selected": <String:captain, darth, homer, jack, joker, tony, wolverine > }
     *
     * response: {"datatype": <string: character>,"data": [images] }
     *
     * error response: {"error": <error string> }
     * message :{"selected": <string: message>, "name": <message string>}
     *
     * @author Babiker, SER321 teaching team
     */

    /**
     * name object
     *
     * @return json object with the client name.
     */

    public static JSONObject name(String s) {
        JSONObject request = new JSONObject();
        request.put("selected", "name");
        request.put("name", s);
        return request;
    }


    /**
     * helper method to construct an image, the method uses another helper method from JsonUtils.
     *
     * @param json json object received from the server containing the image
     * @throws IOException
     */
    public static void makeImage(JSONObject json) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(json.getString("data"));
        ImageIcon icon = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(bais);
            icon = new ImageIcon(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (icon != null) {
            JFrame frame = new JFrame();
            JLabel label = new JLabel();
            label.setIcon(icon);
            frame.add(label);
            frame.setSize(icon.getIconWidth(), icon.getIconHeight());
            frame.show();
        }
    }


    /**
     * helper method to read input stream; it uses another helper method from
     * JsonUtils class to convert bytes into string
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
     * helper method to check if the json object recieved from the server
     * contains an image.
     *
     * @param js json object sent by the server
     * @return true if the json object has an image.
     * @throws IOException
     */
    public static boolean checkImage(JSONObject js) throws IOException {
        String st = js.getString("datatype");

        if (st.equalsIgnoreCase("captain")) {

            return true;

        }
        if (st.equalsIgnoreCase("darth")) {

            return true;
        }
        if (st.equalsIgnoreCase("homer")) {

            return true;
        }
        if (st.equalsIgnoreCase("jack")) {

            return true;
        }

        if (st.equalsIgnoreCase("joker")) {

            return true;
        }

        if (st.equalsIgnoreCase("tony")) {

            return true;
        }


        if (st.equalsIgnoreCase("wolverine")) {

            return true;
        }

        if (st.equalsIgnoreCase("win")) {

            return true;
        }

        if (st.equalsIgnoreCase("lose")) {

            return true;
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        Socket sock;
        int port;
        String host;

        JSONObject request = null;//client request to be sent to server
        try {

            //set the port and host
            port = Integer.parseInt(args[0]);
            host = args[1];
            sock = new Socket(host, port);
            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream();


            Scanner input = new Scanner(System.in);
            String choice;//client entries

            do {


                //read server message
                JSONObject response = read(in);

                if (checkImage(response) == true) {//check if contains an image

                    makeImage(response);//display the  image
                    if(response.has("message")){

                        System.out.println(response.getString("message"));
                    }
                } else if(response.has("reply")){
                    //otherwise it contains a reply text, print it

                    System.out.println(response.getString("reply"));

                }
                //for new game, check if last image was lose or a win
                if(response.getString("datatype").equalsIgnoreCase("lose")||
                        response.getString("datatype").equalsIgnoreCase("win")){

                    request=null;
                }
                choice = input.nextLine(); //read client input


                //if previous request was made, then client name was already sent and the game
                //has already started.
                if (request != null ) {
                    request = new JSONObject();
                    request.put("selected", choice);//create a request with the client input


                    //send the client request to the server  in form of bytes data type
                    //with helper method from JsonUtils
                    NetworkUtils.Send(out, JsonUtils.toByteArray(request));


                    //otherwise, if no request was previously made, send the client name
                }  if (request == null) {
                    request = name(choice);
                    System.out.println(request.getString("name"));
                    NetworkUtils.Send(out, JsonUtils.toByteArray(request));

                }  if (choice.equalsIgnoreCase("quit") ) {//if a client enter end the game will terminates.
                    //send the request to the sever and close the socket and streaming objects
                    NetworkUtils.Send(out, JsonUtils.toByteArray(request));
                    sock.close();
                    out.close();
                    in.close();
                    System.exit(0);
                    break;
                }


            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

