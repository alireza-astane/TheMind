package Logic;

import java.util.concurrent.TimeUnit;

public class Bot extends Player{
    protected static int count = 1;
    public Bot(){
        super();
        this.name = "Bot"+count;
        count++;
    }
    //todo thread

    //todo function to send P

    //TimeUnit.SECONDS.sleep(f(gameData));

    public int f(GameData gameData) throws InterruptedException {
        return this.hand.get(0)-gameData.lastPlayedCard;  //todo you can edit this
    }
}
