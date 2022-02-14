package com.housie.model.ticket;

import com.google.common.annotations.VisibleForTesting;
import com.housie.model.GameData;

import java.util.*;

public class Ticket {

    private final List<TicketSpot[]> ticketRows;
    private final Map<Integer, TicketSpot> ticketNumbers;

    @VisibleForTesting
    public Ticket(GameData gameData) {
        this.ticketNumbers = new HashMap<>();
        this.ticketRows = generateTicket(gameData.getMax(),
                gameData.getRowCount(), gameData.getColumnCount(), gameData.getRowNumbers());

    }

    public List<TicketSpot[]> getTicketRows() {
        return ticketRows;
    }

    public Map<Integer, TicketSpot> getTicketNumbers() {
        return ticketNumbers;
    }

    private List<TicketSpot[]> generateTicket(int max, int rowCount, int colCount, int rowNums) {
        List<TicketSpot[]> generatedTicket = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            generatedTicket.add(generateTicketRow(max, colCount, rowNums));
        }
        return generatedTicket;
    }

    private TicketSpot[] generateTicketRow(int max, int colCount, int rowNums) {

        TicketSpot[] ticketRow = new TicketSpot[colCount];
        Stack<Integer> generatedNums = generateNumsForRow(max, rowNums);

        int emptySpots = colCount - rowNums;
        Set<Integer> generatedEmpties = generateEmptySpotsForRow(colCount, emptySpots);
        int spotIndex = 0;

        while (!generatedNums.isEmpty()) {
            if (!generatedEmpties.contains(spotIndex)) {
                ticketRow[spotIndex] = ticketNumbers.get(generatedNums.pop());
            }
            spotIndex++;
        }
        return ticketRow;
    }

    private Stack<Integer> generateNumsForRow(int max, int count) {
        Stack<Integer> generated = new Stack<>();
        for (int i = 0; i < count; ) {
            Integer randomInt = GameData.randInt(1, max);
            if (!ticketNumbers.containsKey(randomInt)) {
                generated.push(randomInt);
                ticketNumbers.put(randomInt, new TicketSpot(randomInt));
                i++;
            }
        }
        return generated;
    }

    private Set<Integer> generateEmptySpotsForRow(int colCount, int emptyCount) {
        Set<Integer> generated = new HashSet<>();
        for (int i = 0; i < emptyCount; ) {
            Integer randomInt = GameData.randInt(0, colCount - 1);
            if (!generated.contains(randomInt)) {
                generated.add(randomInt);
                i++;
            }
        }
        return generated;
    }

    public void printTicket() {
        for (TicketSpot[] ticketRow : ticketRows) {
            for (TicketSpot ticketSpot : ticketRow) {
                if (ticketSpot != null) {
                    if (ticketSpot.getSpotValue() < 10) {
                        System.out.print("| " + ticketSpot.getSpotValue() + "|");
                    } else {
                        System.out.print("|" + ticketSpot.getSpotValue() + "|");
                    }
                } else {
                    System.out.print("|  |");
                }
            }
            System.out.println();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketRows, ticket.ticketRows) &&
                Objects.equals(ticketNumbers, ticket.ticketNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketRows, ticketNumbers);
    }
}
