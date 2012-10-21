/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
  
#include "pqueue-doublylinkedlist.h"
#include "error.h"
#include <iostream>
 
DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	head = NULL;
}
 
DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
    Node* ptr = head;
    while (ptr != NULL){
        Node* toDelete = ptr;
        ptr = ptr->next;
        delete toDelete;
    }
}

int DoublyLinkedListPriorityQueue::size() {
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
 
bool DoublyLinkedListPriorityQueue::isEmpty() {
    
    return (head == NULL);
}

void DoublyLinkedListPriorityQueue::enqueue(string value){

    if (head == NULL){
        element = new Node;
		element->value = value;
        element->next = NULL;
        element->previous = NULL;
        head = element;

    //There are/is element(s) in the list
	} else {
        Node *newCell = new Node;
   
        head->previous = newCell;
 
        newCell->value = value;
        newCell->next = head;
        newCell->previous = NULL;

		head = head->previous;
	}
}
 
string DoublyLinkedListPriorityQueue::peek() {
	if (isEmpty())
		error("Priority queue is empty!");
	
	if (!isEmpty()){
		curr = head;
		peekVal = curr->value;
		while(curr != NULL){
			//Keep going down the doubly linked list until you see an element that ends in NULL
			if (curr->value <= peekVal){
				peekVal = curr->value;
				curr = curr->next;
			} else {
				curr = curr->next;
			}
		}
		return peekVal;
	}
}

string DoublyLinkedListPriorityQueue::dequeueMin() {

    if (isEmpty()){
        error("Queue is empty!");
    }

	curr = head;
	dequeueVal = curr->value;
	while(curr != NULL){

		//Keep going down the doubly linked list until you see an element that ends in NULL
		if (curr->value <= dequeueVal){
			dequeueVal = curr->value;
			curr = curr->next;
		} else {
			curr = curr->next;
		}
	}
	toBeRemoved = head;
	while (toBeRemoved != NULL){
		if (toBeRemoved->value == dequeueVal){
			if (toBeRemoved->next != NULL){
				if (toBeRemoved->previous == NULL){
					head = head->next;
					toBeRemoved->next->previous = NULL;
					delete toBeRemoved;
					break;
				} else {
					toBeRemoved->previous->next = toBeRemoved->next;
					toBeRemoved->next->previous = toBeRemoved->previous;
					delete toBeRemoved;
					break;
				}
			} else if (toBeRemoved->next == NULL){
				if (toBeRemoved->previous == NULL){
					delete head;
					head = NULL;
					break;
				} else {
					toBeRemoved->previous->next = NULL;
					delete toBeRemoved;
					break;
				}
			}
		} else {
			toBeRemoved = toBeRemoved->next;
		}
	}
    return dequeueVal;
}


 //
 //   Node *tempElement = new Node;       
 //    
 //   Node *curr = head;
 //
 //   //Keep going down the doubleLinkedList until you see an element that ends in NULL 
 //   while (curr->next != NULL){
 //       if (curr->value < tempElement->value){
 //           tempElement = curr;

 //       }
 //       curr = curr->next;
 //   }
 //   string returnString = tempElement->value;

	////First in the list
 //   if (tempElement->previous == NULL){
	//	
 //       head->next = tempElement->next;
 //       head->previous = NULL;
 //       delete tempElement;

	////At the end of the list
	//}else if (tempElement->next = NULL){
 //       (tempElement->previous)->next = NULL;
 //       delete tempElement;

	////Element is in the middle of 2 other elements in the list
 //   }else{
 //       (tempElement->previous)->next = tempElement->next;
 //       tempElement->next = tempElement->previous;
 //       delete tempElement;
 //   }
 //
 //   return returnString;
