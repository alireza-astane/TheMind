package server;

import logic.*;

import java.util.concurrent.TimeUnit;

public class BotHandler implements Runnable{
    private int id;
    private Game game;
    private Bot bot;

    public BotHandler(Bot bot){  //todo game!  //todo pause resume and reset(next level)
        this.bot=bot;
        this.game=bot.getGame();
    }


    public int f(GameData gameData) throws InterruptedException {  //todo no hand or no lpc
        return this.bot.getHand().get(0)- gameData.getLastPlayedCard();  //todo you can edit this
    }



    @Override
    public void run() {
        while (true){
            if(game.getGameState()== GameState.ONGOING){
                try {
                    TimeUnit.SECONDS.sleep(f(game.getGameData()));
                } catch (InterruptedException ignored) {
                }
                if(game.getGameState()== GameState.ONGOING){
                    game.put(this.id);}
            }
        }

    }
}
