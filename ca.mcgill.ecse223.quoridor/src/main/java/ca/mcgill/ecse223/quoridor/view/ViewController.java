package ca.mcgill.ecse223.quoridor.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.io.IOException;

public abstract class ViewController {

    public void closePage(Stage stage){
        stage.close();
    }
    public void popUpWindow(String path){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource(path)));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Quit Game");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e){
            System.out.println("Error");

        }
    }

    public void changePage(String path){
        try {
            Stage currentStage = Main.getCurrentStage();

            Parent page = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(page);
            currentStage.setScene(scene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
