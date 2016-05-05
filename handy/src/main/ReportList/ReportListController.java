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

package main.ReportList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ReportListController implements ControlledScreen {

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

    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }


    @FXML
    protected void handleReport1ButtonAction(ActionEvent event) throws SQLException {
        String query = "SELECT ToolID, AbbrDescription, RentalProfit, CostOfTool, " +
                "RentalProfit - CostOfTool AS TotalProfit " +
                "FROM ( SELECT t.ToolID, AbbrDescription, " +
                "IF( ( t.`DailyRentalCost` *SUM(DATEDIFF(r.EndDate, r.StartDate) ) ) IS NULL," +
                " 0, ( t.`DailyRentalCost` * SUM( DATEDIFF(r.EndDate, r.StartDate) ) ) ) AS RentalProfit, " +
                " SUM( `PurchasePrice` +`TotalServiceCost`) AS CostofTool FROM TOOLS t " +
                " LEFT JOIN RESERVATION_TOOL rt ON t.`ToolID` = rt.`ToolID` " +
                " LEFT JOIN RESERVATIONS r ON r.`ReservationNumber` =rt.`ReservationNumber` " +
                " GROUP BY t.`ToolID` ) AS X ORDER BY TotalProfit DESC";

        ResultSet rs = this.sqlStatement.executeQuery(query);

                int i = 0;
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Tool ID "), 0,0);
        gridPane.add(new Text(" Abbr. Description "), 1,0);
        gridPane.add(new Text(" Rental Profit "), 2,0);
        gridPane.add(new Text(" Cost of Tool  "), 3,0);
        gridPane.add(new Text(" Total Profit"), 4, 0);

        while (rs.next()){
            i++;
            gridPane.add(new Text(rs.getString("ToolID")),0,i);
            gridPane.add(new Text(rs.getString("abbrDescription") + "     "),1,i);
            gridPane.add(new Text(rs.getString("RentalProfit")),2,i);
            gridPane.add(new Text(rs.getString("CostOfTool")),3,i);
            gridPane.add(new Text(rs.getString("TotalProfit")),4,i);
        }
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setHeaderText("Monthly Rental Success");
        ScrollPane sp = new ScrollPane();
        sp.setContent(gridPane);
        dialog.getDialogPane().setContent(sp);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
            }
        });
    }

    @FXML
    protected void handleReport2ButtonAction(ActionEvent event) throws SQLException {

        String queryNew = "SELECT c.FirstName, c.LastName, c.Email, count(r.ReservationNumber)" +
                "as NumberOfRentals FROM customers as c JOIN reservations as r ON c.Email = r.CustomerEmail GROUP BY c.Email";

        ResultSet rs = this.sqlStatement.executeQuery(queryNew);

        int i = 0;
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Customer Name     "), 0, 0);
        gridPane.add(new Text("Customer Email    "), 1, 0);
        gridPane.add(new Text("Number Of Rentals"), 2, 0);

        while (rs.next()) {
            i++;
            gridPane.add(new Text(rs.getString("FirstName") + " " + rs.getString("LastName")), 0, i);
            gridPane.add(new Text(rs.getString("Email") + "     "), 1, i);
            gridPane.add(new Text(rs.getString("NumberOfRentals")), 2, i);

        }

            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setHeaderText("Monthly Customer History:");
            ScrollPane sp = new ScrollPane();

            sp.setContent(gridPane);
            dialog.getDialogPane().setContent(sp);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                }
            });
        }


    @FXML
    protected void handleReport3ButtonAction(ActionEvent event) {
       String query = "SELECT ID, EmpFirstName, EmpLastName, "
        + " COUNT(PickupClerk) as PickUpsHandled, COUNT(DropoffClerk) as DropoffsHandled, "
        + "COUNT(DropoffClerk) + COUNT(PickupClerk)as TotalPickupsDropoffs "
        + "FROM CLERKS c JOIN RESERVATIONS r ON (c.ID = r.DropoffClerk or c.ID = r.PickupClerk) " +
               "and (MONTH(r.`StartDate`)) = MONTH(NOW()) GROUP BY ID";


        try {
            ResultSet rs = this.sqlStatement.executeQuery(query);

            int i = 0;
            GridPane gridPane = new GridPane();
            gridPane.add(new Text("Clerk Name     "), 0, 0);
            gridPane.add(new Text("Total Pick-Ups    "), 1, 0);
            gridPane.add(new Text("Total Drop-Offs    "), 2, 0);
            gridPane.add(new Text("Sum"), 3, 0);

            while (rs.next()) {
                i++;
                gridPane.add(new Text(rs.getString("EmpFirstName") + " " + rs.getString("EmpLastName")), 0, i);
                gridPane.add(new Text(rs.getString("PickUpsHandled") + "     "), 1, i);
                gridPane.add(new Text(rs.getString("DropoffsHandled") + "     "), 2, i);
                gridPane.add(new Text(rs.getString("TotalPickupsDropoffs")), 3, i);

            }

            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setHeaderText("Monthly Clerk History:");
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


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}








