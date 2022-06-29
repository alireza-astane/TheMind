package logic;

import server.ClientHandler;

import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private static int id;
    private String name;
    private int host;
    private Integer capacity;
    private int level;
    private int heartCount;
    private int ninjaCount;
    private int last;
    private List<Integer> freeCards;
    private List<Player> players;
    private List<ClientHandler> clientHandlers;
    private GameStatus status;

    public Game() {
        freeCards = new ArrayList<>();
        players = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        status = GameStatus.PENDING;
        id = 0;
    }

    public void init() {
        level = 1;
        heartCount = this.players.size();
        ninjaCount = 2;

    }

    public void start() {
        init();
        new ShuffleCards(this).init();
    }

    public int getId() {
        id++;
        return id;
    }



    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id)
                return player;
        }
        return null;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public int getNinjaCount() {
        return ninjaCount;
    }

    public void setNinjaCount(int ninjaCount) {
        this.ninjaCount = ninjaCount;
    }

    public List<Integer> getFreeCards() {
        return freeCards;
    }

    public void setFreeCards(List<Integer> freeCards) {
        this.freeCards = freeCards;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }



    public enum GameStatus {
        START,
        PENDING
    }
}
