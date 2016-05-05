package main;

import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by joshuam on 4/11/2016.
 */
public interface ControlledScreen {
    void setScreenParent(ScreenChanger screenPage);
    void setDatabaseConnection(Connection DatabaseConnection) throws SQLException;
    void setArguments(HashMap args);
    void setup(Parent parent);
}
