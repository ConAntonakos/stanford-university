/* 
 * File: MindReading.cpp
 * ---------------------
 * Name: Constantine Antonakos
 * Section: Faye
 * This file is the starter code for the MindReading problem
 * fron Assignment #3, part 1.
 * [TODO: extend the documentation]
 */
 
#include "lexicon.h"
#include "vector.h"
#include <string>
#include <iostream>
#include <cctype>
#include "simpio.h"
 
using namespace std;
 
/* Function: void ListCompletions(string digits, Lexicon & lex);
 * Usage: ListCompletions("72547", english);
 * ==================================================================
 * prints all words from the lexicon that can be formed by extending
 * the given digit sequence.
 */
void listCompletions(string digits, Lexicon & lex, int & counter); 
void listCompletionsRecurseAndDiscoverPrefixes(string digits, string currPrefix, Lexicon & lex, Vector<string> & prefixes);
void findCompletions(string currentPrefix, Lexicon & lex, int & counter);
 
int main() {
    cout << "This will construct valid English words out of an input number sequence." << endl << endl;
     
    Lexicon english("EnglishWords.dat");
 
    int counter = 0;
     
    string digits = getLine("Please enter a series of numbers: ");
    cout << endl;
     
    listCompletions(digits, english, counter);
 
    return 0;
 
}
 
void listCompletions(string digits, Lexicon & lex, int & counter) {
    string prefix = "";
    Vector<string> prefixes;
    cout << "Compiling collection of valid prefixes based on your input...";
    listCompletionsRecurseAndDiscoverPrefixes(digits, prefix, lex, prefixes);
    cout << "Done!" << endl << endl << "Now reading your mind..." << endl << endl;
    foreach (string prefix in prefixes){
        findCompletions(prefix, lex, counter);
    }
    if (counter == 0){
        cout << "Sorry!  Nothing was found.";
    }
     
 
}
 
void listCompletionsRecurseAndDiscoverPrefixes(string digits, string currPrefix, Lexicon & lex, Vector<string> & prefixes){
    static string letters[10] = { "", "", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ" };
 
    if (digits == "" && lex.containsPrefix(currPrefix)){
        prefixes.add(currPrefix);           
    } else {
 
        char subDigit = digits[0];
        int index = subDigit - '0';
        string remainingDigits = digits.substr(1);
             
        string phoneNumbers = letters[index];
 
        for (int i = 0; i < phoneNumbers.length(); i++){
            string nextPrefix = currPrefix + phoneNumbers[i];
            if (lex.containsPrefix(nextPrefix))
                listCompletionsRecurseAndDiscoverPrefixes(remainingDigits, nextPrefix, lex, prefixes);
 
        }
    }
}
 
void findCompletions(string currentPrefix, Lexicon & lex, int & counter){
    if(!lex.containsPrefix(currentPrefix)){
        return;
    } else {
        if (lex.contains(currentPrefix)){
            counter++;
            cout << currentPrefix << endl;
        }
        for (char ch = 'A'; ch <= 'Z'; ch++){
            findCompletions(currentPrefix + ch, lex, counter);
        }
    }
 
}