

package main.ClerkCreateProfile;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.ControlledScreen;
import main.ScreenChanger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ClerkCreateProfileController implements ControlledScreen {

    private ScreenChanger screenChanger;
    private Connection databaseConnection;
    private Statement sqlStatement;

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
    public void setArguments(HashMap args) {}

    @Override
    public void setup(Parent parent) {

    }

    @FXML private TextField clerkIdNumber;
    @FXML private TextField empPasswordField;
    @FXML private TextField empFirstName;
    @FXML private TextField empLastName;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        try {
            sqlStatement.execute("INSERT INTO CLERKS VALUES ('" + Integer.parseInt(clerkIdNumber.getText())
                    + "', '" + empPasswordField.getText() + "', '" + empFirstName.getText() + "', '"
                    + empLastName.getText() + "')");
            HashMap args = new HashMap();
            args.put("clerkId", clerkIdNumber.getText());
            screenChanger.loadScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN, ScreenChanger.CLERK_MAIN_MENU_FXML, args);
            screenChanger.setScreen(ScreenChanger.CLERK_MAIN_MENU_SCREEN);
        } catch (SQLException e) {
            showCreateFailMessage(e);
        }
    }

    private void showCreateFailMessage(SQLException e){
        System.out.println("Failed to create clerk.");
        javafx.scene.control.Dialog dialog = new javafx.scene.control.Dialog();
        dialog.setHeaderText("Failed to create clerk!");
        dialog.setContentText("SQL Error Code: " + e.getErrorCode());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //do nothing
            }
        });
    }

    @FXML protected void handleCancelButtonAction(ActionEvent event) {
            screenChanger.loadScreen(ScreenChanger.LOGIN_SCREEN, ScreenChanger.LOGIN_FXML);
            screenChanger.setScreen(ScreenChanger.LOGIN_SCREEN);
    }
}
