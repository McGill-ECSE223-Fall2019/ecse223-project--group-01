package ca.mcgill.ecse223.quoridor.view;

import javafx.event.ActionEvent;

public class ReplayModeController extends ViewController{
	
	private int stepNumber = 0;
	
	public void initialize() {
		
	}
	
	public void handleForwardStep(ActionEvent actionEvent) {
		
	}
	
	public void handleBackStep(ActionEvent actionEvent) {
		
	}
	
	public void handleStartSkip(ActionEvent actionEvent) {
		
	}
	
	public void handleEndSkip(ActionEvent actionEvent) {
		
	}
	
    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
}
