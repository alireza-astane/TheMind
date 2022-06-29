package server;

import logic.Game;
import logic.Player;

import java.util.List;

public class ServerMessage {
    private MessageType type;
    private Game game;

    public ServerMessage(Game game, MessageType type) {
        this.game = game;
        this.type = type;
    }

    public Message getMessage() {
        return switch (type) {
            case GAME_NAME -> new Message(MessageType.GAME_NAME, "Enter Game Name:");
            case GAME_NAME_SET -> new Message(MessageType.GAME_NAME_SET, "The game is set: " + game.getName());
            case GAME_CAPACITY -> new Message(MessageType.GAME_CAPACITY, "Enter Game Capacity:");
            case GAME_CAPACITY_SET -> new Message(MessageType.GAME_CAPACITY_SET, "The Game capacity is set: " + game.getCapacity());
            case GAME_START -> new Message(MessageType.GAME_START, "When ever you want game to start, enter 'START' word.");
            case ORDER -> new Message(MessageType.ORDER, "Enter order you want as follow: \n 0: to Play \n 1: ninja Request \n 2: Send Emoji");
            case CHOOSE_TARGET -> new Message(MessageType.CHOOSE_TARGET, "Choose player you want to send emoji from list above and then choose emoji you want \n for example 1:2 means that emoji 2 send to player 1");
            case PLAYER_LIST -> new Message(MessageType.PLAYER_LIST, "The players is: " + printGamePlayer());
            case EMOJI_LIST -> new Message(MessageType.EMOJI_LIST, "1:😂  |  2:💪  |  3:😡  |  4:🙄");
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public String printGamePlayer() {
        String stringPlayersId = "";
        for (Player player : game.getPlayers()) {
            stringPlayersId += player.getId() + " ";
        }
        return stringPlayersId;
    }


}
