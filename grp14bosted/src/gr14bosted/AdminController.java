package gr14bosted;

import Domain.Facade;
import Domain.PassChecker;
import Domain.Residence;
import Domain.Ward;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AdminController implements Initializable {

    @FXML
    private TextField userName, userEmail, userPhone, userPass, resPhone, resEmail, resName, resCPR;
    @FXML
    private TextField residenceName, residenceAddress, residencePhone, residenceEmail, wardName, wardDescription;
    @FXML
    private CheckBox privOwn, privAll, privFind, privWrite, privDrugs, privAdmin;
    @FXML
    private ChoiceBox wardCB, userResidenceCB, userWardCB, residentResidenceCB, residentWardCB;
    @FXML
    private Button btnCreateResidence, btnCreateUser, btnCreateWard, btnCreateRes, resSelectPic, userSelectPic;

    private ObservableList<Ward> userWards = FXCollections.observableArrayList();
    private ObservableList<Ward> residentWards = FXCollections.observableArrayList();
    private ObservableList<Residence> residences = FXCollections.observableArrayList();
    
    private File userImage, resImage = null;

    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCreateResidence.setOnAction(e -> {
            if (residenceAllFieldsfilled() && validateInput("navn", residenceName.getText(), 255) && validateInput("adresse", residenceAddress.getText(), 255) && validateInput("telefonnummer", residencePhone.getText(), 255) && validateInput("email", residenceEmail.getText(), 255)) {
                facade.addResidence(residenceName.getText(), residenceAddress.getText(), residencePhone.getText(), residenceEmail.getText());
                residenceCreated();
            }
        });

        btnCreateWard.setOnAction(e -> {
            if (wardAllFieldsfilled() && validateInput("beskrivelse", wardDescription.getText(), 255) && validateInput("navn", wardName.getText(), 255)) {
                Residence r = (Residence) wardCB.getValue();
                facade.newWard(r.getId().toString(), wardDescription.getText(), wardName.getText());
                wardCreated();
            }
        });

        //ChoiceBoxes
        ObservableList<Residence> residences = facade.getResidences();
        ChoiceBox[] cbs = {wardCB, userResidenceCB, residentResidenceCB};
        for (ChoiceBox cb : cbs) {
            cb.setItems(residences);
            cb.getSelectionModel().selectFirst();
        }
        
        userWards = facade.getWards(residences.get(0).getId().toString());
        residentWards = facade.getWards(residences.get(0).getId().toString());
        userWardCB.setItems(userWards);
        residentWardCB.setItems(residentWards);

        userResidenceCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                userWards = facade.getWards(residences.get(newValue.intValue()).getId().toString());
                userWardCB.setItems(userWards);
            }
        });

        residentResidenceCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                residentWards = facade.getWards(residences.get(newValue.intValue()).getId().toString());
                residentWardCB.setItems(userWards);
            }
        });

        btnCreateRes.setOnAction((ActionEvent e) -> {
            if (residentAllFieldsfilled() && validateInput("navn", resName.getText(), 255) && validateInput("telefonnummer", resPhone.getText(), 255) && validateInput("email", resEmail.getText(), 255)) {
                try {
                    Ward tempWard = (Ward) residentWardCB.getSelectionModel().getSelectedItem();
                    facade.newResident(tempWard.getWardNumber(), resName.getText(), resPhone.getText(), resEmail.getText(), resImage,resCPR.getText());
                    residentCreated();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
            }
        });

        btnCreateUser.setOnAction((ActionEvent e) -> {
            if (userAllFieldsfilled() && validateInput("Brugernavn",userName.getText(), 255) && validateInput("email", userEmail.getText(), 255) && validateInput("telefonnummer", userPhone.getText(), 255 ) && validatePass(userPass.getText())) {
                try {
                    Ward temp = (Ward) userWardCB.getSelectionModel().getSelectedItem();
                    facade.newUser(temp.getWardNumber()
                            , userName.getText()
                            , userPass.getText()
                            , userEmail.getText()
                            , userPhone.getText()
                            , privOwn.isSelected()
                            , privAll.isSelected()
                            , privFind.isSelected()
                            , privWrite.isSelected()
                            , privDrugs.isSelected()
                            , privAdmin.isSelected());
                    userCreated();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        userSelectPic.setOnAction((ActionEvent e) -> {
            userImage = openFileSelector();
        });
        
        resSelectPic.setOnAction((ActionEvent e) -> {
            resImage = openFileSelector();
        });

    }

    private File openFileSelector() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".jpg", "*.jpg"));
        return fc.showOpenDialog(null);
    }
    
        private boolean validatePass(String input){
        if (!PassChecker.checkPassword(input)){
            showDialog("fejl ved oprettelse ", "Kodeordet skal minimum indeholde ét stort tegn, ét lille tegn, ét tal og være 8 cifre langt");
        }
        return true;
        
    }
        
    private boolean validateInput(String inputName,String input, int length){
        if (input.length() > length){
            showDialog("Fejl ved oprettelse", inputName + " er for langt, det skal fylde mindre end " + length + " tegn ");
        }
        return input.length() <= length;
        
    }

    private boolean wardAllFieldsfilled() {
        return wardCB.getValue() != null && !wardDescription.getText().isEmpty() && !wardName.getText().isEmpty();
    }

    private boolean residenceAllFieldsfilled() {
        return !residenceName.getText().isEmpty() && !residenceAddress.getText().isEmpty() && !residencePhone.getText().isEmpty() && !residenceEmail.getText().isEmpty();
    }

    private boolean userAllFieldsfilled() {
        boolean allCBUnSelected = !privOwn.isSelected() && !privAll.isSelected() && !privFind.isSelected()
                && !privWrite.isSelected() && !privDrugs.isSelected() && !privAdmin.isSelected();
        return (!allCBUnSelected && !userEmail.getText().isEmpty() && !userName.getText().isEmpty() && !userPass.getText().isEmpty()
                && !userPhone.getText().isEmpty() && userImage != null);
    }

    private boolean residentAllFieldsfilled() {
        return residentWardCB.getSelectionModel().getSelectedItem() != null && resImage != null &&!resName.getText().isEmpty() && !resPhone.getText().isEmpty() && !resEmail.getText().isEmpty();
    }
    
    private void residenceCreated(){
        residenceName.clear();
        residenceAddress.clear();
        residencePhone.clear();
        residenceEmail.clear();
    }
    
    private void wardCreated(){
        wardDescription.clear();
        wardName.clear();
    }
    
    private void userCreated(){
        userEmail.clear();
        userName.clear();
        userPass.clear();
        userPhone.clear();
        userImage = null;
        CheckBox[] cbs = {privOwn, privAll, privFind, privWrite, privDrugs, privAdmin};
        for (CheckBox cb : cbs) {
            cb.setSelected(false);
        }
    }
    
    private void residentCreated(){
        resEmail.clear();
        resName.clear();
        resPhone.clear();
        resCPR.clear();
        resImage = null;
    }

    public void setFacade(Facade f) {
        this.facade = f;
        System.out.println(this.facade);
    }
    
     private void showDialog(String titel, String dialog){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setContentText(dialog);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.show();
    }

}
