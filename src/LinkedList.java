/******

Name: Tristan Jordan

CA Animator: LinkedList Class

Date: 4/15/2024

Notes / Class Description: 
	   This class represents a generic linked list. A linked list may hold a 
	   data type <T>, and consists of a Node<T> pointer to the head node in the
	   list. The head node may be empty for a new linked list. Each node itself
	   holds a data element, and a pointer to the next node. 
       
******/


import java.util.function.Predicate;


public class LinkedList<T> {

	// private Node attribute for the head node of the linked list
	private Node<T> head; 
	
	
	/**
	 * No argument constructor, sets the head node to be a new empty node
	 */
	public LinkedList() {
		this.head = new EmptyNode<T>(); 
	}
	
	
	/**
	 * Getter for the head node of the linked list
	 * 
	 * @return Node - the head node of the linked list
	 */
	public Node<T> getHead() {
		return this.head;
	}
	
	
	/**
	 * Method to add data to the list. Note, this linked list is like a Stack, 
	 * where the base add method adds any new item to be the new head. 
	 * 
	 * @param T - the data type being added
	 * @return boolean - true when added
	 */
	public boolean add(T data) {

		Node<T> next = new DataNode<T>(data, head);
		head = next; 
		
		return true; 
	}
	
	
	/**
	 * Recursive method to find and return the node at a given index
	 * 
	 * @param index - int of node's index we are searching for
	 * @return Node<T> the node at specified index
	 */
	public Node<T> findNode(int index){
		
		if(index < 0) {
			System.out.println("Error, cannot find a negative index...");
		} else if (index > this.getLength(null) - 1) {
			System.out.println("Error, index greater than maximum in list provided...");
		} 
			
		return head.findNode(index, 0);
		
	}
	
	
	/**
	 * Recursive method to count every element where the given predicate returns true
	 * 
	 * @param predicate - a predicate object to evaluate
	 * @return integer - the count of all items in the linked list for which the predicate was true
	 */
	public int getLength(Predicate<T> predicate) {
		return head.countTest(predicate);
	}
	
	
	/**
	 * toString method that can handle a predicate. Recursive, starts chain at head node
	 * 
	 * @param predicate - test to make on data element of each node
	 * @return String - formatted string of all nodes that pass test
	 */
	public String toString(Predicate<T> predicate) {
		
		String result = "Linked-list: [";
		result += head.printTest(predicate);
		
		return result;
	}
	
}

