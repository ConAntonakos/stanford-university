/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

//Name: Constantine Antonakos
//Section Leader: Shayon Saleh

public class Hangman extends ConsoleProgram {

	//Define instance variables
	private HangmanLexicon hangmanLexicon;
	private HangmanCanvas hangmanCanvas;
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/** Tracks the number of guesses the player has */
	private int guessCounter = 8;
	
	//This is the word being guessed
	private String wordToBeGuessed;
	    
	//This is the secret word
	private String secretWord = randomizeSecretWord();

	//This is the latest character entered by the user
	private char inputChar;

	//This string keeps track of all the incorrect guessed letters
	private String incorrectLetters = "";
	
		
	public void run() {
		setUpGame();
		playGame();

	}
	
	public void init() {
		hangmanCanvas = new HangmanCanvas();
		add(hangmanCanvas);
	}
	
	//Set up the game with welcome message, hidden word, and # of guesses left
	private void setUpGame() {
		hangmanCanvas.reset();
		wordToBeGuessed = hideWordWithDashes();
		hangmanCanvas.displayWord(wordToBeGuessed);
		println("Welcome to Hangman!");
	    println("The word now looks like this: " + wordToBeGuessed);
	    println("You have " + guessCounter + " guesses left.");
	}

	//Generates a random word selected from the HangmanLexicon
	private String randomizeSecretWord() {
		hangmanLexicon = new HangmanLexicon();
	    int randomWord = rgen.nextInt(0, (hangmanLexicon.getWordCount() - 1));
	    String chosenWord = hangmanLexicon.getWord(randomWord);
	    return chosenWord;
	}
	
	private String hideWordWithDashes() {
		String result = "";
		for(int i = 0; i < secretWord.length(); i++) {
			result = result + "-";
		}
		return result;
	}
	
	private void playGame() {
		while(guessCounter > 0) {
			String getChar = readLine("Your guess: ");
			while (true) {
				if(getChar.length() > 1) {
					getChar = readLine("You can only guess one letter at a time. Try again: ");
				}
				if(getChar.length() == 1) break;
			}
			inputChar = getChar.charAt(0);
			if(Character.isLowerCase(inputChar)) {
				inputChar = Character.toUpperCase(inputChar);
			}
			checkLetters();
			if (wordToBeGuessed.equals(secretWord)) {
				println(" ");
				println("You guessed the word: " + secretWord);
				println("You win");
				break;
			}
			println("The word now looks like this: " + wordToBeGuessed);
			println("You have " + guessCounter + " guesses left.");

		}
		if (guessCounter == 0) {
			println(" ");
			println("You're completely hung.");
			println("The word was: " + secretWord);
			println("You lose.");
		}
	}
	
	//Updates the wordToBeGuessed if the character entered by the user is correct
	private void checkLetters() {
		//Verifies whether guessed letter exists in the word
		if(secretWord.indexOf(inputChar) == -1) {
			println(" ");
			println("There are no " + inputChar + "'s in the word");
			guessCounter--;
			incorrectLetters = incorrectLetters + inputChar;
			hangmanCanvas.noteIncorrectGuess(incorrectLetters);
		}
		if(secretWord.indexOf(inputChar) != -1) {
			println(" ");
			println("The guess is correct.");
		}
		//Scans through each of the letters of the words and tries to check if a match exists with the guessed letter...
		//if so, update the wordToBeGuessed to reveal the actual guessed letter
		for(int i = 0; i < secretWord.length(); i++) {
			if (inputChar == secretWord.charAt(i)) {
				if(i == 0) {
					wordToBeGuessed = inputChar + wordToBeGuessed.substring(1);
				} else if (i > 0) {
					wordToBeGuessed = wordToBeGuessed.substring(0, i) + inputChar + wordToBeGuessed.substring(i + 1);
				}
				hangmanCanvas.displayWord(wordToBeGuessed);
			}
		}
	}
}


