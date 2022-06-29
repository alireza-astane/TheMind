package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import logic.Game;

public class ClientHandler implements Runnable{
    private final Boolean isHost;
    private final int id;
    private final Game game;
    private final Socket socket;
    private final PrintWriter out;
    private final Gson gson;


    ClientHandler(int id, Socket socket, Game game, Boolean isHost) throws IOException {
        this.id = id;
        this.socket = socket;
        this.game = game;
        this.isHost = isHost;
        this.out = new PrintWriter(socket.getOutputStream());
        gson = new Gson();
    }


    @Override
    public void run() {

        // todo: send to all client than this player is enter the game.
        try {
            Scanner in = new Scanner(socket.getInputStream());
            if (isHost) {
                new SendMessage(socket, new ServerMessage(game, MessageType.GAME_NAME).getMessage()).sendMessage();
                System.out.println("game name send");
                String messageFromClient;
                while (game.getName() == null) {
                    messageFromClient = in.nextLine();
                    new GetMessage(messageFromClient, this);
                }
                new SendMessage(socket, new ServerMessage(game, MessageType.GAME_CAPACITY).getMessage()).sendMessage();
                while (game.getCapacity() == null) {
                    messageFromClient = in.nextLine();
                    new GetMessage(messageFromClient, this);
                }
                new SendMessage(socket, new ServerMessage(game, MessageType.GAME_START).getMessage()).sendMessage();// todo client not handle yet.
            }

            while (true) {
                String messageFromClient = in.nextLine();
                new GetMessage(messageFromClient, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Socket getSocket() {
        return socket;
    }
}
