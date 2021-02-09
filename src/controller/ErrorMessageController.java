package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Class that controls message dialog box. It is initiated with a string which describes the error.
 * @author Kenly Berkowitz
 */
public class ErrorMessageController implements Initializable {

    public static String string;

    @FXML
    public Button okBtn;

    @FXML
    public Label messageLabel;

    /**
     * Initializes error text message.
     * @param url Used to initialize class.
     * @param resourceBundle Used to initialize class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel.setText(string);
    }

    /**
     * Initializes and sets passed in string.
     * @param str Sets the String that is the message to be displayed.
     */
    public static void initString(String str) {
        string = str;
    }

    /**
     * Closes window.
     * @param mouseEvent Mouse event handler that exits the error message dialog box.
     */
    @FXML
    public void exitMessageBtn(MouseEvent mouseEvent) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
}
