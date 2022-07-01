package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessage {
    private Socket socket;
    private final Message message;
    private final Gson gson;
    private final PrintWriter out;


    public SendMessage(Socket socket, Message message) throws IOException {
        this.socket = socket;
        this.message = message;
        this.gson = new Gson();
        this.out = new PrintWriter(socket.getOutputStream());
    }

    public void sendMessage() {
        out.println(gson.toJson(message));
        out.flush();
    }

}
