package logic;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public GameData(int lives,int throwingStars,Integer lastPlayedCard,int level ,ArrayList<PlayerData> playersData){
        this.lives=lives;
        this.throwingStars=throwingStars;
        this.level=level;
        this.lastPlayedCard=lastPlayedCard;
        this.playersData=playersData;
    }

    public static class PlayerData{
        protected int id;
        protected String name;
        protected int countOfCards;
        private List<Integer> hand;

        public PlayerData(int id,String name,int countOfCards, List<Integer> hand){
            this.id=id;
            this.name=name;
            this.countOfCards=countOfCards;
            this.hand = hand;
        }

        public int getId() {
            return id;
        }

        public int getCountOfCards() {
            return countOfCards;
        }

        public String getName() {
            return name;
        }

        public List<Integer> getHand() {
            return hand;
        }

        public void setHand(List<Integer> hand) {
            this.hand = hand;
        }
    }
    protected int lives;
    protected int throwingStars;
    protected Integer lastPlayedCard;
    protected int level;
    protected ArrayList<PlayerData> playersData;

    public static GameData getGameData(Game game){
        ArrayList<PlayerData> playersData = new ArrayList<>();
        for(Player player :game.players){
            PlayerData playerData = new PlayerData(player.id, player.name, player.hand.size(), player.getHand());
            playersData.add(playerData);
        }
        return new GameData(game.lives,game.throwingStars,game.lastPlayedCard,game.level,playersData);
    }

    public ArrayList<PlayerData> getPlayersData() {
        return playersData;
    }

    public int getLastPlayedCard() {
        return lastPlayedCard;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return lives;
    }

    public int getThrowingStars() {
        return throwingStars;
    }
}