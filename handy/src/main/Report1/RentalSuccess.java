package main.Report1;

import java.util.HashMap;
import java.util.Map;

//import a.j.T;
//import com.intellij.openapi.graph.view.tabular.TableGroupNodeRealizer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class RentalSuccess extends Application {

    public static final String Column1MapKey = "A";
    public static final String Column2MapKey = "B";
    public static final String Column3MapKey = "C";
    public static final String Column4MapKey = "D";
    public static final String Column5MapKey = "E";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Rental Success Report");
        stage.setWidth(950);
        stage.setHeight(500);

        final Label label = new Label("Rental Success");
        label.setFont(new Font("Arial", 20));

        TableColumn<Map, String> firstDataColumn = new TableColumn<>("NewTool ID");
        TableColumn<Map, String> secondDataColumn = new TableColumn<>("Abbr. Description");
        TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Rental Profit");
        TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Cost Of NewTool");
        TableColumn<Map, String> fifthDataColumn = new TableColumn<>("Total Profit");

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
        vbox.getChildren().addAll(label, tableView);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);

        stage.show();
    }

    private ObservableList<Map> generateDataInMap() {
        int max = 25;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            Map<String, String> dataRow = new HashMap<>();

            String value1 = "A" + i;
            String value2 = "B" + i;
            String value3 = "C" + i;
            String value4 = "D" + i;
            String value5 = "E" + i;

            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);
            dataRow.put(Column3MapKey, value3);
            dataRow.put(Column4MapKey, value4);
            dataRow.put(Column5MapKey, value5);

            allData.add(dataRow);
        }
        return allData;
    }
}