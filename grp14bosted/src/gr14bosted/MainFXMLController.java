package gr14bosted;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainFXMLController implements Initializable {

    private boolean showWardMenu = false;

    @FXML
    private Pane paneDiary, paneWrite, paneRead;
    @FXML
    private Button buttonWard, buttonWrite, buttonRead, buttonSubmit;
    @FXML
    private AnchorPane wardMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        buttonRead.setOnAction((ActionEvent e) -> {
            paneDiary.setOpacity(0);
            paneDiary.setDisable(true);
            System.out.println("read");
            paneRead.setOpacity(1);
            paneRead.setDisable(false);
        });
        buttonWrite.setOnAction((ActionEvent e) -> {
            paneDiary.setVisible(false);
            System.out.println("write");
            paneWrite.setVisible(true);
        });
        buttonWard.setOnAction((ActionEvent e) -> {
            //to do

            animWardMenu();
        });
        buttonSubmit.setOnAction((ActionEvent e) -> {
            //to do
        });

    }

    public void animWardMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000d), wardMenu);
        if (wardMenu.getTranslateX() == 0) {
            tt.setByX(200);
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.play();
        }else{
        tt.setByX(-wardMenu.getTranslateX());
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.setDuration(Duration.millis(1000d));
            tt.play();
        }
    }
}
