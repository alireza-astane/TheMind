package Logic;

import java.util.concurrent.TimeUnit;

public class Bot extends Player{
    protected static int count = 1;
    public Bot(){
        super();
        this.id = ;
        this.name = "Bot"+count;
        count++;
    }
    //todo thread

    //todo function to send P

    //TimeUnit.SECONDS.sleep(f(gameData));

    public int f(GameData gameData) throws InterruptedException {  //no hand or no lpc
        return this.hand.get(0)-gameData.lastPlayedCard;  //todo you can edit this
    }


    public void reset(){
    }  //this function interrupt bot function




    public void createRequiredBots(Game game) {     //place this function at server
        int n = game.getCapacity() - game.getPlayers().size();
        for(int i=0;i<n;i++){
            Player bot = new Bot();
            game.addPlayer(bot);
        }
    }


}



