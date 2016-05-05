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

package main.RentalReceipt;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class RentalReceiptController implements ControlledScreen {
    @FXML private Text actiontarget;
    @FXML Label resNumberLabel;
    @FXML Label clerkNameLabel;
    @FXML Label customerNameLabel;
    @FXML Label ccNumberLabel;
    @FXML Label startDateLabel;
    @FXML Label endDateLabel;
    @FXML Label totalDepositLabel;
    @FXML Label totalRentalLabel;
    @FXML Label totalCostLabel;

    private String clerkId;

    private double tD;
    private double tR;
    private double tC;



    private Connection databaseConnection;
    private Statement sqlStatement;
    private ScreenChanger screenChanger;

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
        resNumberLabel.setText((String) args.get("reservationNumber"));
        clerkNameLabel.setText((String) args.get("clerkName"));
        customerNameLabel.setText((String) args.get("customerName"));
        ccNumberLabel.setText((String) args.get("ccNumber"));

        startDateLabel.setText(args.get("startDate").toString());
        endDateLabel.setText(args.get("endDate").toString());

        tD = (Double) args.get("totalDeposit");
        tR = (Double) args.get("totalRental");


            tC = tR;


        totalDepositLabel.setText(String.valueOf(tD));
        totalRentalLabel.setText(String.valueOf(tR));
        totalCostLabel.setText(String.valueOf(tC));

    }

    @Override
    public void setup(Parent parent) {

    }



    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        HashMap args = new HashMap();
        args.put("clerkId", clerkId);

        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);

    }

}
