package Client;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client implements Runnable {
    Socket socket;
    int authToken;
    public Client() {    //todo constructor parameters
    }
    private void init() throws IOException {
        //todo read host and port from config file
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("config.json"));
        HashMap<String,String> config = gson.fromJson(reader, HashMap.class);
        int port = Integer.parseInt(config.get("port"));
        String host = config.get("address");

        socket = new Socket(host, port);
        ClientMessenger clientMessenger = new ClientMessenger(this);
        ClientReceiver clientReceiver = new ClientReceiver(this);
        while (true) {
            clientMessenger.listen();
            clientReceiver.listen();
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}