package logic;

import logic.PlayerType;

import java.util.ArrayList;

public class Player {
    public Player(int id ){
        this.id = id;
        hand = new ArrayList<>();
    }
    protected int id;
    protected String name;
    protected ArrayList<Integer> hand;
    protected int authToken;
    protected PlayerType playerType;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAuthToken() {
        return authToken;
    }

    public ArrayList<Integer> getHand() {
        return hand;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }

    public void setHand(ArrayList<Integer> hand) {
        this.hand = hand;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }



    //todo abstract functions act react request ,....
}