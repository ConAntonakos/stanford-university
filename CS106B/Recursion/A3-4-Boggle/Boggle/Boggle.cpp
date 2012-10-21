
/*
 * File: Boggle.cpp
 * ----------------
 * Name: Constantine Antonakos
 * Section: Faye
 * This file is the main starter file for Assignment #3, Part II, Boggle.
 * [TODO: extend the documentation]
 */
  
#include <iostream>
#include <cctype>
#include <cmath>
#include "strlib.h"
#include "gboggle.h"
#include "graphics.h"
#include "grid.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
using namespace std;
  
/* Constants */
const int MIN_WORD_COUNT = 4;
 
/** Variable used to manipulate board size  **/
double boggleBoardSize;
 
  
const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;
  
const string STANDARD_CUBES[16]  = {
   "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
   "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
   "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
   "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
   
const string BIG_BOGGLE_CUBES[25]  = {
   "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
   "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
   "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
   "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
   "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};
  
/* Function prototypes */
void welcome();
void giveInstructions();
  
void toupper(string& word);
bool askQuestion(string question);
 
void shuffleBoard(Vector<string> &, Grid<char> & board, double boggleBoardSize);
void customConfiguration(Grid<char> & board, double boggleBoardSize);
 
void humanTurn(Grid<char> & board, Set<string> & alreadyUsedWords, Lexicon & dictionary);
bool boardContainsWord(Grid<char> & board, string word, int & index, int numRow, int numCol);
 
void computerFindsWords(Grid<char> board, Set<string> & alreadyUsed, Lexicon & dictionary);
void computerRecursion(Grid<char> & board, Set<string> & alreadyUsed, Grid<bool> gridOfFlags, Lexicon & dictionary, string word, int row, int col);
 
void deHighlightCubes(Grid<char> board);
 
/* Main program */
int main() {
      
    initGraphics(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    welcome();
    giveInstructions();
     
    //Initializations
    Vector<string> vectorCubeLetters;
    Set<string> humanWords;
    Set<string> computerWords;
    Grid<char> board(double(sqrt(boggleBoardSize)), double(sqrt(boggleBoardSize)));
    Lexicon dictionary("EnglishWords.dat");
          
    string instructions = "Do you need instructions? (Please enter yes/no)";
    cout << endl;
    if (askQuestion(instructions))
        giveInstructions();
    while (true){
		vectorCubeLetters.clear();
		humanWords.clear();
		computerWords.clear();

        //Ask if user wants to play on Big Boggle board or Standard
        string boggleSize = "Do you want Big Boggle? (Please enter 'yes' for Big Boggle or 'no' for Standard Boggle)";
        cout << endl;
        if (askQuestion(boggleSize)){
            boggleBoardSize = 25.0;
            board.resize(double(sqrt(boggleBoardSize)), double(sqrt(boggleBoardSize)));
        } else {
            boggleBoardSize = 16.0;
            board.resize(sqrt(boggleBoardSize), sqrt(boggleBoardSize));
        }
        //Draw the board
        drawBoard(sqrt(boggleBoardSize), sqrt(boggleBoardSize));
         
        //Ask if user desires a custom configuration
        string customConfig = "Do you want a custom configuration? (Please enter yes/no)";
        cout << endl;
        if (askQuestion(customConfig)){
            customConfiguration(board, boggleBoardSize);
        } else {
            //If not, randomly shuffle and draw cubes
            shuffleBoard(vectorCubeLetters, board, boggleBoardSize);
        }
        // Task 2 & 3
        humanTurn(board, humanWords, dictionary);
     
        //Task 4
        computerFindsWords(board, computerWords, dictionary);
         
        //Replay?
        string replay = "Would you like to play again? (Please enter yes/no)";
        cout << endl;
        if (!askQuestion(replay)){
            cout << endl << "THANKS FOR PLAYING! :)" << endl;
            cout << "(Press ENTER to escape program)";
            break;
        }
        initGraphics(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    }
 
 
          
          
   return 0;
}
 
/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */
 
void welcome() {
   cout << "Welcome!  You're about to play an intense game ";
   cout << "of mind-numbing Boggle.  The good news is that ";
   cout << "you might improve your vocabulary a bit.  The ";
   cout << "bad news is that you're probably going to lose ";
   cout << "miserably to this little dictionary-toting hunk ";
   cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}
 
/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */
 
void giveInstructions() {
   cout << endl;
   cout << "The boggle board is a grid onto which I ";
   cout << "I will randomly distribute cubes. These ";
   cout << "6-sided cubes have letters rather than ";
   cout << "numbers on the faces, creating a grid of ";
   cout << "letters on which you try to form words. ";
   cout << "You go first, entering all the words you can ";
   cout << "find that are formed by tracing adjoining ";
   cout << "letters. Two letters adjoin if they are next ";
   cout << "to each other horizontally, vertically, or ";
   cout << "diagonally. A letter can only be used once ";
   cout << "in each word. Words must be at least four ";
   cout << "letters long and can be counted only once. ";
   cout << "You score points based on word length: a ";
   cout << "4-letter word is worth 1 point, 5-letters ";
   cout << "earn 2 points, and so on. After your puny ";
   cout << "brain is exhausted, I, the supercomputer, ";
   cout << "will find all the remaining words and double ";
   cout << "or triple your paltry score." << endl << endl;
   cout << "Hit ENTER/RETURN when you're ready...";
   getLine();
}
 
 
//Function call to capitalize every letter in a string
void toupper(string & word) {
    for (int i = 0; i < word.length(); i++){
       word[i] = toupper(word[i]);
    }
}
  
// A do-while loop to keep asking for a YES/NO answer to a given question
bool askQuestion(string question) {
    while(true){
        cout << question << " ";
        string answer = getLine();
        if(toupper(answer[0]) == 'N')
            return false;
        else if (toupper(answer[0]) == 'Y')
            return true;
        else
            cout << "Please answer yes or no." << endl << endl;
    }
}
 
/*
 * Function: shuffleBoard
 * Usage: shuffleBoard(Grid<char> & board, double boggleBoardSize);
 * -----------------
 * Randomize cubes and their sides.
 */
  
//Shuffle array and fill the board automatically with randomly chosen letters from constant
void shuffleBoard(Vector<string> & vectorCubeLetters, Grid<char> & board, double boggleBoardSize) {
     
    //Toggle Big Boggle board or Standard Boggle board based on user choice and shift board size accordingly
    if (boggleBoardSize == 16.0){
        int sizeOfArray = sizeof(STANDARD_CUBES) / sizeof(STANDARD_CUBES[0]);
        for (int i = 0; i < sizeOfArray; i++){
            vectorCubeLetters.add(STANDARD_CUBES[i]);
        }
    } else if (boggleBoardSize == 25.0){
        int sizeOfArray = sizeof(BIG_BOGGLE_CUBES) / sizeof(BIG_BOGGLE_CUBES[0]);
        for (int i = 0; i < sizeOfArray; i++){
            vectorCubeLetters.add(BIG_BOGGLE_CUBES[i]);
        }
    }
     
    //
    for(int i = 0; i < vectorCubeLetters.size(); i++) {
        int r = randomInteger(i, vectorCubeLetters.size()-1);
        swap(i, r);
    }
 
    int vecIndex = 0;
    for(int row = 0; row < sqrt(double(boggleBoardSize)); row++){
        for( int col = 0; col < sqrt(double(boggleBoardSize)); col++){
            string cubeSide = vectorCubeLetters.get(vecIndex);
            int randomLetterIndex = randomInteger(0, cubeSide.length()-1);
            char letter = cubeSide.at(randomLetterIndex);
            board[row][col] = letter;
            labelCube(row, col, letter);
            vecIndex++;
        }
    }
}
  
// Requests user for a board configuration input for the given size board
// and labels the cubes with the corresponding input string
void customConfiguration(Grid<char> & board, double boggleBoardSize) {
 
    cout << endl << "Enter a " << boggleBoardSize
         << "-character string to identify which letters you want on the cubes."
         << endl << "The first " << sqrt(boggleBoardSize)
         << " letters are the cubes on the top row from left to right "
         << endl << "next " << sqrt(boggleBoardSize)
         << " letters are the second row, etc."
         << endl << "Enter the string: ";
    string answer;
    //Loop to ask for 16 letters, else keep asking
    do {
        answer = getLine();
        if(answer.size() >= boggleBoardSize) break;
    } while (cout << "String must include " << boggleBoardSize
        << " characters! Try again: ");
    toupper(answer);
    int strIndex = 0;
 
    //Fill board with custom input string 
    for (int row = 0; row < sqrt(boggleBoardSize); row++){
        for (int col = 0; col < sqrt(boggleBoardSize); col++){
            char answerSubStr = answer[strIndex];
            board[row][col] = answerSubStr;
            labelCube(row, col, answerSubStr);
            strIndex++;
        }
    }
}
  
//This loop function is used to create checkpoints and check the existence of the input word from the user on the board
void humanTurn(Grid<char> & board, Set<string> & alreadyUsedWordsHumanBank, Lexicon & dictionary) {
    cout << endl << "Find all the words you can."
    << endl << "Signal defeat by pressing ENTER." << endl << endl;
	int index = 0;
    do {
 
        cout << "Enter a word: ";
        string word = getLine();
  
        if (word.empty())
            break; //the only way out of the do-while loop
  
        toupper(word);
         
        //If word < min word length
        if (word.length() < MIN_WORD_COUNT) {
            cout << "That word doesn't meet the minimum word length." << endl << endl;
        }
        //Word has already been entered
        else if (alreadyUsedWordsHumanBank.contains(word)){
            cout << "You've already found that word!" << endl << endl;
        }
        //If word is contained in lexicon and on board
        else if (dictionary.contains(word) && boardContainsWord(board, word, index, 0, 0)){
            alreadyUsedWordsHumanBank.add(word);
            recordWordForPlayer(word, HUMAN);
            cout << "Found a word!" << endl << endl;
			index = 0;
			pause(1000);
            deHighlightCubes(board);
        }
        else {
            cout << "You can't form that word!" << endl << endl;
        }
    } while(true);
}
 
/** RECURSIVE FUNCTION **/
//Checks to see if board contains word
bool boardContainsWord(Grid<char> & board, string word, int & index, int numRow, int numCol){
    //Peels off first letter of input word, and searches for every occurence until one is found that fits
    //with the word that was inputted
    if (index == 0){
        for(int i = 0; i < board.numRows(); i++){
            for(int j = 0; j < board.numCols(); j++){
                if(board[i][j] == word[index]){
                    ++index;
					if (boardContainsWord(board, word.substr(1), index, i, j)){
						highlightCube(i, j, true);
                        return true;
					}
                }
            }
        }
        return false;
    //Peels the remainder of the input word looking for the letters after the first letter to see
    //if it exists on the board returning true; else, returning false
    } else if (index > 0 && word.length() != 0){
        for (int i = numRow - 1; i <= numRow + 1; i++){
            for (int j = numCol - 1; j <= numCol + 1; j++){
                if (board.inBounds(i,j) && board[i][j] == word[0]){
                    ++index;
					if (boardContainsWord(board, word.substr(1), index, i, j)){
                        highlightCube(i, j, true);
                        return true;
					}
                } 
         
            }
        }
        return false;
    //One we've peeled every single letter from the word, then we've found it on the board
    } else if (word.length() == 0){
        return true;
    //*Optional*: "Not all paths return a value"
    } else {
        cout << "Should not get this far." << endl;
        return false;
    }
}
//For loop on every character on the board, and perform recursion on each character copy
void computerFindsWords(Grid<char> board, Set<string> & alreadyUsed, Lexicon & dictionary){
    //Setup a grid of bools to set on and off as the recursion goes deeper
    Grid<bool> gridOfFlags;
    gridOfFlags.resize(sqrt(boggleBoardSize), sqrt(boggleBoardSize));
     
    //Establish false flags on the every coordinate of the grid of flags
    for (int x = 0; x < gridOfFlags.numRows(); x++){
        for (int y = 0; y < gridOfFlags.numCols(); y++){
            gridOfFlags[x][y] = false;
        }
    }
 
    //Implement recursion on nested "for" loops looping over every character on the Boggle board
    for (int row = 0; row < board.numRows(); row++){
        for (int col = 0; col < board.numCols(); col++){
            computerRecursion(board, alreadyUsed, gridOfFlags, dictionary, "" + board[row][col], row, col);
        }
    }
}
 
/** RECURSIVE FUNCTION **/
//Computer recursive function to search for every word on the board__NOTE: HUMAN WILL BE DEFEATED__
void computerRecursion(Grid<char> & board, Set<string> & alreadyUsed, Grid<bool> gridOfFlags, Lexicon & dictionary, string word, int row, int col){
     
    /** Base Case **/
    if (!dictionary.containsPrefix(word) || gridOfFlags[row][col])
        return;
    //Turn the grid flag on
    gridOfFlags[row][col] = true;
     
    //If actually a word and passes the checks, it's already been used and add the word to the COMPUTER score
    if (word.length() >= MIN_WORD_COUNT){
        if (dictionary.contains(word)){
            if (!alreadyUsed.contains(word)){
                alreadyUsed.add(word);
                recordWordForPlayer(word, COMPUTER);
            }
        }
    }
     
    //Recursive part while checking each of the 8 directions around a single character...
    //and linking if a prefix is found
    for (int x = row - 1; x <= row + 1; ++x){
        for (int y = col - 1; y <= col + 1; ++y){
            if (!board.inBounds(x, y) || (x == row && y == col) || gridOfFlags[x][y]){
                continue;
            } else {
                if (dictionary.containsPrefix(word + board[x][y]))
                    computerRecursion(board, alreadyUsed, gridOfFlags, dictionary, word + board[x][y], x, y);
            }
        }
    }
}
 
void deHighlightCubes(Grid<char> board){
 
    for (int i = 0; i < board.numRows(); i++){
        for (int j = 0; j < board.numCols(); j++){
            highlightCube(i, j, false);
        }
    }
}