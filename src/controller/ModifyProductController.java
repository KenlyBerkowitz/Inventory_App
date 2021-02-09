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
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller class for modifyProductForm.fxml
 * @author Kenly Berkowitz
 *
 */
public class ModifyProductController implements Initializable {

    private ObservableList<Part> tempAssociatedList = FXCollections.observableArrayList();

    public static Product passedProduct = null;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    public TableColumn<Part, Integer> partIdTopCol;

    @FXML
    public TableColumn<Part, String> partNameTopCol;

    @FXML
    public TableColumn<Part, Integer> partInventoryTopCol;

    @FXML
    public TableColumn<Part, Double> partCostTopCol;

    @FXML
    private TableView<Part> associatedPartsTable;
    
    @FXML
    public TableColumn<Part, Integer> partIdBottomCol;

    @FXML
    public TableColumn<Part, String> partNameBottomCol;

    @FXML
    public TableColumn<Part, Integer> partInventoryBottomCol;

    @FXML
    public TableColumn<Part, Double> partCostBottomCol;
   

    @FXML
    private TextField partIdSearchField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField inventoryField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField maxField;

    @FXML
    private TextField minField;

    /**
     * Initializes part that is passed in.
     * @param passProduct Product used to set controller variable passedProduct.
     */
    public static void initPart(Product passProduct) {
        passedProduct = passProduct;
    }

