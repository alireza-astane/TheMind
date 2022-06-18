package Server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private final List<ClientHandler> clientHandlers;
    private ServerStatus status;
    private final OrderHandler orderHandler;


    Server() {
        clientHandlers = new ArrayList<>();
        status = ServerStatus.WAITING;
        orderHandler = new OrderHandler(clientHandlers);
    }

    public void init() {
        System.out.println("Server is running...");
        try {//todo check file name
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("config.json"));
            HashMap<String,String> config = gson.fromJson(reader, HashMap.class);
            int port = Integer.parseInt(config.get("port"));

            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for a connection...");
                Socket socket = serverSocket.accept();

                addNewClientHandler(socket);
                System.out.println("====> There are " + clientHandlers.size() + " clients on the server!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewClientHandler(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(clientHandlers.size(), socket, orderHandler);
        if (status == ServerStatus.WAITING) {
            System.out.println("New connection accepted!");

            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();

            if (clientHandlers.size() == 5) {
                startGame();
            }
        } else {
            clientHandler.sendMessage("Server is full!");
            clientHandler.kill();
        }

    }

    private void startGame() {
        System.out.println("Game is started!");
        status = ServerStatus.STARTED;
        // Start the game
    }


    public int tokenGenerator(){
        SecureRandom secureRandomGenerator = null;
        try {
            secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException ignored) {

        }
        return secureRandomGenerator.nextInt();
    }



}

class OrderHandler {
    private final List<ClientHandler> clientHandlers;

    public  OrderHandler(List<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    public void sendMessageFromClientToAnotherClient(int from, int to, String message) {
        clientHandlers.get(to).sendMessage("New message from client " + from + ": " + message);
    }
}

enum ServerStatus {
    WAITING, STARTED
}




