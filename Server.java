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
        Game game=null;
        ClientHandler clientHandler = new ClientHandler(socket);
        // todo set id to client handler. is this necessary? yes we need to access client handler by id to send messages from logic and we need to access to id by client handler to send id to logic

        //todo get player name

        while(game==null){
            game=setGame(clientHandler,socket);
        }  //this while will make sure that the client will connect to a game and helps to hande errors
        game = setGame(clientHandler,socket);


        if (gameToClientHandlerHashMap.get(game).size() == game.getCapacity()) {
            game.state = GameState.ONGOING;
            // todo message to all that game is started.
            //todo startGame()?
        }

        new Thread(clientHandler).start();
        return game;

    }

    public void endGame(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessage("Server is full!");
        clientHandler.kill();  //todo back to lobby:only inform the client/dont kill!/change the name of this function
    }

    private void startGame() {
        System.out.println("Game is started!");
        status = ServerStatus.STARTED;
        // Start the game  todo game.start()
    }

    private Game findNotStartedGames() {    //todo if there is more than one,return the first one in the list
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
    public Game setGame(ClientHandler clientHandler,Socket socket){
        Game game;
        Game notStartedGame = findNotStartedGames();
        List<Game> availableStartedGames = findAvailableStartedGames();

        if (notStartedGame != null) {
            game = notStartedGame;
            // a game that not started yet is exist...
            gameToClientHandlerHashMap.get(notStartedGame).add(clientHandler);
            Player player = new Player(?);  //todo
            notStartedGame.addPlayer(player);  //todo
            // todo sent that add to this game  (above)
            // todo? if game gets full remove it from notStartedGames ? and return null
        } else if (availableStartedGames.size() > 0) {
            // todo sent request to client to choose game.
            new ServerMessenger(socket).request("choose which game do you want or choose a name for a new game!");  //todo !
            Game choosenGame; // game that client choose.  //todo make a hashmap of availableStartedGames and the names as keys/send names of availableStartedGames/request to choose/access the game from the map by the input string/if the key does not match:create new game
            game = choosenGame;
            if (!availableStartedGames.contains(choosenGame)) // if in this period another player/players add to game and game is full. //todo return null
                endGame(clientHandler);
            Player player = new Player(?);  //todo
            game.addPlayer(player);  //todo
            // todo in request must receive answer of above question and then add client to game.
        } else {
            // this client start new game and he is host.
            game = new Game();
            games.add(game);  //todo what if the host leaves?
            new ServerMessenger(socket).request("You are the host of a new Game, so please enter the name of your game");  //todo remove this line/the name is the string above/just inform the host
            //game.setName();
            //todo request for capacity
            // game.setCapacity(capacity);
            Player player = new Player(?);  //todo
            player.setPlayerType(PlayerType.Host);
            game.addPlayer(player);  //todo
            gameToClientHandlerHashMap.put(game, new ArrayList<ClientHandler>());
            gameToClientHandlerHashMap.get(game).add(clientHandler);
        }

        return game;


    }
}


enum ServerStatus {
    WAITING, STARTED
}



