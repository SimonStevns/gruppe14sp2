package gr14bosted;

import Domain.Diary;
import Domain.Facade;
import Domain.Resident;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
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
    private TextArea diaryTA,journalTA;
    @FXML
    private DatePicker diaryDate;
    @FXML
    private ListView<Resident> residentsLV;
    @FXML
    private ListView<Diary> diariesLV;
    @FXML
    private ChoiceBox topicCB;
    @FXML
    private TextField customTopicTF, searchField;

    private ObservableList<Resident> residents, residentsSearch;
  
    private ObservableList<Diary> diarys;

    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        residents = facade.getCurrentResidents();
        residentsSearch = facade.getCurrentResidents();
        residentsLV.setCellFactory(residentListView -> new ListCell<Resident>(){
            private ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Resident resident, boolean empty){
                super.updateItem(resident, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (resident.getImage() == null) {
                    setText(resident.getName());
                    setGraphic(null);
                } else {
                    imageView.setImage(resident.getImage());
                    setText(resident.getName());
                    setGraphic(imageView);
                }
            }
        });
        
        residentsLV.setItems(residentsSearch);
        residentsLV.getSelectionModel().selectFirst();

        splitPane.lookupAll(".split-pane-divider").stream()
                .forEach(div -> div.setMouseTransparent(true));

        buttonRead.setOnAction((ActionEvent e) -> {
            setVisablePane(paneRead);

            if (selectedResident() != null) {
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

        //Write Diary pane
        buttonSubmit.setOnAction((ActionEvent e) -> {
            if (validateInputDiary("Dagbog", diaryTA.getText(), 1000)) {
                if (topicCB.getValue() != null && selectedResident() != null && !diaryTA.getText().isEmpty()) {
                    facade.addDiaryEntry(selectedResidentUuid(), selectedTopic(), diaryTA.getText(), diaryDate.getValue());
                    clearDiary();
                    goBack();
                    showDialogAutoClose("Dagbog tilføjet", "For beboeren " + selectedResident().getName(), 2d);
                } else {
                    showDialog("Fejl", "Udfyld venligst alle felter");
                }
            }
        });
        diaryTA.setWrapText(true);

        topicCB.setItems(FXCollections.observableArrayList(
                "Fritid", "Familie", "Medicin", new Separator(), "Andet: "
        ));
        topicCB.getSelectionModel().selectFirst();

        topicCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                if (newValue.intValue() + 1 == topicCB.getItems().size()) {
                    customTopicTF.setVisible(true);
                } else {
                    customTopicTF.setVisible(false);
                }
            }
        });

        diarys = FXCollections.observableArrayList();
        diariesLV.setItems(diarys);

        diaryDate.setValue(LocalDate.now());
        diaryDate.setShowWeekNumbers(true);
        journalTA.setWrapText(true);
    }
    
    public void searchUpdate(){
        residentsSearch.clear();
        for (Resident i : residents){
            if(i.getName().toUpperCase().contains(searchField.getText().toUpperCase()))
                residentsSearch.add(i);
            residentsLV.setItems(residentsSearch);
        }
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

    public void goBack() {
        setVisablePane(paneDiary);
        showJournal();
    }
    public void showJournal() {
        journalTA.clear();
        Resident res = residentsLV.getSelectionModel().getSelectedItem();  
        journalTA.appendText(facade.getjournal(res.getCPR()));
    }

    private Resident selectedResident() {
        if (residentsLV.selectionModelProperty().getValue().getSelectedItem() != null) {
            return residentsLV.selectionModelProperty().getValue().getSelectedItem();
        }
        return null;
    }

    private UUID selectedResidentUuid() {
        if (selectedResident() != null) {
            return selectedResident().getID();
        }
        return null;
    }

    private String selectedTopic() {
        if (customTopicTF.isVisible() && customTopicTF.getText() != null) {
            return customTopicTF.getText();
        }

        return (String) topicCB.getSelectionModel().getSelectedItem();
    }

    private void setVisablePane(Pane p) {
        paneMedicine.setVisible(false);
        paneDiary.setVisible(false);
        paneRead.setVisible(false);
        paneWrite.setVisible(false);
        p.setVisible(true);
    }

    private void clearDiary() {
        topicCB.getSelectionModel().selectFirst();
        customTopicTF.clear();
        diaryTA.clear();
        diaryDate.setValue(LocalDate.now());
    }

    private void showDialog(String titel, String dialog) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setContentText(dialog);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.show();
    }

    private void showDialogAutoClose(String titel, String dialog, double duration) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setContentText(dialog);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(duration));
        delay.setOnFinished(event -> alert.close());
        delay.play();
    }

    private boolean validateInputDiary(String inputName, String input, int length) {
        if (input.length() > length) {
            showDialog("Fejl ved oprettelse", "Dagbogen er for lang, den skal fylde mindre end 1000 tegn");
        }
        return input.length() <= length;

    }

}
