package Client;

import Logic.ClientMessage;
import Logic.Codes;
import Logic.Emojis;
import Logic.MessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMessenger {
    //todo how to send message by user input
    Socket socket;
    PrintWriter printWriter;
    Scanner scanner;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    int authToken;

    public ClientMessenger(Client client) throws IOException {
        this.socket=client.socket;
        printWriter = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
        this.authToken = client.authToken;

    }

    public void sendName(String str){
        ClientMessage clientMessage = new ClientMessage(authToken,0, MessageType.NAME,str);
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();

    }

    public void sendIntChoice(String integer){
        ClientMessage clientMessage = new ClientMessage(authToken,0, MessageType.INT_CHOICE,integer);
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();
    }

    public void sendCode(Codes code){
        ClientMessage clientMessage = new ClientMessage(authToken,0, MessageType.CODE,code.name());
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();
    }  //codes except E

    public void sendEmoji(Emojis emoji, int targetId){
        ClientMessage clientMessage = new ClientMessage(authToken,targetId, MessageType.Emoji,emoji.name());
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();
    }

}
