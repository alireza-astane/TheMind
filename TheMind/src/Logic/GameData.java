package Logic;

import java.util.ArrayList;

public class GameData {
    public GameData(int lives,int throwingStars,int lastPlayedCard,int level ,ArrayList<PlayerData> playersData){
        this.lives=lives;
        this.throwingStars=throwingStars;
        this.level=level;
        this.lastPlayedCard=lastPlayedCard;
        this.playersData=playersData;
    }
    public static class PlayerData{
        public PlayerData(int id,String name,int countOfCards){
            this.id=id;
            this.name=name;
            this.countOfCards=countOfCards;
        }
        protected  int id;

        public int getId() {
            return id;
        }

        public int getCountOfCards() {
            return countOfCards;
        }

        public String getName() {
            return name;
        }

        protected  String name;
        protected  int countOfCards;
    }
    protected int lives;
    protected int throwingStars;
    protected int lastPlayedCard;
    protected int level;
    protected ArrayList<PlayerData> playersData;

    public static GameData getGameData(Game game){
        ArrayList<PlayerData> playersData = new ArrayList<>();
        for(Player player :game.Players){
            PlayerData playerData = new PlayerData(player.id, player.name, player.hand.size());
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
