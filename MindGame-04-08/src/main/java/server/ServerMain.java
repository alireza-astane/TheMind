package server;

import com.google.gson.Gson;

public class ServerMain {
    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.init();
    }
}
