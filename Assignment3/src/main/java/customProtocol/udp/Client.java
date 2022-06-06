package customProtocol.udp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.*;

public class Client {
    /*
     * request: { "selected": <int: 1=joke, 2=quote, 3=image, 4=random>,
     * (optional)"min": <int>, (optional)"max":<int> }
     *
     * response: {"datatype": <int: 1-string, 2-byte array>, "type": <"joke",
     * "quote", "image"> "data": <thing to return> }
     *
     * error response: {"error": <error string> }
     */


    public static JSONObject name(String s) {
        JSONObject request = new JSONObject();
        request.put("selected", "name");
        request.put("name", s);
        return request;
    }


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


    public static boolean checkImage(JSONObject js) throws IOException {
        String st = js.getString("datatype");

        if (st.equalsIgnoreCase("cap")) {

            return true;

        }
        if (st.equalsIgnoreCase("dar")) {

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
        return st.equalsIgnoreCase("wolv");
    }

    public static void main(String[] args) throws IOException {
        int port;
        String host;
        DatagramSocket sock;
        JSONObject request = null;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
                host = args[1];
            } else {
                port = 8080;
                host = "localhost";
            }
            sock = new DatagramSocket(port);
            InetAddress address = InetAddress.getByName(host);

            Scanner input = new Scanner(System.in);
            String choice;
            //System.out.println("Please enter your name");
            do {

                NetworkUtils.Tuple messageTuple = NetworkUtils.Receive(sock);
                JSONObject response = JsonUtils.fromByteArray(messageTuple.Payload);

                if (checkImage(response) == true) {

                    makeImage(response);//dispaly the first image
                } else {

                    System.out.println(response.getString("reply"));

                }

                choice = input.nextLine(); // what if not int.. shoudl error handle this


                if (request != null) {
                    request = new JSONObject();
                    request.put("selected", choice);
                    NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(request));

                } else if (request == null) {
                    request = name(choice);
                    NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(request));
                    System.out.println(request.getString("name"));


                } else if (choice == "end") {
                    NetworkUtils.Send(sock, address, port, JsonUtils.toByteArray(request));
                    sock.close();

                    System.exit(0);
                }

            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

