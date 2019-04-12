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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    //remember all need own FXML overide
    @FXML
    private Button button;
    @FXML
    private VBox vbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        button.setOnAction((ActionEvent e) -> {
            System.out.println("ok");
            vbox.requestFocus();
            try {
                goToMain(e);
            } catch (IOException ex) {
                System.out.println("something went wrong");
            }
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
