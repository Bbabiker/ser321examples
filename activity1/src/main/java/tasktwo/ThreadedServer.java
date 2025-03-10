/**
 * File: Server.java
 * Author: Student in Fall 2020B
 * Description: Server class in package taskone.
 */

package tasktwo;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class: Server
 * Description: Server tasks.
 */
class ThreadedServer extends Thread {

    private final Socket socket;
    StringList string;

    public ThreadedServer(Socket socket,StringList str) {

        this.socket = socket;
        string = str;
    }

    public static void main(String[] args) throws Exception {
        int port;
        StringList strings = new StringList();
        Socket sock = null;

        if (args.length <1) {
            // gradle runServer -Pport=9099 -q --console=plain
            System.out.println("Usage: gradle runServer -Pport=9099 -q --console=plain");
            System.exit(1);
        }
        port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be an integer");
            System.exit(2);
        }
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server Started...");
        try {

            while (true) {//multi threade
                System.out.println("Accepting a Request...");
                sock = server.accept();

                ThreadedServer thread = new ThreadedServer(sock, strings);
                thread.start();

                System.out.println("close socket of client ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sock != null) sock.close();
        }

    }

    public void run() {

        Performer performer = new Performer(socket, string);

        performer.doPerform();

    }
}
