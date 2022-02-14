package com.housie;

import com.housie.model.Dealer;
import com.housie.model.GameData;
import com.housie.model.Player;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner myInput = new Scanner( System.in );
        int max = collectMaxInput(myInput);
        int players = collectPlayersInput(myInput);
        String[] sizes = collectTicketSizeInput(myInput);
        int rowNums = collectRowNumbersInput(myInput);

        final GameData game = new GameData(
                max,
                Integer.parseInt(sizes[0].trim()),
                Integer.parseInt(sizes[1].trim()),
                rowNums,
                players);
        final Dealer dealer = new Dealer(game);
        initializeGame(players, game, dealer);

        String input = "";
        myInput.nextLine(); //clean up input buffer
        while(!input.toUpperCase().contains("Q")){
            if(game.isGameComplete()){
                break;
            }
            System.out.println(">>Press 'N' to generate next number");
            input = myInput.nextLine();
            if(input.toUpperCase().contains("N")){
                dealer.setAnnouncedNumber();
                synchronized (game.getInputLock()){
                    game.getInputLock().notify();
                }
            }
        }

    }

    private void initializeGame(int players, GameData game, Dealer dealer) {
        List<Player> playerList = new ArrayList<>();
        for(int i = 0; i < players;i++){
            playerList.add(new Player(game,dealer,i));
        }
        Thread dealerThread  = new Thread(dealer);
        List<Thread> playerThreads = playerList.stream().map(Thread::new).collect(Collectors.toList());

        dealerThread.start();
        playerThreads.forEach(Thread::start);
    }

    private int collectMaxInput(Scanner myInput){
        System.out.println("**** Lets Play Housie *****");
        System.out.println();
        System.out.println("Note: - Press 'Q' to quit any time.");
        System.out.println();
        System.out.println(">>Enter the number range(1-n): ");
        return myInput.nextInt();
    }
    private int collectPlayersInput(Scanner myInput){
        System.out.println(">>Enter Number of players playing the game.: ");
        return myInput.nextInt();
    }
    private String[] collectTicketSizeInput(Scanner myInput){
        myInput.nextLine();
        System.out.println(">>Enter Ticket Size N X M : ");
        String sizeString = myInput.nextLine();
        String[] sizes = sizeString.split("X");
        return sizes;
    }
    private int collectRowNumbersInput(Scanner myInput){
        System.out.println(">>Enter Numbers per Row: ");
        return myInput.nextInt();
    }
}
