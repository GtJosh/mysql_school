package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LoginController implements ControlledScreen{

    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        DatabaseConnection = databaseConnection;
        sqlStatement = DatabaseConnection.createStatement();
    }


    @Override
    public void setArguments(HashMap args) {
    }

    @Override
    public void setup(Parent parent) {

    }


    private ScreenChanger screenChanger;
    public void setScreenParent(ScreenChanger screenPage){
        screenChanger = screenPage;
    }

    private Connection DatabaseConnection;
    private java.sql.Statement sqlStatement;

    public LoginController(){}

    @FXML
    private Text actiontarget;

    @FXML
    private TextField LoginIDField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton clerkRadioButton;

    @FXML
    public void  handleLoginButtonAction(ActionEvent event) throws SQLException {
        if (clerkRadioButton.isSelected()) {
            attemptClerkLogin();
        }
        else {
            attemptCustomerLogin();
        }
    }

    private void attemptClerkLogin() throws SQLException {
        final ResultSet clerkResult = sqlStatement.executeQuery("SELECT password from Clerks WHERE ID = " + Integer.parseInt(LoginIDField.getText()));
        if (clerkResult.next()){
            if (Objects.equals(clerkResult.getString(1), passwordField.getText())){
                HashMap args = new HashMap();
                args.put("clerkId", LoginIDField.getText());
                screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
                screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
            }
            else {
                showPasswordFail();
            }
        }
        else {
            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setContentText("Clerk ID not found.  Create new clerk?");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("go to create new clerk");
                    HashMap args = new HashMap();
                    args.put("clerkId", LoginIDField.getText());
                    screenChanger.loadScreen(ScreenChanger.CLERK_CREATE_PROFILE, ScreenChanger.CLERK_CREATE_PROFILE_FXML, args);
                    screenChanger.setScreen(ScreenChanger.CLERK_CREATE_PROFILE);
                }
            });
        }
    }

    private void attemptCustomerLogin() throws SQLException {
        actiontarget.setText("Attempting Customer Login");
        System.out.println("Login ID text: " + LoginIDField.getText());
        ResultSet customerResult = sqlStatement.executeQuery("SELECT password from Customers WHERE email = '" + LoginIDField.getText() +"'");
        if (customerResult.next()){
            if (Objects.equals(customerResult.getString(1), passwordField.getText())){
                HashMap args = new HashMap();
                args.put("customerId", LoginIDField.getText());
                screenChanger.loadScreen(ScreenChanger.CUSTOMER_MAIN_MENU, ScreenChanger.CUSTOMER_MAIN_MENU_FXML, args);
                screenChanger.setScreen(ScreenChanger.CUSTOMER_MAIN_MENU);
                System.out.println("password matches, go to customer menu");
            }
            else {
                showPasswordFail();
            }
        }
        else {
            System.out.println("Customer not found");
            javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
            dialog.setContentText("Customer email not found.  Create new customer?");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Calling create new Customer");
                    HashMap args = new HashMap();
                    args.put("customerId", LoginIDField.getText());
                    screenChanger.loadScreen(ScreenChanger.CUSTOMER_CREATE_PROFILE, ScreenChanger.CUSTOMER_CREATE_PROFILE_FXML, args);
                    screenChanger.setScreen(ScreenChanger.CUSTOMER_CREATE_PROFILE);
                }
            });
        }
    }

    private void showPasswordFail(){
        System.out.println("Incorrect Password");
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setContentText("Incorrect password!");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //do nothing
            }
        });
    }
}
