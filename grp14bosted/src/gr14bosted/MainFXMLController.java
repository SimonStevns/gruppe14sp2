package gr14bosted;

import Domain.Facade;
import Domain.User;
import Domain.Ward;
import Domain.Resident;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainFXMLController implements Initializable {

    @FXML
    private Pane paneDiary, paneWrite, paneRead, paneMedicine;
    @FXML
    private Button buttonWard, buttonMedicine, buttonWrite, buttonRead, buttonSubmit;
    @FXML
    private AnchorPane wardMenu;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<Resident> residentsLV;
    //@FXML
    //private ChoiceBox topicCB;
    
    private ObservableList<Resident> residents = FXCollections.observableArrayList();
    private User user = null;
    private Ward currentWard = null;
    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        splitPane.lookupAll(".split-pane-divider").stream()
                .forEach(div -> div.setMouseTransparent(true));

        buttonRead.setOnAction((ActionEvent e) -> {
            paneMedicine.setVisible(false);
            paneDiary.setVisible(false);
            System.out.println("read");
            paneRead.setVisible(true);
        });

        buttonWrite.setOnAction((ActionEvent e) -> {
            paneDiary.setVisible(false);
            paneMedicine.setVisible(false);
            System.out.println("write");
            paneWrite.setVisible(true);
        });

        buttonMedicine.setOnAction((ActionEvent e) -> {
            paneDiary.setVisible(false);
            paneRead.setVisible(false);
            System.out.println("checkMedicine");
            paneMedicine.setVisible(true);
        });
        buttonWard.setOnAction((ActionEvent e) -> {
            //to do
            animWardMenu();
        });

        buttonSubmit.setOnAction((ActionEvent e) -> {
            //to do
        });
        
        
        //Write Diary pane
//        topicCB.setItems(FXCollections.observableArrayList(
//                "Sut", "min", "fed", "finger"
//        ));
        ObservableList<Resident> res = facade.getResidents();
        residentsLV.setItems(res);
        
        
    }

    public void animWardMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250d), wardMenu);
        if (wardMenu.getTranslateX() == 0) {
            tt.setByX(56);
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.play();
        } else {
            tt.setByX(-wardMenu.getTranslateX());
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.setDuration(Duration.millis(250d));
            tt.play();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCurrentWard(Ward ward) {
        this.currentWard = ward;
    }
}
