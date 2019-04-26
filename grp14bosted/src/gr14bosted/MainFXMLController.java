package gr14bosted;

import Domain.Diary;
import Domain.Facade;
import Domain.User;
import Domain.Ward;
import Domain.Resident;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private TextArea diaryTA;
    @FXML
    private DatePicker diaryDate;
    @FXML
    private ListView<Resident> residentsLV;
    @FXML
    private ListView<Diary> diariesLV;
    @FXML
    private ChoiceBox topicCB;

    private ObservableList<Resident> residents;
    private ObservableList<Diary> diarys;
    
    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        splitPane.lookupAll(".split-pane-divider").stream()
                .forEach(div -> div.setMouseTransparent(true));

        buttonRead.setOnAction((ActionEvent e) -> {
            setVisablePane(paneRead);
            
            if (selectedResidentUuid() != null) {
                diariesLV.setItems(facade.getResidentdiaries(selectedResidentUuid()));
            }
            
        });

        buttonWrite.setOnAction((ActionEvent e) -> {
            setVisablePane(paneWrite);
        });

        buttonMedicine.setOnAction((ActionEvent e) -> {
            setVisablePane(paneMedicine);
        });
        buttonWard.setOnAction((ActionEvent e) -> {
            //to do
            animWardMenu();
        });

        buttonSubmit.setOnAction((ActionEvent e) -> {
            
            if (topicCB.getValue() != null && diaryTA.getText() != null && selectedResidentUuid() != null) {
                if (diaryDate.getValue()== null) {
                    facade.addDiaryEntry(selectedResidentUuid(), selectedTopic(), diaryTA.getText());
                } else {
                    facade.addDiaryEntry(selectedResidentUuid(), selectedTopic(), diaryTA.getText(), diaryDate.getValue());
                }
            }

        });

        //Write Diary pane
        topicCB.setItems(FXCollections.observableArrayList(
                "Sut", "min", "fede", "finger"
        ));

        residents = facade.getResidents();
        residentsLV.setItems(residents);
        
        diarys = FXCollections.observableArrayList();
        diariesLV.setItems(diarys);

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

    public void setFacade(Facade f) {
        this.facade = f;
    }
    
    private UUID selectedResidentUuid(){
        if (residentsLV.selectionModelProperty().getValue().getSelectedItem() != null) {
            return residentsLV.selectionModelProperty().getValue().getSelectedItem().getID();
        }
        return null;
    }
    private String selectedTopic(){
        return (String) topicCB.getSelectionModel().getSelectedItem();
    }
    private void setVisablePane (Pane p){
        paneMedicine.setVisible(false);
        paneDiary.setVisible(false);
        paneRead.setVisible(false);
        p.setVisible(true);
    }
}
