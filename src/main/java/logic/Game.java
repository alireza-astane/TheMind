package logic;

import java.util.*;

public class Game {
    private static int counter;
    protected String name;
    Integer lastPlayedCard;
    protected ArrayList<Integer> totalCards;
    protected ArrayList<Integer> playedCards;
    protected ArrayList<Integer> correctCards;
    protected GameState gameState;
    protected Integer capacity;
    protected ArrayList<Player> players;
    protected GameData gameData;
    protected int lives;
    protected int throwingStars=2;
    protected int level=1;
    protected HashMap<Integer, Boolean> playerVoteResult;
    protected Boolean voteResult;

    public Game(){
        this.totalCards = new ArrayList<>();
        for(int i=1;i<101;i++){this.totalCards.add(i);}
        Collections.shuffle(this.totalCards);
        counter = 0;
        this.players = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.correctCards = new ArrayList<>();
    }

    public void startVote(int id) {
        voteResult = null;
        playerVoteResult = new HashMap<>();
        for (Player player : players) {
            playerVoteResult.put(player.getId(), null);
        }
        playerVoteResult.put(id, true);
    }

    public void setResult(int id, Boolean result) {
        if (result)
            playerVoteResult.put(id, true);
        else {
            voteResult = false;
        }
        if (checkAllPlayerAgree())
            voteResult = true;
    }

    protected Boolean checkAllPlayerAgree() {
        for (Boolean result : playerVoteResult.values()) {
            if (!result)
                return false;
        }
        return true;
    }

    protected HashMap<Player,Integer> idByPlayer = new HashMap<>();

    protected HashMap<Integer,Player> playerById = new HashMap<>();

    public void update(){
        this.gameData=GameData.getGameData(this);
    }

    public boolean checkOrder(){
        boolean result = true;
        for(int i=0;i<this.playedCards.size();i++){
            if(!Objects.equals(playedCards.get(i), this.correctCards.get(i))){
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
                this.correctCards.add(card);
                arrayList.add(card);
            }
            Collections.sort(arrayList);
            System.out.println(this.getPlayers());
            this.getPlayers().get(i).hand.addAll(arrayList);
        }
        Collections.sort(this.correctCards);
    }

    public void reward() {
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
        return this.playedCards.size() == this.correctCards.size();
    }

    //public methods:
    public Player getPlayer(int id){
        return this.playerById.get(id);
    }

    public int getId(Player player){
        return this.idByPlayer.get(player);
    }

    public ArrayList<Object> put(int id){
        //this.setGameState(GameState.WAITING);

        Result result = null;
        HashMap<Integer,ArrayList<Integer>> hashMap = null;
        Player player = this.getPlayer(id);
        if(player.hand.size()>0){
            int card = player.hand.get(0);
            this.setLastPlayedCard(card);
            this.playedCards.add(card);
            ArrayList<Integer> list = player.getHand();
            System.out.println("card: " + card);
            list.remove((Object) card);
            player.setHand(list);
            if(this.checkOrder()){
                if(this.checkEndLevel()){
                    if(this.checkEndGame()){
                        result=Result.CORRECT_WIN;
                    }
                    else{
                        result=Result.CORRECT_NEXT_LEVEL;
                        this.lastPlayedCard = null;
                        this.level++;
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
                    for(Player p:this.players){
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
                            result=Result.WRONG_NEXT_LEVEL;
                            this.lastPlayedCard = null;
                            this.level++;
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
        //this.setGameState(GameState.ONGOING);
        return new ArrayList<Object>(Arrays.asList(result,hashMap));
    }


    public ArrayList<Object> doThrowingStar(){
        //this.setGameState(GameState.WAITING);
        Result result = null;
        ArrayList<Integer> list = new ArrayList<>();
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        for(Player player:this.players){
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
                this.lastPlayedCard = null;
                this.level++;
                this.giveHands();
            }
        }
        else{
            result=Result.THROWINGSTAR_CONTINUE;
        }
        this.update();
        //this.setGameState(GameState.ONGOING);
        return new ArrayList<Object>(Arrays.asList(result,hashMap));
    }
    public GameData getGameData(){return gameData;}

    public void start(){
        setGameState(GameState.ONGOING);
        this.giveHands();
        this.update();
    }

    public void addPlayer(Player player){
        this.playerById.put(player.id,player);
        this.idByPlayer.put(player,player.id);
        this.players.add(player);
    }

    public void replacePlayer(Player newPlayer,Player oldPlayer){
        this.addPlayer(newPlayer);
        newPlayer.hand=oldPlayer.hand;
        this.playerById.remove(oldPlayer.id);
        this.idByPlayer.remove(oldPlayer);
        this.players.remove(oldPlayer);
    }

    public int getId() {
        counter++;
        return counter;
    }
    //setters and getters


    public int getThrowingStars() {
        return throwingStars;
    }

    public int getLives() {
        return lives;
    }

    public Integer getLastPlayedCard() {
        return lastPlayedCard;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Integer> getCorrectCards() {
        return correctCards;
    }

    public ArrayList<Integer> getTotalCards() {
        return totalCards;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Integer> getPlayedCards() {
        return playedCards;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
        this.lives=capacity;
    }

    public void setCorrectCards(ArrayList<Integer> correctCards) {
        this.correctCards = correctCards;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setLastPlayedCard(Integer lastPlayedCard) {
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
        this.players = players;
    }

    public void setThrowingStars(int throwingStars) {
        this.throwingStars = throwingStars;
    }

    public void setTotalCards(ArrayList<Integer> totalCards) {
        this.totalCards = totalCards;
    }

    public HashMap<Player, Integer> getIdByPlayer() {
        return idByPlayer;
    }

    public void setIdByPlayer(HashMap<Player, Integer> idByPlayer) {
        this.idByPlayer = idByPlayer;
    }

    public HashMap<Integer, Player> getPlayerById() {
        return playerById;
    }

    public void setPlayerById(HashMap<Integer, Player> playerById) {
        this.playerById = playerById;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Boolean> getPlayerVoteResult() {
        return playerVoteResult;
    }

    public void setPlayerVoteResult(HashMap<Integer, Boolean> playerVoteResult) {
        this.playerVoteResult = playerVoteResult;
    }

    public Boolean getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(Boolean voteResult) {
        this.voteResult = voteResult;
    }
}