/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 * Seems to be fun game, but more complicated than the previous assignments
 * need to commit
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
    public static void main(String[] args) {
        new Yahtzee().start(args);
    }
	

    public void run() {
        IODialog dialog = getDialog();
        nPlayers = dialog.readInt("Enter number of players");
        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
        playGame();
    }


    private void playGame() {
        /* You fill this in */
        display.waitForPlayerToClickRoll(1);
        rollDice(dice);
        display.displayDice(dice); // put a try-catch block here

        //player can reroll twice
        for (int i = 0; i < 2; i++) {
            display.waitForPlayerToSelectDice();
            reRollDice(dice);
            display.displayDice(dice);
        }
        
    }







    /* initial roll of N_DICE */
    private void rollDice(int[] d) {
        for (int i = 0; i < N_DICE; i++) {
            d[i] = rgen.nextInt(1, 6);
        }
    }


    /* reroll reach die depending on whether it is selected by player
     * should consider combine this with initial roll */
    private void reRollDice(int[] d) {
        for (int i = 0; i < N_DICE; i++) {
            if (display.isDieSelected(i)) d[i] = rgen.nextInt(1, 6);
        }
    }

    
		
    /* Private instance variables */
    private int nPlayers;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[] dice = new int[N_DICE];

}
