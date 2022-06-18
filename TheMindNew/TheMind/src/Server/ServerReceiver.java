package Server;

import Logic.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerReceiver {

    public ServerReceiver(Socket socket) throws IOException {
        this.socket = socket;
    }
    Socket socket;
    Player thisPlayer;

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    Scanner in = new Scanner(socket.getInputStream());
    public ClientMessage decode(String messageStr){
        return gson.fromJson(messageStr, ClientMessage.class);
    }

    public void handle(String messageStr){
        ClientMessage clientMessage = decode(messageStr);
        switch (clientMessage.getType()){
            case Emoji -> emojiHandler(clientMessage);
            case CHOICE -> choiceHandler(clientMessage);
            case CODE -> codeHandler(clientMessage);
        }
    }

    private void choiceHandler(ClientMessage clientMessage) {
        if(player.name.equeals(null)){
            //set player name
        }
        //todo access game object from player
        switch (game.state){
            case GameState.WAITING_FOR_CAPACITY -> ;  //set game capacity
            case GameState.WAITING_FOR_GAME_NAME -> ; //connect player to the game
            case GameState.WAITING_TO_SET_GAME_NAME ->  ; //set game name
            //if choice is invalid IGNORE AND WAIT FOR ANOTHER
        }
    }

    private void emojiHandler(ClientMessage clientMessage) {
        //todo use clientMessage.getTargetId() to access targets serverMessenger ,use try catch in order to ignore wrong ids
        //todo access to the senders name
        ServerMessenger serverMessenger = ;
        serverMessenger.sendEmoji(senderName,clientMessage.getContext());
    }

    public void codeHandler(ClientMessage clientMessage){
        Codes code = Codes.valueOf(clientMessage.getContext());
        switch (code){
            case N -> codeNo();
            case P -> codePut();
            case Q -> codeQuit();
            case S -> codeStart();
            case T -> codeThrowingStar();
            case Y -> codeYes();
        }
    }
    public void codeStart(Game game){
        if((game.state == GameState.WAITING_FOR_START) && (game.getPlayers().get(0) == thisPlayer)){
            game.start();
        }
    }

    public void codePut(Game game) {
        if(game.state==GameState.ONGOING){
            game.play(thisplayer.id);
        }
    }

    public void codeYes(Game game) {
        if(game.state==GameState.WAITNG_FOR_VOTE){
            //set this player's answer as yes
        }
    }

    public void codeNo() {
        if(game.state==GameState.WAITNG_FOR_VOTE){
            //set this player's answer as no
        }
    }

    public void codeQuit(){
        player.quitGame(); //remove plater from game add a bot and add player to the lobby
    }

    public void codeThrowingStar(){
        if(game.state==GameState.ONGOING){
            gaem.vote();
        }
    }

    public void listen() {
        String input = in.nextLine();
        handle(input);
    }

    public void setThisPlayer(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }
}
