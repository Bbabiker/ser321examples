/**
 * File: Server.java
 * Author: Student in Fall 2020B
 * Description: Server class in package taskone.
 */

package taskthree;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Class: Server
 * Description: Server tasks.
 */
class ThreadPoolServer extends Thread {

    private final Socket socket;
    StringList string;

    public ThreadPoolServer(Socket socket, StringList str) {

        this.socket = socket;
        string = str;
    }

    public static void main(String[] args) throws Exception {

        int port;
        int clients=0;
        StringList strings = new StringList();
        Socket sock = null;

        if (args.length != 2) {
            // gradle runServer -Pport=9099 -q --console=plain
            System.out.println("Usage: gradle runServer -Pport=9099 -Pnum=number of connection -q --console=plain");
            System.exit(1);
        }
        port = -1;
        try {
            port = Integer.parseInt(args[0]);
            clients=Integer.parseInt(args[1]);// number of allowed cleints
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be an integer");
            System.exit(2);
        }
        Executor pool = Executors.newFixedThreadPool(clients);
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server Started...");
        try {

            while (true) {//multi threade
                System.out.println("Accepting a Request...");
                sock = server.accept();

                pool.execute(new ThreadPoolServer(sock, strings) );
                //ThreadPoolServer thread = new ThreadPoolServer(sock, strings);
               // thread.start();

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
