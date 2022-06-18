package Logic;

import java.util.ArrayList;

public class Game {
    protected String name;
    protected int lastPlayedCard;
    protected ArrayList<Integer> playedCards;
    protected ArrayList<Player> players;
    protected ArrayList<Integer> unusedCards;
    protected GameData gameData;
    protected int lives;
    protected int throwingStars;
    protected int level;
    protected int capacity;

    public Game() {
        playedCards = new ArrayList<>();
        players = new ArrayList<>();
        unusedCards = new ArrayList<>();

    }

    public void initializeUnusedCards() {
        for (int i = 1; i < 101; i++) {
            unusedCards.add(i);
        }
    }

    public GameState state = GameState.WAITING_FOR_GAME_NAME;

    public void endGame(){}
    public void update(){}

    public int getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void setLastPlayedCard(int lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }

    public ArrayList<Integer> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(ArrayList<Integer> playedCards) {
        this.playedCards = playedCards;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players = players;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getThrowingStars() {
        return throwingStars;
    }

    public void setThrowingStars(int throwingStars) {
        this.throwingStars = throwingStars;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Integer> getUnusedCards() {
        return unusedCards;
    }
}
