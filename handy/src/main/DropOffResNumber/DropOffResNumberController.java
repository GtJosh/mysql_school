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
package main.DropOffResNumber;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.Main;
import main.ScreenChanger;

import java.sql.Statement;
import java.util.HashMap;

public class DropOffResNumberController implements ControlledScreen {
    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;
    private String reservationNumber;
    private String clerkId;
    private String toolid;
    private String dailyRentalCost;
    private double totalDeposit = 0.00;
    private double totalRental = 0.00;
    private Date startDate;
    private Date endDate;

    @FXML
    private TextField dropOffResNum;


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

    }

    @Override
    public void setup(Parent parent) {

    }



    @FXML
    protected void handleEnterButtonAction(ActionEvent event) throws SQLException {
        //TODO: Probabaly need to check the DB to see if reservation exists and throw error box up if not
        String query = "SELECT ReservationNumber from Reservations WHERE ReservationNumber =" + dropOffResNum.getText() + ";";

        ResultSet r = this.sqlStatement.executeQuery(query);
        HashMap args = new HashMap();
        if (r.next()) {
            args.put("clerkId", clerkId);
            args.put("ReservationNumber", dropOffResNum.getText());
            System.out.println(args);
            screenChanger.loadScreen(ScreenChanger.DROP_OFF, ScreenChanger.DROP_OFF_FXML, args);
            screenChanger.setScreen(ScreenChanger.DROP_OFF);
        } else {
            System.out.println("Failed to find reservation.");
            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setHeaderText("Failed to find reservation.");
            dialog.setContentText("SQL Error Code: ");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        }
    }


    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event){
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }


    private static long daysBetween(Date one, Date two) {
        long difference = (one.getTime() - two.getTime()) / 86400000;
        return Math.abs(difference);
    }


}