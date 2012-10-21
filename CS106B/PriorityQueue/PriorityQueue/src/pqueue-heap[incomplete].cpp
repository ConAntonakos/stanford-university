
/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
  
#include "pqueue-heap.h"
#include "error.h"
 
HeapPriorityQueue::HeapPriorityQueue() {
	data = new string[];
    logicalHeapSize = 0;
    allocatedArraySize = 0;
}
 
HeapPriorityQueue::~HeapPriorityQueue() {
    delete[] data;
    logicalHeapSize = 0;
    allocatedArraySize = 0;
}
 
int HeapPriorityQueue::size() {
     
    return logicalHeapSize;
}
 
bool HeapPriorityQueue::isEmpty() {
    return logicalHeapSize == 0;
}
/** NEEDS TO BE FIXED **/
void HeapPriorityQueue::enqueue(string value) {
    
	//Check to see if heap is empty
	if (logicalHeapSize == 0){

		//Just add it
        data[0] = value;

		//Update logical heap size to next index
        logicalHeapSize++;
    }else{
        //Check to see if there is memory left in array for new entry
        if (logicalHeapSize >= allocatedArraySize){
            error("Out of memory!");

        }else{
            //Add to array using logicalHeapSize as index
            data[logicalHeapSize] = value;

            //Increament since array just grew by 1
            int childIndex = logicalHeapSize;
			
			//Next empty spot
            logicalHeapSize++;
 
            //Now check for propper lex order value, by checking curr to parent
            int parentIndex = getParentIndex(childIndex);
            
			while (data[childIndex] < data[parentIndex]){
                bubbleUp(childIndex); //Perform the swap
                 
                //Now shift the up one row to see if the bubbleUp needs to continue
                childIndex = parentIndex;
                parentIndex = getParentIndex(childIndex);
 
            }
             
        }
    }
 
}
 
string HeapPriorityQueue::peek() {
    if (isEmpty()){
        error("Heap is empty!");
    }else{
        return data[0];
    }
	return "";
}
 
string HeapPriorityQueue::dequeueMin() {
    //Swap the max lex value item and swap it with the min lex value
    int minValIdx = 0; /* always 0 */
    int maxValIdx = (logicalHeapSize -1); /* logicalHeapSize hold the number of elements in the help, but to get the position, need og -1 since index array is indexed at 0 */
    string minValue = data[0];
 
    //swap min for max
    data[minValIdx] = data[maxValIdx];
    //but max is still in the heap, so have to remove and decrement logicalHeapSize
    data[maxValIdx] = "";
    logicalHeapSize--;
 
    int parentIndex = 0; //Have to start bubbleDown at the top of the help
    int leftChildIndex = 1; 
    int rightChildIndex = 2;
    
	//If any of the child is less than the parent, do bubbleDown to swap
    while ((data[parentIndex] > data[leftChildIndex]) || (data[parentIndex] > data[rightChildIndex])){
        //Have to check to make sure to swap out the highest lex val of the 2 child
        if ((data[rightChildIndex] < data[leftChildIndex]) && (data[parentIndex] > data[leftChildIndex])){

			//bubbleDown parent with leftChild
            bubbleDown(parentIndex, leftChildIndex);

            //Update to keep checking
            parentIndex = leftChildIndex;
        }else{
			//BubbleDown parent ith rightChild
            bubbleDown(parentIndex, rightChildIndex);
            
			//Update to keep checking
            parentIndex = rightChildIndex;
        }
        //Update to keep checking
        leftChildIndex = getLeftChildIndex(parentIndex);
        rightChildIndex = getRightChildIndex(parentIndex);
    }
 
 
    return minValue;
}
 
void HeapPriorityQueue::bubbleDown(int parent, int child){
    string swapTemp = data[child];
 
    data[child] = data[parent];
    data[parent] = swapTemp;
}
 
void HeapPriorityQueue::bubbleUp(int nodeIndex){
    
	//nodeIndex is the child
    int parentIndex = getParentIndex(nodeIndex);
    string tempSwap = data[nodeIndex];
    data[nodeIndex] = data[parentIndex];
    data[parentIndex] = tempSwap;
}
 
int HeapPriorityQueue::getLeftChildIndex(int nodeIndex) {
    return 2 * nodeIndex;
}
 
int HeapPriorityQueue::getRightChildIndex(int nodeIndex) {
    return 2 * nodeIndex + 1;
}
 
int HeapPriorityQueue::getParentIndex(int nodeIndex) {
    return nodeIndex / 2;
}
    