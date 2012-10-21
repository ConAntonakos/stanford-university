
/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
  
#include "pqueue-linkedlist.h"
#include <iostream>
#include "error.h"
 
LinkedListPriorityQueue::LinkedListPriorityQueue() {
    head = NULL;
}
 
LinkedListPriorityQueue::~LinkedListPriorityQueue() {

    while (head != NULL){
        Node* next = head->next;
        delete head;
        head = next;
    }
}
 
int LinkedListPriorityQueue::size() {
	if (isEmpty()){
		return 0;
	} else {
		int count = 0;
		for (Node *currElem = head; currElem != NULL; currElem = currElem->next){
			cout << currElem->value << endl;
			count += 1;
		}
		return count;
	}
}
 
bool LinkedListPriorityQueue::isEmpty(){
	return (head == NULL);

}
 
void LinkedListPriorityQueue::enqueue(string value) {
	newNode = new Node;
	newNode->value = value;
	newNode->next = NULL;
	newNode->prev = NULL;

    if (head == NULL){
		head = newNode;
		newNode->next = NULL;
		return;
	}

	//There are/is element(s) in the list
	//Only 1 element in list
	if (newNode->value <= head->value){
		//Stick new element at the beginning
		newNode->next = head;
		head = newNode;
		newNode->prev = head;
		return;
	}

		//if (firstNode->value < value){
		//	firstNode->next = secondNode;
		//	firstNode->next->previous = firstNode;
		//		
		//} else {
		//	//Stick new element at the begining
		//	secondNode->next = firstNode;
		//	secondNode->previous = head;
		//	firstNode->previous = secondNode;
		//	firstNode = secondNode;
		//	secondNode = firstNode;
		//	head = firstNode;
		//	return;
		//}
	curr = head;
	while (curr->next != NULL){
		if (newNode->value <= curr->next->value){
			if (newNode->value > curr->value) {
				curr->next->prev = newNode;
				newNode->next = curr->next;
				curr->next = newNode;
				newNode->prev = curr;
				return;
			} else /*if (newNode->value < curr->value)*/ {
				newNode->next = curr->next;
				curr->next->prev = newNode;
				curr->next = newNode;
				newNode->prev = curr;
				return;
			}
		}
		curr = curr->next;
	}

	if (newNode->value == curr->value){
		newNode->next = curr->next;
		curr->next->prev = newNode;
		curr->next = newNode;
		newNode->prev = curr;
		return;
	} else {
		curr->next = newNode;
		newNode->next = NULL;
		return;
	}
}

//	} else {
//
//		//If there are multiple elements in list
//		prevElem = head->previous;
//		currElem = head->next;
//		nextElem = currElem->next;
//		Node *toBeAdded = new Node;
//		toBeAdded->value = value;
//		toBeAdded->next = NULL;
//		toBeAdded->previous = NULL;
//		while (currElem->next != NULL){
//			if (currElem->value < value){
//				toBeAdded->next = nextElem;
//				nextElem->previous = toBeAdded;
//				currElem->next = toBeAdded;
//				toBeAdded->previous = currElem;
//				currElem = toBeAdded;
//				return;
//			}
//			if (currElem->value >= value){
//				prevElem->next = toBeAdded;
//				currElem->previous = toBeAdded;
//				toBeAdded->next = currElem;
//				toBeAdded->previous = prevElem;
//				return;
//			}
//		}
//	}
//}
    //                } else {
				//		 currElement->next = currElement->next;
				//		 return;
				//	}
				//		} else
				//			 // value is lower than the current element - no need to continue searching
				//		{
				//			currElement = new Node(value,currElement);
				//			return;
				//		}
    //            }
				//
				//currElement->next = new Node(value);
 
string LinkedListPriorityQueue::peek() {
    if (isEmpty())
		error("Linked list is empty");
		/*return "Linked list is empty";*/
    return head->value;
}
 
string LinkedListPriorityQueue::dequeueMin() {
	
	if (isEmpty()){
		error("Linked list is empty");
		/*return "Linked list is empty";*/
	} else {
		Node *toBeRemoved = head;
		string minValue = toBeRemoved->value;
		head = head->next;
		delete toBeRemoved;
		return minValue;
	}
}