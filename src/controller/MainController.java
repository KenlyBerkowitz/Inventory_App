package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import main.Main;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * MainController class that controls mainForm.fxml
 * @author Kenly Berkowitz
 */
public class MainController implements Initializable {

    @FXML
    public TableView<Part> partsTable;

    @FXML
    public TableColumn<Part, Integer> partIdCol;

    @FXML
    public TableColumn <Part, String>partNameCol;

    @FXML
    public TableColumn<Part,Integer> partInvCol;

    @FXML
    public TableColumn<Part, Double> partPriceCol;

    @FXML
    public TableView<Product> productsTable;

    @FXML
    public TableColumn<Product, Integer> productIdCol;

    @FXML
    public TableColumn<Product, String> productNameCol;

    @FXML
    public TableColumn<Product, Integer> productInvCol;

    @FXML
    public TableColumn<Product, Double> productPriceCol;

    @FXML
    private AnchorPane mainScreen;

    @FXML
    private Button exitProgramBtn;

    @FXML
    private Button addPart;

    @FXML
    private Button modifyPart;

    @FXML
    private Button deletePartButton;

    @FXML
    private TextField searchPartField;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button modifyProductBtn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TextField searchProductField;

    /**
     * Add part.
     * @param event Mouse click event handler for Add Part Button.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document.
     */
    @FXML
    private void addPartBtn(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/addPartForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Part");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        resetInit();
    }

    /**
     * Add Product.
     * @param event Mouse click event handler for Add Product Button.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document.
     */
    @FXML
    private void addProductBtn(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/addProductForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        resetInit();
    }

    /**
     * Delete Part.
     * @param event Mouse click event handler for delete Part Button.
     * @exception IOException Main.initErrorMessageBox() throws an IOException if it cannot lad the FXML file.
     */
    @FXML
    private void deletePartBtn(MouseEvent event) throws IOException {
            Part tempPart = partsTable.getSelectionModel().getSelectedItem();
            if (tempPart == null) {
                Main.initErrorMessageBox("You must select a part from the table in order to delete it.");
            } else {
                partsTable.getSelectionModel().select(null);
                Main.inv.deletePart(tempPart);
                resetInit();
            }
    }

    /**
     * Delete Product.
     * @param event Mouse click event handler for delete Product Button.
     * @exception IOException Main.initErrorMessageBox() throws an IOException if it cannot lad the FXML file.
     */
    @FXML
    private void deleteProductBtn(MouseEvent event) throws IOException {
        Product tempProduct = productsTable.getSelectionModel().getSelectedItem();
        if (tempProduct == null)
            Main.initErrorMessageBox("You must select a product from the table in order to delete it.");
        else if (!tempProduct.getAllAssociatedParts().isEmpty()) {
            Main.initErrorMessageBox("You have Associated parts with this product and must be removed in order to delete product.");
        }
        else {
            productsTable.getSelectionModel().select(null);
            Main.inv.deleteProduct(tempProduct);
            resetInit();
        }
    }


    /**
     * Closes program.
     * @param event Mouse click event handler to exit program.
     * @exception IOException Throws a Runtime Exception if it cannot close the program.
     */
    @FXML
    private void exitProgramBtn(MouseEvent event) throws IOException {
        System.exit(0);
    }

