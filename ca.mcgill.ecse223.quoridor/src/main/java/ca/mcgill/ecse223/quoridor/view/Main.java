package ca.mcgill.ecse223.quoridor.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;



public class Main extends Application{

    public static void main (String[] args){
        launch(args);
    }

    private static Stage currentStage;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource("");
//        loader.setLocation(getClass().getResource("foo.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
//        Parent root = loader.load();
        stage.setTitle("Quoridor");
        stage.setScene(new Scene(root,600,400));
        stage.show();
        currentStage = stage;
    }

    public static void setCurrentStage(Stage aStage) {
        currentStage = aStage;
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }
}
/*

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
*/