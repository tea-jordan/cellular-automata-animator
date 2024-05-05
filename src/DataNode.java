/******

Name: Tristan Jordan

CA Animator: DataNode Class

Date: 4/15/2024

Notes / Class Description: 
	   This class represents a data node for my implementation of a linked-list. 
	   The data note has Data of some type <T>, and a pointer to the next Node,
	   also of type <T>.
       
******/


import java.util.function.Predicate;


public class DataNode<T> implements Node<T> {

	// attributes include data, and pointer to next
	private T data;
	private Node<T> next;


	/**
	 * Constructor for a new data node
	 * 
	 * @param data - an object of type T for this node's data
	 * @param next - a Node<T> which is the next node in the linked list
	 */
	public DataNode(T data, Node<T> next) {
		this.data = data; 
		this.next = next;
	} 


	/**
	 * Getter for data
	 * 
	 * @return T - data of this node
	 */
	public T getData() {
		return this.data;
	}


	/**
	 * Getter for next node
	 * 
	 * @return Node<T> - this node's next node pointed to
	 */
	@Override
	public Node<T> getNext() {
		return this.next;
	}

	
	/**
	 * Setter for the next node
	 * 
	 * @param nextNode - a Node<T> to be set to this node's next 
	 */
	@Override
	public void setNext(Node<T> nextNode) {
		this.next = nextNode;
	}


	/**
	 * A method to find a node specified by index
	 * 
	 * @param int - index, which index to find
	 * @param current - variable which tracks progress towards node down the recursive calls
	 * 
	 * @return Node<T> - the node, if found
	 */
	@Override
	public Node<T> findNode(int index, int current) {
		if(index == current) {
			return this;
		} else {
			return next.findNode(index, current + 1); // recursive call incrementing current if not found
		}
	}

	
	/**
	 * A method to count nodes in the linked list given an argument (null to count all)
	 * 
	 * @param Predicate<T> - a Predicate object to count nodes based on a certain condition
	 * 						 enter null to count all
	 * 
	 * @return int - the number of nodes which met the predicate condition
	 */
	@Override
	public int countTest(Predicate<T> predicate) {

		boolean result; // initialize a boolean var to track result of test

		// if predicate == null, this means we want to count everything so set result to true, else call test method
		if(predicate == null) {
			result = true;
		} else {
			result = predicate.test(data);
		}

		// if result true, increment count by 1, then recursive call, else increment by 0 & recursive call
		if(result == true) {
			return 1 + next.countTest(predicate);
		} else {
			return 0 + next.countTest(predicate);
		}

	}

	
	/**
	 * A method for printing the linked list, also given a predicate like the count method above. 
	 * 
	 * @param Predicate<T> - which nodes to print, null to print all
	 * 
	 * @return String - a formatted string with info. for all nodes to be printed
	 */
	@Override
	public String printTest(Predicate<T> predicate) {

		boolean result; // initialize var to track result

		// if predicate null, assume we want string of everything, so default to true, else test method
		if(predicate == null) {
			result = true;
		} else {
			result = predicate.test(data);
		}

		// if result is true, recursive call adding onto string, different case for when next is DataNode or EmptyNode
		if(result == true) {

			String currentString = "";

			if(!(this.getNext() instanceof EmptyNode)) {
				currentString = " " + this.getData().toString() + ",";
			}

			if(this.getNext() instanceof EmptyNode) {
				currentString = " " + this.getData().toString() + " ]";
			}

			String newStr = next.printTest(predicate);
			return currentString + newStr;

		} else {
			return next.printTest(predicate);
		}
	}

}

