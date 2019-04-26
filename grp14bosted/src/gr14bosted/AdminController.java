/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr14bosted;

import Domain.Facade;
import Persistens.Connect;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;


public class AdminController implements Initializable {
    @FXML
    private AnchorPane tabNewUser;
    @FXML
    private TextField userName, userEmail, userPhone, userPass, resPhone, resEmail, resName;

    @FXML
    private CheckBox privOwn, privAll, privFind, privWrite, privDrugs, privAdmin;
   
    @FXML
    private Button btnCreateUser,btnCreateRes,resSelectPic, userSelectPic;
    
    private File currentPic = null;
    
    private Facade facade = new Facade();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        btnCreateRes.setOnAction((ActionEvent e) -> {
             if (residentAllFieldsfilled()) {
                 try {
                     facade.newResident(resName.getText(), resPhone.getText(), resEmail.getText(), currentPic);
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
                     facade.newUser(userName.getText(), userPass.getText(), userEmail.getText(), userPhone.getText(), privOwn.isSelected()
                            , privAll.isSelected(), privFind.isSelected(), privWrite.isSelected(), privDrugs.isSelected(), privAdmin.isSelected());
                 } catch (SQLException ex) {
                     Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         });
        
        resSelectPic.setOnAction((ActionEvent e) -> {
            openFileSelector();
        });
        
        
    }
    
    private void openFileSelector(){
        facade.getResidents();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg bilede", "*.jpg"));
        currentPic = fc.showOpenDialog(null);   
    }
    
    private boolean userAllFieldsfilled(){
        boolean allCBUnSelected = !privOwn.isSelected() && !privAll.isSelected() && !privFind.isSelected() 
                && !privWrite.isSelected() && !privDrugs.isSelected() && !privAdmin.isSelected() ;
        System.out.println(allCBUnSelected);
        return (!allCBUnSelected &&!userEmail.getText().isEmpty() && !userName.getText().isEmpty() && !userPass.getText().isEmpty()
                && !userPhone.getText().isEmpty() && currentPic != null);
    }
    
    private boolean residentAllFieldsfilled(){
        return currentPic != null && !resName.getText().isEmpty() && !resPhone.getText().isEmpty() && !resEmail.getText().isEmpty();
    }
    
    public void setFacade(Facade f){
        this.facade = f;
    }
    
}
