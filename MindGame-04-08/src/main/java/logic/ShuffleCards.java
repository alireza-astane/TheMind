package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShuffleCards {
    private final Game game;
    private final Random random;

    public ShuffleCards(Game game) {
        this.game = game;
        random = new Random();
    }

    public void init() {
        setRawCardsToGame();
        setInitCardToAllPlayer();
    }

    public void setInitCardToAllPlayer() {
        for (Player player : game.getPlayers()) {
            player.setCards(shuffle());
        }
    }

    public void setRawCardsToGame() {
        List<Integer> rawCards = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            rawCards.add(i);
        }
        game.setFreeCards(rawCards);
    }


    private List<Integer> shuffle() {
        List<Integer> shuffledCards = new ArrayList<>();
        int index;
        int number;
        for (int i = 0; i < game.getLevel(); i++) {
            index = random.nextInt(game.getFreeCards().size());
            number = game.getFreeCards().get(index);

            shuffledCards.add(number);
            game.getFreeCards().remove(index);
        }
        Collections.sort(shuffledCards);
        return shuffledCards;
    }
}
