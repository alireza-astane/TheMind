package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    Socket socket;
    int authToken;
    public Client() {    //todo constructor parameters
    }
    private void init() throws IOException {
        //todo read host and port from config.json file
        socket = new Socket("localhost", 8080);
        ClientMessenger clientMessenger = new ClientMessenger(this);
        ClientReceiver clientReceiver = new ClientReceiver(this);


        while (true) {
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