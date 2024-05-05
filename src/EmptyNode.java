/******

Name: Tristan Jordan

CA Animator: EmptyNode Class

Date: 4/15/2024

Notes / Class Description: 
	   This class represents an empty node in my implementation of a linked list. 
	   An empty node has no data or next pointer, it is basically null, to tell
	   when we've reached the end of the list. 
       
******/


import java.util.function.Predicate;


public class EmptyNode<T> implements Node<T> {

	
	/**
	 * Base case for getNext(), if we've reached the empty node return null
	 */
	@Override
	public Node<T> getNext() {
		return null;
	}

	
	/**
	 * setNext(), if this is called on empty node, do nothing
	 */
	@Override
	public void setNext(Node<T> nextNode) {
		// do nothing, we'd never set next on an empty node
	}

	
	/**
	 * getData(), null as EmptyNodes have no data
	 */
	@Override
	public T getData() {
		return null;
	}

	
	/**
	 * findNode - return null if this point reached (e.g., node is not present in linked-list)
	 */
	@Override
	public Node<T> findNode(int index, int current) {
		return null;
	}

	
	/**
	 * Return 0 when count reaches the empty node
	 */
	@Override
	public int countTest(Predicate<T> predicate) {
		return 0;
	}

	
	/**
	 * Return empty string when print reaches the empty node
	 */
	@Override
	public String printTest(Predicate<T> predicate) {
		return "";
	}

}

