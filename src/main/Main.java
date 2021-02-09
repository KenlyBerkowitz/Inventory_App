package main;

import controller.ErrorMessageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Product;

import java.io.IOException;

//Location of javadoc folder: /Inventory_App/src/javadoc

/**
 * Main method to start the program
 * @author Kenly Berkowitz
 */
public class Main extends Application {

    public static Inventory inv = new Inventory();

    /**
     * Loads mainForm.fxml to start the program.
     * @param primaryStage Creates new stage object.
     * @exception Exception Loader throws a Runtime Exception if it cannot retrieve the FXML document.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        primaryStage.setTitle("Inventory App");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main method.
     * @param args Starts new program.
     */
    public static void main(String[] args) {

        InHouse part1 = new InHouse(inv.getGenPartID(), "Tire", 20.99, 15, 1, 20, 988);
        InHouse part2 = new InHouse(inv.getGenPartID(), "Pedal", 39.99, 15, 1, 20, 788);
        InHouse part3 = new InHouse(inv.getGenPartID(), "Rim", 22.99, 15, 1, 20, 390);
        InHouse part4 = new InHouse(inv.getGenPartID(), "Brake", 19.99, 15, 1, 20, 678);
        InHouse part5 = new InHouse(inv.getGenPartID(), "Light", 14.99, 15, 1, 20, 564);

        inv.addPart(part1);
        inv.addPart(part2);
        inv.addPart(part3);
        inv.addPart(part4);
        inv.addPart(part5);

        Product product1 = new Product(inv.getGenProductID(), "Bike", 100.99, 15, 1, 20);
        Product product2 = new Product(inv.getGenProductID(), "Trick Bike", 150.99, 15, 1, 20);
        Product product3 = new Product(inv.getGenProductID(), "Speed 3000", 269.99, 15, 1, 20);
        Product product4 = new Product(inv.getGenProductID(), "Cruiser", 199.99, 15, 1, 20);
        Product product5 = new Product(inv.getGenProductID(), "City Slicker", 142.99, 15, 1, 20);

        inv.addProduct(product1);
        inv.addProduct(product2);
        inv.addProduct(product3);
        inv.addProduct(product4);
        inv.addProduct(product5);

        launch(args);
    }

    /**
     * Initializes error message box.
     * @param string String is passed to initialize error message.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document.
     */
    public static void initErrorMessageBox(String string) throws IOException {
        ErrorMessageController.initString(string);
        Parent root = FXMLLoader.load(Main.class.getResource("/view/messageDialogBox.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Error");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

}

