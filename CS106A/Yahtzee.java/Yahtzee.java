/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

// Name: Constantine Antonakos
// Section Leader: Shayon Saleh

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int[] yahtzeeDice;
	private int[][] scoreKeeper;
	private int[] diceFrequencyArray = new int[6];

	
	public void run() {
		/* You shouldn't need to change this */
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players:");
		while (nPlayers > MAX_PLAYERS){
			nPlayers = dialog.readInt("Enter number of players:");
		}
		playerNames = new String[nPlayers];
		
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player: " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	
	//Play the game!
	private void playGame() {
		scoreKeeper = initializeScoreList();
		
		for (int currRound = 0; currRound < N_SCORING_CATEGORIES; currRound++){
			for (int playerIndex = 1;  playerIndex <= nPlayers; playerIndex++){
				
				display.printMessage(playerNames[playerIndex - 1] + "'s turn!  Click the \"Roll Dice\" button to roll the dice.");
				display.waitForPlayerToClickRoll(playerIndex - (playerIndex - 1));
				yahtzeeDice = randomizedDice();
				display.displayDice(yahtzeeDice);

				rerollsPerPlayerTurn();
				calculateDiceFrequency();

				display.printMessage("Please select a category for this roll!");
				while (true){	
					int category = display.waitForPlayerToSelectCategory();
					if (category == YAHTZEE && scoreKeeper[playerIndex-1][YAHTZEE-1] > 0){
						if (validateDiceCategories(YAHTZEE))
							bonusYahtzee(playerIndex);
							break;
					} else if (scoreKeeper[playerIndex-1][category-1] == -1){
							calculateRoundScore(playerIndex, category);
							break;
					}
					display.printMessage("You have already selected this category!  Please select another!");
				}
				zeroOutDiceFrequency();
			}
		}
		calculateResults();
		calculateWinner();
	}

	//Zeroes out the frequency histogram
	private void zeroOutDiceFrequency() {
		for (int index = 0; index < diceFrequencyArray.length; index++){
			diceFrequencyArray[index] = 0;
		}
		
	}

	//Establishes that the player has 2 rerolls after the initial roll
	private void rerollsPerPlayerTurn(){
		for (int i = 0; i < 2; i++){
			display.printMessage("Please select the dice you wish to re-roll!");
			display.waitForPlayerToSelectDice();	
			checkWhichDieIsSelectedAndReroll();
			display.displayDice(yahtzeeDice);
		}
	}
	

	/* BONUS YAHZTEE! */
	private void bonusYahtzee(int playerIndex) {
		if (scoreKeeper[playerIndex-1][YAHTZEE-1] > 0){
			scoreKeeper[playerIndex-1][YAHTZEE-1] += BONUS_YAHTZEE_VALUE;
			scoreKeeper[playerIndex-1][LOWER_SCORE-1] += BONUS_YAHTZEE_VALUE;
			scoreKeeper[playerIndex-1][TOTAL-1] += BONUS_YAHTZEE_VALUE;
			display.updateScorecard(YAHTZEE, playerIndex, scoreKeeper[playerIndex-1][YAHTZEE-1]);
			display.updateScorecard(TOTAL, playerIndex, scoreKeeper[playerIndex-1][YAHTZEE-1]);
			pause(500);
			display.printMessage("Congratulations, " + playerNames[playerIndex - 1] + "! You scored a Bonus Yahtzee for an extra 100 points!  Click to continue!");
			waitForClick();
		}
	}
		
	//This calculates the score per round per player;
	//It also verifies that the category the player clicks on, it validates the score of the dice hand dealt
	private void calculateRoundScore(int playerIndex, int category) {
		int roundScore;
		
		if (validateDiceCategories(category)){
			roundScore = calculateScore(category);
		} else {
			roundScore = 0;
		}
		
		/* Keep tally of each individual category score and add to 2-dimensional array */
		scoreKeeper[playerIndex-1][category-1] += 1 + roundScore;
		
		/* Because each entry in the 2-dimensional grid array is initialized to -1,
		 * have to make sure that after we add the initial amount PLUS 1 to normalize 
		 * the scores to zero.
		 */
		if (category == ONES || category == TWOS || category == THREES ||
			category == FOURS || category == FIVES || category == SIXES){
			if (scoreKeeper[playerIndex-1][UPPER_SCORE-1] == -1){
				scoreKeeper[playerIndex-1][UPPER_SCORE-1] += 1 + roundScore;
			} else {
				scoreKeeper[playerIndex-1][UPPER_SCORE-1] += roundScore;

			}
		}
		
		/* Repeated from above */
		if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND || category == FULL_HOUSE ||
			category == SMALL_STRAIGHT || category == LARGE_STRAIGHT || category == YAHTZEE || category == CHANCE){
			if (scoreKeeper[playerIndex-1][LOWER_SCORE-1] == -1){
				scoreKeeper[playerIndex-1][LOWER_SCORE-1] += 1 + roundScore;
			} else {
				scoreKeeper[playerIndex-1][LOWER_SCORE-1] += roundScore;
			}
		}
		
		/* Repeated from above */
		if (scoreKeeper[playerIndex-1][TOTAL-1] == -1){
			scoreKeeper[playerIndex-1][TOTAL-1] += 1 + roundScore;
		} else if (scoreKeeper[playerIndex-1][TOTAL-1] > -1){
			scoreKeeper[playerIndex-1][TOTAL-1] += roundScore;
		} else if (scoreKeeper[playerIndex-1][category-1] != -1){
			display.printMessage("You've already selected that category!  Choose another!");
		}
		/* Finally, update the GUI with the scores */
		display.updateScorecard(category, playerIndex, roundScore);
		display.updateScorecard(TOTAL, playerIndex, scoreKeeper[playerIndex-1][TOTAL-1]);
	}



	//Validates the dice roll according to the dice category chosen by the player 
	private boolean validateDiceCategories(int category) {
		
		if (category == ONES || category == TWOS || category == THREES ||
			category == FOURS || category == FIVES || category == SIXES || category == CHANCE){
			return true;
		} else {
			for (int freqIndex = 0; freqIndex < diceFrequencyArray.length; freqIndex++){
				if (checkForMultiplesOfAKindAndFullHouse(category, freqIndex)){
					return true;
				}
				if (checkForStraights(category, freqIndex)){
					return true;
				}

				if (category == YAHTZEE && (diceFrequencyArray[freqIndex] == 5)){
					return true;
				}
					

			}
		return false;
		}
	}

	/*** FUNCTION DECOMPOSITION OF ABOVE Part 1 ***/
	private boolean checkForMultiplesOfAKindAndFullHouse(int category, int freqIndex) {
		if (category == THREE_OF_A_KIND && diceFrequencyArray[freqIndex] >= 3){
			return true;
		}
		if (category == FOUR_OF_A_KIND && diceFrequencyArray[freqIndex] >= 4){
			return true;
		}
		if (category == FULL_HOUSE && diceFrequencyArray[freqIndex] == 3){
			for (int additionalCount = 0; additionalCount < diceFrequencyArray.length; additionalCount++){
				if (additionalCount == 2){
					return true;
				}
			}

		}
		return false;
		
	}
	/*** FUNCTION DECOMPOSTION Part 2 ***/
	private boolean checkForStraights(int category, int freqIndex){
		if (category == SMALL_STRAIGHT){
			if (diceFrequencyArray[2] == 0 || diceFrequencyArray[3] == 0){
				return false;
			} else {
				if (diceFrequencyArray[0] == 0 && diceFrequencyArray[1] == 0
						|| diceFrequencyArray[1] == 0 && diceFrequencyArray[2] == 0
						|| diceFrequencyArray[4] == 0 && diceFrequencyArray[5] == 0){
					return false;
				} else {
					return true;
				}
			}
		}
		if (category == LARGE_STRAIGHT){
			if (diceFrequencyArray[1] == 0 || diceFrequencyArray[2] == 0 || diceFrequencyArray[3] == 0
				|| diceFrequencyArray[4] == 0){
				return false;
			} else if (diceFrequencyArray[0] != 0 || diceFrequencyArray[5] != 0){
				return true;
			} else {
				return false;						}
		}
		return false;
	}


	//A histogram that collects the frequencies of each dice according to the dice hand dealt
	private void calculateDiceFrequency() {
		for (int index = 0; index < yahtzeeDice.length; index++){
			if (yahtzeeDice[index] == ONES)
				diceFrequencyArray[0]++;
	
			if (yahtzeeDice[index] == TWOS)
				diceFrequencyArray[1]++;
				
			if (yahtzeeDice[index] == THREES)
				diceFrequencyArray[2]++;
		
			if (yahtzeeDice[index] == FOURS)
				diceFrequencyArray[3]++;
			
			if (yahtzeeDice[index] == FIVES)
				diceFrequencyArray[4]++;
			
			if (yahtzeeDice[index] == SIXES)
				diceFrequencyArray[5]++;
		}
	}
	



	//Calculate the score according to what requirements of the category and what dice were randomly rolled
	private int calculateScore(int category) {
		int sumScore = 0;

		if (category == ONES || category == TWOS || category == THREES ||
				category == FOURS || category == FIVES || category == SIXES){
			for (int i = 0; i < N_DICE; i++){
				if (category == yahtzeeDice[i]){
					sumScore += yahtzeeDice[i];
				}
			}
		} else if (category == THREE_OF_A_KIND){
			for (int i = 0; i < diceFrequencyArray.length; i++){
				if (diceFrequencyArray[i] >= 3){
					sumScore += THREES * (i + 1);
				}
			}
		} else if (category == FOUR_OF_A_KIND){
			for (int i = 0; i < diceFrequencyArray.length; i++){
				if (diceFrequencyArray[i] >= 4){
					sumScore += FOURS * (i + 1);
				}
			}
		} else if (category == FULL_HOUSE){
			sumScore += 25;
		
		} else if (category == SMALL_STRAIGHT){
			sumScore += 30;
		
		} else if (category == LARGE_STRAIGHT){
			sumScore += 40;
		
		} else if (category == YAHTZEE){
			sumScore += 50;
			
		} else if (category == CHANCE){
			for (int i = 0; i < N_DICE; i++){
				sumScore += yahtzeeDice[i];
			}

		}
		return sumScore;
	}
	

	//Initializes the multidimensional array
	public int[][] initializeScoreList(){
		int[][] scoreList = new int[nPlayers][N_CATEGORIES];
		for (int i = 0; i < scoreList.length; i++){
			for (int j = 0; j < scoreList[i].length; j++){
				scoreList[i][j] = -1;
			}
		}
		return scoreList;
	}


	//Checks which die is selected by the player and randomly rerolls the dice
	private void checkWhichDieIsSelectedAndReroll() {
		for (int index = 0; index < N_DICE-1; index++){
			if(display.isDieSelected(index)){
				int randomizedRerollDie = rgen.nextInt(1, N_DICE+1);
				yahtzeeDice[index] = randomizedRerollDie;
			}
		}
		
	}



	private int[] randomizedDice(){
		
		int[] dice = new int[5];
		
		//Legitimate mode
		for (int i = 0; i < N_DICE; i++){
			int randomDice = rgen.nextInt(1, N_DICE+1);
			dice[i] += randomDice;
		}
		
//		//Cheat mode - Straights Galore
//		for (int i = 0; i < N_DICE; i++){
//			dice[i] += i+1;
//		}
		
//		//Cheat mode - Multiples of a Kind
//		for (int i = 0; i < N_DICE; i++){
//			dice[i] += 6;
//		}
		return dice;
	}
	
	/* Tally up the totals for each player */
	private void calculateResults() {
		for(int i = 1; i <= nPlayers; i++) {
			display.updateScorecard(UPPER_SCORE, i, scoreKeeper[i-1][UPPER_SCORE-1]);
			display.updateScorecard(LOWER_SCORE, i, scoreKeeper[i-1][LOWER_SCORE-1]);
			if(scoreKeeper[i-1][UPPER_SCORE-1] >= 63) {
				scoreKeeper[i-1][UPPER_BONUS-1] += 1 + 35;
			} else if (scoreKeeper[i-1][UPPER_SCORE-1] < 63){
				scoreKeeper[i-1][UPPER_BONUS-1] = 0;				
			}
			display.updateScorecard(UPPER_BONUS, i, scoreKeeper[i-1][UPPER_BONUS-1]);
			scoreKeeper[i-1][TOTAL-1] = scoreKeeper[i-1][TOTAL-1] + scoreKeeper[i-1][UPPER_BONUS-1];
			display.updateScorecard(TOTAL, i, scoreKeeper[i-1][TOTAL-1]);
		}
	}

	/* Calculates which player has the highest score and what the highest score is 
	 * and prints that information in a message at the very end of the game.*/
	private void calculateWinner() {
		int winningScore = 0;
		int winningPlayerNumber = 0;
		for(int i = 1; i <= nPlayers; i++) {
			int x = scoreKeeper[i-1][TOTAL-1];
			if( x > winningScore) {
				winningScore = x;
				winningPlayerNumber = i - 1;
			}
		}
		display.printMessage("Congratulations, " + playerNames[winningPlayerNumber] + ", you're the winner with a total score of " + winningScore + "!");
	}
}
