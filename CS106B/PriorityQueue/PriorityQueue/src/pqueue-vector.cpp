
/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
  
#include "pqueue-vector.h"
#include "error.h"
 
 
VectorPriorityQueue::VectorPriorityQueue() {
    
}
 
VectorPriorityQueue::~VectorPriorityQueue() {
    
}
 
int VectorPriorityQueue::size() {
    return myVector.size();
}
 
bool VectorPriorityQueue::isEmpty() {
    
    return myVector.size() == 0;
}
 
void VectorPriorityQueue::enqueue(string value) {
    myVector.push_back(value);
}
 
string VectorPriorityQueue::peek() {

    if (myVector.isEmpty()){
        error("Queue is empty!");
    }
    string tempElement = myVector[0];

    foreach (string element in myVector){
        if (element < tempElement)
            tempElement = element;
    }
    return tempElement;
}
 
string VectorPriorityQueue::dequeueMin() {
    
    if (myVector.isEmpty()){
        error("Queue is empty!");
    }
    string tempElement = myVector[0];
    int counter = 0;
    for (int i = 0 ; i < myVector.size(); i++)
    {
        if (myVector[i] < tempElement)
        {
            tempElement = myVector[i];
            counter = i;
        }
    }
    myVector.removeAt(counter);
    return tempElement;
}