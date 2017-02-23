/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 * Seems to be fun game, but more complicated than the previous assignments
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
        display.displayDice(dice);

        
    }







    /* rolls N_DICE for once */
    private void rollDice(int[] d) {
        for (int i = 0; i < N_DICE; i++) {
            d[i] = rgen.nextInt(1, 6);
        }
    }

    
		
/* Private instance variables */
    private int nPlayers;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[] dice = new int[N_DICE];

}
