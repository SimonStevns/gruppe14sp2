package gr14bosted;

import Domain.Facade;
import Domain.Privileges;
import Domain.User;
import Domain.Ward;
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
import Persistens.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private Connect bostedCon = new Connect(Connect.BOSTED_URL, "root", "");
    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        button.setOnAction((ActionEvent e) -> {
            vbox.requestFocus();

            try {
                if (facade.setUser(username.getText(), password.getText())) {
                    Main.showMain(facade);
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
}
