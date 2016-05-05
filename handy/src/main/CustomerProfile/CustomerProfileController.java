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

package main.CustomerProfile;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CustomerProfileController implements ControlledScreen {

    private ScreenChanger screenChanger;
    private Statement sqlStatement;
    private String customerId;

    @Override
    public void setScreenParent(ScreenChanger screenPage) {
        this.screenChanger = screenPage;
    }

    @Override
    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        Connection databaseConnection1 = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
    }

    @Override
    public void setArguments(HashMap args) {
        customerId = (String) args.get("customerId");
        try {
            ResultSet getCustomerResultSet = sqlStatement.executeQuery("SELECT * FROM CUSTOMERS WHERE EMAIL = '"
                    + customerId + "'");
            getCustomerResultSet.next();
            email.setText(customerId);
            name.setText(getCustomerResultSet.getString("FirstName") + " " + getCustomerResultSet.getString("LastName"));
            homePhone.setText(getCustomerResultSet.getString("HomePhone"));
            workPhone.setText(getCustomerResultSet.getString("WorkPhone"));
            address.setText(getCustomerResultSet.getString("Address"));

//            ResultSet getReservationsResultSet = sqlStatement.executeQuery("SELECT ReservationNumber, " +
//                    "StartDate, EndDate, Deposit, DailyRentalCost, EmpFirstName as 'Pick-Up Clerk' " +
//                    "WHERE r.PickupClerk = c.ID, EmpFirstName as 'Drop-Off Clerk' WHERE " +
//                    "r.DropoffClerk = c.ID, ToolID FROM Reservations r where Email = '" + customerId + "' " +
//                    "INNER JOIN Reservation-Tool as rt on rt.ReservationNumber = r.ReservationNumber" +
//                    "INNER JOIN Tool as t on t.toolID = rt.toolID INNER JOIN clerks as c on c.ID = r.EID");
//TODO convert the clerk ID's to first names and format the table display better
            //TODO make ok button at top or scrollbar on history
            ResultSet getReservationsResultSet = sqlStatement.executeQuery("" +
                    "SELECT " +
                        "reservations.ReservationNumber," +
                        "StartDate," +
                        "EndDate," +
                        "PUC.EmpFirstName," +
                        "DOC.EmpFirstName," +
                        "AbbrDescription, " +
                        "DepositCost, " +
                        "DailyRentalCost " +
                    "FROM " +
                        "Reservations LEFT JOIN Reservation_tool USING (ReservationNumber) LEFT JOIN Tools USING (toolID) " +
                    "LEFT JOIN Clerks AS PUC ON reservations.PickupClerk = PUC.ID " +
                    "LEFT JOIN Clerks AS DOC ON reservations.DropoffClerk = DOC.ID " +
                    "WHERE " +
                    "CustomerEmail='" + customerId + "' ORDER BY EndDate ASC");

            historyGrid.add(new Text("Reservation"), 0, 0);
            historyGrid.add(new Text("Start"), 1, 0);
            historyGrid.add(new Text("End"), 2, 0);
            historyGrid.add(new Text("Deposit"), 3, 0);
            historyGrid.add(new Text("Daily"), 4, 0);
            historyGrid.add(new Text("Pickup"), 5, 0);
            historyGrid.add(new Text("Dropoff"), 6, 0);

            int i = 0;
            while (getReservationsResultSet.next()){
                i++;
                historyGrid.add(new Text(getReservationsResultSet.getString("ReservationNumber")),0,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("StartDate")),1,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("EndDate")),2,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("DepositCost")),3,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("DailyRentalCost")),4,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("PUC.EmpFirstName")),5,i);
                historyGrid.add(new Text(getReservationsResultSet.getString("DOC.EmpFirstName")),6,i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML Text email;
    @FXML Text name;
    @FXML Text homePhone;
    @FXML Text workPhone;
    @FXML Text address;
    @FXML Text reservationsHistory;
    @FXML GridPane historyGrid;
    @FXML ScrollPane reservationsHistoryScroll;

    @FXML protected void handleOKButtonAction(ActionEvent event) {
        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.CUSTOMER_MAIN_MENU, ScreenChanger.CUSTOMER_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CUSTOMER_MAIN_MENU);
    }
}

