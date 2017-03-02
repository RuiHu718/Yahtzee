/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 * Seems to be fun game, but more complicated than the previous assignments
 * 
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

        for (int k = 0; k < 2; k++) { // try two rounds first
        
            for (int j = 0; j < 2; j++) { // try two players first

                display.printMessage(playerNames[j] + ", it is your turn");
                display.waitForPlayerToClickRoll(j+1); // player index starts from 1
                rollDice(dice);
                display.displayDice(dice); // put a try-catch block here

                //player can reroll twice
                for (int i = 0; i < 2; i++) {
                    display.printMessage("select dice and roll again");
                    display.waitForPlayerToSelectDice();
                    reRollDice(dice);
                    display.displayDice(dice);
                }

                //Strategy: store scores in an array which is indexed by categories
                display.printMessage("pick a category");
                int category = display.waitForPlayerToSelectCategory();

                //if (!myCheckCategory(dice, category)) {
                if (!YahtzeeMagicStub.checkCategory(dice, category)) {
                    display.printMessage("category mismatch!");
                    pause(20);
                    categories[j][category-1] = -1;
                } else {
                    while (true) {
                        // category starts from 1 so it is off by one compare to array
                        // there are two arrarys: categories and scores
                        // possible values for categories: -1 and 0, indicating whether
                        // a category has been picked before
                        // possible values for scores: 0 and int > 0
                        if (categories[j][category-1] == 0) {
                            categories[j][category-1] = -1;
                            scores[j][category-1] = calScore(category, dice);
                            break;
                        } else {
                            display.printMessage("You have picked this category before, pick another one this time.");
                            category = display.waitForPlayerToSelectCategory();
                        }
                    }
                }

                display.updateScorecard(category, j+1, scores[j][category-1]);
                display.updateScorecard(TOTAL, j+1, calCurrentTotal(scores[j]));

                // for testing for now
                // for (int i = 0; i < N_CATEGORIES; i++) {
                //     println(categories[j][i]);
                // }
            }
        }

        presentFinalResult();
    }


    /* my own implementation of checkCategory */
    private boolean myCheckCategory(int[] dice, int category) {
        return checkThreeOfKind(dice, category);
    }


    private boolean checkThreeOfKind(int[] dice, int category) {
        // this arrary keep track of how many times every side turn out in dice
        // note this array has 7 elements:0-6, I made it this way so don't have to
        // deal with the off by one issue caused by die starts from 1 but array 0
        int[] numOfDiffSides = new int[7];

        //if you see a six, then increment numOfDiffSides[6] by 1
        for (int i = 0 ; i < N_DICE; i++) {
            numOfDiffSides[dice[i]] += 1;
        }

        for (int j = 1; j < 7; j++) { // starts from 1 because 0 position will always be 0
            if (numOfDiffSides[j] >= 3) return true;
        }

        return false;
    }

    private void presentFinalResult(){

        // for (int i = 0; i < nPlayers; i++) {
        //     for (int j = 0; j < N_CATEGORIES; j++){
        //         println(scores[i][j]);
        //     }
        // }
        
        for (int i = 0; i < nPlayers; i++) {
            for (int j = 0; j < 6; j++) {
                scores[i][6] += scores[i][j]; // calculating upper score, note the off by 1 issue
            }

            //println(scores[i][6]);

            if (scores[i][6] >= 63) scores[i][7] = 35; // bonus if upper score >= 63

            //println(scores[i][7]);

            for (int k = 8; k < 15; k++){
                scores[i][15] += scores[i][k]; // calculating lower score
            }

            //println(scores[i][15]);            

            scores[i][16] = scores[i][6] + scores[i][7] + scores[i][15]; // total=upper+lower+bonus

            //println(scores[i][16]);

            display.updateScorecard(7, i+1, scores[i][6]);
            display.updateScorecard(8, i+1, scores[i][7]);
            display.updateScorecard(16, i+1, scores[i][15]);
            display.updateScorecard(17, i+1, scores[i][16]);            
        }
    }

    
    private int calCurrentTotal(int[] s) {
        int sum = 0;
        for (int i = 0; i < N_CATEGORIES-1; i++) { // should not count the last one, total
            sum += s[i];
        }
        return sum;
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
        d[3] = 1;
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
    private int[][] categories = new int[MAX_PLAYERS][N_CATEGORIES];
    private int[][] scores = new int[MAX_PLAYERS][N_CATEGORIES];

}
