/******

Name: Tristan Jordan

CA Animator: View Interface

Date: 4/15/2024

Notes / Interface Description: 
	   This interface outlines what kinds of things the View should be able to do.
       
******/


import java.awt.Color;
import java.awt.event.ActionListener;


public interface View {

	void setListeners(ActionListener clicks); // important method, must be able to set action listener on buttons
	
	public void addPixel(int x, int y, Color c); // add a "pixel" (5x5 JFrame) of given Color to given coords.
	
	public void updateDisplay(); // refresh displayed elements
	public void shiftDisplay(); // shift display up by a constant factor (pixelDimensions)
	public void clearDisplay(); // clear display completely
	
	public void initializeGOLPanels(); // create panels for GOL display
	public void setGOLPanelColor(int i, int j, Color c); // set a GOL panel to a specific color 
	public void displayGOLBoard(); // display the GOL panels
	
}

