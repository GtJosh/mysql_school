package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Main extends Application {

    static final String URL = "jdbc:mysql://localhost";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static final String LOGIN_SCREEN = "login";
    public static final String LOGIN_FXML = "login.fxml";
    public static final String CLERK_MAIN_MENU_SCREEN = "clerkMainMenu";
    public static final String CLERK_MAIN_MENU_FXML = "ClerkMainMenu/clerkMainMenu.fxml";
    private static Connection databaseConnection;

    public static Connection getConnection() {
        if (databaseConnection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                databaseConnection = DriverManager.getConnection(URL, USER, PASS);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return databaseConnection;
    }

    @Override
    public void start(Stage primaryStage) {

        ScreenChanger mainContainer = new ScreenChanger();
        mainContainer.loadScreen(LOGIN_SCREEN, LOGIN_FXML);
        mainContainer.loadScreen(CLERK_MAIN_MENU_SCREEN, CLERK_MAIN_MENU_FXML);
        mainContainer.setScreen("login");
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Handyman Tools");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {


        if (args.length > 0 && args[0].equalsIgnoreCase("Pop")) {
            setUpDatabaseConnection(true);
        } else {
            setUpDatabaseConnection(false);
        }
        launch(args);

    }



    private static void setUpDatabaseConnection(boolean populateTestData) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        try (Statement st = getConnection().createStatement()) {
            ensureDatabaseExists(st, populateTestData);
        }
    }






    private static void ensureDatabaseExists(Statement sqlStatement, boolean populateTestData) throws SQLException {


        ResultSet resultSet = sqlStatement.executeQuery("SHOW DATABASES LIKE 'HANDYMAN'");
        if (resultSet.next()) {
            sqlStatement.execute("USE HANDYMAN");
            populateTestData(sqlStatement, populateTestData);
            return;
        }

        try {
            sqlStatement.execute("CREATE DATABASE HANDYMAN");
            sqlStatement.execute("USE HANDYMAN");
            sqlStatement.execute("" +

                    "CREATE TABLE CUSTOMERS (  \n" +
                    "  Email VARCHAR(50) NOT NULL UNIQUE,  \n" +
                    "  Password VARCHAR(50) NOT NULL,  \n" +
                    "  FirstName VARCHAR(50),  \n" +
                    "  LastName VARCHAR(50),  \n" +
                    "  HomePhone VARCHAR(15),  \n" +
                    "  WorkPhone VARCHAR(15),  \n" +
                    "  Address VARCHAR(50),  \n" +
                    "  PRIMARY KEY (Email))");

            sqlStatement.execute("" +
                    "CREATE TABLE CLERKS (  \n" +
                    "  ID SMALLINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,  \n" +
                    "  Password VARCHAR(50) NOT NULL,  \n" +
                    "  EmpFirstName VARCHAR(50),  \n" +
                    "  EmpLastName VARCHAR(50),  \n" +
                    "  PRIMARY KEY (ID)  \n" +
                    ")");

            sqlStatement.execute("" +
                    "CREATE TABLE RESERVATIONS (  \n" +
                    "  ReservationNumber MEDIUMINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,  \n" +
                    "  StartDate DATE NOT NULL,  \n" +
                    "  EndDate DATE NOT NULL,  \n" +
                    "  CreditCardNumber BIGINT UNSIGNED,  \n" +
                    "  CreditCardExpiry DATE,  \n" +
                    "  PickupClerk SMALLINT UNSIGNED,  \n" +
                    "  DropoffClerk SMALLINT UNSIGNED,  \n" +
                    "  CustomerEmail VARCHAR(50),  \n" +
                    "  PRIMARY KEY (ReservationNumber),  \n" +
                    "  FOREIGN KEY (PickupClerk) REFERENCES CLERKS(ID),  \n" +
                    "  FOREIGN KEY (CustomerEmail) REFERENCES CUSTOMERS(Email),    \n" +
                    "  CHECK(StartDate < EndDate),  \n" +
                    "  CHECK(CreditCardExpiry > EndDate)  \n" +
                    ")");

            sqlStatement.execute("" +
                    "CREATE TABLE TOOLS (  \n" +
                    "  AbbrDescription VARCHAR(50),  \n" +
                    "  TypeCategory ENUM('Hand Tools', 'Construction Equipment', 'Power Tools'),  \n" +
                    "  ToolID SMALLINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,  \n" +
                    "  DepositCost SMALLINT UNSIGNED,  \n" +
                    "  DailyRentalCost SMALLINT UNSIGNED,  \n" +
                    "  PurchasePrice SMALLINT UNSIGNED,  \n" +
                    "  FullDescription VARCHAR(250),  \n" +
                    "  AvailableToRent BOOLEAN,  \n" +
                    "  TotalServiceCost SMALLINT UNSIGNED,  \n" +
                    "  PRIMARY KEY (ToolID)  \n" +
                    ")");

            sqlStatement.execute("" +

                    "CREATE TABLE SERVICEREQUEST (  \n" +
                    "  ToolID SMALLINT UNSIGNED NOT NULL,  \n" +
                    "  StartDate DATE NOT NULL,  \n" +
                    "  EndDate DATE NOT NULL,  \n" +
                    "  EstimatedCost SMALLINT UNSIGNED,  \n" +
                    "  PRIMARY KEY (StartDate, ToolID),    \n" +
                    "  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)  \n" +
                    ")");

//            "CREATE TABLE SERVICEREQUEST (  \n" +
//                    "  ToolID SMALLINT UNSIGNED NOT NULL,  \n" +
//                    "  StartDate DATE NOT NULL,  \n" +
//                    "  EndDate DATE NOT NULL,  \n" +
//                    "  EstimatedCost SMALLINT UNSIGNED,  \n" +
//                    "  PRIMARY KEY (StartDate, ToolID),    \n" +
//                    "  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)  \n" +
//                    ")");


            sqlStatement.execute("" +
                    "CREATE TABLE RESERVATION_TOOL (  \n" +
                    "  ReservationNumber MEDIUMINT UNSIGNED NOT NULL,  \n" +
                    "  ToolID SMALLINT UNSIGNED NOT NULL,  \n" +
                    "  FOREIGN KEY (ReservationNumber) REFERENCES RESERVATIONS(ReservationNumber),  \n" +
                    "  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)\n" +
                    ")");

            sqlStatement.execute("" +
                    "CREATE TABLE ACCESSORIES (  \n" +
                    "  ToolID SMALLINT UNSIGNED NOT NULL,  \n" +
                    "  Accessory VARCHAR(50) NOT NULL,  \n" +
                    "  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)   \n" +
                    ")");


            populateTestData(sqlStatement, populateTestData);

            sqlStatement.execute("SET sql_mode = ''");

        } catch (Exception e) {


            resultSet = sqlStatement.executeQuery("SHOW DATABASES LIKE 'HANDYMAN'");
            if (resultSet.next()) {
                sqlStatement.execute("DROP DATABASE HANDYMAN");
            }
            throw e;
        }
    }

    private static void populateTestData(Statement sqlStatement, boolean populateTestData) throws SQLException {
        if (populateTestData) {
//            sqlStatement.execute("DELETE FROM TOOLS");
//            sqlStatement.execute("DELETE FROM RESERVATIONS");
//            sqlStatement.execute("DELETE FROM RESERVATION_TOOL");
//            sqlStatement.execute("DELETE FROM ACCESSORIES");
            sqlStatement.execute("DELETE FROM CUSTOMERS");
            sqlStatement.execute("DELETE FROM CLERKS");


            for (int i = 1; i <= 5; i++) {
                sqlStatement.execute("INSERT INTO Customers VALUES ('customer" + i + "@email.com', 'password', 'Joe', 'Smith', '555-1212', '555-1212', '123 main St')");
                sqlStatement.execute("INSERT INTO Clerks (ID,Password,EmpFirstName,EmpLastName) VALUES (0,'password','Jane', 'Doe')");
                sqlStatement.execute("INSERT INTO Tools VALUES " +
                        "('Hammer','Hand Tools',0,30,5,25,'Cobalt Claw framing hammer',true,0)," +
                        "('Saw', 'Hand Tools',0,30,5,25,'Stanley Hand carpenter saw, 24in',true,0)," +
                        "('#2 Phillips','Hand Tools',0,10,2,8,'Craftsman 7 inch #2 phillips screwdriver',true,0)," +
                        "('Med Flathead','Hand Tools',0,10,2,7,'Craftsman 7 inch med flathead screwdriver',true,0)," +
                        "('3/8 drill','Power Tools',0,150,25,125,'3/8 dewault drill, keyless chuck, 18 volt',true,0)," +
                        "('6in circular saw','Power Tools',0,130,20,105,'6 inch makitia circular saw, corded',true,0)," +
                        "('Nailgun','Power Tools',0,230,50,225,'dewault 4 inch nailer, electric',true,0)," +
                        "('Cement Mixer','Construction Equipment',0,5000,300,4500,'50 gallon gas cement mixer',true,0)," +
                        "('100 amp generator','Construction Equipment',0,1500,500,1450,'100 amp diesel generator, catapiller',true,0)," +
                        "('bobcat loader','Construction Equipment',0,15000,1300,14500,'catapiller bobcat loader and trailer',true,0)"
                );
                sqlStatement.execute("INSERT INTO Reservations VALUES " +
                        "(0, '2016-04-2" + i + "', '2016-04-2" + (i + 3) + "', 55555555, '2017-08-31', " + i + ", " + i + "" +
                        ", 'customer" + i + "@email.com')");
                sqlStatement.execute("INSERT INTO Reservation_Tool VALUES (" + i + "," + (i * 3) + ")");
                sqlStatement.execute("INSERT INTO Reservation_Tool VALUES (" + i + "," + (i * 3 + 1) + ")");
                sqlStatement.execute("INSERT INTO ServiceRequest VALUES (" + i + ", '2016-05-1" + i + "', '2016-05-2" + (i + 3) + "', 10)");
                sqlStatement.execute("INSERT INTO Accessories VALUES (" + (i * 2) + ", 'littleAccessory')");
                sqlStatement.execute("INSERT INTO Accessories VALUES (" + (i * 2) + ", 'bigAccessory')");

            }
        }

    }
}


//TODO: UPDATE STATEMENT ESTIMATED SERVICE ORDER COST IN TOOL TABLE
//TODO: CHANGE AVAILABLETORENT FLAG TO TOOL SOLD FLAG