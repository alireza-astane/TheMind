package server;

import client.Client;
import client.ClientMain;
import logic.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.UIDefaults.ProxyLazyValue;

import logic.Player;
import org.w3c.dom.NameList;

public class Server {
    public static Server server;
    private final HashMap<Game, List<ClientHandler>> gameToClientHandler;
    private final List<Game> games;
    private Game game;
    private static int counter;

    public static Server getInstance() {
        if (server == null)
            server = new Server();
        return server;
    }

    Server() {
        gameToClientHandler = new HashMap<>();
        games = new ArrayList<>();
        counter = 0;
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();

                addNewClientHandler(socket);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // TODO: گار وسط اینکه بازیکن داره اسم وارد میکنه یکی بخواد وارد بشه یمرینه


    public void addNewClientHandler(Socket socket) throws IOException, InterruptedException {
        ClientHandler clientHandler;
        int id;
        if (game == null) {
            game = new Game();
            id = game.getId();
            gameToClientHandler.put(game, new ArrayList<>());
            games.add(game);
            clientHandler = new ClientHandler(id, socket, game, true);
        } else {
            id = game.getId();
            clientHandler = new ClientHandler(id, socket, game, false);
        }
        if (game.getStatus() == Game.GameStatus.PENDING) {
            Player player = new Player();
            player.setId(id);
            game.getPlayers().add(player);
            gameToClientHandler.get(game).add(clientHandler);
            // todo:  ممکنه مثلا ۵ نفر وارد بازی شده باشن ولی ظرفیت وارد نشده و بعد که وراد شد میزنه ۳ مثلا --- هندل شد باید تست بشه.
            if (game.getCapacity() != null && game.getCapacity() == gameToClientHandler.get(game).size()) {
                game.setStatus(Game.GameStatus.START);
                // todo: اینجا باید به هاست پیام بده که ظرفیت پره بیا و استارت رو بزن وقتی زد تابع استارت در گیم صدا زده بشه
                startGame();// todo شاید بهتر باشه که داخل خود گیم همچین تابعی وجود داشته باشه. شاید
            }
            new Thread(clientHandler).start();
        } else {
            Message serverMessage = new Message(MessageType.WRONG_VALUE, "Game IS FULL!");
            new SendMessage(socket, serverMessage).sendMessage();
            socket.close();
        }

    }

    public HashMap<Game, List<ClientHandler>> getGameToClientHandler() {
        return gameToClientHandler;
    }

    public void startGame() {
        // todo start function that create boy if needed.
    }

    public ClientHandler findClientHandlerByPlayerId(Game game, int id) {
        for (ClientHandler clientHandler : gameToClientHandler.get(game)) {
            if (clientHandler.getId() == id) {
                return clientHandler;
            }
        }
        return null;
    }


}
