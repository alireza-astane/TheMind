package client;

import com.google.gson.Gson;
import server.Message;
import server.MessageType;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GetMessage {
    public static GetMessage getMessage;
    private Socket socket;
    private String messageString;
    private Message message;
    private final Gson gson;
    private Thread sendThread;
    private SendMessage sendMessage;

    public static void getInstance(Socket socket, String messageString) throws IOException, InterruptedException {
        if (getMessage == null) {
            getMessage = new GetMessage(socket, messageString);
        }
        else {
            getMessage.setMessageString(messageString);
            getMessage.setSocket(socket);
            getMessage.run();
        }
    }


    public GetMessage(Socket socket, String messageString) throws IOException, InterruptedException {
        this.socket = socket;
        this.messageString = messageString;
        gson = new Gson();
        run();
    }

    public void run() throws IOException {
        if (sendThread != null) {
            sendThread.stop();
        }
        decodeMessage();
        sendToSendClass();
    }

    public void decodeMessage() {
        this.message = gson.fromJson(messageString, Message.class);
    }

    public void sendToSendClass() throws IOException {
        ArrayList<MessageType> inputNeeded = new ArrayList<>();
        inputNeeded.add(MessageType.GAME_NAME);
        inputNeeded.add(MessageType.GAME_CAPACITY);
        inputNeeded.add(MessageType.GAME_START);
        inputNeeded.add(MessageType.ORDER);
        inputNeeded.add(MessageType.CHOOSE_TARGET);
        inputNeeded.add(MessageType.THROWINGSTAR_REQUEST);
        inputNeeded.add(MessageType.ENTER_NAME);
        
        if (inputNeeded.contains(message.getType())) {
            sendMessage = new SendMessage(socket, message);
            sendThread = new Thread(sendMessage);
            sendThread.setDaemon(true);
            sendThread.start();
        } else {
            System.out.println(message.getText());
        }
    }

    public void setSendThread(Thread sendThread) {
        this.sendThread = sendThread;
    }

    public static GetMessage getGetMessage() {
        return getMessage;
    }

    public static void setGetMessage(GetMessage getMessage) {
        GetMessage.getMessage = getMessage;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getMessageString() {
        return messageString;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Gson getGson() {
        return gson;
    }

    public Thread getSendThread() {
        return sendThread;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }
}