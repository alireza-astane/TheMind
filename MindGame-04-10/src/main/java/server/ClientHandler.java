package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.print.attribute.standard.Sides;

import com.google.gson.Gson;
import logic.Game;

public class ClientHandler implements Runnable{
    private Boolean isHost;
    private int id;
    private Game game;
    private final Socket socket;
    private PrintWriter out;
    private Gson gson;
    private String choosenGame;

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        gson = new Gson();
        choosenGame = null;
    }

    @Override
    public void run() {
        try {
            new SendMessage(socket, new Message(MessageType.PLAYER_ENTER, "Welcom your id is: " + game.getPlayer(id).getId())).sendMessage();
//            new GetMessage(null, this).sendToAllClient(new Message(MessageType.PLAYER_ENTER, "new Player is entered by id: " + game.getPlayer(id).getId()));
            Scanner in = new Scanner(socket.getInputStream());
            if (isHost) {
                String messageFromClient;
                while (game.getName() == null) {
                    new SendMessage(socket, new ServerMessage().getMessage(game, MessageType.GAME_NAME, null)).sendMessage();
                    messageFromClient = in.nextLine();
                    new GetMessage(messageFromClient, this);
                }
                new SendMessage(socket, new ServerMessage().getMessage(game, MessageType.GAME_CAPACITY, null)).sendMessage();
                while (game.getCapacity() == null) {
                    messageFromClient = in.nextLine();
                    new GetMessage(messageFromClient, this);
                }
                new SendMessage(socket, new ServerMessage().getMessage(game, MessageType.GAME_START, null)).sendMessage();
            } else
                new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "you join to game " + game.getName())).sendMessage();

            while (true) {
                String messageFromClient = in.nextLine();
                new GetMessage(messageFromClient, this);
            }

        } catch (IOException | InterruptedException e) {
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

    public Boolean getHost() {
        return isHost;
    }

    public void setHost(Boolean host) {
        isHost = host;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getChoosenGame() {
        return choosenGame;
    }

    public void setChoosenGame(String choosenGame) {
        this.choosenGame = choosenGame;
    }
}
