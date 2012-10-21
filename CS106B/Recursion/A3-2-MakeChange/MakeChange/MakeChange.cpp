
/*
 * File: MakeChange.cpp
 * ----------------------------
 * Name: Constantine Antonakos
 * Section: Faye
 * This file is the starter code for the MakeChange problem
 * fron Assignment #3, part 2.
 * [TODO: extend the documentation]
 */
 
#include <iostream>
#include "simpio.h"
#include <string>
#include "console.h"
#include "vector.h"
 
using namespace std;
 
/* Function: int MakeChange(int amount, Vector<int>& denominations)
 * ==================================================================
 * Takes an amount along with the Vector of available coin values,
 * and returns the minimum number of coins required.
 */
 
 
//FUNCTION PROTOTYPES
int makeChange(int amount, Vector<int> & denominations);
 
int processingChange(int amount, Vector<int> denom, int & index, int & quarterCount, 
                      int & dimeCount, int & nickelCount, int & pennyCount, int & totalCount);
 
void countUpIndividualCoinDenominations(int & index, int & quarterCount, 
                                        int & dimeCount, int & nickelCount, int & pennyCount);
 
 
int main() {
    //Declare and initialize the vector of denominations
    Vector<int> denominations;
    denominations += 25, 10, 5, 1; /* <--This can be altered */
 
    cout << "Welcome to MakeChange!" << endl << endl;
    cout << "It will attempt to return the minimum value " << endl;
    cout << "of coins needed to process the amount of change you input." << endl << endl;
 
    int changeAmount = getInteger("Please enter an amount: $0.");
 
    cout << endl << "Processing change... ";
    int makeChangeResult = makeChange(changeAmount, denominations);
    cout << "Done!" << endl << endl;
     
    if (makeChangeResult != -1){
        cout << "The minimum number of coins needed was " << makeChangeResult << "!" << endl;
    } else if (makeChangeResult == -1){
        cout << "No proper combination of coins was found for input amount.  Sorry!" << endl;
    }
     
    return 0;
}
 
int makeChange(int amount, Vector<int> & denominations){
    //Use the counter to add up totals per coin denomination and print out separately later **OPTIONAL**
    int quarterCount = 0;
    int dimeCount = 0;
    int nickelCount = 0;
    int pennyCount = 0;
    int index = 0;
    int totalCount = 0;
 
    return processingChange(amount, denominations, index, quarterCount, dimeCount, nickelCount, pennyCount, totalCount);
 
}
 
//PROCESS THE CHANGE function
int processingChange(int amount, Vector<int> denominations, int & index, int & quarterCount, 
                      int & dimeCount, int & nickelCount, int & pennyCount, int & totalCount){
     
    //Get the value of the coin from the array
    int coinValue = denominations[index];
     
    int lastIndexDenominations = denominations.size()-1;
 
    //Move onto the next index of the vector, i.e., from quarters to dimes if quarters are zero
    //If amount is zero, meaning we've subtracted everything evenly return total minimum # of coins
    if (amount == 0){
        return totalCount;
 
    //If the amount is EQUAL to the coin value, then substract as well, decrement coinValue from amount, and increment totalCount
    } else if (amount == coinValue){
        amount -= coinValue;
        ++totalCount;
        //countUpIndividualCoinDenominations(index, quarterCount, dimeCount, nickelCount, pennyCount);
 
    //If the coinValue is less than the amount, subtract the amount by the coinValue and continue
    } else if (coinValue != 0 && coinValue < amount){
        amount -= coinValue;
        ++totalCount;
        //countUpIndividualCoinDenominations(index, quarterCount, dimeCount, nickelCount, pennyCount);
 
    } else {
        //If coinValue is greater than amount and its last index of vector, then not enough coins to give based on input
        if (coinValue > amount && index < lastIndexDenominations){
            index++;
        } else if (coinValue > amount && index == lastIndexDenominations || coinValue == 0 && index == lastIndexDenominations){
            return -1;
        }
    }
        /* IGNORE CODE **********************************************************************************************
         * else if 
         *  //Else if its not the last index, and coin value is too big, continue and move onto next coin denomination
         *  continue
         *
         * for int coinNum = 0; coinNum < denominations[i]; ++coinNum
         */
 
    /** RECURSION **/
    return processingChange(amount, denominations, index, quarterCount, 
                                            dimeCount, nickelCount, pennyCount, totalCount);
     
}
 
void countUpIndividualCoinDenominations(int & index, int & quarterCount, 
                                        int & dimeCount, int & nickelCount, int & pennyCount){
    if (index == 0){
        ++quarterCount;
    } else if (index == 1){
        ++dimeCount;
    } else if (index == 2){
        ++nickelCount;
    } else if (index == 3){
        ++pennyCount;
    }
}
 
//Discove the minimum amount of coins needed to return the amount of change
//Based on a greedy algorithm
//int makeChange(int amount, Vector<int>& denominations) {
//
//
//}
 
    