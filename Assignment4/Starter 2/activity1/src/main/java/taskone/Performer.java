/**
 * File: Performer.java
 * Author: Student in Fall 2020B
 * Description: Performer class in package taskone.
 */

package taskone;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class: Performer 
 * Description: Threaded Performer for server tasks.
 */
class Performer {

    private StringList state;
    private Socket conn;

    public Performer(Socket sock, StringList strings) {
        this.conn = sock;
        this.state = strings;
    }

    public static JSONObject error(String err) {
        JSONObject json = new JSONObject();
        json.put("error", err);
        return json;
    }

    public JSONObject add(String str) {
        JSONObject json = new JSONObject();
        json.put("datatype", 1);
        json.put("type", "add");
        state.add(str);
        json.put("data", state.toString());
        return json;
    }

    public JSONObject pop() {
        JSONObject json = new JSONObject();
        json.put("datatype", 2);
        json.put("type", "pop");


        if (state.size() > 0) {
            state.getList().remove(0);
            json.put("data", state.toString());
        } else {
            json.put("data", "null");
        }

        //json.put("data", state.toString());
        return json;
    }

    public JSONObject dispaly() {
        JSONObject json = new JSONObject();
        json.put("datatype", 3);
        json.put("type", "display");
        // state.add(str);
        if(state.size()>0){
            json.put("data", state.toString());
        }else {
            json.put("data", "null");
        }
        return json;
    }

    public JSONObject count() {
        JSONObject json = new JSONObject();
        json.put("datatype", 4);
        json.put("type", "count");
        String s= String.valueOf(state.size());
       if(state.size()>0){

           json.put("data",s);
       }else {
           String.valueOf(0);
           json.put("data", s);
       }
        return json;
    }

    public JSONObject switching( String s) {
       // int firstElement=0; int secElement=0;
        int firstElement=0; int secElement=0;
        StringTokenizer inTokens = new StringTokenizer(s);
        firstElement= Integer.parseInt(inTokens.nextToken());

        if (inTokens.hasMoreTokens()) {
            secElement =Integer.parseInt(inTokens.nextToken());
        }

        JSONObject json = new JSONObject();
        json.put("datatype", 5);
        json.put("type", "switching");


        if ((firstElement>=0 && firstElement<state.size())&&(secElement>0 &&secElement<state.size())) {
            Collections.swap( state.getList(),firstElement,secElement);

            json.put("data", state.toString());
        }else{
            json.put("data", "null");

        }

        return json;
    }



    public void doPerform() {
        boolean quit = false;
        OutputStream out = null;
        InputStream in = null;
        try {
            out = conn.getOutputStream();
            in = conn.getInputStream();
            System.out.println("Server connected to client:");
            while (!quit) {
                byte[] messageBytes = NetworkUtils.receive(in);
                JSONObject message = JsonUtils.fromByteArray(messageBytes);
                JSONObject returnMessage = new JSONObject();

                int choice = message.getInt("selected");
                String inStr;
                switch (choice) {
                    case (1):
                        inStr = (String) message.get("data");
                        returnMessage = add(inStr);
                        break;
                    case (2):
                        returnMessage = pop();
                        break;
                    case (3):
                        returnMessage = dispaly();
                        break;
                    case (4):
                        returnMessage = count();
                        break;
                    case (5):
                        System.out.println(message.getString("data").substring(0));
                        returnMessage = switching(message.getString("data"));
                        break;
                    default:
                        returnMessage = error("Invalid selection: " + choice
                                + " is not an option");
                        break;
                }
                // we are converting the JSON object we have to a byte[]
                byte[] output = JsonUtils.toByteArray(returnMessage);
                NetworkUtils.send(out, output);
            }
            // close the resource
            System.out.println("close the resources of client ");
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
