package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import logic.Game;
import logic.GameState;
import logic.Player;
import logic.Result;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMessage {
    private final Game game;
    private final String messageString;
    private Socket socket;
    private final ClientHandler clientHandler;
    private Message message;
    private final Gson gson;
    private final ServerMessage templateServerMessage;

    public GetMessage(String messageString, ClientHandler clientHandler) throws IOException, InterruptedException {
        this.clientHandler = clientHandler;
        this.messageString = messageString;
        this.socket = clientHandler.getSocket();
        this.game = clientHandler.getGame();
        this.gson = new Gson();
        templateServerMessage = new ServerMessage();
        decodeMessage();
        if (message != null)
            connectToLogic();
    }

    public void decodeMessage() {
        try {
            this.message = gson.fromJson(messageString, Message.class);
        } catch (JsonSyntaxException e) {
            this.message = null;
            e.printStackTrace();
        }
    }
    public void connectToLogic() throws IOException, InterruptedException {
        ArrayList<Object> resultArrayList;
        Result result;
        HashMap<Integer, List<Integer>> lostCards;

        Message serverMessage;
        if (game.getGameState() != GameState.WAITING) {
            if (message.getType() == MessageType.GAME_NAME) {
                String gameName = message.getText();
                List<String> nameOfAllGames = Server.getInstance().getHashMapGamesAndItsNames(Server.getInstance().getGameToClientHandler().keySet().stream().toList()).keySet().stream().toList();
                if (!nameOfAllGames.contains(gameName) && !gameName.equals("")) {
                    game.setName(gameName);
                    serverMessage = templateServerMessage.getMessage(game, MessageType.GAME_NAME_SET, null);
                    sendToAllClient(serverMessage);
                } else {
                    new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "Name is used before."));
                }
            } else if (message.getType() == MessageType.GAME_CAPACITY) {
                try {
                    int capacity = Integer.parseInt(message.getText());
                    if (capacity > 1) {
                        game.setCapacity(capacity);
                        serverMessage = templateServerMessage.getMessage(game, MessageType.GAME_CAPACITY_SET, null);
                        sendToAllClient(serverMessage);
                    } else {
                        serverMessage = new Message(MessageType.WRONG_VALUE, "Game Capacity must be positive number greater than 1");
                        new SendMessage(socket, serverMessage).sendMessage();
                        serverMessage = new Message(MessageType.GAME_CAPACITY, "Enter Game Capacity:");
                        new SendMessage(socket, serverMessage).sendMessage();
                    }
                } catch (NumberFormatException e) {
                    serverMessage = new Message(MessageType.WRONG_VALUE, "Capacity must be a Number...");
                    new SendMessage(socket, serverMessage).sendMessage();
                    serverMessage = new Message(MessageType.GAME_CAPACITY, "Enter Game Capacity:");
                    new SendMessage(socket, serverMessage).sendMessage();
                }
            } else if (message.getType() == MessageType.GAME_START) {
                if (message.getText().equals("START")) {
                    Server.getInstance().createRequiredBots(game);
                    game.start();
                    startBots();

                    serverMessage = new Message(MessageType.WRONG_VALUE, "Game is START... \n");
                    sendToAllClient(serverMessage);
                    printOrderAndGameDataAndPlayerHand();
                } else
                    new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.GAME_START, null)).sendMessage();
            } else if (message.getType() == MessageType.ORDER) {
                switch (message.getText()) {
                    case "0" -> {
                        try {
                            serverMessage = new Message(MessageType.PLAY, "Player no. " + clientHandler.getId() + " Play with Card: " + game.getPlayers().get(clientHandler.getId() - 1).getHand().get(0).toString());
                            sendToAllClient(serverMessage);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        game.setGameState(GameState.WAITING);
                        stopBots();
                        resultArrayList = game.put(clientHandler.getId());
                        game.setGameState(GameState.ONGOING);  //todo you can place it later if necessary
                        startBots();
                        result = (Result) resultArrayList.get(0);
                        lostCards = (HashMap<Integer, List<Integer>>) resultArrayList.get(1);
                        switch (result) {
                            case CORRECT_NEXT_LEVEL -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.CORRECT, null));
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.NEXT_LEVEL, null));
                                printOrderAndGameDataAndPlayerHand();
                            }
                            case CORRECT_CONTINUE -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.CORRECT, null));
                                printOrderAndGameDataAndPlayerHand();
                            }
                            case CORRECT_WIN -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.CORRECT, null));
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WIN, null));
                                printOrderAndGameDataAndPlayerHand();
                            }
                            case WRONG_LOSE -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WRONG, null));
                                for (Map.Entry<Integer, List<Integer>> entry : lostCards.entrySet()) {
                                    if (entry.getValue().size() > 0)
                                        sendToAllClient(new Message(MessageType.LOST_CARD, "Player no. " + entry.getKey() + " Lost these cards: " + entry.getValue()));
                                }
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.LOSE, null));
                            }
                            case WRONG_WIN -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WRONG, null));
                                for (Map.Entry<Integer, List<Integer>> entry : lostCards.entrySet()) {
                                    if (entry.getValue().size() > 0)
                                        sendToAllClient(new Message(MessageType.LOST_CARD, "Player no. " + entry.getKey() + " Lost these cards: " + entry.getValue()));
                                }
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WIN, null));
                            }
                            case WRONG_NEXT_LEVEL -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WRONG, null));
                                for (Map.Entry<Integer, List<Integer>> entry : lostCards.entrySet()) {
                                    if (entry.getValue().size() > 0)
                                        sendToAllClient(new Message(MessageType.LOST_CARD, "Player no. " + entry.getKey() + " Lost these cards: " + entry.getValue()));
                                }
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.NEXT_LEVEL, null));
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.GAME_DATA, null));
                                printOrderAndGameDataAndPlayerHand();
                            }
                            case WRONG_CONTINUE -> {
                                sendToAllClient(templateServerMessage.getMessage(game, MessageType.WRONG, null));
                                for (Map.Entry<Integer, List<Integer>> entry : lostCards.entrySet()) {
                                    if (entry.getValue().size() > 0)
                                        sendToAllClient(new Message(MessageType.LOST_CARD, "Player no. " + entry.getKey() + " Lost these cards: " + entry.getValue()));
                                }
                                printOrderAndGameDataAndPlayerHand();
                            }
                            case NOCARD -> {
                                new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.NOCARD, null)).sendMessage();
                                sendToAllClient(templateServerMessage.getMessage(null, MessageType.ORDER, null));
                            }
                        }
                    }
                    case "1" -> {
                        game.setGameState(GameState.WAITING);  //todo maybe replace
                        stopBots();
                        sendToAllClient(templateServerMessage.getMessage(game, MessageType.THROWINGSTAR_REQUEST, null));
                        game.startVote(clientHandler.getId());
                        while (game.getVoteResult() == null) {
                            Thread.sleep(20);
                        }
                        if (game.getVoteResult()) {
                            sendToAllClient(templateServerMessage.getMessage(game, MessageType.THROWINGSTAR_ACCEPTED, null));
                            resultArrayList = game.doThrowingStar();
                            game.setGameState(GameState.ONGOING);  //todo maybe replace
                            startBots();
                            result = (Result) resultArrayList.get(0);
                            lostCards = (HashMap<Integer, List<Integer>>) resultArrayList.get(1);
                            for (Map.Entry<Integer, List<Integer>> entry : lostCards.entrySet()) {
                                if (entry.getValue().size() > 0)
                                    sendToAllClient(new Message(MessageType.LOST_CARD, "Player no. " + entry.getKey() + " Lost these cards: " + entry.getValue()));
                            }
                            switch (result) {
                                case THROWINGSTAR_WIN -> sendToAllClient(templateServerMessage.getMessage(game, MessageType.WIN, null));
                                case THROWINGSTAR_NEXTLEVEL -> sendToAllClient(templateServerMessage.getMessage(game, MessageType.NEXT_LEVEL, null));
                            }
                        } else {
                            sendToAllClient(templateServerMessage.getMessage(game, MessageType.THROWINGSTAR_REJECTED, null));
                            new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.NOCARD, null)).sendMessage();
                            printOrderAndGameDataAndPlayerHand();
                        }
                    }
                    case "2" -> {
                        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.PLAYER_LIST, null)).sendMessage();
                        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.EMOJI_LIST, null)).sendMessage();
                        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.CHOOSE_TARGET, null)).sendMessage();
                    }
                }
            } else if (message.getType() == MessageType.THROWINGSTAR_REQUEST) {
                int id = clientHandler.getId();
                if (message.getText().equals("0"))
                    game.setResult(id, true);
                else if (message.getText().equals("1"))
                    game.setResult(id, false);
                else {
                    new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "Wrong input..."));
                    new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.THROWINGSTAR_REQUEST, null));
                }

            } else if (message.getType() == MessageType.CHOOSE_TARGET) {
                try {
                    String[] orderParameters = message.getText().split(":");
                    int target = Integer.parseInt(orderParameters[0]);
                    int emojiNumber = Integer.parseInt(orderParameters[1]);
                    String emoji = null;
                    switch (emojiNumber) {
                        case 1 : emoji = "😂";
                        case 2 : emoji = "💪";
                        case 3 : emoji = "😡";
                        case 4 : emoji = "🙄";
                    }
                    if (emoji == null)
                        sendForEmojiRequester(socket);
                    else {
                        Socket targetSocket = Server.getInstance().findClientHandlerByPlayerId(game, target).getSocket();
                        String toTargetMessage = "\n A message from Player" + clientHandler.getId() + "\n";
                        toTargetMessage += emoji + "\n";
                        new SendMessage(targetSocket, new Message(MessageType.WRONG_VALUE, toTargetMessage)).sendMessage();
                        printOrderAndGameDataAndPlayerHand();
                        new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "Emoji has been send.")).sendMessage();
                    }
                } catch (IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
                    new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "Wrong Format. try again:"));
                    sendForEmojiRequester(socket);
                }
            } else {
                serverMessage = new Message(MessageType.GAME_CAPACITY_SET, message.getText());
                new SendMessage(socket, serverMessage).sendMessage();
            }
        } else {
            serverMessage = new Message(MessageType.WRONG_VALUE, "Now server is busy! try again A few milliseconds later!!");
            new SendMessage(socket, serverMessage);
        }
    }

    public void sendToAllClient(Message serverMessage) throws IOException {
        List<ClientHandler> clientHandlers = Server.getInstance().getGameToClientHandler().get(game);
        for (ClientHandler clientHandler : clientHandlers) {
            socket = clientHandler.getSocket();
            new SendMessage(socket, serverMessage).sendMessage();
        }
    }

    public void sendCardsToAllPLayer() throws IOException {
        for (Player player : game.getPlayers()) {
            new SendMessage(Server.getInstance().getGameToClientHandler().get(game).get(player.getId() - 1).getSocket(), templateServerMessage.getMessage(game, MessageType.CARD_LIST, player.getId())).sendMessage();
        }
    }

    public void printOrderAndGameDataAndPlayerHand() throws IOException {
        sendToAllClient(templateServerMessage.getMessage(null, MessageType.BREAK_LINE, null));
        sendToAllClient(templateServerMessage.getMessage(game, MessageType.GAME_DATA, null));
        sendToAllClient(templateServerMessage.getMessage(game, MessageType.PLAYER_CARD_COUNT, null));
        sendCardsToAllPLayer();
        sendToAllClient(templateServerMessage.getMessage(null, MessageType.ORDER, null));
    }

    public void sendForEmojiRequester(Socket socket) throws IOException {
        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.PLAYER_LIST, null)).sendMessage();
        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.EMOJI_LIST, null)).sendMessage();
        new SendMessage(socket, templateServerMessage.getMessage(game, MessageType.CHOOSE_TARGET, null)).sendMessage();
    }

    public void startBots(){
        List<Thread> botHandlerThreads = Server.getInstance().getGameToBotHandlerThreads().get(game);
        for(Thread thread:botHandlerThreads){
            thread.start();
        }


    }

    public void stopBots(){
        List<Thread> botHandlerThreads = Server.getInstance().getGameToBotHandlerThreads().get(game);
        for(Thread thread:botHandlerThreads){
            thread.interrupt();
        }
    }


}
