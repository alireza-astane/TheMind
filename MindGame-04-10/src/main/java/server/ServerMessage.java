package server;

import logic.Game;
import logic.Player;

import java.awt.font.ShapeGraphicAttribute;
import java.util.List;

public class ServerMessage {


    public Message getMessage(Game game, MessageType type, Integer id) {
        return switch (type) {
            case GAME_NAME -> new Message(MessageType.GAME_NAME, "Enter Game Name:");
            case GAME_NAME_SET -> new Message(MessageType.GAME_NAME_SET, "The game is set: " + game.getName());
            case GAME_CAPACITY -> new Message(MessageType.GAME_CAPACITY, "Enter Game Capacity:");
            case GAME_CAPACITY_SET -> new Message(MessageType.GAME_CAPACITY_SET, "The Game capacity is set: " + game.getCapacity());
            case GAME_START -> new Message(MessageType.GAME_START, "When ever you want game to start, enter 'START' word.");
            case ORDER -> new Message(MessageType.ORDER, "Enter order you want as follow: \n 0: to Play \n 1: ninja Request \n 2: Send Emoji");
            case CHOOSE_TARGET -> new Message(MessageType.CHOOSE_TARGET, "Choose player you want to send emoji from list above and then choose emoji you want \n for example 1:2 means that emoji 2 send to player 1");
            case PLAYER_LIST -> new Message(MessageType.PLAYER_LIST, "The players is: " + printGamePlayer(game));
            case EMOJI_LIST -> new Message(MessageType.EMOJI_LIST, "1:😂  |  2:💪  |  3:😡  |  4:🙄");
            case CORRECT ->  new Message(MessageType.CORRECT, "✅ correct!");
            case WRONG -> new Message(MessageType.WRONG, "❌ incorrect!");
            case NEXT_LEVEL -> new Message(MessageType.NEXT_LEVEL, "Game Go To next Level!, current Level is: " + game.getLevel());
            case WIN -> new Message(MessageType.WIN, "Congratulation! You win! 🏆");
            case LOSE -> new Message(MessageType.LOSE, "you lose...");
            case THROWINGSTAR_REQUEST -> new Message(MessageType.THROWINGSTAR_REQUEST, "Are you agree to use ThrowingStar? 0: Yes | 1: No");
            case THROWINGSTAR_ACCEPTED -> new Message(MessageType.THROWINGSTAR_ACCEPTED, "All player agree to use ThrowingStar card");
            case NOCARD -> new Message(MessageType.NOCARD, "You have no card to play :(");
            case THROWINGSTAR_REJECTED -> new Message(MessageType.THROWINGSTAR_REJECTED, "ThrowingStar request is rejected...");
            case CARD_LIST -> new Message(MessageType.CARD_LIST, "🔻🔻🔻Your Hands is: " + printPlayerCard(game, id) + "\n");
            case GAME_DATA -> new Message(MessageType.GAME_DATA, printGameData(game));
            case BREAK_LINE -> new Message(MessageType.BREAK_LINE, "---------------------------");
            case PLAYER_CARD_COUNT -> new Message(MessageType.PLAYER_CARD_COUNT, printPlayerLeftCardCount(game));
            case ENTER_NAME -> new Message(MessageType.ENTER_NAME, "Please enter your name:");
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public String printGamePlayer(Game game) {
        String stringPlayersId = "";
        for (Player player : game.getPlayers()) {
            stringPlayersId += player.getId() + " ";
        }
        return stringPlayersId;
    }

    public String printPlayerCard(Game game, int id) {
        Player player = game.getPlayer(id);
        List<Integer> cards = player.getHand();
        StringBuilder stringCards = new StringBuilder();
        for (Integer card : cards) {
            stringCards.append(card.toString()).append(" ");
        }
        return stringCards.toString();
    }

    public String printPlayerLeftCardCount(Game game) {
        String res = "***PLAYER LEFT CARD***\n";
        for (Player player : game.getPlayers()) {
            res += player.getName() + ": " + player.getHand().size() + "\n";
        }
        return res;
    }

    public String printGameData(Game game) {
        String gameData = "*** GAME DATA ***\n";
        gameData += "Level: " + game.getLevel() + "\n";
        gameData += "Heart card count: " + game.getLives() + "\n";
        gameData += "Throwing Star Card count: " + game.getThrowingStars() + "\n";
        if (game.getLastPlayedCard() != null)
            gameData += "Last PlayedCard: " + game.getLastPlayedCard();
        return gameData;
    }
}
