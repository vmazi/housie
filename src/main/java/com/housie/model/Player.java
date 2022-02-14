package com.housie.model;

import com.google.common.annotations.VisibleForTesting;
import com.housie.model.ticket.Ticket;
import com.housie.model.ticket.TicketSpot;

import java.util.Objects;

public class Player implements Runnable {

    private final int id;
    private final GameData gameData;
    private final Ticket ticket;
    private int totalNumbersFound;

    public Player(GameData gameData, int id) {

        this.id = id;
        this.gameData = gameData;
        this.totalNumbersFound = 0;
        ticket = new Ticket(gameData);
        System.out.println("Player: " + id + "has the following Ticket:");
        ticket.printTicket();
    }

    public int getId() {
        return id;
    }
    @VisibleForTesting
    public Ticket getTicket() {
        return ticket;
    }

    public void run() {
        // synchronize all players on the game loop
        synchronized(gameData.getGameLoopLock()) {
            //players execute while the game is not complete
            while(!gameData.isGameComplete()) {
                // wait until a number is announced by the dealer or it becomes the player's turn
                while(!gameData.isNoAnnouncedFlag() || !gameData.getPlayerTurnFlag()[id]) {
                    try {
                        gameData.getGameLoopLock().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //all winning combos may have been triggered by other players
                if(!gameData.isGameComplete()) {
                    markNumber(gameData.getAnnouncedNumber());
                    calculateWinners();
                    // mark player turn as done
                    gameData.getPlayerTurnFlag()[id] = false;
                }
                //notify all others waiting on the game lock
                gameData.getGameLoopLock().notifyAll();

            }
        }
    }
    @VisibleForTesting
    public void markNumber(Integer numberAnnounced) {
        if(ticket.getTicketNumbers().containsKey(numberAnnounced)){
            this.totalNumbersFound++;
            ticket.getTicketNumbers().get(numberAnnounced).setMarked(true);
        }
    }

    @VisibleForTesting
    public void calculateWinners(){
        if(!gameData.isEarlyFiveSuccess()){
            if(this.totalNumbersFound >= 5){
                gameData.setEarlyFivePlayer(this);
            }
        }
        if(!gameData.isTopLineSuccess()){
            boolean topLineComplete = true;
            TicketSpot[] firstRow = ticket.getTicketRows().get(0);
            for(TicketSpot ticket : firstRow){
                if(ticket!=null){
                    if(!ticket.isMarked()){
                        topLineComplete = false;
                        break;
                    }
                }
            }
            if(topLineComplete){
                gameData.setTopLinePlayer(this);
            }
        }
        if(this.totalNumbersFound == (gameData.getRowNumbers() * gameData.getRowCount())) {
            if(!gameData.isFullHouseSuccess()){
                gameData.setFullHouseWinner(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                totalNumbersFound == player.totalNumbersFound &&
                Objects.equals(gameData, player.gameData) &&
                Objects.equals(ticket, player.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameData, ticket, totalNumbersFound);
    }
}