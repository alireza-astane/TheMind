package Server;
import Logic.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerMessenger {
    private final Socket socket;
    public ServerMessenger(Socket socket) throws IOException {
        this.socket=socket;
        out = new PrintWriter(socket.getOutputStream());
    }
    GsonBuilder gsonBuilder = new GsonBuilder();

    private final PrintWriter out;
    Gson gson = gsonBuilder.create();

    public void info(String info){
        ServerMessage serverMessage = new ServerMessage(MessageType.INFO,info);
        out.println(gson.toJson(serverMessage));
        out.flush();
    }
    // "The Game Has Bean Started!"
    // "The Star Has Bean Thrown!"
    // "It was a Mistake"
    // name+" Played Number "+num
    // "The Game is Over! You Won!"
    // "The Game is Over! You Lost!"
    // "It was a Correct Action!"
    // name+" Joined the Game!"
    // name + " Left the Game"


    public void request(String request){
        ServerMessage serverMessage = new ServerMessage(MessageType.REQUEST,request);
        out.println(gson.toJson(serverMessage));
        out.flush();   //todo wait for answer
    }
    // "There is No Incomplete Game in Lobby......
    // "Enter Game's Name Please!"
    // "Enter Your Name Please!"
    // "Enter Game Capacity?"
    // "Do You Agree With Throwing Star Request?(Y for Yes and N for No)"

    public void sendGameData(Game game){
        GameData gameData = GameData.getGameData(game);
        ServerMessage serverMessage = new ServerMessage(MessageType.GAME_DATA,gson.toJson(gameData));
        out.println(gson.toJson(serverMessage));
        out.flush();
    }

    public void sendEmoji(String senderName, Emojis emoji){
        ServerMessage serverMessage = new ServerMessage(MessageType.Emoji,senderName + " Sent You the Emoji: "+emoji.getStr());
        out.println(gson.toJson(serverMessage));
        out.flush();
    }
}
