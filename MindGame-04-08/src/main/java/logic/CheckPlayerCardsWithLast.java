package logic;

import java.util.HashMap;
import java.util.List;

public class CheckPlayerCardsWithLast {
    private final Game game;
    private final int playerId;
    private HashMap<Integer, List<Integer>> playersRemovedCards;

    public CheckPlayerCardsWithLast(Game game, int playerId) {
        this.game = game;
        this.playerId = playerId;
        playersRemovedCards = new HashMap<>();
    }

//    public HashMap<Integer, List<Integer>> checkAndMakeChanges() {
//        HashMap<Integer, List<Integer>>
//        for (Player player : game.getPlayers()) {
//            if (player.getId() != playerId) {
//                for (player.getCards()) {
//
//                }
//
//            }
//        }
//    }


}
