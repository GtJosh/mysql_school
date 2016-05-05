package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

/**
 * Created by joshuam on 4/11/2016.
 */
public class ScreenChanger extends StackPane {

    public static final String LOGIN_SCREEN = "login";
    public static final String LOGIN_FXML = "login.fxml";
    public static final String CHECK_AVAILABILITY = "checkAvailability";
    public static final String CHECK_AVAILABILITY_FXML = "CheckAvailability/checkAvailability.fxml";
    public static final String CLERK_CREATE_PROFILE = "clerkCreateProfile";
    public static final String CLERK_CREATE_PROFILE_FXML = "ClerkCreateProfile/clerkCreateProfile.fxml";
    public static final String CLERK_MAIN_MENU_SCREEN = "clerkMainMenu";
    public static final String CLERK_MAIN_MENU_FXML = "ClerkMainMenu/clerkMainMenu.fxml";
    public static final String CUSTOMER_CREATE_PROFILE = "customerCreateProfile";
    public static final String CUSTOMER_CREATE_PROFILE_FXML = "CustomerCreateProfile/customerCreateProfile.fxml";
    public static final String CUSTOMER_MAIN_MENU = "CustomerMainMenu";
    public static final String CUSTOMER_MAIN_MENU_FXML = "CustomerMainMenu/customerMainMenu.fxml";
    public static final String CUSTOMER_PROFILE = "CustomerProfile";
    public static final String CUSTOMER_PROFILE_FXML = "CustomerProfile/customerProfile.fxml";
    public static final String DROP_OFF = "dropOff";
    public static final String DROP_OFF_FXML = "DropOff/dropOff.fxml";
    public static final String DROP_OFF_RES_NUMBER = "dropOffResNumber";
    public static final String DROP_OFF_RES_NUMBER_FXML = "DropOffResNumber/dropOffResNumber.fxml";
    public static final String MAKE_RESERVATION = "makeReservation";
    public static final String MAKE_RESERVATION_FXML = "MakeReservation/makeReservation.fxml";
    public static final String NEW_TOOL = "newTool";
    public static final String NEW_TOOL_FXML = "NewTool/newTool.fxml";
    public static final String PICK_UP = "pickUp";
    public static final String PICK_UP_FXML = "PickUp/pickup.fxml";
    public static final String PICK_UP_RES_NUMBER = "pickUpResNumber";
    public static final String PICK_UP_RES_NUMBER_FXML = "PickUpResNumber/pickUpResNumber.fxml";
    public static final String RENTAL_CONTRACT = "rentalContract";
    public static final String RENTAL_CONTRACT_FXML = "RentalContract/rentalContract.fxml";
    public static final String RENTAL_RECEIPT = "rentalReceipt";
    public static final String RENTAL_RECEIPT_FXML = "RentalReceipt/rentalReceipt.fxml";
    public static final String REPORT_LIST = "reportList";
    public static final String REPORT_LIST_FXML = "ReportList/reportList.fxml";
    public static final String RESERVATION_FINAL = "reservationFinal";
    public static final String RESERVATION_FINAL_FXML = "ReservationFinal/reservationFinal.fxml";
    public static final String RESERVATION_SUMMARY = "reservationSummary";
    public static final String RESERVATION_SUMMARY_FXML = "ReservationSummary/reservationSummary.fxml";
    public static final String SELL_TOOL = "sellTool";
    public static final String SELL_TOOL_FXML= "SellTool/sellTool.fxml";
    public static final String SELL_TOOL_PRICE = "sellToolPrice";
    public static final String SELL_TOOL_PRICE_FXML = "SellToolPrice/sellToolPrice.fxml";
    public static final String SERVICE_ORDER = "serviceOrder";
    public static final String SERVICE_ORDER_FXML = "ServiceOrder/serviceOrder.fxml";
    public static final String TOOL_AVAILABILITY = "ToolAvailability";
    public static final String VIEW_DETAILS = "viewDetails";
    public static final String VIEW_DETAILS_FXML = "ViewDetails/viewDetails.fxml";

    private HashMap<String, Node> screens = new HashMap<>();

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            loadScreen.prefHeight(800);
            loadScreen.prefWidth(800);
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            myScreenController.setScreenParent(this);
            myScreenController.setDatabaseConnection(Main.getConnection());
            addScreen(name, loadScreen);
            return true;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean loadScreen(String name, String resource, HashMap args) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            myScreenController.setScreenParent(this);
            myScreenController.setDatabaseConnection(Main.getConnection());
            myScreenController.setArguments(args);
            myScreenController.setup(loadScreen);
            addScreen(name, loadScreen);
            return true;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean setScreen(final String name) {
        System.out.println("setting screen to "+name);
        if (screens.get(name) != null) {
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            }
            else {
                getChildren().add(screens.get(name));
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }
    }

    public boolean setScreen(final String name, HashMap args) {
        System.out.println("setting screen to "+name);
        if (screens.get(name) != null) {
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            }
            else {
                getChildren().add(screens.get(name));
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }
    }
    public boolean unloadScreen(String name) {
        if(screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}
