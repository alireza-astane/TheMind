package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import logic.Game;
import logic.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class GetMessage {
    private final Game game;
    private final String messageString;
    private Socket socket;
    private final ClientHandler clientHandler;
    private Message message;
    private final Gson gson;

    public GetMessage(String messageString, ClientHandler clientHandler) throws IOException {
        this.clientHandler = clientHandler;
        this.messageString = messageString;
        this.socket = clientHandler.getSocket();
        this.game = clientHandler.getGame();
        this.gson = new Gson();
        decodeMessage();
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

    public void connectToLogic() throws IOException {
        Message serverMessage;
        if (message.getType() == MessageType.GAME_NAME) {
            System.out.println("in the game name in get messag eof server");
            game.setName(message.getText());
            serverMessage = new Message(MessageType.GAME_NAME_SET, "The game is set: " + game.getName());
            sendToAllClient(serverMessage);
        } else if (message.getType() == MessageType.GAME_CAPACITY) {
            try {
                int capacity = Integer.parseInt(message.getText());
                System.out.println("game capacity is " + Server.getInstance().getGameToClientHandler().get(game).size());
                if (capacity > 1) {
                    if (capacity < Server.getInstance().getGameToClientHandler().get(game).size()) {
                        serverMessage = new Message(MessageType.WRONG_VALUE, "Now " + Server.getInstance().getGameToClientHandler().get(game).size() + " Player is enter to the game...");
                        new SendMessage(socket, serverMessage).sendMessage();
                        serverMessage = new Message(MessageType.GAME_CAPACITY, "Enter Game Capacity:");
                        new SendMessage(socket, serverMessage).sendMessage();
                    } else {
                        game.setCapacity(capacity);
                        serverMessage = new Message(MessageType.GAME_CAPACITY_SET, "The Game capacity is set: " + game.getCapacity());
                        sendToAllClient(serverMessage);
                    }
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
                game.start();
                // todo start game
                for (Player player : game.getPlayers()) {
                    serverMessage = new Message(MessageType.WRONG_VALUE, "your card is: " + printPlayerCard(player));
                    new SendMessage(Server.getInstance().getGameToClientHandler().get(game).get(player.getId() - 1).getSocket(), serverMessage).sendMessage();
                }
                serverMessage = new Message(MessageType.WRONG_VALUE, "Game is START... \n");
                sendToAllClient(serverMessage);
                serverMessage = new ServerMessage(game, MessageType.ORDER).getMessage();
                System.out.println(serverMessage.getText());
                sendToAllClient(serverMessage);

            } else {
                new SendMessage(socket, new ServerMessage(game, MessageType.GAME_START).getMessage()).sendMessage();// todo client not handle yet.
            }
        } else if (message.getType() == MessageType.ORDER) {
            switch (message.getText()) {
                case "0":
                    System.out.println("zeroooo");
                    Player player = game.getPlayerById(clientHandler.getId());
                    sendToAllClient(new Message(MessageType.WRONG_VALUE, "Player " + player.getId() + "is play!: " + " his card is: " + player.getCards().get(0)));
                    game.setLast(player.getCards().get(0));


                    // TODO: 1. send to all that player play and show the card.
                    // TODO: 2. if fail: send all player that every player lost which cards --> checl heartCard to fail or not --> send cardCountHeart or if fail send to them and finish the game.

                    // TODO: 2. if correct: done. --> check that go to next level or not.
                    // TODO: 2.


                    break;
                case "1":
                    // todo send to other player to answer to request.
                    break;
                case "2":
                    new SendMessage(socket, new ServerMessage(game, MessageType.PLAYER_LIST).getMessage()).sendMessage();
                    new SendMessage(socket, new ServerMessage(game, MessageType.EMOJI_LIST).getMessage()).sendMessage();
                    new SendMessage(socket, new ServerMessage(game, MessageType.CHOOSE_TARGET).getMessage()).sendMessage();
                    break;
            }
        } else if (message.getType() == MessageType.CHOOSE_TARGET) {
            // TODO not handle if inupt isn't correct...
            new SendMessage(socket, new Message(MessageType.WRONG_VALUE, "has been send")).sendMessage();
            new SendMessage(socket, new ServerMessage(game, MessageType.ORDER).getMessage()).sendMessage();
            String[] orderParameters = message.getText().split(":");
            int target = Integer.parseInt(orderParameters[0]);
            String emoji = orderParameters[1];
            emoji = "aliadsjflasjdlfjaslidjfoasjodfjoasdifjoiajsdofjoiasd";
            // todo this is wrong. because maybe id is different from list order.
            Socket targetSocket = Server.getInstance().findClientHandlerByPlayerId(game, target).getSocket();
            new SendMessage(targetSocket, new Message(MessageType.WRONG_VALUE, emoji)).sendMessage();
            new SendMessage(targetSocket, new ServerMessage(game, MessageType.ORDER).getMessage()).sendMessage();

        } else {
            serverMessage = new Message(MessageType.GAME_CAPACITY_SET, message.getText());
            new SendMessage(socket, serverMessage).sendMessage();
        }
    }

    public void sendToAllClient(Message serverMessage) throws IOException {
        List<ClientHandler> clientHandlers = Server.getInstance().getGameToClientHandler().get(game);
        for (ClientHandler clientHandler : clientHandlers) {
            socket = clientHandler.getSocket();
            new SendMessage(socket, serverMessage).sendMessage();
        }
    }

    public String printPlayerCard(Player player) {
        List<Integer> cards = player.getCards();
        StringBuilder stringCards = new StringBuilder();
        for (Integer card : cards) {
            stringCards.append(card.toString()).append(" ");
        }
        return stringCards.toString();
    }



}
