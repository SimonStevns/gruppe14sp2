package gr14bosted;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

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
        });
        
    }
}
