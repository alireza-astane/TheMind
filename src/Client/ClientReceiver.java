package Client;

import Logic.GameData;
import Logic.ServerMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientReceiver {
    Socket socket;
    public ClientReceiver(Client client) throws IOException {
        this.socket=client.socket;
    }

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();


    Scanner scanner = new Scanner(socket.getInputStream());




    public ServerMessage decode(String messageStr){
        return gson.fromJson(messageStr, ServerMessage.class);
    }
    public void handle(String messageStr){
        ServerMessage serverMessage = decode(messageStr);
        switch (serverMessage.getType()){
            case INFO -> showInfo(serverMessage.getContext());
            case GAME_DATA -> showGameData(serverMessage.getContext());
            case Emoji -> showEmojis(serverMessage.getContext());
            case REQUEST -> showRequest(serverMessage.getContext());
        }
    }

    public void showInfo(String context){
        System.out.println(context);
    }

    public void showRequest(String context){
        System.out.println(context);
        //todo wait for the answer and send it to the server
    }

    public void showEmojis(String context){
        System.out.println(context);
    }

    public void showGameData(String context){
        GameData gameData = gson.fromJson(context, GameData.class);
        System.out.println("Game Level : "+gameData.getLevel());
        System.out.println("Last Played Card : "+gameData.getLastPlayedCard());
        System.out.println("Throwing Stars : "+gameData.getThrowingStars());
        System.out.println("Lives : "+gameData.getLives());

        System.out.println("Player name     Player Id    Player's Cards");

        for(GameData.PlayerData playerData : gameData.getPlayersData()){
            System.out.println(playerData.getName()+" "+playerData.getId()+" "+playerData.getCountOfCards());
        }

        //todo show it's own hand 1.connect player to this object in order to access the hand
        System.out.println("Your Hand: "+hand);

    }

    public void listen() {
        String input = scanner.nextLine();
        handle(input);
    }
}
