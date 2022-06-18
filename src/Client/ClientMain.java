package Client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client();  //todo  parameters
        new Thread(client).start();

    }
}
