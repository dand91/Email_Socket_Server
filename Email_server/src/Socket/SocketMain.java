package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class SocketMain {

    public static void main(String[] args) {

        try {

            @SuppressWarnings("resource")
            ServerSocket ss = new ServerSocket(4321);

            while (true) {

                Socket cs = ss.accept();
                System.out.println(cs.getInetAddress());

                new ServerConnection(cs).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerConnection extends Thread {

    private PrintWriter out;
    private BufferedReader in;

    public ServerConnection(Socket socket) throws IOException {

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }


    @Override
    public void run() {


        String input;

        try {

            HashMap<String, String> hm = new HashMap<String, String>();

            while ((input = in.readLine()) != null) {

                String[] inputList = input.split(":");

                if (inputList[0].equals("EMAIL")) {

                    hm.put("EMAIL", inputList[1]);

                    out.println("RECEIVED EMAIL");
                    out.flush();

                } else if (inputList[0].equals("SUBJECT")) {

                    hm.put("SUBJECT", inputList[1]);

                    out.println("RECEIVED SUBJECT");
                    out.flush();

                } else if (inputList[0].equals("TEXT")) {

                    hm.put("TEXT", inputList[1]);

                    out.println("RECEIVED TEXT");
                    out.flush();

                } else {

                    out.println("RECEIVED NULL");
                    out.flush();
                }

                if (hm.size() == 3) {

                    out.println("SENDING EMAIL");
                    out.flush();

                    String email = hm.get("EMAIL");
                    String subject = hm.get("SUBJECT");
                    String text = hm.get("TEXT");

                    hm.clear();

                    SendHTMLEmail html = new SendHTMLEmail(text, subject, email);
                    html.send();

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
