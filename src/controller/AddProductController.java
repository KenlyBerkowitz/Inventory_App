package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for addProductForm.fxml
 * @author Kenly Berkowitz
 */
public class AddProductController implements Initializable {

    private ObservableList<Part> tempAssociatedList = FXCollections.observableArrayList();

    @FXML
    public TableView<Part> partTopTable;

    @FXML
    public TableColumn<Part, Integer> partIdTopCol;

    @FXML
    public TableColumn<Part, String> partNameTopCol;

    @FXML
    public TableColumn<Part, Integer> partInventoryTopCol;

    @FXML
    public TableColumn<Part, Double> partCostTopCol;

    @FXML
    public TableView<Part> associatedPartsTable;

    @FXML
    public TableColumn<Part, Integer> partIdBottomCol;

    @FXML
    public TableColumn<Part, String> partNameBottomCol;

    @FXML
    public TableColumn<Part, Integer> partInventoryBottomCol;

    @FXML
    public TableColumn<Part, Double> partCostBottomCol;

    @FXML
    private TextField searchField;

    @FXML
    private Button addAssociatedBtn;

    @FXML
    private Button removeAsscPart;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField idField;

    @FXML
    private TextField productName;

    @FXML
    private TextField inventoryQuantity;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField maxInventory;

    @FXML
    private TextField minInventory;

