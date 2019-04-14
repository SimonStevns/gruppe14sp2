package gr14bosted;

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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        button.setOnAction((ActionEvent e) -> {
            vbox.requestFocus();
            
            try {
                bostedCon.openConnection();
                ResultSet rs = bostedCon.query("SELECT * FROM users WHERE email = \"" + username.getText() + "\" AND pass = \"" + password.getText() + "\";");
                if (rs.next()) {                    
                    System.out.println(rs.getString("prime"));
                    Main.showMain(null);
                } else {
                    displayError("E-mail og/eller kode er ugyldig");
                }
            } catch (SQLException ex) {
                displayError("Tjek venligst din forbindelse til databasen");
            } catch (IOException ex){
                displayError("Noget gik galt, prøv igen");
            }
            String user = username.getText();
            
        });
        
        errorLabel.setVisible(false);
    }
    
    private void displayError(String error){
        errorLabel.setVisible(true);
        errorLabel.setText(error);
    }
    
    private User rsToUser(){
        boolean[] b = {true,true,true,true,true,true};
        Privileges w1p = new Privileges(b);
        //User user = new User(w1p, "Simon Stevns","Simon@test.dk", "test", "88888888", new Ward(wardnumber, null, null),new ArrayList<>());
        
        return null;
    }
}
