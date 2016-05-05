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

package main.ClerkMainMenu;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClerkMainMenuController implements ControlledScreen {

    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;
    private String clerkId;

    public void setScreenParent(ScreenChanger screenPage){
        this.screenChanger = screenPage;
    }
    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
    }

    public void setArguments(HashMap args) {
        clerkId = (String) args.get("clerkId");
        System.out.println("Logging in clerk " + clerkId);
    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML private Text actiontarget;
    
    @FXML protected void handlePickUpReservationButtonAction(ActionEvent event) {
        HashMap args = new HashMap();
        args.put("clerkId", clerkId);
        screenChanger.loadScreen(ScreenChanger.PICK_UP_RES_NUMBER, ScreenChanger.PICK_UP_RES_NUMBER_FXML, args);
        screenChanger.setScreen(ScreenChanger.PICK_UP_RES_NUMBER);
    }

    @FXML protected void handleDropOffReservationButtonAction(ActionEvent event) {
        HashMap args = new HashMap();
        args.put("clerkId", clerkId);
        screenChanger.loadScreen(ScreenChanger.DROP_OFF_RES_NUMBER, ScreenChanger.DROP_OFF_RES_NUMBER_FXML, args);
        screenChanger.setScreen(ScreenChanger.DROP_OFF_RES_NUMBER);
    }

    @FXML protected void handleServiceOrderButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.SERVICE_ORDER, ScreenChanger.SERVICE_ORDER_FXML);
        screenChanger.setScreen(ScreenChanger.SERVICE_ORDER);

    }

    @FXML protected void handleAddNewToolButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.NEW_TOOL, ScreenChanger.NEW_TOOL_FXML);
        screenChanger.setScreen(ScreenChanger.NEW_TOOL);
    }

    @FXML protected void handleSellToolButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.SELL_TOOL, ScreenChanger.SELL_TOOL_FXML);
        screenChanger.setScreen(ScreenChanger.SELL_TOOL);
    }

    @FXML protected void handleGenerateReportsButtonAction(ActionEvent event) {

        screenChanger.loadScreen(ScreenChanger.REPORT_LIST, ScreenChanger.REPORT_LIST_FXML);
        screenChanger.setScreen(ScreenChanger.REPORT_LIST);

//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ReportList/reportList.fxml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Stage stage = new Stage();
//
//        stage.setScene(new Scene(root, 400, 300));
//
//        stage.show();
    }

    @FXML protected void handleExitButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.LOGIN_SCREEN, ScreenChanger.LOGIN_SCREEN);
        screenChanger.setScreen(ScreenChanger.LOGIN_SCREEN);
    }


}
