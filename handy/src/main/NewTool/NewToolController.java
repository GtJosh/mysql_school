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

package main.NewTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class NewToolController implements ControlledScreen{


    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;



    public void setScreenParent(ScreenChanger screenPage){
        this.screenChanger = screenPage;
    }
    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
    }
    public void setArguments(HashMap args) {
//        clerkId = Integer.parseInt((String) args.get("clerkId"));
//        System.out.println("Loging in clerk " + clerkId);
    }

    @Override
    public void setup(Parent parent) {

    }

    @FXML private Text actiontarget;
    @FXML private TextField abbrDescription;
    @FXML private ComboBox<String> newToolType;
    @FXML private TextField depositAmount;
    @FXML private TextField rentalPrice;
    @FXML private TextField purchasePrice;
    @FXML private TextField fullDescription;
    @FXML private Label accessoriesLabel;
    @FXML private TextField accessories;


//                getCustomerResultSet.next();
//                email.setText(customerId);
//                name.setText(getCustomerResultSet.getString("FirstName")+getCustomerResultSet.getString("LastName"));
//                homePhone.setText(getCustomerResultSet.getString("HomePhone"));
//                workPhone.setText(getCustomerResultSet.getString("WorkPhone"));


    //TODO: VALIDATE TEXTFIELDS FOR ADD TOOL
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
         String toolType = newToolType.getSelectionModel().getSelectedItem();

         int toolID = 0;
         int serviceCost = 0;
         int availableToRent = 1;

        try {
            sqlStatement.execute("INSERT INTO TOOLS VALUES ('" + abbrDescription.getText()
                    + "', '" + toolType + "','" + toolID + "','" + depositAmount.getText() + "', '"
                    + rentalPrice.getText() + "', '" + purchasePrice.getText() + "', '"
                    + fullDescription.getText() + "','" + availableToRent + "', '" + serviceCost + "')");

            System.out.println(newToolType.getSelectionModel().getSelectedItem().equals("Power Tools"));
            if(newToolType.getSelectionModel().getSelectedItem().equals("Power Tools")) {

                ResultSet toolIDaccessory = sqlStatement.executeQuery("SELECT max(toolID) from tools");
                if(toolIDaccessory.next()){
                    String tia = toolIDaccessory.getString(1);
                    sqlStatement.execute("INSERT INTO ACCESSORIES VALUES (" + tia +",'" + accessories.getText() + "')");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        HashMap args = new HashMap();
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }

    @FXML protected void handleBackToMainMenuButtonAction(ActionEvent event){
        screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML);
        screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
    }

}
