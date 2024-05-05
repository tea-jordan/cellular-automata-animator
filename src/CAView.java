/******

Name: Tristan Jordan

CA Animator: CAView Class

Date: 4/15/2024

Notes / Class Description: 
	   This class implements our View interface for the CA animator. In addition to
	   this interface, the CAView class also extends JFrame for the GUI components. 

 ******/


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CAView extends JFrame implements View {

	// adding this serial version so that JFrame will not be cranky
	private static final long serialVersionUID = 1L;

	// declaring static variables that represent various dimensions for the view
	public static final int WIDTH = 1020;
	public static final int HEIGHT = 700;
	public static final int optionsWIDTH = 220;
	public static final int displayWIDTH = WIDTH - optionsWIDTH;
	public static final int pixelDimension = 5; 
	public static final int gameNumRows = 120;
	public static final int gameNumCols = 80;

	// attributes for the entire window, which will consist of two JPanels for the options & animation panel
	private JFrame window;
	private JPanel optionsPanel;
	private JPanel animationPanel;

	// this 2D array will store JPanels for the GOL animation
	private JPanel[][] gameOfLifePanels;

	// text field for inputting rule number
	private JTextField ruleInput;
	
	// JLabels for text messages in options panel
	private JLabel ruleHeaderText;
	private JLabel ruleMessage;
	private JLabel borderText;
	private JLabel gameOfLifeHeader;
	
	// JButtons for the simple CA rules
	private JButton saveRuleButton;
	private JButton startMeUp;
	private JButton stopRightThere;
	private JButton reset;

	// JButtons for the Game of Life
	private JButton generateGameOfLifeBoard;
	private JButton animateGameOfLife;
	private JButton pauseGameOfLife;
	private JButton removeBoard; 


	/**
	 * No arg constructor for the view
	 */
	public CAView() {

		// ----------------------- This Section Creates the Entire Window ----------------------- //

		window = new JFrame("Cellular Automata Simulator");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setSize(WIDTH, HEIGHT);
		window.setResizable(false); 
		window.setVisible(true);
		window.setLocationRelativeTo(null);


		// ----------------------- This Section Creates the Options Panel ----------------------- //
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setSize(optionsWIDTH, HEIGHT);
		optionsPanel.setLocation(0, 0);
		optionsPanel.setBackground(new Color(200, 200, 200));
		optionsPanel.setVisible(true);


		// ---------------------- This Section Creates the Animation Panel ---------------------- //
		animationPanel = new JPanel();
		animationPanel.setSize(WIDTH - optionsWIDTH, HEIGHT);
		animationPanel.setLocation(optionsWIDTH, 0);
		animationPanel.setLayout(null);
		animationPanel.setBackground(Color.white);
		animationPanel.setBackground(new Color(28, 22, 120));
		animationPanel.setVisible(true);

		
		// --------------- This Section Creates Specific Items w/in Options Panel --------------- //

		// header message text for input of CA rule
		ruleHeaderText = new JLabel();
		ruleHeaderText.setBounds(10, 5, optionsWIDTH-10, 80);
		optionsPanel.add(ruleHeaderText);
		ruleHeaderText.setFont(new Font("Monospaced", Font.BOLD, 14));
		ruleHeaderText.setText("<html>Enter a number between 0 - 255, inclusive. (defaults to 0 on invalid entry).</html>"); 
		//ruleHeaderText.setBackground(new Color(255, 255, 255)); // if we want to change background color
		//ruleHeaderText.setForeground(new Color(0, 0, 0)); // if we want to change font color
		//ruleHeaderText.setOpaque(true); // uncomment this to show background color

		// text field box where rule will be input
		ruleInput = new JTextField(4);
		ruleInput.setBounds(10,100,50,25);
		optionsPanel.add(ruleInput);
		ruleInput.setVisible(true);

		// button to save the rule input in text field
		saveRuleButton = new JButton("saveRule");
		optionsPanel.add(saveRuleButton);
		saveRuleButton.setBounds(70,100,140,26);
		saveRuleButton.setVisible(true);
		saveRuleButton.setFont(new Font("Monospaced", Font.ITALIC, 16));
		saveRuleButton.setFocusable(false);
		saveRuleButton.setText("Save rule.");

		// button to start the animation
		startMeUp = new JButton("startMeUp");
		optionsPanel.add(startMeUp);
		startMeUp.setBounds(10, 135, 200, 26);
		startMeUp.setVisible(true);
		startMeUp.setFont(new Font("Monospaced", Font.ITALIC, 16));
		startMeUp.setFocusable(false);
		startMeUp.setText("Animate rule.");

		// button to pause animation
		stopRightThere = new JButton("stopRightThere");
		optionsPanel.add(stopRightThere);
		stopRightThere.setBounds(10, 170, 200, 26);
		stopRightThere.setVisible(true);
		stopRightThere.setFont(new Font("Monospaced", Font.ITALIC, 16));
		stopRightThere.setFocusable(false);
		stopRightThere.setText("Pause animation.");

		// button to reset the screen
		reset = new JButton("reset");
		optionsPanel.add(reset);
		reset.setBounds(10, 205, 200, 26);
		reset.setVisible(true);
		reset.setFont(new Font("Monospaced", Font.ITALIC, 16));
		reset.setFocusable(false);
		reset.setText("Clear screen.");

		// header message text for input of CA rule
		ruleMessage = new JLabel();
		ruleMessage.setBounds(10, 230, optionsWIDTH-10, 80);
		optionsPanel.add(ruleMessage);
		ruleMessage.setForeground(new Color(22, 121, 171));
		ruleMessage.setFont(new Font("Monospaced", Font.BOLD, 15));
		ruleMessage.setText("<html>Check out rule numbers {91, 118, 169, 131, 105, and 193}!</html>"); 

		// text box to separate options panel into sections
		borderText = new JLabel();
		borderText.setBounds(10,275, optionsWIDTH-10, 80);
		optionsPanel.add(borderText);
		borderText.setForeground(new Color(130, 77, 116)); 
		borderText.setFont(new Font("Monospaced", Font.BOLD, 20));
		borderText.setText("-----------------"); 

		// Header text for the game of life
		gameOfLifeHeader = new JLabel();
		gameOfLifeHeader.setBounds(20, 310, optionsWIDTH-10, 80);
		optionsPanel.add(gameOfLifeHeader);
		gameOfLifeHeader.setForeground(new Color(190, 123, 114));
		gameOfLifeHeader.setFont(new Font("Monospaced", Font.BOLD, 22));
		gameOfLifeHeader.setText("<html>Conway's Game of Life</html>");

		// button to generate new board
		generateGameOfLifeBoard = new JButton("genBoardGOL");
		optionsPanel.add(generateGameOfLifeBoard);
		generateGameOfLifeBoard.setBounds(10, 390, 200, 26);
		generateGameOfLifeBoard.setVisible(true);
		generateGameOfLifeBoard.setFont(new Font("Monospaced", Font.ITALIC, 16));
		generateGameOfLifeBoard.setFocusable(false);
		generateGameOfLifeBoard.setText("Create new board.");

		// button to start the game animation
		animateGameOfLife = new JButton("animateGOL");
		optionsPanel.add(animateGameOfLife);
		animateGameOfLife.setBounds(10, 425, 200, 26);
		animateGameOfLife.setVisible(true);
		animateGameOfLife.setFont(new Font("Monospaced", Font.ITALIC, 16));
		animateGameOfLife.setFocusable(false);
		animateGameOfLife.setText("Animate game.");

		// button to start the animation
		pauseGameOfLife = new JButton("pauseGOL");
		optionsPanel.add(pauseGameOfLife);
		pauseGameOfLife.setBounds(10, 460, 200, 26);
		pauseGameOfLife.setVisible(true);
		pauseGameOfLife.setFont(new Font("Monospaced", Font.ITALIC, 16));
		pauseGameOfLife.setFocusable(false);
		pauseGameOfLife.setText("Pause game.");

		// button to start the animation
		removeBoard = new JButton("removeGOLBoard");
		optionsPanel.add(removeBoard);
		removeBoard.setBounds(10, 495, 200, 26);
		removeBoard.setVisible(true);
		removeBoard.setFont(new Font("Monospaced", Font.ITALIC, 16));
		removeBoard.setFocusable(false);
		removeBoard.setText("Delete board.");

		// initializing the game of life array to store JPanels
		gameOfLifePanels = new JPanel[gameNumRows][gameNumCols];
		initializeGOLPanels();

		// adding everything to the window & calling repaint() to update display
		window.add(optionsPanel);
		window.add(animationPanel);
		window.repaint();

	}


	// ---------------------------------- Display Related Methods ---------------------------------- //
	
	
	/**
	 * This method initializes all panels for the game of life, then stores them in the 2D array
	 */
	public void initializeGOLPanels() {
		for(int i = 0; i < gameNumRows; i++) {
			for(int j = 0; j < gameNumCols; j++) {
				JPanel panel = new JPanel();
				panel.setSize(pixelDimension, pixelDimension);
				panel.setLocation(100 + (i*pixelDimension), 115 + (j*pixelDimension));
				panel.setBackground(Color.white);
				gameOfLifePanels[i][j] = panel;
			}
		}
	}


	/**
	 * This method can be called to change any panel color for the game of life panels
	 * 
	 * @param i - integer representing the row index of panel to change
	 * @param j - integer representing the col index of panel to change
	 * @param c - Color object with the new color to set
	 */
	public void setGOLPanelColor(int i, int j, Color c) {
		this.gameOfLifePanels[i][j].setBackground(c);
	}


	/**
	 * Method to display game of life panels, loops to add each panel to the animation panel.
	 */
	public void displayGOLBoard() {
		for(int i = 0; i < gameNumRows; i++) {
			for(int j = 0; j < gameNumCols; j++) {
				animationPanel.add(gameOfLifePanels[i][j]);
			}
		}
	}


	/**
	 * Method to add a pixel anywhere to the board 
	 * Note: what I'm calling a "pixel" is really a JPanel with wider dimensions so that 
	 * 	     less have to be stored / animated
	 * 
	 * @param x - integer with the x coordinate for the top-left corner of the panel to be set
	 * @param y - integer with the y coordinate for the top-left corner of the panel to be set
	 * @param c - a Color object for the desired color of the newly created panel
	 */
	public void addPixel(int x, int y, Color c) {
		JPanel panel = new JPanel();
		panel.setSize(pixelDimension, pixelDimension);
		panel.setLocation(x, y);
		panel.setBackground(c);
		animationPanel.add(panel);
	}


	/**
	 * A method to update the GUI's display. Swing's revalidate() and repaint() are called
	 * to check all components on the display, and redraw if necessary. 
	 */
	public void updateDisplay() {
		animationPanel.revalidate();
		animationPanel.repaint();
	}


	/**
	 * A method to shift displayed elements by a constant (pixelDimension)
	 */
	public void shiftDisplay() {

		// get a component array of all elements on the animation panel
		Component[] elements = animationPanel.getComponents();

		// note: lowest y will always be at beginning of array, so quick check to 
		// remove any that are out of the display area
		for(Component c : elements) {
			if(c.getY() > 0) {
				break;
			}
			else if(c.getY() < 0) {
				animationPanel.remove(c);
			}
		}

		// once elements outside of display removed, shift the starting y-coord of every other
		// element up by the "pixel" dimension - this is done this way so that the new row of 
		// simple CA can be printed at the bottom. 
		for(Component c : elements) {
			c.setLocation(c.getX(), c.getY() - pixelDimension);
		}

	}


	/**
	 * If needed, a method to clear the entire display. Removes all elements from
	 * animation panel, then calls revalidate() and repaint()
	 */
	public void clearDisplay() {
		animationPanel.removeAll();
		animationPanel.revalidate();
		animationPanel.repaint();
	}


	// --------------------------- Method for Action Listeners on Buttons --------------------------- //
	
	/**
	 * A method to set listeners to all buttons, provided a given listener. 
	 * For this MVC setup, the controller will be passed in as the listener. 
	 */
	@Override
	public void setListeners(ActionListener clicks) {
		// CA animation buttons
		saveRuleButton.addActionListener(clicks);
		startMeUp.addActionListener(clicks);
		stopRightThere.addActionListener(clicks);
		reset.addActionListener(clicks);

		// GOL buttons
		generateGameOfLifeBoard.addActionListener(clicks);
		animateGameOfLife.addActionListener(clicks);
		pauseGameOfLife.addActionListener(clicks);
		removeBoard.addActionListener(clicks);
	}


	// --------------------------------- Basic Getters and Setters --------------------------------- //
	
	
	/**
	 * Getter for the button to create GOL board
	 * 
	 * @return - JButton
	 */
	public JButton getGenerateGOLButton() {
		return this.generateGameOfLifeBoard;
	}


	/**
	 * Getter for the button to animate GOL 
	 * 
	 * @return - JButton
	 */
	public JButton getAnimateGameOfLife() {
		return this.animateGameOfLife;
	}


	/**
	 * Getter for the button to pause GOL animation
	 * 
	 * @return - JButton
	 */
	public JButton getPauseGameOfLife() {
		return this.pauseGameOfLife;
	}


	/**
	 * Getter for the button to remove GOL board
	 * 
	 * @return - JButton
	 */
	public JButton getRemoveGOLBoard() {
		return this.removeBoard;
	}


	/**
	 * Getter for the button to save rule text from input box
	 * 
	 * @return - JButton
	 */
	public JButton getSaveRuleButton() {
		return this.saveRuleButton;
	}

	
	/**
	 * Getter for the text from the rule input field
	 * 
	 * @return - String - text of number entered
	 */
	public String getLastRuleText() {
		return this.ruleInput.getText();
	}


	/**
	 * Getter for the button to start CA animation
	 * 
	 * @return - JButton
	 */
	public JButton getStartButton() {
		return this.startMeUp;
	}


	/**
	 * Getter for the button to pause CA animation
	 * 
	 * @return - JButton
	 */
	public JButton getPauseButton() {
		return this.stopRightThere;
	}

	
	/**
	 * Getter for the button to reset the CA animation
	 * 
	 * @return - JButton
	 */
	public JButton getResetButton() {
		return this.reset;
	}

}

