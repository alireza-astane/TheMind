package Logic;

import java.util.*;

public class Game {
    public Game(){
        this.totalCards = new ArrayList<>();
        for(int i=1;i<101;i++){this.totalCards.add(i);}
        Collections.shuffle(this.totalCards);
    }
    protected int counter=1;
    int lastPlayedCard;
    protected ArrayList<Integer> totalCards;
    protected ArrayList<Integer> playedCards;
    protected ArrayList<Integer> correctCords;
    protected GameState gameState;
    protected int capacity;
    protected ArrayList<Player>  Players;
    protected GameData gameData;
    protected int lives;
    protected int throwingStars=2;
    protected int level=1;

    protected ArrayList<Player> humans = new ArrayList<>();

    protected ArrayList<Player> bots = new ArrayList<>();

    protected HashMap<Player,Integer> idByPlayer = new HashMap<>();

    protected HashMap<Integer,Player> playerById = new HashMap<>();

    public void update(){
        this.gameData=GameData.getGameData(this);
    }

    public boolean checkOrder(){
        boolean result = true;
        for(int i=0;i<this.playedCards.size();i++){
            if(!Objects.equals(playedCards.get(i), this.correctCords.get(i))){
                result = false;
                break;
            }
        }
        return result;
    }

    public void giveHands(){
        for(int i=0;i<this.capacity;i++){
            ArrayList<Integer> arrayList = new ArrayList<>();
            for(int j=0;j<this.level;j++){
                int card = this.totalCards.get(0);
                this.totalCards.remove(0);
                this.correctCords.add(card);
                arrayList.add(card);
            }
            Collections.sort(arrayList);
            this.getPlayers().get(i).hand.addAll(arrayList);

        }
        Collections.sort(this.correctCords);
    }

    public void reward(){
        switch (this.level){
            case 2 : {this.throwingStars++;}
            case 3 : {this.lives++;}
            case 5 : {this.throwingStars++;}
            case 6 : {this.lives++;}
            case 8 : {this.throwingStars++;}
            case 9 : {this.lives++;}
            default: {}
        }
    }

    public boolean checkEndGame(){
        return this.level>12;
    }

    public boolean checkEndLevel(){
        return this.playedCards.size()==this.correctCords.size();
    }

    //public methods:
    public Player getPlayer(int id){
        return this.playerById.get(id);
    }

    public int getId(Player player){
        return this.idByPlayer.get(player);
    }

    public ArrayList<Object> put(int id){
        this.setGameState(GameState.WAITING);

        Result result = null;
        HashMap<Integer,ArrayList<Integer>> hashMap = null;
        Player player = this.getPlayer(id);
        if(player.hand.size()>0){
            int card = player.hand.get(0);
            this.setLastPlayedCard(card);
            this.playedCards.add(card);
            ArrayList<Integer> list = player.getHand();
            list.remove(card);
            player.setHand(list);
            if(this.checkOrder()){
                if(this.checkEndLevel()){
                    if(this.checkEndGame()){
                        result=Result.CORRECT_WIN;
                    }
                    else{
                        result=Result.CORRECT_NEXTLEVEL;
                        this.level++;
                        reward();
                        this.giveHands();
                    }
                }
                else{
                    result=Result.CORRECT_CONTINUE;
                }
            }

            else{
                if(this.lives>0){
                    this.lives--;
                    hashMap = new HashMap<>();
                    for(Player p:this.Players){
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        for(Integer c:p.hand){
                            if(c<card){
                                arrayList.add(c);
                            }
                        }
                        ArrayList<Integer> arrayList1 = p.hand;
                        arrayList1.removeAll(arrayList);
                        p.setHand(arrayList1);

                        hashMap.put(p.id,arrayList);
                        this.playedCards.addAll(arrayList);
                    }
                    Collections.sort(this.playedCards);
                    this.setLastPlayedCard(this.playedCards.get(this.playedCards.size()-1));



                    if(this.checkEndLevel()){
                        if(this.checkEndGame()){
                            result=Result.WRONG_WIN;
                        }
                        else{
                            result=Result.WRONG_NEXTLEVEL;
                            this.level++;
                            reward();
                            this.giveHands();
                        }
                    }
                    else{
                        result=Result.WRONG_CONTINUE;
                    }

                }
                else{
                    result=Result.WRONG_LOSE;
                }

            }
            this.update();
        }
        else{
            result=Result.NOCARD;
        }
        this.setGameState(GameState.ONGOING);
        return new ArrayList<Object>(Arrays.asList(result,hashMap));
    }


