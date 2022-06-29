package client;

import java.util.ArrayList;
import java.util.List;

public class ClientMain {
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            Client client = new Client();
            Thread thread = new Thread(client);
            thread.start();

        }
    }
}