    /**
     * Searches for Part.
     * I had to check if tempList is null because the function lookupProduct() returns an observable list or null if not products are found. As well, I had to check if the first element in the observable list is null because the .add() function returns a product or null if not found.
     * @param keyEvent Key event Enter handler to listen for Enter key to be pressed. Function will then search for part.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no product is selected from the table. The error will be caught and a message dialog box will appear.
     */
    @FXML
    public void searchPart(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ObservableList<Part> tempList = FXCollections.observableArrayList();

            if (searchField.getText().trim().isEmpty()){
                resetInit();
            }
            else {
                if (isNumber(searchField.getText().trim())) {
                    tempList.add(Main.inv.lookupPart(Integer.parseInt(searchField.getText().trim())));
                    if (tempList.get(0) == null) {
                        Main.initErrorMessageBox("The item you are looking for does not exist.");
                    }
                } else {
                    tempList = Main.inv.lookupPart(searchField.getText().trim());
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
     * Adds part to associated list.
     * @param event Mouse event handler to for add associated part button.
     */
    @FXML
    private void addAssociatedBtn(MouseEvent event) {
        Part tempPart = partTopTable.getSelectionModel().getSelectedItem();
        if (tempPart != null && isDuplicatePart(tempPart))
            tempAssociatedList.add(tempPart);
    }

    /**
     * Closes window.
     * @param event Mouse event handler to for cancel button.
     */
    @FXML
    private void cancelBtn(MouseEvent event) {
        closeWindow();
    }

    /**
     * Remove associated part from observable list.
     * @param event Mouse event handler to remove associated part button.
     */
    @FXML
    private void removeAsscPart(MouseEvent event) {
        Part tempPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        tempAssociatedList.remove(tempPart);
    }


    /**
     * Creates and updates inventory.
     * @param event Mouse event handler to save Product.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @exception NumberFormatException Runtime error may occur if the method tries to parse a value from a text field that does not belong. Error will be caught and error message dialog box will display. In the future, it would be better to do each validation in a separate function and return the value needed there or return booleans for each and evaluate in an IF statement.
     */
    @FXML
    private void saveBtn(MouseEvent event) throws IOException {
        try
        {
            //clears the styles for the errors
            maxInventory.setStyle("-fx-border-color: none");
            minInventory.setStyle("-fx-border-color: none");

            //starts validation with the minInventory and maxInventory and then sets all the fields and creates new object
            if((isValid("min")) && (isValid("max")) && isStockBetweenMaxMin()) {
                String name = productName.getText().trim();
                double price = Double.parseDouble(productPrice.getText().trim());
                int stock = Integer.parseInt(inventoryQuantity.getText().trim());
                int min = Integer.parseInt(minInventory.getText().trim());
                int max = Integer.parseInt(maxInventory.getText().trim());

                //create new temp Product Object to insert into DB
                Product product = new Product(Main.inv.getGenProductID(), name, price, stock, min, max);

                //check if the tempAssociated list is not empty and adds a part to new product object
                if(!tempAssociatedList.isEmpty()){
                    for (Part tempPart: tempAssociatedList){
                        product.addAssociatedPart(tempPart);
                    }
                }

                //checks if it is a duplicate part name, if not, add the part to inventory
                if (isDuplicateProductName(product.getName()))
                    Main.inv.addProduct(product);

                //closes the window
                closeWindow();
            }
            else {
                clearFields();
                Main.initErrorMessageBox("Inventory/Max/Min  Inventory max must be <= 20. Inventory min must be >= 1. Inv must be between the two.");
            }
        }
        catch(NumberFormatException | IOException e)
        {
            Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
        }
        finally
        {
            clearFields();
        }
        closeWindow();
    }

    /**
     * Initializes bothe tableviews.
     * @param url Used to initialize class.
     * @param resourceBundle Used to initialize class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPartsTable(Main.inv.getAllParts());
        setAssociatedPartsTable(tempAssociatedList);
    }

    /**
     * Closes window.
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets parts table.
     * @param list Pass in observable list of type Part to set parts table.
     */
    private void setPartsTable(ObservableList<Part> list) {
        partTopTable.setItems(list);

        partIdTopCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryTopCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostTopCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Set associated parts table.
     * @param list Pass in Observable list of type Part to set associated parts table
     */
    private void setAssociatedPartsTable(ObservableList<Part> list) {
        associatedPartsTable.setItems(list);

        partIdBottomCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameBottomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryBottomCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostBottomCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Checks for a duplicate part in associated parts list.
     * @param partToAdd Part to check to see if it is a duplicate in the temporary object's associated list
     * @return False if it is in the associated list. Otherwise, return true.
     */
    private boolean isDuplicatePart(Part partToAdd) {
        for(Part part: tempAssociatedList) {
            if (part.getId() == partToAdd.getId())
                return false;
        }
        return true;
    }


    /**
     * Checks to see if min and max are valid inputs.
     * @param minMaxValue String to determine if min or max is being evaluated.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @return True if min is > 0 and less than max, else return false. Same for max evaluation
     */
    private boolean isValid(String minMaxValue) throws IOException {
        boolean b = false;
        if (minMaxValue.equals("min")) {
            try
            {
                int minMax = Integer.parseInt(minInventory.getText().trim());
                if (minMax > 0 && minMax < Integer.parseInt(maxInventory.getText().trim()))
                    b = true;
                else {
                    minInventory.setStyle("-fx-border-color: red");
                    Main.initErrorMessageBox("Min must be more than 0 and less than max.");
                }
            }
            catch (NumberFormatException | IOException e) {
                Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
            }
        }
        if (minMaxValue.equals("max")) {
            try
            {
                int minMax = Integer.parseInt(maxInventory.getText().trim());
                if (Integer.parseInt(minInventory.getText().trim()) < minMax && minMax <= 20)
                    b = true;
                else {
                    maxInventory.setStyle("-fx-border-color: red");
                    Main.initErrorMessageBox("Max must be less than 20 and more than max.");
                }
            }
            catch (NumberFormatException e)
            {
                Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
            }
        }
        return b;
    }

    /**
     * Clears all text fields.
     */
    private void clearFields() {
        productName.setText("");
        inventoryQuantity.setText("");
        productPrice.setText("");
        maxInventory.setText("");
        minInventory.setText("");
    }


    /**
     * Checks main.inv whether a product name is a duplicate within.
     * @param productName Product name to check to see if it is a duplicate part name in main inventory.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @return Returns false if it is a duplicate name that exists in the main inventory. Otherwise, it returns true.
     */
    private boolean isDuplicateProductName(String productName) throws IOException {
        for(Product product: Main.inv.getAllProducts()) {
            if (product.getName().contains(productName)){
                //error messageBox
                Main.initErrorMessageBox("Cannot use duplicate name.");
                return false;
            }
        }
        return true;
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
     * Resets part table and text field.
     */
    private void resetInit() {
        setPartsTable(Main.inv.getAllParts());
        searchField.setText("Enter ID or Part Name");

    }

    /**
     * Clears search field.
     * @param mouseEvent Clears search field.
     */
    public void clearSearchField(MouseEvent mouseEvent) {
        searchField.setText("");
    }

    /**
     * Checks to see if the stock is between max and min.
     * @return True if stock is between min and max variables, else return false.
     */
    private boolean isStockBetweenMaxMin() {
        int stock = Integer.parseInt(inventoryQuantity.getText().trim());
        int min = Integer.parseInt(minInventory.getText().trim());
        int max = Integer.parseInt(maxInventory.getText().trim());

        return stock >= min && stock <= max;
    }
}
