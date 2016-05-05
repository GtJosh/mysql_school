package main.PickUpResNumber;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PickUpResNumber extends Application {

    public static void main(String[] args) {
        Application.launch(PickUpResNumber.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pickUpResNumber.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        primaryStage.setTitle("PickUp-ReservationNumber.htm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
