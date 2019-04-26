package gr14bosted;

import Domain.Facade;
import Domain.Privilege;
import Domain.Privileges;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class FXMLController implements Initializable {

    //remember all need own FXML overide
    @FXML
    private Button button;
    @FXML
    private VBox vbox;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;

    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        button.setOnAction((ActionEvent e) -> {
            vbox.requestFocus();

            try {
                if (facade.setUser(username.getText(), password.getText())) {
                    if (facade.hasPrivlege(Privilege.ADMIN)) {
                        selectShow();
                    } else {
                        Main.showMain(facade);
                    }
                } else {
                    displayError("E-mail og/eller kode er ugyldig");
                }
            } catch (SQLException ex) {
                displayError("Tjek venligst din forbindelse til databasen");
                ex.printStackTrace();
            } catch (IOException ex) {
                displayError("Noget gik galt, prøv igen");
                ex.printStackTrace();
            }

        });

        errorLabel.setVisible(false);
    }

    private void displayError(String error) {
        errorLabel.setVisible(true);
        errorLabel.setText(error);
    }
    
    private void selectShow(){
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Hvilken side vil du tilgå?");
            
            ButtonType buttonAdmin = new ButtonType("Admin");
            ButtonType buttonMain = new ButtonType("Hovedside");
            
            alert.getButtonTypes().setAll(buttonAdmin, buttonMain);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAdmin){
                    Main.showAdmin(facade);
            } else {
                Main.showMain(facade);
            }
        } catch (IOException ex) {}
    }
}
