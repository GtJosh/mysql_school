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

package main.DropOff;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.ControlledScreen;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import main.Main;
import main.ScreenChanger;
import java.util.HashMap;


public class DropOffController implements ControlledScreen {

    private Connection databaseConnection;
    private Statement sqlStatement;
    private String reservationNumber;
    private ScreenChanger screenChanger;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private double totalDeposit = 0.00;
    private double totalRental = 0.00;
    private String clerkName;
    private String ccNumber;
    private String customerName;
    private String clerkId;


    @FXML
    private TextField toolIDField;
    @FXML
    private Label rn;
    @FXML
    private Label dr;
    @FXML
    private Label ec;


    //TODO needs to be passed from DropOffResNumber
    //private TextField dropOffResNum;

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
        clerkId = (String) args.get("clerkId");
        reservationNumber = ((String) args.get("ReservationNumber"));
        String newQuery = "SELECT ReservationNumber, t.ToolID, t.DailyRentalCost, t.DepositCost, t.AbbrDescription, "
                + "CreditCardNumber, c.FirstName, c.LastName, PickupClerk, DropoffClerk, "
                + "dclerk.EmpFirstName, dclerk.EmpLastName, StartDate, EndDate FROM RESERVATIONS AS r "
                + "INNER JOIN Reservation_Tool AS rt USING(ReservationNumber) "
                + "JOIN customers AS c ON CustomerEmail = c.Email "
                + "LEFT JOIN clerks AS dclerk ON DropoffClerk = dclerk.ID "
                + "INNER JOIN TOOLS AS t USING(ToolID) WHERE ReservationNumber =" + reservationNumber +";";

        ResultSet rs = null;

        try {
            rs = this.sqlStatement.executeQuery(newQuery);


            while (rs.next()) {
                rn.setText(String.valueOf(reservationNumber));
                clerkName = rs.getString("EmpFirstName") + " " + rs.getString("EmpLastName");
                customerName = rs.getString("FirstName") + " " + rs.getString("LastName");
                ccNumber = rs.getString("CreditCardNumber");
                startDate = rs.getDate("StartDate");
                endDate = rs.getDate("EndDate");
                long days = daysBetween(startDate, endDate);
                totalDeposit = totalDeposit + rs.getDouble("DepositCost");
                totalRental = totalRental + (rs.getDouble("DailyRentalCost") * days);

            }

            dr.setText(String.valueOf(totalDeposit));
            ec.setText(String.valueOf(totalRental));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup(Parent parent) {

        System.out.println("Start of start method");
        try {
            setDatabaseConnection(Main.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String newQuery =
                "select ToolID, AbbrDescription " +
                        "from tools where toolId in " +
                        "(select toolID from reservation_tool where ReservationNumber='" + reservationNumber +"')";
        System.out.println("testing gridpane");

        GridPane gp = (GridPane) parent;

        for(Node node : gp.getChildren()){
            if (node.getId() != null && node.getId().equalsIgnoreCase("rob")){
                GridPane pane = (GridPane) node;
                pane.setHgap(10);

                ResultSet rs = null;

                try {
                    System.out.println("trying sql query");
                    sqlStatement.execute("USE HANDYMAN");
                    rs = sqlStatement.executeQuery(newQuery);
                    int i=0;
                    while(rs.next()){
                        String toolID = rs.getString("toolID");
                        String abbrDescription = rs.getString("abbrDescription");
                        pane.add(new Text("" + i + ")"), 0, i);
                        pane.add(new Text(toolID),1,i);
                        pane.add(new Text(abbrDescription),2,i++);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @FXML
    protected void handleDropOffButtonAction(ActionEvent event) throws SQLException {


        String newQuery = "UPDATE reservations SET DropoffClerk='"
                + clerkId  + "'Where ReservationNumber ='"+ reservationNumber +"'";

        sqlStatement.execute(newQuery);

        HashMap args = new HashMap();
        args.put("reservationNumber", reservationNumber);
        args.put("clerkName", clerkName);
        args.put("customerName", customerName);
        args.put("ccNumber", ccNumber);
        args.put("startDate", startDate);
        args.put("endDate", endDate);
        args.put("totalDeposit", totalDeposit);
        args.put("totalRental", totalRental);


        System.out.println(args);

        screenChanger.loadScreen(ScreenChanger.RENTAL_RECEIPT, ScreenChanger.RENTAL_RECEIPT_FXML, args);
        screenChanger.setScreen(ScreenChanger.RENTAL_RECEIPT);



    }




    @FXML
    protected void handleViewDetailsButtonAction(ActionEvent event) throws SQLException {


        ResultSet toolInfo = this.sqlStatement.executeQuery("SELECT * FROM TOOLS WHERE ToolID = " + toolIDField.getText() + ";");


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

    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        HashMap args = new HashMap();
        args.put("clerkId", clerkId);

        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);

    }

    private static long daysBetween(Date one, Date two) {
        long difference = (one.getTime() - two.getTime()) / 86400000;
        return Math.abs(difference);
    }



}


