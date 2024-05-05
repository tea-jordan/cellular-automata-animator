/******

Name: Tristan Jordan

CA Animator: Model Interface

Date: 4/15/2024

Notes / Interface Description: 
	   This interface outlines what kinds of things the general Model should be able to do.
	   For the general Cellular Automata model this includes methods related to simple CA rules,
	   and the 2D Game of Life. 
       
******/


public interface Model {

	/**
	 * Required methods for simple Cellular Automata (CA) model
	 */
	public void setRule(int ruleNum); // set the CA rule given a rule number
	public void resetRule(); // clear current CA rule
	public int getRuleIndex(int leftPos, int centerPos, int rightPos); // find rule index given 3 binary digits
	public void setState(int[] newState); // change CA state given a new state
	public int[] recalcState(); // calculate and return a new state based on current state
	
	/**
	 * Required methods for the Game of Life (GOL) model
	 */
	public void randomizeBoard(); // randomize a new board for GOL
	public void setInitialGOLBoard(int[][] newBoard); // setup a new initial state for GOL
	public void setCurrentGOLBoard(int[][] newBoard); // setup a new current state for GOL
	public int calc2DDensity(int rowIndex, int colIndex); // calculate density of a cell's neighborhood
	public int get2DRule(int currentState, int numNeighbors); // return new state given current state num neighbors
	public int[][] recalcGameState(); // calculate and return the next game state given current state
	
}

