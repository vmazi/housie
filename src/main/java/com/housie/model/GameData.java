package com.housie.model;

import java.util.Random;

public class GameData {
    private final int max;
    private final int rowCount;
    private final int columnCount;
    private final int rowNumbers;
    private final int playerCount;

    private int announcedNumber = 0;
    private boolean noAnnouncedFlag = false;
    private boolean fullHouseSuccess;
    private Player fullHouseWinner;
    private boolean earlyFiveSuccess;
    private Player earlyFivePlayer;
    private boolean topLineSuccess;
    private Player topLinePlayer;

    private final boolean[] playerTurnFlag;

    private final Object gameLoopLock = new Object();
    private final Object inputLock = new Object();

    public GameData(int max, int rowCount, int columnCount, int rowNumbers, int playerCount){
        this.max = max;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.rowNumbers = rowNumbers;
        this.playerCount = playerCount;
        this.playerTurnFlag = new boolean[playerCount];
    }

    public int getMax() {
        return max;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowNumbers() {
        return rowNumbers;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean isFullHouseSuccess() {
        return fullHouseSuccess;
    }

    public Player getFullHouseWinner() {
        return fullHouseWinner;
    }

    public boolean isEarlyFiveSuccess() {
        return earlyFiveSuccess;
    }

    public Player getEarlyFivePlayer() {
        return earlyFivePlayer;
    }

    public boolean isTopLineSuccess() {
        return topLineSuccess;
    }

    public Player getTopLinePlayer() {
        return topLinePlayer;
    }

    public boolean[] getPlayerTurnFlag() {
        return playerTurnFlag;
    }

    public Object getGameLoopLock() {
        return gameLoopLock;
    }

    public Object getInputLock() {
        return inputLock;
    }

    public synchronized void setFullHouseWinner(Player fullHouseWinner){
        if(!this.fullHouseSuccess){
            this.fullHouseSuccess = true;
            this.fullHouseWinner = fullHouseWinner;
        }
    }
    public synchronized void setEarlyFivePlayer(Player earlyFivePlayer) {
        if(!this.earlyFiveSuccess) {
            this.earlyFiveSuccess = true;
            this.earlyFivePlayer = earlyFivePlayer;
        }
    }

    public synchronized void setTopLinePlayer(Player topLinePlayer) {
        if(!this.topLineSuccess) {
            this.topLineSuccess = true;
            this.topLinePlayer = topLinePlayer;
        }
    }

    public int getAnnouncedNumber() {
        return announcedNumber;
    }

    public void setAnnouncedNumber(int announcedNumber) {
        this.announcedNumber = announcedNumber;
    }

    public boolean isNoAnnouncedFlag() {
        return noAnnouncedFlag;
    }
    public void setNoAnnouncedFlag(boolean noAnnouncedFlag) {
        this.noAnnouncedFlag = noAnnouncedFlag;
    }

    public boolean isGameComplete(){
        return this.isTopLineSuccess() && this.isEarlyFiveSuccess() && this.isFullHouseSuccess();
    }

    /**
     * Method to generate random numbers, inclusive bounds.
     * @param min inclusive min bound
     * @param max inclusive max bound
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}