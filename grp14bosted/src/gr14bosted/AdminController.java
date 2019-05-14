/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr14bosted;

import Domain.Facade;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AdminController implements Initializable {

    @FXML
    private TextField userName, userEmail, userPhone, userPass, resPhone, resEmail, resName,rescpr;
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

    private File currentPic = null;

    private Facade facade = new Facade();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCreateResidence.setOnAction(e -> {
            if (residenceAllFieldsfilled()) {
                facade.addResidence(residenceName.getText(), residenceAddress.getText(), residencePhone.getText(), residenceEmail.getText());
            }
        });

        btnCreateWard.setOnAction(e -> {
            if (wardAllFieldsfilled()) {
                Residence r = (Residence) wardCB.getValue();
                facade.newWard(r.getId().toString(), wardDescription.getText(), wardName.getText());
            }
        });

        //ChoiceBoxes
        ObservableList<Residence> residences = facade.getResidences();
        wardCB.setItems(residences);
        wardCB.getSelectionModel().selectFirst();
        userResidenceCB.setItems(residences);
        userResidenceCB.getSelectionModel().selectFirst();
        residentResidenceCB.setItems(residences);
        residentResidenceCB.getSelectionModel().selectLast();

        userResidenceCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                setUserWards(facade.getWards(residences.get(newValue.intValue()).getId().toString()));
            }
        });

        residentResidenceCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newValue) {
                setResWard(facade.getWards(residences.get(newValue.intValue()).getId().toString()));
            }
        });

        userWards = facade.getWards(residences.get(0).getId().toString());
        residentWards = facade.getWards(residences.get(0).getId().toString());
        userWardCB.setItems(userWards);
        residentWardCB.setItems(residentWards);

        btnCreateRes.setOnAction((ActionEvent e) -> {
            if (residentAllFieldsfilled()) {
                try {
                    facade.newResident(resName.getText(), resPhone.getText(), resEmail.getText(), currentPic,facade.getBorgerRowNum(rescpr.getText()));
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        resSelectPic.setOnAction((ActionEvent e) -> {
            openFileSelector();
        });

        btnCreateUser.setOnAction((ActionEvent e) -> {
            if (userAllFieldsfilled()) {
                try {
                    facade.newUser(userName.getText(), userPass.getText(), userEmail.getText(), userPhone.getText(), privOwn.isSelected(), privAll.isSelected(), privFind.isSelected(), privWrite.isSelected(), privDrugs.isSelected(), privAdmin.isSelected());
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        userSelectPic.setOnAction((ActionEvent e) -> {
            openFileSelector();
        });

    }

    private void openFileSelector() {
        facade.getResidents();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg bilede", "*.jpg"));
        currentPic = fc.showOpenDialog(null);
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
                && !userPhone.getText().isEmpty() && currentPic != null);
    }

    private boolean residentAllFieldsfilled() {
        return currentPic != null && !resName.getText().isEmpty() && !resPhone.getText().isEmpty() && !resEmail.getText().isEmpty();
    }

    private void setUserWards(ObservableList<Ward> wards) {
        userWardCB.setItems(wards);
        userWardCB.getSelectionModel().selectFirst();
    }

    private void setResWard(ObservableList<Ward> wards) {
        residentWardCB.setItems(wards);
        residentWardCB.getSelectionModel().selectFirst();
    }

    public void setFacade(Facade f) {
        this.facade = f;
    }

}
