package server;

import logic.Player;
import logic.Game;
import logic.PlayerType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


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
        Game game=null;
        ClientHandler clientHandler = new ClientHandler(socket);

        while(game==null){
            game=setGame(clientHandler,socket);
        }
        gameToClientHandler.get(game).add(clientHandler);
        new Thread(clientHandler).start();

    }

    public Game setGame(ClientHandler clientHandler, Socket socket) throws IOException, InterruptedException {
        System.out.println("setgame...");
        Scanner in = new Scanner(socket.getInputStream());
        Game game = null;
        Game notStartedGame = findNotStartedGame();
        List<Game> availableStartedGame = findAvailableStartedGame();
        String playerName = "";
        while (playerName.equals("")) {
            new SendMessage(socket, new ServerMessage().getMessage(null, MessageType.ENTER_NAME, null)).sendMessage();
            playerName = in.nextLine();
        }
        if (notStartedGame != null) {
            game = notStartedGame;
            int id = game.getId();
            clientHandler.setId(id);
            clientHandler.setGame(game);
            clientHandler.setHost(game.getPlayers().size() <= 0);
            Player player = new Player(id);
            player.setPlayerType(PlayerType.HUMAN);
            player.setName(playerName);
            game.addPlayer(player);
        } else if (availableStartedGame.size() > 0) {
            List<Game> availableGames = findAvailableStartedGame();
            HashMap<String, Game> hashMapGamesAndItsNames = getHashMapGamesAndItsNames(availableGames);
            // todo هش مپ بالا باید ارسال به به سند مسیج
            new SendMessage(socket, new ServerMessage().getMessage(null, MessageType.CHOOSE_GAME, null));
            String gameName = in.nextLine();
            if (hashMapGamesAndItsNames.containsKey(gameName)) {
                if (findAvailableStartedGame().contains(hashMapGamesAndItsNames.get(gameName))) {
                    game = hashMapGamesAndItsNames.get(gameName);
                    int id = game.getId();
                    clientHandler.setId(id);
                    clientHandler.setGame(game);
                    clientHandler.setHost(false);
                    Player player = new Player(id);
                    player.setPlayerType(PlayerType.HUMAN);
                    player.setName(playerName);
                    game.addPlayer(player);
                }
            } else {
                game = new Game();
                game.setName(gameName);
                int id = game.getId();
                clientHandler.setId(id);
                clientHandler.setGame(game);
                clientHandler.setHost(true);
                Player player = new Player(id);
                player.setPlayerType(PlayerType.HUMAN);
                player.setName(playerName);
                game.addPlayer(player);
                gameToClientHandler.put(game, new ArrayList<>());
            }
        } else {
            game = new Game();
            int id = game.getId();
            clientHandler.setId(id);
            clientHandler.setGame(game);
            clientHandler.setHost(true);
            Player player = new Player(id);
            player.setPlayerType(PlayerType.HUMAN);
            player.setName(playerName);
            game.addPlayer(player);
            gameToClientHandler.put(game, new ArrayList<>());
        }
        return game;
    }

    public HashMap<Game, List<ClientHandler>> getGameToClientHandler() {
        return gameToClientHandler;
    }

    private Game findNotStartedGame() {
        Game notStartedGame = null;
        for (Game game : gameToClientHandler.keySet()) {
            if (game.getGameState() == null && game.getCapacity() != null && game.getPlayers().size() < game.getCapacity()) {
                notStartedGame = game;
                break;
            }
        }
        return notStartedGame;
    }

    public List<Game> findAvailableStartedGame() {
        List<Game> availableStartedGame = new ArrayList<>();
        for (Game game : gameToClientHandler.keySet()) {
            for (Player player : game.getPlayers()) {
                if (player.getPlayerType() == PlayerType.BOT) {
                    availableStartedGame.add(game);
                    break;
                }
            }
        }
        return availableStartedGame;
    }



    public HashMap<String, Game> getHashMapGamesAndItsNames(List<Game> availableGames) {
        HashMap<String, Game> hashMapGamesAndItsNames = new HashMap<>();
        for (Game game : availableGames) {
            hashMapGamesAndItsNames.put(game.getName(), game);
        }
        return hashMapGamesAndItsNames;
    }

    public ClientHandler findClientHandlerByPlayerId(Game game, int id) {
        for (ClientHandler clientHandler : gameToClientHandler.get(game)) {
            if (clientHandler.getId() == id) {
                return clientHandler;
            }
        }
        return null;
    }

    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        Server.server = server;
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Server.counter = counter;
    }


}
