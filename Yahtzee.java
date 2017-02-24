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

        //Strategy: store scores in an array which is indexed by categories
        
        //int score = 0;
        int category = display.waitForPlayerToSelectCategory();

        if (!YahtzeeMagicStub.checkCategory(dice, category)) {
            display.printMessage("category mismatch!");
            // tricky code here
            // can't use 0 here because you can't distinguish betwee the cases
            // of a category never been picked and has been wrongly picked
            // have to consider this when adding up to get the total score
            categories[category-1] = -1;
        } else {
            while (true) {
                // category starts from 1 so it is off by one compare to array
                // there are two arrarys: categories and scores
                // possible values for categories: -1 and 0, indicating whether
                // a category has been picked before
                // possible values for scores: 0 and int > 0
                if (categories[category-1] == 0) {
                    categories[category-1] = -1;
                    scores[category-1] = calScore(category, dice);
                    break;
                } else {
                    display.printMessage("You have picked this category before, pick another one this time.");
                }
            }
        }

        display.updateScorecard(category, 1, scores[category-1]);

        // for testing for now
        for (int i = 0; i < N_CATEGORIES; i++) {
            println(categories[i]);
        }
        
    }



    /* return score given particular set of dice config and category */
    private int calScore(int category, int[] d) {
        int result = 0;
        if (category == 14) result = 50; // Yahtzee
        if (category == 13) result = 40; // Large straight
        if (category == 12) result = 30; // Small straight
        if (category == 11) result = 25; // Full house
        // for chance, 3kind, and 4kind, return sum of all dies
        if (category == 15 || category == 10 || category == 9) result = sumAll(d);
        // for ones->sixes, return value accordingly
        if (category == 1 || category == 2 || category == 3 || category == 4 ||
            category == 5 || category == 6) result = sumSingles(d, category);

        return result;
    }


    /* returns value for ones->sixes according to rules*/
    private int sumSingles(int[] a, int cate) {
        int sum = 0;
        for (int i = 0; i < N_DICE; i++) {
            if (a[i] == cate) sum += a[i];
        }
        return sum;
    }

    
    private int sumAll(int[] a) {
        int sum = 0;
        for (int i = 0; i < N_DICE; i++) sum += a[i];
        return sum;
    }


    /* initial roll of N_DICE */
    private void rollDice(int[] d) {
        // for (int i = 0; i < N_DICE; i++) {
        //     d[i] = rgen.nextInt(1, 6);
        // }
        //for testing
        d[0] = 1;
        d[1] = 1;
        d[2] = 2;
        d[3] = 2;
        d[4] = 1;
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
    private int[] categories = new int[N_CATEGORIES];
    private int[] scores = new int[N_CATEGORIES];

}
