/******

Name: Tristan Jordan

CA Animator: CAModel Class

Date: 4/15/2024

Notes / Class Description: 
	   This class represents the model for the CA animations. It implements our Model
	   interface, and stores the data / tools needed for generating CA animations. 
       
******/

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class CAModel implements Model {  


	// for the simple Cellular Automata (CA) examples, store the rule as a linked list
	private LinkedList<Integer> rule;

	// for simple CAs, data will be stored as an array of ints (0s or 1s). baseCase will have middle element as 1, rest 0s.
	private int[] currentState;
	private int[] baseCase;

	// 2D array of ints will store the initial and current states of the Game of Life (GOL). (0 = "dead"; 1 = "alive")
	private int[][] initialStateTable;
	private int[][] currentStateTable;

	// # of rows & columns to be used for 2D GOL; must match the dimensions of panels in the view
	private final static int gameNumRows = CAView.gameNumRows;
	private final static int gameNumCols = CAView.gameNumCols;

	// we will need a random number generator to initialize the GOL board.
	private Random rand;


	/**
	 * No argument constructor for the CA Model
	 */
	public CAModel() {

		// linked list to store rule for simple CAs
		this.rule = new LinkedList<Integer>();

		// simple CA base case & current state identical to start, but will not always be the case
		this.baseCase = new int[156];
		this.baseCase[77] = 1;
		this.currentState = new int[156];
		this.currentState[77] = 1;

		// initialize 2D array for GOL at specified dimensions
		this.initialStateTable = new int[gameNumRows][gameNumCols];

		// initialize random number generator
		this.rand = new Random();

	}

	
	// ------------------------ Methods Specific to the Simple CA Animation ------------------------ //


	/**
	 * A recursive method to take in a provided integer, rule number, and set the model's rule linked
	 * list to equal that integer's binary representation in base 2. 
	 * 
	 * E.g., setRule(10) will set the model's rule to { 0 > 0 > 0 > 0 > 1 > 0 > 1 > 0 }, as 10's
	 * binary representation is [1, 0, 1, 0]
	 * 
	 * Note: we add front leading 0s to the linked list up until 8 spaces because there are 256 possible 
	 * rules (0 - 255), or 2^8, and 255's binary representation is [1, 1, 1, 1, 1, 1, 1, 1], so 8 spaces
	 * is enough to hold the maximum rule's binary representation. 
	 * 
	 * @param int - ruleNum, the integer to convert to binary for this CA's rule
	 */
	public void setRule(int ruleNum) {

		// base case for recursion, if provided number is 0, we add 0's until length of linked list is 8
		if(ruleNum == 0) {
			while(this.rule.getLength(null) < 8) {
				this.rule.add(0);
			}
			return;
		} else {
			// procedure for converting a base 10 int to base 2, we:
			// 1) Add the remainder after division of our number by 2 to the linked list
			// 2) Make a recursive call, setting in the new number to be (initial num - remainder) / 2
			int remainder = ruleNum % 2;
			this.rule.add(remainder);
			setRule(((ruleNum - remainder) / 2));
		}
	}


	/**
	 * A method to find the index of our rule linked list based on three input states,
	 * a left, center, and right state. (this is also why our rule linked list is of length 8...
	 * with 3 states, each 0 or 1, there are 2 ^ 3 or 8 possible configurations). 
	 * 
	 * @param int - leftPos, the left most integer of a set of 3 in our CA's state array
	 * @param int - centerPos, the center integer of a set of 3 in our CA's state array
	 * @param int - rightPos, the right most integer of a set of 3 in our CA's state array
	 * 
	 * @return int - based on the provided state of 3, this method returns the integer we should use
	 * 				 to index the rule linked list, in order to find what the new state of the center
	 * 				 element should be after a change based on itself and its neighbors. 
	 */
	public int getRuleIndex(int leftPos, int centerPos, int rightPos) {

		// we need to convert a binary number back to an integer, so these 3 vars
		// will hold the squared, first power, and 0th power part required 
		// (left pos corresponds to squared part, center to 1st, and right to 0th)
		// Note: [1, 0, 1] = [1 * (2^2)] + [0 * (2^1)] + [1 * (2^0)] = 5
		int squaredPart = 0;
		int firstPart = 0;
		int zerothPart = 0;

		// only calculate the corresponding power if the given bit is a 1
		if(leftPos == 1) {
			squaredPart = (int) Math.pow(2, 2);
		}
		if(centerPos == 1) {
			firstPart = (int) Math.pow(2, 1);
		}
		if(rightPos == 1) {
			zerothPart = (int) Math.pow(2, 0);
		}

		// return the sum of all parts, this is the bin # converted back to int, for indexing rule list
		return squaredPart + firstPart + zerothPart;

	}


	/**
	 * This method calculates a new state based on the given CA's input state
	 * 
	 * @return int[] - a new array of integer's with the state after changes
	 */
	public int[] recalcState() {

		// create a new array of ints, same length as original
		int stateLength = currentState.length;
		int newState[] = new int[stateLength];

		// Edge Case: to calculate the very first element of array, we have to account for the fact
		// that there's no element left of 0, so we wrap around, using the last element of array
		int firstIndex = getRuleIndex(currentState[stateLength - 1], currentState[0], currentState[1]);
		newState[0] = rule.findNode(firstIndex).getData();

		// for bulk of the array we can iterate and call our function to change middle element based on rule
		for(int i = 1; i < (stateLength - 1); i++) {
			int index = getRuleIndex(currentState[i-1], currentState[i], currentState[i+1]);
			newState[i] = rule.findNode(index).getData();
		}

		// Other edge case, last element has no right neighbor, so wrap around & use 0
		int lastIndex = getRuleIndex(currentState[stateLength - 2], currentState[stateLength - 1], currentState[0]);
		newState[stateLength - 1] = rule.findNode(lastIndex).getData();

		return newState;

	}


	// --------------------------- Methods Specific to the Game Of Life ---------------------------- //


	/**
	 * This method uses our random number generator to randomize the initial game state
	 */
	public void randomizeBoard() {
		
		for(int i = 0; i < gameNumRows; i++) {
			
			int[] tempInitial = this.initialStateTable[i];
			
			// convert to list integer as easier for mapping to int
			List<Integer> converted = Arrays.stream(tempInitial).boxed().collect(Collectors.toList());
			
			// this logic maps every element to 0 or 1 based on result of the ternary
			List<Integer> result = converted.stream()
					.map((x) -> x = ((rand.nextInt(15) == 1) ? 1 : 0))
					.collect(Collectors.toList());
			
			// convert back to array as type of initial data
			int[] resultBackToArray = result.stream()
					.mapToInt((x) -> (x))
					.toArray();
			
			// store random row in the state table
			this.initialStateTable[i] = resultBackToArray;
		}
		
		// original code commented out below, this way was simpler to me but re-wrote to use
		// a higher order function per requirements.. I need to take tylenol to read the above code :-)
		/**
		for(int i = 0; i < gameNumRows; i++) {
			for(int j = 0; j < gameNumCols; j++) {
				if(rand.nextInt(15) == 1) {
					this.initialStateTable[i][j] = 1;
				} else {
					this.initialStateTable[i][j] = 0;
				}
			}
		}
		*/
	}


	/**
	 * Given any particular cell in the table, this method calculates the number of 
	 * neighbors around that cell (including the cell itself)
	 * 
	 * @param rowIndex - integer for which row the cell is in
	 * @param colIndex - integer for which col the cell is in
	 * @return int - returns the number of 'neighbors' around (and including) our cell
	 * 				 ... i.e., how many are 'alive' or have state of 1
	 */
	public int calc2DDensity(int rowIndex, int colIndex) {

		int numNeighbors = 0; 

		// loop around cell's immediate neighborhood, increment count for any states of 1
		for(int i = (rowIndex - 1); i < rowIndex + 2; i++) {
			for(int j = (colIndex - 1); j < colIndex + 2; j++) {
				if(initialStateTable[i][j] == 1) {
					numNeighbors += 1;
				}
			}
		}
		return numNeighbors;
	}


	/**
	 * This method returns a new state for a cell given its neighborhood
	 * 
	 * @param currentState - integer of the current cell being evaluated
	 * @param numNeighbors - integer of that cell's number of neighbors
	 * 
	 * @return int - what the cell's new state should be given its neighborhood
	 */
	public int get2DRule(int currentState, int numNeighbors) {

		switch(numNeighbors) {
		case 0:
			return 0; // 0 will always return 0, as if 0, no change, and if 1, die from loneliness :-(
		case 1:
			return 0; // same logic as 0
		case 3:
			return 1; // 3 is the condition for birth, so always return 1

			// ---------- For cases 5 - 9, always return 0 as 'dead' due to over-population :-( ---------- //
		case 5:
			return 0;
		case 6:
			return 0;
		case 7: 
			return 0;
		case 8: 
			return 0;
		case 9:
			return 0;
		default:
			return currentState; // for default state, these conditions result in no birth or death, so keep cell's state the same
		}
	}


	/**
	 * A method to calculate a new GOL state based on initial state
	 * 
	 * @return int[][] - a 2D array of integers with new states after changes
	 */
	public int[][] recalcGameState() {

		// use map method to make a new copy of the initial state
		int[][] copy = Arrays.stream(initialStateTable).map(int[]::clone).toArray(int[][]::new);

		// loop through all (excluding border edges) to find our new states after changes
		// note: doing this to not have to deal with the many edge cases where border cells
		//       do not have a full neighborhood
		for(int i = 1; i < (initialStateTable.length - 1); i++) {
			for(int j = 1; j < (initialStateTable[0].length - 1); j++) {

				// get the initial state, and see how many neighbors cell has
				int initialState = initialStateTable[i][j];
				int numNeighbors = calc2DDensity(i, j);

				// calculate what the cell should change to, then set that in our new 2D array
				int result = get2DRule(initialState, numNeighbors);
				copy[i][j] = result;
			}
		}
		return copy; // return final copy, to be new state
	}

	
	// --------------------------------- Basic Getters and Setters --------------------------------- //


		/**
		 * Getter for the current rule
		 * 
		 * @return - a LinkedList of type Integer, this model's current rule
		 */
		public LinkedList<Integer> getRule() {
			return this.rule;
		}


		/**
		 * Getter for the simple CA's base case
		 * 
		 * @return an integer array of all 0s, where only the middle element is 1
		 */
		public int[] getBaseCase() {
			return this.baseCase;
		}


		/**
		 * Getter for the simple CA's current state
		 * 
		 * @return an integer array containing the most recent state of the CA animation 
		 */
		public int[] getState() {
			return this.currentState;
		}


		/**
		 * Getter for the initial state of the GOL board 
		 * (may be first state, or initial state prior to a more recent change occurring)
		 * 
		 * @return a 2D array of integers; GOL's initial state
		 */
		public int[][] getInitialGOLBoard(){
			return this.initialStateTable;
		}


		/**
		 * Getter for the current state of the GOL boar
		 * 
		 * @return a 2D array of integers; GOL's current state
		 */
		public int[][] getCurrentGOLBoard(){
			return this.currentStateTable;
		}


		/**
		 * A method to set the simple CA's current state to the provided state
		 * 
		 * @param newState - an integer array representing the CA's new state after changes made
		 */
		public void setState(int[] newState) {
			this.currentState = newState;
		}


		/**
		 * A method to reset the current rule for the simple CA; sets it to a new empty LinkedList<Integer>
		 */
		public void resetRule() {
			this.rule = new LinkedList<Integer>();
		}


		/**
		 * A method to set the GOL's initial state board to the provided board
		 * 
		 * @param newBoard - a 2D array of integers
		 */
		public void setInitialGOLBoard(int[][] newBoard) {
			this.initialStateTable = newBoard;
		}


		/**
		 * A method to set the GOL's current state board to the provided board
		 * 
		 * @param newBoard - a 2D array of integers
		 */
		public void setCurrentGOLBoard(int[][] newBoard) {
			this.currentStateTable = newBoard;
		}

}

