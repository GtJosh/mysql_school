package main.RentalReceipt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RentalReceipt extends Application {

    public static void main(String[] args) {
        Application.launch(RentalReceipt.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rentalReceipt.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        primaryStage.setTitle("RentalReceipt.htm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}