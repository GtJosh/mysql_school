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

package main.CheckAvailability;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.ResultSet;
import java.sql.Statement;

public class  CheckAvailabilityController implements ControlledScreen {

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
    }

    @Override
    public void setArguments(HashMap args) {
        customerId = (String) args.get("customerId");
    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML private RadioButton handToolsRadioButton;
    @FXML private RadioButton constructionEquipRadioButton;
    @FXML private RadioButton powerToolsRadioButton;
    @FXML private TextField checkAvailabilityStartDate;
    @FXML private TextField checkAvailabilityEndDate;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws SQLException {
        String type;
        if (handToolsRadioButton.isSelected()){
            type = "Hand Tools";
        }
        else if (constructionEquipRadioButton.isSelected()){
            type = "Construction Equipment";
        }
        else if (powerToolsRadioButton.isSelected()) {
            type = "Power Tools";
        }
        else {
            type = "";
        }
        System.out.println("start:" + checkAvailabilityStartDate.getText() + "\tend: "+ checkAvailabilityEndDate.getText());
        ResultSet availableTools =
        sqlStatement.executeQuery("" +
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
                        "r.startDate > '" + checkAvailabilityEndDate.getText() + "' " +
                        "OR " +
                        "r.endDate < '" + checkAvailabilityStartDate.getText() + "' " +
                    ")" +
                    //Check not on service request
                    "AND (" +
                        "s.startDate is null " +
                        "OR " +
                        "s.startDate > " + "'" + checkAvailabilityEndDate.getText() + "' " +
                        "OR "+
                        "s.endDate < '" + checkAvailabilityStartDate.getText() + "'" +
                    ")");


//        Stage stage = new Stage();
//        ToolAvailability ta = new ToolAvailability();
//
//        ta.start(stage);

        String availableToolsString = "Tool ID\tDesc\tDeposit\tPrice/Day";

//        String availableToolsString = "Tool ID\tDesc\t\t\t\tDeposit\t\tPrice/Day";
        int i = 0;
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Tool ID "), 0,0);
        gridPane.add(new Text(" Description "), 1,0);
        gridPane.add(new Text(" Deposit "), 2,0);
        gridPane.add(new Text(" Price/Day"), 3,0);


        while (availableTools.next()){
            i++;
            gridPane.add(new Text(availableTools.getString("toolID")),0,i);
            gridPane.add(new Text(availableTools.getString("abbrDescription")),1,i);
            gridPane.add(new Text(availableTools.getString("DepositCost")),2,i);
            gridPane.add(new Text(availableTools.getString("DailyRentalCost")),3,i);
            //availableToolsString += "\n" +availableTools.getString("toolID") + "\t\t" +
//                    availableTools.getString("abbrDescription") + "\t\t" +
//                    availableTools.getString("DepositCost") + "\t\t\t" +
//                    availableTools.getString("DailyRentalCost");
        }
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setHeaderText("Available Tools:");
        //dialog.setContentText(availableToolsString);
        ScrollPane sp = new ScrollPane();

        sp.setContent(gridPane);
        //sp.setContent(new Text(availableToolsString));
        dialog.getDialogPane().setContent(sp);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
            }
        });
    }

    @FXML protected void handleReturnButtonAction(ActionEvent event) {
        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.CUSTOMER_MAIN_MENU, ScreenChanger.CUSTOMER_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CUSTOMER_MAIN_MENU);
    }

}
