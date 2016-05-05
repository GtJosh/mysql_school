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

package main.SellTool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SellToolController implements ControlledScreen {

    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;

    public void setScreenParent(ScreenChanger screenPage) {
        this.screenChanger = screenPage;
    }

    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
    }

    public void setArguments(HashMap args) {
    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML
    private TextField toolIdToSell;

    @FXML
    protected void handleBackToMainMenuButtonAction(ActionEvent event) {
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }

    //TODO: VALIDATE TEXTFIELDS FOR SELL TOOL
    @FXML
    protected void handleSellToolButtonAction(ActionEvent event) {
        ResultSet getSellPriceResultSet = null;

        try {
            getSellPriceResultSet = sqlStatement.executeQuery("SELECT * FROM TOOLS WHERE TOOLID='" + toolIdToSell.getText() + "'");
            if (getSellPriceResultSet.next()) {
                HashMap args = new HashMap();
                args.put("sellToolID", toolIdToSell.getText());
                screenChanger.loadScreen(ScreenChanger.SELL_TOOL_PRICE, ScreenChanger.SELL_TOOL_PRICE_FXML, args);
                screenChanger.setScreen(ScreenChanger.SELL_TOOL_PRICE);
            } else {
                System.out.println("Fail");
                showInvalidToolID();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


}
    private void showInvalidToolID() {
        System.out.println("Invalid Tool ID");
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setContentText("Invalid Tool ID!");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //do nothing
            }
        });
    }
}




