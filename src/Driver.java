/******

Name: Tristan Jordan

CA Animator: Driver Class

Date: 4/15/2024

Notes / Class Description: 
	   This driver includes a main method to start the program. It initializes the
	   model and view, which are then passed to the controller to run the program. 
       
******/


public class Driver {

	public static void main(String[] args) {

		// create model and view, then pass these over to the controller
		CAModel model = new CAModel();
		CAView view = new CAView();
		CAController controller = new CAController(model, view);
		
		// call this method to await user choice from the controller
		controller.choiceMenu();
		
	}

}

