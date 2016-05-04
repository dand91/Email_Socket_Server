package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class ClientMain extends Thread {

    public static void main(String args[]) {


        String address = "213.21.69.152";

        int port = 4321;

        try {

            @SuppressWarnings("resource")
            Socket socket = new Socket(address, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

            writer w = new writer(read, out);
            w.start();
            reader r = new reader(in);
            r.start();


        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}

class reader extends Thread {

    private BufferedReader in;

    public reader(BufferedReader in) {

        this.in = in;
    }

    public void run() {

        String line;

        for (; ; ) {

            try {

                line = in.readLine();
                System.out.print("Received from server: ");
                System.out.println(line);

                if (line == null) {

                    System.out.println("Server is down.");
                    System.exit(1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

class writer extends Thread {


    private PrintWriter out;
    private BufferedReader read;

    public writer(BufferedReader read, PrintWriter out) {

        this.out = out;
        this.read = read;

    }

    public void run() {

        String msg;

        for (; ; ) {

            try {

                System.out.print(">");

                msg = read.readLine();

                if (msg.equals("QUIT")) {

                    System.out.println("Client shutting down.");
                    break;
                }

                System.out.print("sending '" + msg + "' to server...");

                out.println(msg);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
