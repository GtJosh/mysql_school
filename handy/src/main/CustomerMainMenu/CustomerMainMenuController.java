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

package main.CustomerMainMenu;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.CustomerProfile.Profile;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CustomerMainMenuController implements main.ControlledScreen {

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

    @FXML protected void handleViewProfileButtonAction(ActionEvent event){

        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.CUSTOMER_PROFILE, ScreenChanger.CUSTOMER_PROFILE_FXML, args);
        screenChanger.setScreen(ScreenChanger.CUSTOMER_PROFILE);

    }

    @FXML protected void handleCheckToolAvailabilityButtonAction(ActionEvent event) {
        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.CHECK_AVAILABILITY, ScreenChanger.CHECK_AVAILABILITY_FXML, args);
        screenChanger.setScreen(ScreenChanger.CHECK_AVAILABILITY);
    }

    @FXML protected void handleMakeReservationButtonAction(ActionEvent event){
        final HashMap args = new HashMap();
        args.put("customerId", customerId);
        screenChanger.loadScreen(ScreenChanger.MAKE_RESERVATION, ScreenChanger.MAKE_RESERVATION_FXML, args);
        screenChanger.setScreen(ScreenChanger.MAKE_RESERVATION);
    }

    @FXML protected void handleExitButtonAction(ActionEvent event){
        screenChanger.loadScreen(ScreenChanger.LOGIN_SCREEN, ScreenChanger.LOGIN_FXML);
        screenChanger.setScreen(ScreenChanger.LOGIN_SCREEN);
    }

}