    public ArrayList<Object> doThrowingStar(){
        this.setGameState(GameState.WAITING);
        Result result = null;
        ArrayList<Integer> list = new ArrayList<>();
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        for(Player player:this.Players){
            try{
            int card = player.getHand().get(0);
            hashMap.put(player.id,new ArrayList<>(List.of(card)));
            list.add(card);
            ArrayList<Integer> list2 = player.getHand();
            list2.remove(card);
            player.setHand(list2);
            } catch (Exception ignored) {}
        }
        Collections.sort(list);
        this.playedCards.addAll(list);
        this.setLastPlayedCard(list.get(list.size()-1));

        if(this.checkEndLevel()){
            if(this.checkEndGame()){
                result=Result.THROWINGSTAR_WIN;
            }
            else{
                result=Result.THROWINGSTAR_NEXTLEVEL;
                this.level++;
                reward();
                this.giveHands();
            }
        }
        else{
            result=Result.THROWINGSTAR_CONTINUE;
        }
        this.update();
        this.setGameState(GameState.ONGOING);
        return new ArrayList<Object>(Arrays.asList(result,hashMap));
    }
    public GameData getGameData(){return gameData;}

    public void start(){
        this.giveHands();
        this.update();
    }

    public void addPlayer(Player player){
        player.setId(counter);
        counter++;
        this.playerById.put(player.id,player);
        this.idByPlayer.put(player,player.id);
        this.Players.add(player);
        if(player.playerType==PlayerType.BOT){
            this.bots.add(player);
        }
        else{this.humans.add(player);}
    }

    public void replacePlayer(Player newPlayer,Player oldPlayer){
        newPlayer.hand=oldPlayer.hand;
        this.playerById.remove(oldPlayer.id);
        this.idByPlayer.remove(oldPlayer);
        this.Players.remove(oldPlayer);
        this.addPlayer(newPlayer);
        if(oldPlayer.playerType==PlayerType.HUMAN){
            this.humans.remove(oldPlayer);
        }
        else{this.bots.remove(oldPlayer);}

    }
    //setters and getters


    public int getThrowingStars() {
        return throwingStars;
    }

    public int getLives() {
        return lives;
    }

    public int getLastPlayedCard() {
        return lastPlayedCard;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Integer> getCorrectCords() {
        return correctCords;
    }

    public ArrayList<Integer> getTotalCards() {
        return totalCards;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public ArrayList<Integer> getPlayedCards() {
        return playedCards;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Player> getBots() {
        return bots;
    }

    public ArrayList<Player> getHumans() {
        return humans;
    }

    public HashMap<Integer, Player> getPlayerById() {
        return playerById;
    }

    public HashMap<Player, Integer> getIdByPlayer() {
        return idByPlayer;
    }

    public int getCounter() {
        return counter;
    }

    public void setBots(ArrayList<Player> bots) {
        this.bots = bots;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setHumans(ArrayList<Player> humans) {
        this.humans = humans;
    }

    public void setIdByPlayer(HashMap<Player, Integer> idByPlayer) {
        this.idByPlayer = idByPlayer;
    }

    public void setPlayerById(HashMap<Integer, Player> playerById) {
        this.playerById = playerById;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.lives=capacity;
    }

    public void setCorrectCords(ArrayList<Integer> correctCords) {
        this.correctCords = correctCords;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setLastPlayedCard(int lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPlayedCards(ArrayList<Integer> playedCards) {
        this.playedCards = playedCards;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players = players;
    }

    public void setThrowingStars(int throwingStars) {
        this.throwingStars = throwingStars;
    }

    public void setTotalCards(ArrayList<Integer> totalCards) {
        this.totalCards = totalCards;
    }

}
