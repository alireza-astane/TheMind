package Server;

import Logic.ClientMessage;
import Logic.Codes;
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

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    Scanner in = new Scanner(socket.getInputStream());
    public ClientMessage decode(String messageStr){
        return gson.fromJson(messageStr, ClientMessage.class);
    }

    public void handle(String messageStr){
        ClientMessage clientMessage = decode(messageStr);
        switch (clientMessage.getType()){
            case Emoji -> ;
            case NAME -> ;
            case INT_CHOICE -> ;
            case CODE -> codeHandler(Codes.valueOf(clientMessage.getContext()));
        }
    }

    public void codeHandler(Codes code){
        switch (code){
            case E -> codeEmoji();
            case N -> codeNo();
            case P -> codePut();
            case Q -> codeQuit();
            case S -> codeStart();
            case T -> codeThrowingStar();
            case Y -> codeYes();
        }
    }

    public void setClientName(){}

    public void setCapacity(){}

    public void getOptionInt(){}
    //codes
    public void codeStart(){}

    public void codePut(){}

    public void codeYes(){}

    public void codeNo(){}

    public void codeQuit(){}

    public void codeThrowingStar(){}

    public void codeEmoji(){}

    public void listen() {
        String input = in.nextLine();
        handle(input);
    }
}
