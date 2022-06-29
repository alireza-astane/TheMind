package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client implements Runnable{
    private GetMessage getMessage;

    private void init() throws IOException {
        Socket sendSocket = new Socket("localhost", 8080);

        PrintWriter printWriter = new PrintWriter(sendSocket.getOutputStream());
        Scanner scanner = new Scanner(sendSocket.getInputStream());

        try {
            while (true) {
                String input = scanner.nextLine();
                GetMessage.getInstance(sendSocket, input);
            }
        } catch (NoSuchElementException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}