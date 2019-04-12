package gr14bosted;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Persistens.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private Connect con = new Connect(Connect.BOSTED_URL, "root", "");
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        button.setOnAction((ActionEvent e) -> {
            vbox.requestFocus();
            
            try {
                con.openConnection();
                ResultSet rs = con.query("SELECT * FROM users WHERE email = \"" + username.getText() + "\" AND pass = \"" + password.getText() + "\";");
                if (rs.next()) {                    
                    System.out.println(rs.getString("prime"));
                    
                    goToMain(e);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String user = username.getText();
            
           /* try {
                goToMain(e);
            } catch (IOException ex) {
                System.out.println("something went wrong");
            }*/
        });
    }

    private void goToMain(ActionEvent event) throws IOException {
        Parent MainFXMLParent = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
        Scene main = new Scene(MainFXMLParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(main);
        window.show();
    }
}
