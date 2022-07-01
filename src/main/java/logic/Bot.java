package logic;
public class Bot extends Player{
    protected static int count = 1;
    private final Game game;

    public Bot(Game game){
        super();
        this.name = "Bot"+count;
        this.playerType=PlayerType.BOT;
        this.game=game;
        count++;
    }
    public Game getGame() {
        return game;
    }
}



