package com.housie.model;


import com.google.common.annotations.VisibleForTesting;

import java.util.HashSet;
import java.util.Set;

public class Dealer implements Runnable {

    private final GameData gameData; //shared data
    private int numberAnnounced = 0;
    private boolean topLineNotified = false;
    private boolean earlyFiveNotified = false;
    private boolean fullHouseNotified = false;
    private boolean allNotified = false;
    private final Set<Integer> numbersAnnouncedSet;


    public Dealer(GameData gameData) {
        this.gameData = gameData;
        this.numbersAnnouncedSet = new HashSet<>();
    }


    public void run() {
        //dealer should synchronize on the game loop
        synchronized (gameData.getGameLoopLock()) {
            //dealer executes until game over
            while (!gameData.isGameComplete()) {
                // set number announced flag to false before announcing the number
                gameData.setNoAnnouncedFlag(false);
                // set turn for all players
                resetAllPlayers();
                // wait while input has been entered by the user
                synchronized (gameData.getInputLock()) {
                    while (0 == numberAnnounced) {
                        try {
                            gameData.getInputLock().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // after input lock is released, set the new number for the whole game
                gameData.setAnnouncedNumber(numberAnnounced);
                // reset the internal announced number
                numberAnnounced = 0;
                gameData.setNoAnnouncedFlag(true);
                // notify all the players waiting for the number using the game lock
                gameData.getGameLoopLock().notifyAll();
                // wait using the game lock until all players are done checking against new number
                while (!allPlayersDone()) {
                    try {
                        //have the dealer wait until the players finish checking their ticket
                        gameData.getGameLoopLock().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                notifyWinners(false); //notify of any applicable winners
            }
            notifyWinners(false);
            gameData.getGameLoopLock().notifyAll(); // If at all any player is waiting
        }
    }

    public void notifyWinners(boolean test){
        if (gameData.isTopLineSuccess() && !topLineNotified) {
            System.out.println("We have a winner: Player#" + gameData.getTopLinePlayer().getId() +
                    " has won 'Top Line' winning combination.");
            topLineNotified = true;
        }

        if (gameData.isEarlyFiveSuccess() && !earlyFiveNotified) {
            System.out.println("We have a winner: Player#" + gameData.getEarlyFivePlayer().getId() +
                    " has won 'First Five' winning combination.");
            earlyFiveNotified = true;
        }

        if (gameData.isFullHouseSuccess() && !fullHouseNotified) {
            System.out.println("We have a winner: Player# " + gameData.getFullHouseWinner().getId() +
                    " has won 'Full House' winning combination.");
            fullHouseNotified = true;
        }
        if(topLineNotified && earlyFiveNotified && fullHouseNotified ){
            allNotified = true;
            System.out.println("***** Game Over *****");
            System.out.println("======================");
            System.out.println("       Summary        ");
            System.out.println("Early Five Winner: Player#" + gameData.getEarlyFivePlayer().getId());
            System.out.println("Top Line Winner: Player#" + gameData.getTopLinePlayer().getId());
            System.out.println("Full House Winner: Player#" + gameData.getFullHouseWinner().getId());

            System.out.println("======================");
            if(!test){
                System.exit(0);
            }
        }
    }
    @VisibleForTesting
    boolean allPlayersDone() {
        for (int i = 0; i < gameData.getPlayerCount(); i++) {
            if (gameData.getPlayerTurnFlag()[i]) {
                return false;
            }
        }
        return true;
    }

    private void resetAllPlayers() {
        for (int i = 0; i < gameData.getPlayerCount(); i++) {
            gameData.getPlayerTurnFlag()[i] = true;
        }
    }

    public void setAnnouncedNumber() {
        int newNumber = GameData.randInt(1,gameData.getMax());
        while(numbersAnnouncedSet.contains(newNumber)){
            newNumber = GameData.randInt(1,gameData.getMax());
        }
        numberAnnounced = newNumber;
        System.out.println("Next Number is: " + newNumber);
        numbersAnnouncedSet.add(newNumber);

    }
}