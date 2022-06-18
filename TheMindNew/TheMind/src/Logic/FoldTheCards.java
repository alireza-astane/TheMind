package Logic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FoldTheCards {
    private Game game;
    public FoldTheCards(Game game) {
        this.game = game;
    }

    private ArrayList<Integer> getCards() {
        ArrayList<Integer> cards = new ArrayList<>();
        for (int i = 0; i < game.getLevel(); i++) {
            cards.add(getRandomCard());
        }
        return cards;
    }

    private int getRandomCard() {
        int index = ThreadLocalRandom.current().nextInt(0, game.getUnusedCards().size());
        game.getUnusedCards().remove(index);
        return game.getUnusedCards().get(index);

    }



}