    /**
     * Modify Part.
     * @param event Mouse click event handler to modify product.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no part is selected from the table. The error will be caught and a message dialog box will appear.
     */
    @FXML
    private void modifyPartBtn(MouseEvent event) throws IOException {
        try {
            ModifyPartController.initPart(partsTable.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyPartForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch(IOException e) {
            Main.initErrorMessageBox("You must select a part from the table in order to modify it.");
        }

    }

    /**
     * Modify Product.
     * @param event Mouse click event handler to modify product.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no product is selected from the table. The error will be caught and a message dialog box will appear.
     */
    @FXML
    private void modifyProductBtn(MouseEvent event) throws IOException {
        try {
            ModifyProductController.initPart(productsTable.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyProductForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (IOException e) {
            Main.initErrorMessageBox("You must select a product from the table in order to modify it.");
        }
    }

    /**
     * Search Part.
     * I had to check if tempList is null because the function lookupProduct() returns an observable list or null if not products are found. As well, I had to check if the first element in the observable list is null because the .add() function returns a product or null if not found.
     * @param event Keyboard Enter event handler to search for product. Once found, the table will populate with found parts. If none is found, it will throw error message.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no product is selected from the table. The error will be caught and a message dialog box will appear.
     */
    @FXML
    private void searchPartKeyPressed(KeyEvent event) throws IOException {

            if (event.getCode().equals(KeyCode.ENTER)) {
                ObservableList<Part> tempList = FXCollections.observableArrayList();

                if (searchPartField.getText().trim().isEmpty())
                    resetInit();
                else {
                    if (isNumber(searchPartField.getText().trim())) {
                        tempList.add(Main.inv.lookupPart(Integer.parseInt(searchPartField.getText().trim())));
                        if (tempList.get(0) == null) {
                            Main.initErrorMessageBox("The item you are looking for does not exist.");
                        }
                    }
                    else {
                        tempList = Main.inv.lookupPart(searchPartField.getText().trim());
                        if (tempList == null) {
                            Main.initErrorMessageBox("The item you are looking for does not exist.");
                        }
                    }
                    if (tempList == null || tempList.get(0) == null)
                        resetInit();
                    else
                        setPartsTable(tempList);
                }
            }


    }

    /**
     * Search Product.
     * @param keyEvent Keyboard Enter event handler to search for product. Once found, the table will populate with found parts. If none is found, it will throw error message.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no product is selected from the table. The error will be caught and a message dialog box will appear.
     * I had to check if tempList is null because the function lookupProduct() returns an observable list or null if not products are found. As well, I had to check if the first element in the observable list is null because the .add() function returns a product or null if not found.
     */
    @FXML
    private void searchProductKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ObservableList<Product> tempList = FXCollections.observableArrayList();

            if (searchProductField.getText().trim().isEmpty())
                resetInit();
            else {
                if (isNumber(searchProductField.getText().trim())) {
                    tempList.add(Main.inv.lookupProduct(Integer.parseInt(searchProductField.getText().trim())));
                    if (tempList.get(0) == null) {
                        Main.initErrorMessageBox("The item you are looking for does not exist.");
                    }
                }
                else {
                    tempList = Main.inv.lookupProduct(searchProductField.getText().trim());
                    if (tempList == null) {
                        Main.initErrorMessageBox("The item you are looking for does not exist.");
                    }
                }

                if(tempList == null || tempList.get(0) == null) {
                    resetInit();
                }
                else
                    setProductsTable(tempList);
            }
        }
    }

    /**
     * Clear part search field.
     * @param mouseEvent Mouse click event handler to clear part search field.
     */
    @FXML
    private void clearPartTextField(MouseEvent mouseEvent) {
        searchPartField.setText("");
    }

    /**
     * Clear product search field.
     * @param mouseEvent Mouse click event handler to clear product search field.
     */
    @FXML
    private void clearProductTextField(MouseEvent mouseEvent) {
        searchProductField.setText("");
    }

    /**
     * Resets tables to default.
     * @param url Used to initialize class.
     * @param resourceBundle Used to initialize class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetInit();
    }

    /**
     * Sets parts table.
     * @param list Passes in an observable list to set the Parts Table with.
     */
    private void setPartsTable(ObservableList<Part> list) {
        partsTable.setItems(list);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Sets products table.
     * @param list2 Passes in an observable list to set the Products Table with.
     */
    private void setProductsTable(ObservableList<Product> list2) {
        productsTable.setItems(list2);

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Checks to see if all characters in the string are numbers and return a boolean.
     * @param isNum String to check whether a string is a whole number.
     * @return Return false is one character is not a number, return true if all characters are numbers.
     */
    private boolean isNumber(String isNum) {
        for (int i = 0; i < isNum.length(); i++)
            if (!Character.isDigit(isNum.charAt(i)))
                return false;
        return true;
    }

    /**
     * Resets tables and text fields to empty.
     */
    private void resetInit() {
        setPartsTable(Main.inv.getAllParts());
        setProductsTable(Main.inv.getAllProducts());
        searchPartField.setText("Enter ID or Part Name");
        searchProductField.setText("Enter ID or Product Name");
    }
}

