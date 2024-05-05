/******

Name: Tristan Jordan

CA Animator: Node Interface

Date: 4/15/2024

Notes / Interface Description: 
	   This interface outlines what kinds of general things a Node in a linked-list
	   should be able to do. 
       
******/


import java.util.function.Predicate;


public interface Node<T> {
	
	// getters & setters for node, data, etc.	
	public Node<T> getNext(); 
	public void setNext(Node<T> nextNode);
	public T getData(); 

	// method to find a node at a given index
	public Node<T> findNode(int index, int current);

	// method for counting 
	public int countTest(Predicate<T> predicate);   

	// methods for string representation
	public String printTest(Predicate<T> predicate);
	
}

