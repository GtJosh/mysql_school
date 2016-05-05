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

package main.RentalContract;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.Main;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class RentalContractController implements ControlledScreen {
    @FXML private Text actiontarget;
    private Connection databaseConnection;
    private Statement sqlStatement;
    private String clerkID;
    private String reservationNumber;
    private ScreenChanger screenChanger;
    private String estimatedCost;
    private String requiredDeposit;
    private String clerkName;
    private String customerName;
    private String startDate;
    private String endDate;
    private String creditCardNumber;
    private List<String> tools;

    //FXML Fields
    @FXML private Label reservationNumberLabel;
    @FXML private Label clerkNameLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label creditCardNumberLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;
    @FXML private Label depositLabel;
    @FXML private Label estimatedRental;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {

    }

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
        clerkName = (String) args.get("clerkName");
        customerName = (String) args.get("customerName");
        startDate = (String) args.get("startDate");
        endDate = (String) args.get("endDate");
        requiredDeposit = (String) args.get("depositAmount");
        estimatedCost = (String) args.get("estimatedCost");
        tools = (List<String>) args.get("toolsList");
        reservationNumber = (String) args.get("reservationNumber");
        creditCardNumber = (String) args.get("creditCardNumber");
    }

    @Override
    public void setup(Parent parent) {
        try {
            setDatabaseConnection(Main.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GridPane gridPane = (GridPane) parent;
        GridPane listPane = null;
        ResultSet rs = null;

        for (Node node: gridPane.getChildren()) {
            if(node.getId() != null && node.getId().equalsIgnoreCase("toolsList")) {
                listPane = (GridPane) node;
            }
        }

        try {
            int index = 0;
            for (String id: tools) {
                String query = "SELECT ToolID, AbbrDescription FROM TOOLS WHERE ToolID = " + id + ";";
                rs = this.sqlStatement.executeQuery(query);
                while (rs.next()) {
                    listPane.add(new Text("" + (index + 1) + ".\t"), 0, index);
                    listPane.add(new Text(rs.getString("toolID") + "\t"), 1, index);
                    listPane.add(new Text(rs.getString("abbrDescription")),2,index++);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        reservationNumberLabel.setText(reservationNumber);
        customerNameLabel.setText(customerName);
        clerkNameLabel.setText(clerkName);
        startDateLabel.setText(startDate);
        endDateLabel.setText(endDate);
        depositLabel.setText(requiredDeposit);
        estimatedRental.setText(estimatedCost);
        creditCardNumberLabel.setText(creditCardNumber);

    }

    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        HashMap args = new HashMap();
        args.put("clerkId", clerkID);

        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);

    }
}
