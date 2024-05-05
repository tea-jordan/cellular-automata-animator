/******

Name: Tristan Jordan

CA Animator: Controller Interface

Date: 4/15/2024

Notes / Interface Description: 
	   This interface outlines what kinds of things our controller should be able to do. 
	   Generally the controller must:
		   1) Offer a choice menu and handle selections from that menu
		   2) animate a simple CA rule
		   3) animate Conway's Game Of Life
       
******/


public interface Controller {

	void choiceMenu();
	void animateSimpleRule();
	void animateGameOfLife();
	
}

