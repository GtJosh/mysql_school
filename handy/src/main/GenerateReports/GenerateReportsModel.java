package main.GenerateReports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateReportsModel {

    private Connection databaseConnection;
    private Statement sqlStatement;

    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
        this.sqlStatement = databaseConnection.createStatement();
    }

    public ResultSet getReportOne() {
        String query = "SELECT ToolID, AbbrDescription, SUM(x.x * x.y) as RentalProfit, SUM(t.PurchasePrice + t.TotalServiceCost) as CostOfTool, COALESCE(SUM(x.x * x.y), 0) - COALESCE(SUM(t.PurchasePrice + t.TotalServiceCost), 0) as TotalProfit " +
                "FROM (SELECT SUM(t.DailyRentalCost) as x,  SUM(DATEDIFF(r.EndDate, r.StartDate)) as y " +
                "FROM TOOLS as t " +
                "INNER JOIN RESERVATION_TOOL as rt ON rt.`ToolID` = t.`ToolID` " +
                "INNER JOIN RESERVATIONS as r ON r.`ReservationNumber` = rt.`ReservationNumber` " +
                "GROUP BY t.`ToolID`) as x, TOOLS as t";

        try {
//            this.sqlStatement = this.databaseConnection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getReportTwo() {
        String query = "SELECT `FirstName`, `LastName`, `Email`, SUM(`CustomerEmail`) AS NumberOfRentals " +
                "FROM Customers t, Reservations r " +
                "WHERE t.`Email` = r.`CustomerEmail` " +
                "ORDER BY NumberOfRentals DESC, LastName DESC";
        try {
//            this.sqlStatement = this.databaseConnection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getReportThree() {
//        String query = "SELECT `EmpFirstName`, `EmpLastName`, SUM(`PickupClerk`) as PickUpsHandled, SUM(`DropoffClerk`) as DropoffsHandled, SUM(DropoffClerk + PickupClerk) as TotalPickupsDropoffs " +
//                "FROM CLERKS c, RESERVATIONS r " +
//                "WHERE (c.`ID` = r.`DropoffClerk` or c.`ID` = r.`PickupClerk`) and (MONTH(r.`StartDate`) = MONTH(NOW()) or MONTH(r.`EndDate`) = MONTH(NOW()))";

        String queryNew =  "SELECT c.FirstName, c.LastName, c.Email, count(r.ReservationNumber)" +
        "as NumberOfRentals FROM customers as c JOIN reservations as r ON c.Email = r.CustomerEmail GROUP BY c.Email";

        try {
//            this.sqlStatement = this.databaseConnection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery(queryNew);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
