package gr14bosted;

import Domain.Diary;
import Domain.Facade;
import Domain.Prescription;
import Domain.Privilege;
import Domain.Resident;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainFXMLController implements Initializable {
    @FXML
    private Label residenceL;
    @FXML
    private Pane paneDiary, paneWrite, paneRead, paneMedicine;
    @FXML
    private Button buttonWard, buttonMedicine, buttonWrite, buttonRead, buttonSubmit, prescriptionBtn, diariesReadFull;
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
    private ListView<Prescription> prescriptionLV;
    @FXML
    private ChoiceBox topicCB;
    @FXML
    private TextField customTopicTF, searchField;
    
    private ObservableList<Resident> residents, residentsSearch;
  
    private ObservableList<Diary> diaries;
    private ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();
    
    private HashMap<CheckBox, Prescription> prescriptionCb = new HashMap();
    
    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // privlege checks
        if (facade.hasPrivlege(Privilege.VIEWALLDIARYS) || facade.hasPrivlege(Privilege.VIEWOWNDIARYS)){
            buttonRead.setVisible(true);
        }
        if (facade.hasPrivlege(Privilege.WRITEDIARY)) {
            buttonWrite.setVisible(true);
        }
        if (facade.hasPrivlege(Privilege.DRUGDISTRIBUTION)) {
            buttonMedicine.setVisible(true);
        }
        if (facade.hasPrivlege(Privilege.FINDJOURNAL)) {
            journalTA.setVisible(true);
        }
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

        buttonRead.setOnAction(e -> {
            setVisablePane(paneRead);

            if (selectedResident() != null) {
                diariesLV.setItems(facade.getResidentDiaries(selectedResidentUuid()));
            }
        });

        buttonWrite.setOnAction((ActionEvent e) -> {
            setVisablePane(paneWrite);
        });
        
        // Medicine pane
        buttonMedicine.setOnAction(e -> {
            setVisablePane(paneMedicine);
            prescriptions.clear();
            prescriptions.addAll(facade.getPrescriptions(residents));
        });
        
        prescriptionLV.setItems(prescriptions);
        prescriptionLV.setCellFactory((ListView<Prescription> prescriptionListView) -> new ListCell<Prescription>(){
            private final ImageView imageView = new ImageView();
            private final Label name = new Label();
            private final Label drug = new Label();
            private final CheckBox cb = new CheckBox();
            private final HBox hb = new HBox(imageView, name, drug, cb);
            @Override
            protected void updateItem(Prescription prescription, boolean empty){
                super.updateItem(prescription, empty);
                setText(null);
                if (empty) {
                    setGraphic(null);
                } else {
                    imageView.setImage(prescription.getImage());
                    
                    name.setText("\n    " + prescription.getName());
                    drug.setText(prescription.getDrugDescription() + "\n" + prescription.getDosage() + "\n" + prescription.getIndication());
                    drug.paddingProperty().setValue(new Insets(0,0,0,10));
                    
                    cb.setText("Udleveret");
                    cb.setSelected(true);
                    cb.paddingProperty().setValue(new Insets(25, 10, 10, 10));
                    bindCheckBox(cb, prescription);
                    
                    setGraphic(hb);
                }
            }
        });
        
        prescriptionBtn.setOnAction(e ->{
            String names ="Dagbøger tilføjet for: \n";
            String topic = "Medicin";
            
            for (CheckBox cb : prescriptionCb.keySet()) {
                Prescription prescription = prescriptionCb.get(cb);
                names += prescription.getName() + "\n";
                if (cb.isSelected()) {
                    facade.addDiaryEntry(prescription.getID(), topic, prescription.diaryString(), LocalDate.now());
                } else {
                    String reason = showInputTextDialog(null, "Hvorfår har " + prescription.getName() + " ikke fået " + prescription.getDrugDescription() + "?", "Årsag:");
                    facade.addDiaryEntry(prescription.getID(), topic, reason, LocalDate.now());
                }
            }
            goBack();
            showDialog(prescriptionCb.keySet().size() + " dagbøger tilføjet", names);
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

        diaries = FXCollections.observableArrayList();
        diariesLV.setItems(diaries);
        
        diariesReadFull.setOnAction(e ->{
            if (diariesLV.getSelectionModel().getSelectedItem() != null) {
                Diary diary = diariesLV.getSelectionModel().getSelectedItem();
                showDialog("Dagbog for " + diary.getDate(), "Emne:\n" + diary.getTopic() + "\nIndehold:\n " + diary.getText());
            }
        });
        
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
            tt.setByX(230);
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
        delay.setOnFinished(e -> alert.close());
        delay.play();
    }
    
    private String showInputTextDialog(String titel, String header, String content){
        String returnString = "";
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titel);
        dialog.setHeaderText(header);
        dialog.setContentText(content);       
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            returnString = result.get();
        }
        return returnString;
    }

    private boolean validateInputDiary(String inputName, String input, int length) {
        if (input.length() > length) {
            showDialog("Fejl ved oprettelse", "Dagbogen er for lang, den skal fylde mindre end 1000 tegn");
        }
        return input.length() <= length;
    }
    
    private void bindCheckBox(CheckBox cb, Prescription prescription){
        if (!prescriptionCb.containsValue(prescription)){
            this.prescriptionCb.putIfAbsent(cb, prescription);
        }
    }

}
