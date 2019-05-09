package Domain;

import Persistens.Connect;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;


public class Facade 
{
    private Connect bostedCon = new Connect(Connect.BOSTED_URL, "root", "");
    private Connect borgerCon = new Connect(Connect.BORGER_URL, "root", "");
    private Connect medicinCon = new Connect(Connect.MEDICIN_URL, "root", "");
    

    public void newResident(String name, String phone, String email, File pic) throws SQLException{
        bostedCon.openConnection();
        
        PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO `test_residents` (`residentID`, `caseworkerID`, `name`, `email`, `phone`) VALUES (?, ?, ?, ?, ?)");
        
        pstmt.setString(1, UUID.randomUUID().toString());
        pstmt.setString(2, UUID.randomUUID().toString());
        pstmt.setString(3, name);
        pstmt.setString(4, email);
        pstmt.setString(5, phone);
        
        pstmt.executeUpdate();
        bostedCon.closeConnection();
        
        
    }
    
    public ObservableList<Resident> getResidents(){
        try {
            bostedCon.openConnection();
            ResultSet rs = bostedCon.query("SELECT residentID, name, phone FROM test_residents");
            ObservableList<Resident> returnList= FXCollections.observableArrayList();
            while (rs.next()) {
                Resident resident = new Resident(
                        UUID.fromString(rs.getString("residentID"))
                        , rs.getString("name")
                        , rs.getString("phone"));
                returnList.add(resident);
            }
            bostedCon.closeConnection();
            
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
