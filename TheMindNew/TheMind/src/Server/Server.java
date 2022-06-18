package Server;

import Logic.Game;
import Logic.GameState;
import Logic.Player;
import Logic.PlayerType;
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
    private final List<Game> games;
    private ServerStatus status;
    private HashMap<Game, ArrayList<ClientHandler>> gameToClientHandlerHashMap;


    Server() {
        games = new ArrayList<>();
        status = ServerStatus.WAITING;
        gameToClientHandlerHashMap = new HashMap<>();
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

                Game game = addNewClientHandler(socket);
                System.out.println("====> There are " + gameToClientHandlerHashMap.get(game).size() + " clients on the server!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game addNewClientHandler(Socket socket) throws IOException {
        Game game;
        ClientHandler clientHandler = new ClientHandler(socket);
        // todo set id to client handler. is this necessary?

        Game notStartedGame = findNotStartedGames();
        List<Game> availableStartedGames = findAvailableStartedGames();

        if (notStartedGame != null) {
            game = notStartedGame;
            // a game that not started yet is exist...
            gameToClientHandlerHashMap.get(notStartedGame).add(clientHandler);
            // todo sent that add to this game
        } else if (availableStartedGames.size() > 0) {
            // todo sent request to client to choose game.
            new ServerMessenger(socket).request("choose which game do you want");
            Game choosenGame; // game that client choose.
            game = choosenGame;
            if (!availableStartedGames.contains(choosenGame)) // if in this period another player/players add to game and game is full.
                endGame(clientHandler);
            // todo in request must receive answer of above question and then add client to game.
        } else {
            // this client start new game and he is host.
            game = new Game();
            games.add(game);
            new ServerMessenger(socket).request("You are the host of a new Game, so please enter the name of your game");
//            game.setName();
            // todo get name from client and then set to game.
            gameToClientHandlerHashMap.put(game, new ArrayList<ClientHandler>());
            gameToClientHandlerHashMap.get(game).add(clientHandler);
        }

        if (gameToClientHandlerHashMap.get(game).size() == game.getCapacity()) {
            game.state = GameState.ONGOING;
            // todo message to all that game is started.
        }

        // create player and add to game players.

        new Thread(clientHandler).start();
        return game;

    }

    public void endGame(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessage("Server is full!");
        clientHandler.kill();
    }

    private void startGame() {
        System.out.println("Game is started!");
        status = ServerStatus.STARTED;
        // Start the game
    }

    private Game findNotStartedGames() {
        Game notStartedGame = null;
        for (Game game : games) {
            if (game.state != GameState.WAITNG_FOR_VOTE && game.state != GameState.ONGOING)
                notStartedGame = game;
        }
        return notStartedGame;
    }

    private List<Game> findAvailableStartedGames() {
        List<Game> availableStartedGames = new ArrayList<>();
        for (Game game : games) {
            for (Player player : game.getPlayers()) {
                if (player.getPlayerType() == PlayerType.HUMAN) {
                    availableStartedGames.add(game);
                    break;
                }
            }
        }
        return availableStartedGames;
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


enum ServerStatus {
    WAITING, STARTED
}




