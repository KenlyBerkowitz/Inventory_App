package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import main.Main;
import model.InHouse;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller class for modifyPartForm.fxml
 * @author Kenly Berkowitz
 */
public class ModifyPartController implements Initializable {

    public static Part passedPart = null;

    @FXML
    private RadioButton inHouseRadioBtn;

    @FXML
    private ToggleGroup toggleButtonGroup;

    @FXML
    private RadioButton outsourcedRadioBtn;

    @FXML
    private TextField modifyPartID;

    @FXML
    private TextField partName;

    @FXML
    private TextField inventoryQuantity;

    @FXML
    private TextField priceCost;

    @FXML
    private TextField maxInventory;

    @FXML
    private TextField minInventory;

    @FXML
    private TextField typeIDField;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    /**
     * Closes window.
     * @param mouseEvent Mouse event handler to close window.
     */
    public void cancelBtn(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates and updates inventory.
     * @param mouseEvent Mouse event handler to save modified Part.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @exception NumberFormatException Runtime error may occur if the method tries to parse a value from a text field that does not belong. Error will be caught and error message dialog box will display. In the future, it would be better to do each validation in a separate function and return the value needed there or return booleans for each and evaluate in an IF statement.
     */
    public void saveBtn(MouseEvent mouseEvent) throws IOException {
        try
        {
            //clears the styles for the errors
            maxInventory.setStyle("-fx-border-color: none");
            minInventory.setStyle("-fx-border-color: none");

            //starts validation with the minInventory and maxInventory and then sets all the fields and creates new object
            if((isValid("min")) && (isValid("max")) && isStockBetweenMaxMin()) {

                String name = partName.getText().trim();
                double price = Double.parseDouble(priceCost.getText().trim());
                int stock = Integer.parseInt(inventoryQuantity.getText().trim());
                int min = Integer.parseInt(minInventory.getText().trim());
                int max = Integer.parseInt(maxInventory.getText().trim());

                //condition to determine what kind on object to create and put into the DB
                if (inHouseRadioBtn.isSelected()) {
                    int idCompanyName = Integer.parseInt(typeIDField.getText().trim());
                    InHouse part = new InHouse(passedPart.getId(), name, price, stock, min, max, idCompanyName);

                    if (!part.getName().equals(passedPart.getName())) {
                        if (isDuplicatePartName(part.getName())) {
                            for (int i = 0; i < Main.inv.getAllParts().size(); i++) {
                                if (passedPart.getId() == Main.inv.getAllParts().get(i).getId())
                                    Main.inv.updatePart(i, part);
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < Main.inv.getAllParts().size(); i++) {
                            if (passedPart.getId() == Main.inv.getAllParts().get(i).getId())
                                Main.inv.updatePart(i, part);
                        }
                    }
                }
                else if (outsourcedRadioBtn.isSelected()) {
                    String idCompanyName = typeIDField.getText().trim();
                    Outsourced part = new Outsourced(passedPart.getId(), name, price, stock, min, max, idCompanyName);

                    if (!part.getName().equals(passedPart.getName())) {
                        if (isDuplicatePartName(part.getName())) {
                            for (int i = 0; i < Main.inv.getAllParts().size(); i++) {
                                if (passedPart.getId() == Main.inv.getAllParts().get(i).getId())
                                Main.inv.updatePart(i, part);
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < Main.inv.getAllParts().size(); i++) {
                            if (passedPart.getId() == Main.inv.getAllParts().get(i).getId())
                                Main.inv.updatePart(i, part);

                        }
                    }
                }

                //closes the window
                closeWindow();
            }
            else {
                resetFields();
                Main.initErrorMessageBox("Inventory/Max/Min  Inventory max must be <= 20. Inventory min must be >= 1. Inv must be between the two.");
            }
        }
        catch(NumberFormatException | IOException e)
        {
            Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
        }
    }

    /**
     * Sets fields.
     * @param url Used to initialize class.
     * @param resourceBundle Used to initialize class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetFields();

    }

    /**
     * Initializes passed part.
     * @param passPart Part used to set controller variable passedPart.
     */
    public static void initPart(Part passPart) {
        passedPart = passPart;
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
            catch (NumberFormatException e) {
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
            catch (NumberFormatException | IOException e)
            {
                Main.initErrorMessageBox("Please ensure all fields are the correct types. ");
            }
        }
        return b;
    }

    /**
     * Closes window.
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks main.inv whether a product name is a duplicate within.
     * @param partName Product name to check to see if it is a duplicate part name in main inventory.
     * @exception IOException Main.initErrorMessageBox throws a Runtime Exception if it cannot retrieve the FXML document inside that class. The error will be caught and a message dialog box will appear.
     * @return Returns false if it is a duplicate name that exists in the main inventory. Otherwise, it returns true.
     */
    private boolean isDuplicatePartName(String partName) throws IOException {
        for(Part part1: Main.inv.getAllParts()) {
            if (part1.getName().contains(partName)){
                //error messageBox
                Main.initErrorMessageBox("Cannot use duplicate name.");
                return false;
            }
        }
        return true;
    }

    /**
     * Resets fields to initial state.
     */
    public void resetFields() {
        if (passedPart instanceof InHouse) {
            inHouseRadioBtn.setSelected(true);
            typeIDField.setText(String.valueOf(((InHouse) passedPart).getMachineID()));
        }
        else {
            outsourcedRadioBtn.setSelected(true);
            typeIDField.setText(((Outsourced) passedPart).getCompanyName());
        }

        modifyPartID.setText(String.valueOf(passedPart.getId()));
        partName.setText(passedPart.getName());
        inventoryQuantity.setText(String.valueOf(passedPart.getStock()));;
        priceCost.setText(String.valueOf(passedPart.getPrice()));
        maxInventory.setText(String.valueOf(passedPart.getMax()));
        minInventory.setText(String.valueOf(passedPart.getMin()));;
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


