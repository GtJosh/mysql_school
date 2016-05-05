package main.DropOffResNumber;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DropOffResNumber extends Application {

    public static void main(String[] args) {
        Application.launch(DropOffResNumber.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dropOffResNumber.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        primaryStage.setTitle("DropOff-ReservationNumber.htm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
