package Client;

import Logic.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMessenger {
    Scanner scanner = new Scanner(System.in);
    Socket socket;
    PrintWriter printWriter;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    int authToken;

    public ClientMessenger(Client client) throws IOException {
        this.socket=client.socket;
        printWriter = new PrintWriter(socket.getOutputStream());
        //scanner = new Scanner(socket.getInputStream());
        this.authToken = client.authToken;

    }


    public void sendChoice(String choice){
        ClientMessage clientMessage = new ClientMessage(authToken,0, MessageType.CHOICE,choice);
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();
    }

    public void sendCode(Codes code){
        ClientMessage clientMessage = new ClientMessage(authToken,0, MessageType.CODE,code.name());
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();
    }  //codes except E

    public void sendEmoji(){
        try{
        System.out.println("Select Emoji");
        Emojis emoji = Emojis.valueOf(scanner.nextLine());
        System.out.println("Select target Id");
        int targetId = Integer.parseInt(scanner.nextLine());
        ClientMessage clientMessage = new ClientMessage(authToken,targetId, MessageType.Emoji,emoji.name());
        printWriter.println(gson.toJson(clientMessage));
        printWriter.flush();}
        catch (IllegalArgumentException ignored) {  //ignore wrong emoji
        }

    }

    public void listen() {
        String input = scanner.nextLine();
        handle(input);
    }

    public void handle(String messageStr){
        switch (messageStr){
            case "S" -> sendCode(Codes.S);
            case "E" -> sendEmoji();
            case "N" -> sendCode(Codes.N);
            case "P" -> sendCode(Codes.P);
            case "Q" -> sendCode(Codes.Q);
            case "T" -> sendCode(Codes.T);
            case "Y" -> sendCode(Codes.Y);
            default -> sendChoice(messageStr);
        }
    }

}
