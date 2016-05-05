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

package main.PickUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.Main;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.*;

public class PickupController implements ControlledScreen {

    private Connection databaseConnection;
    private Statement sqlStatement;
    private String clerkID;
    private String reservationNumber;
    private ScreenChanger screenChanger;
    private double estimatedCost = 0.0;
    private double requiredDeposit = 0.0;
    private String clerkName;
    private String customerName;
    private String startDate;
    private String endDate;
    private List<String> tools = new ArrayList<>();

    @FXML private TextField toolID;
    @FXML private Text actiontarget;
    @FXML private Label reservationNumberText;
    @FXML private TextField expDate;
    @FXML private TextField ccNumber;
    @FXML private Label depositAmount;
    @FXML private Label estimatedCostLabel;

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
        clerkID = (String) args.get("clerkID");
        reservationNumber = (String) args.get("reservationNumber");
        String sqlModeQueryMode = "SET sql_mode = '';";
        String query = "SELECT c.FirstName, c.LastName, `EmpFirstName`, EmpLastName, r.`ReservationNumber` " +
                "FROM RESERVATIONS r " +
                "JOIN CUSTOMERS as c ON r.`CustomerEmail` = c.`Email` " +
                "JOIN CLERKS as clerks ON clerks.`ID` = " + clerkID + " " +
                "WHERE r.`ReservationNumber` = "+ reservationNumber + ";";
        try{
            this.sqlStatement.executeQuery(sqlModeQueryMode);
            ResultSet rs = this.sqlStatement.executeQuery(query);
            while (rs.next()) {
                clerkName = rs.getString("EmpFirstName") + " " + rs.getString("EmpLastName");
                customerName = rs.getString("FirstName") + " " + rs.getString("LastName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void setup(Parent parent) {
        try {
            setDatabaseConnection(Main.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT ReservationNumber, t.ToolID, t.AbbrDescription, t.DepositCost, " +
                "(t.DailyRentalCost * SUM(DATEDIFF(r.EndDate, r.StartDate))) as EstimatedRentalCost, r.EndDate, r.StartDate " +
                "FROM RESERVATIONS as r " +
                "INNER JOIN Reservation_Tool AS rt USING (ReservationNumber) " +
                "INNER JOIN TOOLS AS t USING (ToolID) " +
                "WHERE ReservationNumber = " + reservationNumber + " " + "GROUP BY t.ToolID;";
        ResultSet rs = null;
        GridPane gridPane = (GridPane) parent;
        GridPane listPane = null;

        for (Node node: gridPane.getChildren()) {
            if(node.getId() != null && node.getId().equalsIgnoreCase("toolsList")) {
                listPane = (GridPane) node;
            }
        }

        try {
            rs = this.sqlStatement.executeQuery(query);

            listPane.setHgap(10);
            int index = 0;
            while (rs.next()) {
                reservationNumberText.setText(String.valueOf(reservationNumber));
                estimatedCost += rs.getDouble("EstimatedRentalCost");
                requiredDeposit += rs.getDouble("DepositCost");
                startDate = rs.getString("StartDate");
                endDate = rs.getString("EndDate");

                String toolID = rs.getString("ToolID");
                listPane.add(new Text("" + (index + 1) + "."), 0, index);
                listPane.add(new Text(toolID), 1, index);
                listPane.add(new Text(rs.getString("abbrDescription")),2,index++);
                tools.add(toolID);
            }

            depositAmount.setText(currencyFormat(requiredDeposit));
            estimatedCostLabel.setText(currencyFormat(estimatedCost));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String currencyFormat(Double amount) {
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }

    @FXML protected void handleCompletePickupButtonAction(ActionEvent event) throws SQLException {
        if (ccNumber.getText().length() < 16 || expDate.getText().length() < 4) {

        }
        else {
            String query = "UPDATE RESERVATIONS SET CreditCardNumber = " + ccNumber.getText()
                    + ", CreditCardExpiry = " + expDate.getText() + ", PickupClerk = " + clerkID + ";";
            this.sqlStatement.execute(query);
            for (String toolID : tools) {
                this.sqlStatement.execute("UPDATE TOOLS SET AvailableToRent = FALSE WHERE ToolID = " + toolID + ";");
            }

            HashMap args = new HashMap();
            args.put("reservationNumber", reservationNumber);
            args.put("clerkName", clerkName);
            args.put("customerName", customerName);
            args.put("startDate", startDate);
            args.put("endDate", endDate);
            args.put("depositAmount", currencyFormat(requiredDeposit));
            args.put("estimatedCost", currencyFormat(estimatedCost));
            args.put("toolsList", tools);
            args.put("creditCardNumber", ccNumber.getText());

            screenChanger.loadScreen(ScreenChanger.RENTAL_CONTRACT, ScreenChanger.RENTAL_CONTRACT_FXML, args);
            screenChanger.setScreen(ScreenChanger.RENTAL_CONTRACT);
        }
    }

    @FXML protected void handleViewDetailsButtonAction(ActionEvent event) throws SQLException{
        ResultSet toolInfo = this.sqlStatement.executeQuery("SELECT * FROM TOOLS WHERE ToolID = " + toolID.getText() + ";");


        int i = 0;
        GridPane gridPane = new GridPane();

        gridPane.add(new Text("Tool ID: "), 0,1);
        gridPane.add(new Text("Abbr Description: "), 0,2);
        gridPane.add(new Text("Type Category: "), 0,3);
        gridPane.add(new Text("Deposit Cost: "), 0,4);
        gridPane.add(new Text("Daily Rental Cost: "), 0,5);
        gridPane.add(new Text("Purchase Price: "), 0,6);
        gridPane.add(new Text("Total Service Cost: "), 0,7);
        gridPane.add(new Text("Full Description: "), 0,8);




        while (toolInfo.next()){
            i++;
            gridPane.add(new Text(toolInfo.getString("toolID")),1,1);
            gridPane.add(new Text(toolInfo.getString("abbrDescription")),1,2);
            gridPane.add(new Text(toolInfo.getString("TypeCategory")),1,3);
            gridPane.add(new Text(toolInfo.getString("DepositCost")),1,4);
            gridPane.add(new Text(toolInfo.getString("DailyRentalCost")),1,5);
            gridPane.add(new Text(toolInfo.getString("PurchasePrice")),1,6);
            gridPane.add(new Text(toolInfo.getString("TotalServiceCost")),1,7);
            gridPane.add(new Text(toolInfo.getString("FullDescription")),1,8);

        }
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setHeaderText("Tool Details:");
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
}