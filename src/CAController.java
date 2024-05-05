/******

Name: Tristan Jordan

CA Animator: CAController Class

Date: 4/15/2024

Notes / Class Description: 
	   This class is our controller for the program. It implements two interfaces:
	    	1) Controller - to require the methods we need the controller to handle
	    	2) ActionListener - to handle what to do when a button is pressed in the view. 

 ******/


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CAController implements Controller, ActionListener {

	// getting static dimension vars to be used for animation methods
	private final static int pixelDimension = CAView.pixelDimension;
	private final static int gameNumRows = CAView.gameNumRows;
	private final static int gameNumCols = CAView.gameNumCols;

	// the controller must have an instance of the model and the view
	private CAModel caModel;
	private CAView caView;

	// other vars to store last selected data, and bools to help determine when to animate
	private int lastSelectedRule;
	private boolean startSimpleAnimation;
	private boolean gameBoardGenerated;
	private boolean startGameOfLife;


	/**
	 * Constructor for the controller
	 * 
	 * @param caModel - an instance of a CAModel
	 * @param caView - an instance of a CAView
	 */
	public CAController(CAModel caModel, CAView caView) {

		// initialize vars & set defaults for tracking vars
		this.caModel = caModel;
		this.caView = caView;
		this.lastSelectedRule = -1; 
		this.startSimpleAnimation = false;
		this.gameBoardGenerated = false;
		this.startGameOfLife = false;

		// important, upon construction call the view's set listeners method, giving
		// the controller (this) as the action listener
		caView.setListeners(this);

	}


	// --------------------------------- Menu & Animation Methods ---------------------------------- //


	/**
	 * A method to loop until a choice is selected for animation
	 */
	public void choiceMenu() {

		while(true) {
			// animate simple CA rule if button selected
			if(this.startSimpleAnimation == true) {
				animateSimpleRule();
			}

			// animate GOL if button selected
			if(this.startGameOfLife == true) {
				animateGameOfLife();
			}

			// else sleep & await selection
			sleep(100);
		}
	}


	/**
	 * This method animates the simple CA rules
	 */
	@Override
	public void animateSimpleRule() {

		// If called prior to rule selection, wait until valid value selected
		while(lastSelectedRule < 0) {
			sleep(100);
		}

		// If rule has not yet been set, set the selected rule in the model
		if(caModel.getRule().getLength(null) == 0) {
			caModel.setRule(lastSelectedRule);
		}

		// Start off by drawing the current state
		drawState();

		// As long as animate is true, call the view to shift & update display, 
		// then call the model to reset the state & re-draw
		while(startSimpleAnimation == true) {
			sleep(100);
			caView.shiftDisplay();
			caView.updateDisplay();
			caModel.setState(caModel.recalcState());
			drawState();
		}

	}


	/**
	 * This method animates the Game of Life
	 */
	@Override
	public void animateGameOfLife() {

		// if called before board was generated, generate a board
		if(!gameBoardGenerated) {
			generateGOLBoard();
		}

		// then as long as animation var is true
		while(startGameOfLife == true) {

			// slight delay
			sleep(175);

			// use the model to re-calc the game state, and set that to current state
			int[][] newState = caModel.recalcGameState();
			caModel.setCurrentGOLBoard(newState);

			// draw the board, then use the View to update display
			drawGOLBoard();
			caView.updateDisplay();

			// before next iteration of loop, set the newly generated "current" state to next cycle's initial state
			caModel.setInitialGOLBoard(newState);

		}

	}


	// ----------------------------------- ActionListener Method ------------------------------------ //


	/**
	 * This method is required by the ActionListener interface, specifies what to do 
	 * when an action event is detected (i.e., button press). 
	 * 
	 * @param ActionEvent e, the detected event (for our case, only buttons being pressed). 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// if the source is the save rule button, grab text and try converting to integer
		// if invalid, just set to 0
		if(e.getSource() == caView.getSaveRuleButton()) {
			try {
				lastSelectedRule = Integer.valueOf(caView.getLastRuleText());
				if(lastSelectedRule > 255 || lastSelectedRule < 0) {
					lastSelectedRule = 0;
				}
			}
			catch(NumberFormatException nfe) {
				lastSelectedRule = 0;
			}
		}

		
		// if the source is the start CA button, we want to flip the controller's bool var to true
		if(e.getSource() == caView.getStartButton()) {
			this.startSimpleAnimation = true;
		}

		
		// if the source is pause CA button, flip bool back to false
		if(e.getSource() == caView.getPauseButton()) {
			this.startSimpleAnimation = false;
		}

		
		// if reset selected...
		if(e.getSource() == caView.getResetButton()) {
			
			this.startSimpleAnimation = false; // stop animation
			sleep(225); // slight delay

			// clear the view's display, then reset the model's state & rule
			caView.clearDisplay();
			caModel.setState(caModel.getBaseCase());
			caModel.resetRule();
		}


		// if generate GOL selected, create a new board then flip bool to true
		if(e.getSource() == caView.getGenerateGOLButton()) {
			generateGOLBoard();
			this.gameBoardGenerated = true;
		}


		// if animate GOL selected, flip bool var
		if(e.getSource() == caView.getAnimateGameOfLife()) {
			this.startGameOfLife = true;
		}


		// if pause GOL selected, flip back to false
		if(e.getSource() == caView.getPauseGameOfLife()) {
			this.startGameOfLife = false;
		}


		// if remove board selected, we want to reset both vars to false and clear the display
		if(e.getSource() == caView.getRemoveGOLBoard()) {
			this.startGameOfLife = false;
			this.gameBoardGenerated = false;
			caView.clearDisplay();
		}

	}


	// -------------------------------------- Helper Methods --------------------------------------- //
	

	/**
	 * A helper method so that Thread.sleep can be called from various places w/o needing 
	 * the try/catch block in each spot 
	 * 
	 * @param sleepTime - int representing how long (ms) to wait
	 */
	public void sleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * A method to draw the state for the simple CA rules
	 */
	public void drawState() {
		
		// grab the state, and then loop through, if int is == 1, draw on bottom of screen
		int[] currentState = this.caModel.getState();

		for(int i = 0; i < currentState.length; i++) {
			if(currentState[i] == 1) {
				// y defaulted to 660 as we are always drawing on bottom of display, shifting rest up
				caView.addPixel(i * pixelDimension, 660, new Color(133, 118, 255));
			}
		}
	}


	/**
	 * A method to generate a new GOL board
	 */
	public void generateGOLBoard() {
		
		// have the model randomize its board, then fetch this initial matrix
		caModel.randomizeBoard();
		int[][] board = caModel.getInitialGOLBoard();

		// loop through all elements, and use the View to set panel colors based on values
		for(int i = 0; i < gameNumRows; i++) {
			for(int j = 0; j < gameNumCols; j++) {
				if(board[i][j] == 1) {
					caView.setGOLPanelColor(i, j, Color.black);
				} else {
					caView.setGOLPanelColor(i, j, Color.white);
				}
			}
		}

		// call the view's methods to display and update after panel colors set
		caView.displayGOLBoard();
		caView.updateDisplay();
	}


	/**
	 * A method to draw to the GOL board
	 */
	public void drawGOLBoard(){

		// grab both the initial and current state of the board
		int[][] priorState = caModel.getInitialGOLBoard();
		int[][] currentState = caModel.getCurrentGOLBoard();

		// loop through every element in the table...
		for(int i = 0; i < gameNumRows; i++) {
			for(int j = 0; j < gameNumCols; j++) {

				/**
				 * We will use the view's method to set panel colors. For the GOL
				 * we want to draw in different ways based on the change between prior & current state:
				 * 		1) If cell goes from 0 -> 0, keep display white
				 * 		2) If cell goes from 0 -> 1, this represents 'birth', draw with a reddish color
				 * 		3) If cell goes from 1 -> 1, this is alive, so keep display black
				 * 		4) If cell goes from 1 -> 0, this represents 'death', draw with a bluish color
				 */
				if((priorState[i][j] == 0) && (currentState[i][j] == 0)) {
					caView.setGOLPanelColor(i, j, Color.white);
				} else if((priorState[i][j] == 0) && (currentState[i][j] == 1)) {
					caView.setGOLPanelColor(i, j, new Color(255, 167, 50));
				} else if((priorState[i][j] == 1) && (currentState[i][j] == 1)) {
					caView.setGOLPanelColor(i, j, Color.black);
				} else if((priorState[i][j] == 1) && (currentState[i][j] == 0)) {
					caView.setGOLPanelColor(i, j, new Color(133, 29, 176));
				}
			}
		}
	}

	
}

