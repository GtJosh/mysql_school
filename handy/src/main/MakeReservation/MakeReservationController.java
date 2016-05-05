/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package main.MakeReservation;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MakeReservationController implements ControlledScreen {

    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;
    private String customerId;

    @Override
    public void setScreenParent(ScreenChanger screenPage) {
        this.screenChanger = screenPage;
    }

    @Override
    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
        //toolTypeComboBox.getSelectionModel().selectFirst();
        //handleToolTypeSelectedAction();
    }

    @Override
    public void setArguments(HashMap args) {
        customerId = (String) args.get("customerId");
        datepickerEnd.setValue(LocalDate.now());
        datepickerStart.setValue(LocalDate.now());
    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML private DatePicker datepickerStart;
    @FXML private DatePicker datepickerEnd;
    @FXML private ComboBox toolTypeComboBox;
    @FXML private ComboBox toolsComboBox;
    @FXML private VBox toolsSelectedVbox;
    private ArrayList<Integer> desiredToolIDs = new ArrayList<>();

    @FXML protected void handleToolTypeSelectedAction() {
        if (datepickerEnd.getValue() != null && datepickerStart.getValue() != null){
            System.out.println("Entering handle Tool Type Selected");
            System.out.println("Selected: " + toolTypeComboBox.getValue().toString());
            ResultSet availableTools = getAvailableTools();
            toolsComboBox.getItems().clear();
            try {
                while (availableTools.next()) {
                    String toolString = "";
                    toolString += availableTools.getString("toolID") + " ";
                    toolString += availableTools.getString("abbrDescription") + " $";
                    toolString += availableTools.getString("DailyRentalCost");
                    toolsComboBox.getItems().add(toolString);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML protected void handleAddToolButtonAction(ActionEvent event) {
        String addedTool = toolsComboBox.getValue().toString();
        String[] splitAddToolString = addedTool.split(" ");
        int addToolID = Integer.parseInt(splitAddToolString[0]);
        desiredToolIDs.add(addToolID);
        try {
            sqlStatement.execute("UPDATE Tools SET AvailableToRent = false WHERE ToolID=" + addToolID);
            toolsSelectedVbox.getChildren().add(new Text(addedTool));
            handleToolTypeSelectedAction();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML protected void handleRemoveLastToolButtonAction() {
        int toolIndex = toolsSelectedVbox.getChildren().size()-1;
        Text removedToolText = (Text) toolsSelectedVbox.getChildren().get(toolIndex);
        String removedToolString = removedToolText.getText();

        toolsSelectedVbox.getChildren().remove(toolIndex);
        String[] splitRemoveToolString = removedToolString.split(" ");
        try {
            sqlStatement.execute("UPDATE Tools SET AvailableToRent = true" +
                    " WHERE ToolID=" + splitRemoveToolString[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleCancelAndReturnButton(ActionEvent event) {
        while (toolsSelectedVbox.getChildren().size() > 0) {
            handleRemoveLastToolButtonAction();
        }
        returnToCustomerMainMenu();
    }

    @FXML protected void handleCalculateTotalButtonAction(ActionEvent event) {

        int totalRentalPrice = 0, totalDeposit = 0, numberOfDays;
        numberOfDays = (int) datepickerEnd.getValue().toEpochDay() - (int) datepickerStart.getValue().toEpochDay();
        VBox desiredTools = new VBox();
        int i = 0;
        for (Node toolNode : toolsSelectedVbox.getChildren()) {
            i++;
            int toolId = getToolIdFromNode(toolNode);
            try {
                ResultSet toolResult = sqlStatement.executeQuery("SELECT DepositCost, DailyRentalCost, " +
                        "AbbrDescription FROM Tools WHERE ToolID=" + toolId);
                toolResult.next();
                totalDeposit += toolResult.getInt("DepositCost");
                totalRentalPrice += numberOfDays * toolResult.getInt("DailyRentalCost");
                desiredTools.getChildren().add(new Text(i + ". " + toolResult.getString("AbbrDescription")));
            } catch (SQLException e) {
                System.out.println("Get tool for subtotal calc failed");
                e.printStackTrace();
            }
        }
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Tools Desired"), 0,0);
        gridPane.add(desiredTools, 0, 1, 2, 1);
        gridPane.add(new Text("Start Date " + datepickerStart.getValue().toString()), 0, 2); //format(DateTimeFormatter.BASIC_ISO_DATE)), 0, 2);
        gridPane.add(new Text("End Date " + datepickerEnd.getValue().toString()), 0, 3);
        gridPane.add(new Text("Deposit: " + totalDeposit), 0, 4);
        gridPane.add(new Text("Total Rental Price: $" + totalRentalPrice), 0, 5);

        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setHeaderText("Reservation Summary");
        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                createReservation();
            }
            else {
                return;
            }
        });

    }

    private int getToolIdFromNode(Node toolNode) {
        Text textNode = (Text) toolNode;
        String toolString = textNode.getText();
        String[] splitToolString = toolString.split(" ");
        return Integer.parseInt(splitToolString[0]);
    }

    private void createReservation() {

        try {
            sqlStatement.execute("INSERT INTO Reservations VALUES (0, '" +
                    datepickerStart.getValue().toString() + "', '" +
                    datepickerEnd.getValue().toString() + "', " +
                    "NULL, " +
                    "NULL," +
                    "NULL, " +
                    "NULL, '" +
                    customerId + "')"
            );
            ResultSet resNumResultSet = sqlStatement.executeQuery("SELECT LAST_INSERT_ID()");
            resNumResultSet.next();
            int reservationNumber = resNumResultSet.getInt("LAST_INSERT_ID()");
            for (Node toolNode : toolsSelectedVbox.getChildren()) {
                int toolId = getToolIdFromNode(toolNode);
                sqlStatement.execute("INSERT INTO Reservation_Tool VALUES " +
                        "(" + reservationNumber + ", " + toolId +")");
            }
            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setHeaderText("Reservation Created!");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    //Do nothing
                }
            });
            returnToCustomerMainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private ResultSet getAvailableTools(){

        String type = toolTypeComboBox.getValue().toString();
        System.out.println("start:" + datepickerStart.getValue().toString() + "\tend: "+
                datepickerEnd.getValue().toString());
        try {
            return sqlStatement.executeQuery("" +
                "SELECT" +
                " t.toolID, t.abbrDescription, t.DepositCost, t.DailyRentalCost, t.AvailableToRent " +
                "FROM " +
                "Tools AS t " +
                "LEFT JOIN Reservation_Tool AS rt USING (toolID)" +
                "LEFT JOIN Reservations AS r USING (ReservationNumber)" +
                "LEFT JOIN ServiceRequest AS s USING (toolID)" +
                "WHERE " +
                //Check type
                "t.TypeCategory = '" + type + "' " +
                //Check not sold
                "AND" +
                " t.AvailableToRent = true " +
                //Check not on existing reservation
                "AND (" +
                "r.ReservationNumber is null " +
                "OR " +
                "r.startDate > '" + datepickerEnd.getValue().toString() + "' " +
                "OR " +
                "r.endDate < '" + datepickerStart.getValue().toString() + "' " +
                ")" +
                //Check not on service request
                "AND (" +
                "s.startDate is null " +
                "OR " +
                "s.startDate > " + "'" + datepickerEnd.getValue().toString() + "' " +
                "OR "+
                "s.endDate < '" + datepickerStart.getValue().toString() + "'" +
                ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void returnToCustomerMainMenu(){
        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.CUSTOMER_MAIN_MENU, ScreenChanger.CUSTOMER_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CUSTOMER_MAIN_MENU);
    }

}