    /**
     * I had to check if tempList is null because the function lookupProduct() returns an observable list or null if not products are found. As well, I had to check if the first element in the observable list is null because the .add() function returns a product or null if not found.
     * @param keyEvent Key event Enter handler to listen for Enter key to be pressed. Function will then search for part.
     * @exception IOException Loader throws a Runtime Exception if it cannot retrieve the FXML document. The initPart() will also throw a IOException if no product is selected from the table. The error will be caught and a message dialog box will appear.
     */
    @FXML
    public void searchFieldBtn(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ObservableList<Part> tempList = FXCollections.observableArrayList();

            if (partIdSearchField.getText().trim().isEmpty()){
                setPartsTable(Main.inv.getAllParts());
            }
            else {
                if (isNumber(partIdSearchField.getText().trim())) {
                    tempList.add(Main.inv.lookupPart(Integer.parseInt(partIdSearchField.getText().trim())));
                    if (tempList.get(0) == null) {
                        Main.initErrorMessageBox("The item you are looking for does not exist.");
                    }
                } else {
                    tempList = Main.inv.lookupPart(partIdSearchField.getText().trim());
                    if (tempList == null) {
                        Main.initErrorMessageBox("The item you are looking for does not exist.");
                    }
                }

                if(tempList == null || tempList.get(0) == null) {
                    setPartsTable(Main.inv.getAllParts());
                    setAssociatedPartsTable(tempAssociatedList);
                    setFields();
                }
                else
                    setPartsTable(tempList);
            }
        }
    }

    /**
     * Adds a part to the associated list.
     * @param event Mouse event handler to for add associated part button.
     */
    @FXML
    void addPartBtn(MouseEvent event) {
        Part tempPart = partsTable.getSelectionModel().getSelectedItem();
        if (tempPart != null && isDuplicatePart(tempPart))
            tempAssociatedList.add(tempPart);
    }

    /**
     * Closes window.
     * @param event Mouse event handler to for cancel button.
     */
    @FXML
    void cancelBtn(MouseEvent event) {
        closeWindow();

    }

    /**
     * Removes part from associated list in part.
     * @param event Mouse event handler to remove associated part button.
     */
    @FXML
    void removePartBtn(MouseEvent event) {
        Part tempPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        tempAssociatedList.remove(tempPart);
    }

    /**
     * Creates and updates inventory.
     * @param event Mouse event handler to save modified product.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @exception NumberFormatException Runtime error may occur if the method tries to parse a value from a text field that does not belong. Error will be caught and error message dialog box will display. In the future, it would be better to do each validation in a separate function and return the value needed there or return booleans for each and evaluate in an IF statement.
     */
    @FXML
    void saveBtn(MouseEvent event) throws IOException {
        try
        {
            //starts validation with the minInventory and maxInventory and then sets all the fields and creates new object
            if((isValid("min")) && (isValid("max")) && isStockBetweenMaxMin()) {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(inventoryField.getText().trim());
                int min = Integer.parseInt(minField.getText().trim());
                int max = Integer.parseInt(maxField.getText().trim());

                //create new temp Product Object to insert into DB
                Product product = new Product(passedProduct.getId(), name, price, stock, min, max);

                //check if the tempAssociated list is not empty and adds a part to new product object
                if(!tempAssociatedList.isEmpty()){
                    for (Part tempPart: tempAssociatedList){
                        product.addAssociatedPart(tempPart);
                    }
                }

                //checks if it is a duplicate product name, if not, add the part to inventory
                if (!product.getName().equals(passedProduct.getName())) {
                    if (isDuplicateProductName(product.getName())) {
                        for (int i = 0; i < Main.inv.getAllProducts().size(); i++) {
                            if (passedProduct.getId() == Main.inv.getAllProducts().get(i).getId())
                                Main.inv.updateProduct(i, product);
                        }
                    }
                }
                else {
                    for (int i = 0; i < Main.inv.getAllProducts().size(); i++) {
                        if (passedProduct.getId() == Main.inv.getAllProducts().get(i).getId())
                            Main.inv.updateProduct(i, product);
                    }
                }
                    closeWindow();
            }
            else {
                setFields();
                Main.initErrorMessageBox("Inventory/Max/Min  Inventory max must be <= 20. Inventory min must be >= 1. Inv must be between the two.");
            }
        }
        catch(NumberFormatException | IOException e)
        {
            Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
        }
        finally
        {
            setFields();
        }
        closeWindow();


    }

    /**
     * Initializes tempAssociatedList, and sets tableviews and text fields
     * @param url Used to initialize class.
     * @param resourceBundle Used to initialize class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTempAssociatedList();
        setPartsTable(Main.inv.getAllParts());
        setAssociatedPartsTable(tempAssociatedList);
        setFields();
    }

    /**
     * Sets Parts table.
     * @param list Pass in observable list of type Part to set parts table.
     */
    private void setPartsTable(ObservableList<Part> list) {
        partsTable.setItems(list);

        partIdTopCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryTopCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostTopCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Sets associated parts table
     * @param list Pass in observable list of type Part to set associated parts table.
     */
    private void setAssociatedPartsTable(ObservableList<Part> list) {
        associatedPartsTable.setItems(list);

        partIdBottomCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameBottomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryBottomCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostBottomCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Resets fields to initial state.
     */
    public void setFields() {
        idField.setText(String.valueOf(passedProduct.getId()));
        nameField.setText(passedProduct.getName());
        inventoryField.setText(String.valueOf(passedProduct.getStock()));;
        priceField.setText(String.valueOf(passedProduct.getPrice()));
        maxField.setText(String.valueOf(passedProduct.getMax()));
        minField.setText(String.valueOf(passedProduct.getMin()));;
    }

    /**
     * Checks main.inv whether a product name is a duplicate within.
     * @param productName Product name to check to see if it is a duplicate part name in main inventory.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @return Returns false if it is a duplicate name that exists in the main inventory. Otherwise, it returns true.
     */
    private boolean isDuplicateProductName(String productName) throws IOException {
        for(Product product1: Main.inv.getAllProducts()) {
            if (product1.getName().contains(productName)){
                //error messageBox
                Main.initErrorMessageBox("Cannot use duplicate name.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to make sure to not add duplicate parts inside tempAssociatedList.
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
     * Initializes tempList from the passed product associated list.
     */
    private void initTempAssociatedList() {
        tempAssociatedList.addAll(passedProduct.getAllAssociatedParts());
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
     * Clear search field.
     * @param mouseEvent Mouse event handler to clear search field.
     */
    public void clearSearchField(MouseEvent mouseEvent) {
        partIdSearchField.setText("");
    }

    /**
     * Closes window.
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks to see if min and max are valid inputs.
     * @param minMaxValue String to determine if min or max is being evaluated.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @return True if min is > 0 and less than max, else return false. Same for max evaluation.
     */
    private boolean isValid(String minMaxValue) throws IOException {
        boolean b = false;
        if (minMaxValue.equals("min")) {
            try
            {
                int minMax = Integer.parseInt(minField.getText().trim());
                if (minMax > 0 && minMax < Integer.parseInt(maxField.getText().trim()))
                    b = true;
                else {
                    minField.setStyle("-fx-border-color: red");
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
                int minMax = Integer.parseInt(maxField.getText().trim());
                if (Integer.parseInt(minField.getText().trim()) < minMax && minMax <= 20)
                    b = true;
                else {
                    maxField.setStyle("-fx-border-color: red");
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
     * Checks to see if the stock is between max and min.
     * @return True if stock is between min and max variables, else return false.
     */
    private boolean isStockBetweenMaxMin() {
        int stock = Integer.parseInt(inventoryField.getText().trim());
        int min = Integer.parseInt(minField.getText().trim());
        int max = Integer.parseInt(maxField.getText().trim());

        return stock >= min && stock <= max;
    }


}
