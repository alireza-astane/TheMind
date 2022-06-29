package client;

import com.google.gson.Gson;
import server.Message;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.desktop.OpenURIEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendMessage implements Runnable{
    private Boolean finished = false;
    private String clientResponse;
    private Socket socket;
    private final Message message;
    private final PrintWriter out;
    private final Gson gson;
    private final Scanner scanner;

    public SendMessage(Socket socket, Message message) throws IOException {
        this.socket = socket;
        this.message = message;
        this.out = new PrintWriter(socket.getOutputStream());
        this.gson = new Gson();
        scanner = new Scanner(System.in);
        scanner.useDelimiter(System.lineSeparator());
    }

    public void init() throws IOException, InterruptedException {
        System.out.println(message.getText());
        this.clientResponse = getInput();
        sendMessage();
        scanner.reset();
    }

    private boolean hasNextLine(Scanner scanner) throws IOException {
        while (System.in.available() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return scanner.hasNextLine();
    }

    public String getInput() throws InterruptedException, IOException {


        String out = "";
        do {
            if (hasNextLine(scanner))
                out = scanner.nextLine();
        } while (out.equals(""));
        return out;
//
//        do {
//            out = scanner.nextLine().trim();
//        } while ("".equals(out));
//        return out;
    }

    public void sendMessage() {
        Message clientMessage = new Message(message.getType(), clientResponse);
        out.println(gson.toJson(clientMessage));
        out.flush();
        this.finished = true;
    }

    public void closeScanner() {
        scanner.close();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Boolean getFinished() {
        return finished;
    }
}