package com.housie;

import com.housie.model.GameData;
import com.housie.model.Player;
import com.housie.model.ticket.Ticket;
import com.housie.model.ticket.TicketSpot;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MainApplicationTest {

    @Test
    public void printTicket(){
        Ticket testTicket = new Ticket(new GameData(20,3, 10, 5, 5));
        testTicket.printTicket();
    }

    @Test
    public void makeTicketsWithSpaces(){
        Ticket testTicket = new Ticket(new GameData(20,3, 10, 5, 5));
        assertEquals(testTicket.getTicketNumbers().size(),(3 * 5));
    }

    @Test
    public void calculateEarlyFiveWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        List<Integer> fiveNumbers = new ArrayList<>();
        Set<Integer> numberSet = testPlayer.getTicket().getTicketNumbers().keySet();
        int count = 0;
        for(Integer number : numberSet){
            if(count<5){
                fiveNumbers.add(number);
                count++;
            }
        }
        for(Integer announce : fiveNumbers){
            testPlayer.markNumber(announce);
        }
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();
        assertTrue(gameData.isEarlyFiveSuccess());
        assertEquals(testPlayer,gameData.getEarlyFivePlayer());
    }

    @Test
    public void calculateFullHouseWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        Set<Integer> numberSet = testPlayer.getTicket().getTicketNumbers().keySet();

        for(Integer announce : numberSet){
            testPlayer.markNumber(announce);
        }
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();

        assertTrue(gameData.isFullHouseSuccess());
        assertEquals(testPlayer,gameData.getFullHouseWinner());
    }

    @Test
    public void calculateTopLineWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        TicketSpot[] firstTicketRow = testPlayer.getTicket().getTicketRows().get(0);
        int filledTicketSpots = 0;
        for(TicketSpot announced : firstTicketRow){
            if(announced != null){
                testPlayer.markNumber(announced.getSpotValue());
                filledTicketSpots++;

            }
        }
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();

        assertEquals(filledTicketSpots,5);
        assertTrue(gameData.isTopLineSuccess());
        assertEquals(testPlayer,gameData.getTopLinePlayer());
    }

    @Test
    public void calculateNoEarlyFiveWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        TicketSpot[] firstTicketRow = testPlayer.getTicket().getTicketRows().get(0);
        int filledTicketSpots = 0;
        int lastInRow = -1;
        for(TicketSpot announced : firstTicketRow){
            if(announced != null){
                filledTicketSpots++;
                lastInRow = announced.getSpotValue();
            }
        }
        testPlayer.markNumber(lastInRow);
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();

        assertEquals(filledTicketSpots,5);
        assertFalse(gameData.isEarlyFiveSuccess());
        assertNull(gameData.getTopLinePlayer());
    }

    @Test
    public void calculateNoFullHouseWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        TicketSpot[] firstTicketRow = testPlayer.getTicket().getTicketRows().get(0);
        int filledTicketSpots = 0;
        int lastInRow = -1;
        for(TicketSpot announced : firstTicketRow){
            if(announced != null){
                filledTicketSpots++;
                lastInRow = announced.getSpotValue();
            }
        }
        testPlayer.markNumber(lastInRow);
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();

        assertEquals(filledTicketSpots,5);
        assertFalse(gameData.isFullHouseSuccess());
        assertNull(gameData.getFullHouseWinner());
    }

    @Test
    public void calculateNoTopLineWinner(){
        GameData gameData = new GameData(20,3, 10, 5, 5);

        Player testPlayer = new Player(gameData,0);
        Player testPlayer2 = new Player(gameData,1);

        TicketSpot[] firstTicketRow = testPlayer.getTicket().getTicketRows().get(0);
        int filledTicketSpots = 0;
        int lastInRow = -1;
        for(TicketSpot announced : firstTicketRow){
            if(announced != null){
                filledTicketSpots++;
                lastInRow = announced.getSpotValue();
            }
        }
        testPlayer.markNumber(lastInRow);
        testPlayer.calculateWinners();
        testPlayer2.calculateWinners();

        assertEquals(filledTicketSpots,5);
        assertFalse(gameData.isTopLineSuccess());
        assertNull(gameData.getTopLinePlayer());
    }
}