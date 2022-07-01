package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final PrintWriter out;
    private int id;


    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());

    }


    @Override
    public void run() {
        System.out.println("New ClientHandler is running...");
        try {
            ServerReceiver serverReceiver = new ServerReceiver(socket);

            while (true) {

                System.out.println("ClientHandler is waiting for a message...");



                serverReceiver.listen();



                //String messageFromClient = in.nextLine();
                //System.out.println("Message from Client: " + messageFromClient);
                //String[] order = messageFromClient.split("-");
                //if (order[0].equals("SEND_MESSAGE_TO_A_CLIENT")) {
                //    int to = Integer.parseInt(order[1]);
                //    String message = order[2];
                //    orderHandler.sendMessageFromClientToAnotherClient(id, to, message);
                //}
                //sendMessage("You are client number " + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void kill() throws IOException {
        socket.close();
    }

    public void setId(int id) {
        this.id = id;
    }
}