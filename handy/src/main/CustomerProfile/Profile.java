package main.CustomerProfile;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.*;

import java.util.HashMap;
import java.util.Map;


public class Profile extends Application {

    public static final String Column1MapKey = "A";
    public static final String Column2MapKey = "B";
    public static final String Column3MapKey = "C";
    public static final String Column4MapKey = "D";
    public static final String Column5MapKey = "E";
    public static final String Column6MapKey = "F";
    public static final String Column7MapKey = "G";
    public static final String Column8MapKey = "H";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Rental Success Report");
        stage.setWidth(950);
        stage.setHeight(1000);

        GridPane gp = new GridPane();

        Text t1 = new Text("Profile");
        t1.setFont(new Font(32));

        //TODO: Define variables for results from SQL
        //TODO: Place variables in text field similar to below but with gp.add(x, 2, x)

        Text t2 = new Text("Email Address:");
        t2.setFont(new Font(18));

        Text t3 = new Text("Name:");
        t3.setFont(new Font(18));

        Text t4 = new Text("Home Phone:");
        t4.setFont(new Font(18));

        Text t5 = new Text("Work Phone:");
        t5.setFont(new Font(18));

        Text t6 = new Text("Address:");
        t6.setFont(new Font(18));

        Text t7 = new Text("Reservation History");
        t7.setFont(new Font(22));

        Text t8 = new Text("*********************************");
        t8.setFont(new Font(18));

        Line line = new Line(0.0f, 0.0f, 900.0f, 0.0f);
        Line line1 = new Line(0.0f, 0.0f, 900.0f, 0.0f);

        Text blank1 = new Text("");
        Text blank2 = new Text("");
        Text blank3 = new Text("");


        gp.add(t1, 1, 1);
        gp.add(blank1, 1, 2);
        gp.add(line, 1, 3);
        gp.add(blank2, 1, 4);
        gp.add(t2, 1, 5);
        gp.add(t3, 1, 6);
        gp.add(t4, 1, 7);
        gp.add(t5, 1, 8);
        gp.add(t6, 1, 9);
        gp.add(blank3, 1, 10);
        gp.add(line1, 1, 11);
        gp.add(t7, 1, 12);
        gp.add(t8, 1, 13);


        TableColumn<Map, String> firstDataColumn = new TableColumn<>("Res #");
        TableColumn<Map, String> secondDataColumn = new TableColumn<>("Tools");
        TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Start");
        TableColumn<Map, String> fourthDataColumn = new TableColumn<>("End");
        TableColumn<Map, String> fifthDataColumn = new TableColumn<>("Rental Price");
        TableColumn<Map, String> sixthDataColumn = new TableColumn<>("Deposit");
        TableColumn<Map, String> seventhDataColumn = new TableColumn<>("Pick-Up Clerk");
        TableColumn<Map, String> eighthDataColumn = new TableColumn<>("Drop-Off Clerk");

        firstDataColumn.setCellValueFactory(new MapValueFactory(Column1MapKey));
        firstDataColumn.setMinWidth(130);
        secondDataColumn.setCellValueFactory(new MapValueFactory(Column2MapKey));
        secondDataColumn.setMinWidth(400);
        thirdDataColumn.setCellValueFactory(new MapValueFactory(Column3MapKey));
        thirdDataColumn.setMinWidth(130);
        fourthDataColumn.setCellValueFactory(new MapValueFactory(Column4MapKey));
        fourthDataColumn.setMinWidth(130);
        fifthDataColumn.setCellValueFactory(new MapValueFactory(Column5MapKey));
        fifthDataColumn.setMinWidth(130);
        sixthDataColumn.setCellValueFactory(new MapValueFactory(Column6MapKey));
        sixthDataColumn.setMinWidth(130);
        seventhDataColumn.setCellFactory(new MapValueFactory(Column7MapKey));
        seventhDataColumn.setMinWidth(130);
        eighthDataColumn.setCellFactory(new MapValueFactory(Column8MapKey));
        eighthDataColumn.setMinWidth(130);



        TableView tableView = new TableView<>(generateDataInMap());

        tableView.setEditable(true);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn, fourthDataColumn, fifthDataColumn);
        Callback<TableColumn<Map, String>, TableCell<Map, String>>
                cellFactoryForMap = (TableColumn<Map, String> p) ->
                new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
        firstDataColumn.setCellFactory(cellFactoryForMap);
        secondDataColumn.setCellFactory(cellFactoryForMap);
        thirdDataColumn.setCellFactory(cellFactoryForMap);
        fourthDataColumn.setCellFactory(cellFactoryForMap);
        fifthDataColumn.setCellFactory(cellFactoryForMap);

        final VBox vbox = new VBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(gp, tableView);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);

        stage.show();
    }

    private ObservableList<Map> generateDataInMap() {
        int max = 5;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            Map<String, String> dataRow = new HashMap<>();

            String value1 = "A" + i;
            String value2 = "B" + i;
            String value3 = "C" + i;
            String value4 = "D" + i;
            String value5 = "E" + i;
            String value6 = "F" + i;
            String value7 = "G" + i;
            String value8 = "H" + i;

            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);
            dataRow.put(Column3MapKey, value3);
            dataRow.put(Column4MapKey, value4);
            dataRow.put(Column5MapKey, value5);
            dataRow.put(Column6MapKey, value6);
            dataRow.put(Column7MapKey, value7);
            dataRow.put(Column8MapKey, value8);

            allData.add(dataRow);
        }
        return allData;
    }
}