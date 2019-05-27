package Domain;

import Persistens.Connect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import javafx.collections.ObservableList;
import java.util.UUID;
import javafx.collections.FXCollections;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class Facade {

    final private Connect bostedCon = new Connect(Connect.BOSTED_URL, "root", "");
    final private Connect borgerCon = new Connect(Connect.BORGER_URL, "root", "");
    final private Connect medicinCon = new Connect(Connect.MEDICIN_URL, "root", "");

    private static User currentUser;
    private static UUID currentWardID;


    public void newResident(UUID wardID, String name, String phone, String email, File pic, String cpr) throws SQLException, FileNotFoundException {
        bostedCon.openConnection();

        PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO `residents` (`residentID`, `name`, `email`, `phone`, `picture`, `cpr`) VALUES (?, ?, ?, ?, ?, ?);");
        
        String residentID = UUID.randomUUID().toString();
        FileInputStream fis = new FileInputStream(pic);
        
        pstmt.setString(1, residentID);
        pstmt.setString(2, name);
        pstmt.setString(3, email);
        pstmt.setString(4, phone);
        pstmt.setBlob(5, fis, pic.length());
        pstmt.setString(6, cpr);
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = bostedCon.getPreparedstmt("INSERT INTO `residents_" + wardID.toString() + "` (`residentID`) VALUES (?)");
        pstmt.setString(1, residentID);
        pstmt.executeUpdate();
        pstmt.close();
        bostedCon.closeConnection();

    }

    public void newUser(UUID wardID, String name, String pass, String email, String phone, File pic,
            boolean own, boolean all, boolean find, boolean write, boolean drug, boolean admin) throws SQLException, FileNotFoundException {
        bostedCon.openConnection();
        PreparedStatement pstmt = bostedCon.getPreparedstmt("SELECT privlegeID FROM privleges "
                + "WHERE VIEWOWN = ? AND VIEWALL = ? AND FIND = ? AND WRITEDIARY = ? AND DRUG = ? AND ADMIN = ?;");

        pstmt.setBoolean(1, own);
        pstmt.setBoolean(2, all);
        pstmt.setBoolean(3, find);
        pstmt.setBoolean(4, write);
        pstmt.setBoolean(5, drug);
        pstmt.setBoolean(6, admin);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {

            pstmt = bostedCon.getPreparedstmt("INSERT INTO users (userID, pass, email, name, privlegeID, phone, primeWard, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            
            UUID userID = UUID.randomUUID();
            FileInputStream fis = new FileInputStream(pic);
            
            pstmt.setString(1, userID.toString());
            pstmt.setString(2, pass);
            pstmt.setString(3, email);
            pstmt.setString(4, name);
            pstmt.setInt(5, rs.getInt("privlegeID"));
            pstmt.setString(6, phone);
            pstmt.setString(7, wardID.toString());
            pstmt.setBlob(8, fis, pic.length());
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = bostedCon.getPreparedstmt("INSERT INTO `users_" + wardID.toString() + "` (`userID`) VALUES (?)");
            pstmt.setString(1, userID.toString());
            pstmt.executeUpdate();
            pstmt.close();
        }
        bostedCon.closeConnection();
    }

    public ObservableList<Resident> getCurrentResidents() {
        try {
            bostedCon.openConnection();
            ResultSet rs = bostedCon.query(
                      "SELECT residents.residentID, cpr, residents.picture ,name, phone "
                    + "FROM `residents_" + currentWardID.toString() + "`"
                    + "INNER JOIN residents ON `residents_" + currentWardID.toString() + "`.residentID = residents.residentID "
                    + "WHERE residents.picture IS NOT NULL;");
            ObservableList<Resident> returnList = FXCollections.observableArrayList();
            while (rs.next()) {
                Resident resident = new Resident(
                        UUID.fromString(rs.getString("residentID"))
                        , rs.getString("cpr")
                        , new Image(rs.getBlob("picture").getBinaryStream(), 50, 50, false, false)
                        , rs.getString("name")
                        , rs.getString("phone"));
                returnList.add(resident);
            }
            return returnList;

        } catch (SQLException ex) {
            ex.printStackTrace();
          
        } finally {
            bostedCon.closeConnection();
        }
        return null;

    }    
    
//    public ObservableList<Resident> getResidents(UUID wardID) {
//        try {
//            bostedCon.openConnection();
//            ResultSet rs = bostedCon.query(
//                      "SELECT residentID, name, phone "
//                    + "FROM `residents_" + currentWardID.toString() + "`"
//                    + "INNER JOIN ;");
//            ObservableList<Resident> returnList = FXCollections.observableArrayList();
//            while (rs.next()) {
//                Resident resident = new Resident(
//                        UUID.fromString(rs.getString("residentID")), rs.getString("name"), rs.getString("phone"));
//                returnList.add(resident);
//            }
//            return returnList;
//
//        } catch (SQLException ex) {
//        } finally {
//            bostedCon.closeConnection();
//        }
//        return null;
//    }


    public boolean setUser(String email, String pass) throws SQLException {
        bostedCon.openConnection();

        PreparedStatement pstmt = bostedCon.getPreparedstmt(
                "SELECT users.userID, users.pass, users.name ,users.email, users.phone, users.primeWard, "
                + "privleges.VIEWOWN, privleges.VIEWALL, privleges.FIND, privleges.WRITEDIARY, privleges.DRUG, privleges.ADMIN "
                + "FROM users "
                + "INNER JOIN privleges ON users.privlegeID = privleges.privlegeID "
                + "WHERE email = ? AND pass = ?;");
        pstmt.setString(1, email);
        pstmt.setString(2, pass);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            boolean[] b = {
                rs.getBoolean("VIEWOWN"), rs.getBoolean("VIEWALL"), rs.getBoolean("FIND"), rs.getBoolean("WRITEDIARY"), rs.getBoolean("DRUG"), rs.getBoolean("ADMIN")
            };
            currentUser = new User(
                    new Privileges(b), UUID.fromString(rs.getString("userID")), rs.getString("name"), rs.getString("email"), rs.getString("pass"), rs.getString("phone"));
            currentWardID = UUID.fromString(rs.getString("primeWard"));
            bostedCon.closeConnection();
            return true;
        }
        bostedCon.closeConnection();
        return false;

    }

    public boolean hasPrivlege(Privilege p) {
        return this.currentUser.hasPrivlege(p);
    }

    public void addDiaryEntry(UUID residentID, String topic, String text, LocalDate date) {
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO `diaries_" + currentWardID.toString() + "` (residentID, authorID, topic, text, date ) VALUES (? ,? ,? ,?, ?)");
            pstmt.setString(1, residentID.toString());
            pstmt.setString(2, currentUser.getID().toString());
            pstmt.setString(3, topic);
            pstmt.setString(4, text);
            pstmt.setDate(5, java.sql.Date.valueOf(date));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            bostedCon.closeConnection();
        }
    }

    public ObservableList<Diary> getResidentdiaries(UUID residentID) {
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("SELECT topic, text, date FROM `diaries_" + currentWardID.toString() + "` where residentID = ? ORDER BY date DESC ;");
            pstmt.setString(1, residentID.toString());
            ResultSet rs = pstmt.executeQuery();

            ObservableList resultList = FXCollections.observableArrayList();

            while (rs.next()) {
                Diary tempDiary = new Diary(rs.getString("topic"), rs.getString("text"), rs.getDate("date"));
                resultList.add(tempDiary);
            }

            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            bostedCon.closeConnection();
        }
        return FXCollections.observableArrayList();

    }

    public void addResidence(String name, String address, String phone, String email) {
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO residences (residenceID, name , address, email, phone) VALUES (?, ?, ?, ?, ?);");

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
        } finally {
            bostedCon.closeConnection();
        }
    }

    public void newWard(String residenceID, String description, String name) {
        try {
            String wardID = UUID.randomUUID().toString();
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO wards (wardID, residenceID, description, name) VALUES (?, ?, ?, ?);");

            pstmt.setString(1, wardID);
            pstmt.setString(2, residenceID);
            pstmt.setString(3, description);
            pstmt.setString(4, name);
            pstmt.executeUpdate();
            pstmt.close();

            bostedCon.update(MessageFormat.format("CREATE TABLE IF NOT EXISTS `grp14bosted`.`residents_{0}` "
                    + "( `residentID` VARCHAR(36) NOT NULL PRIMARY KEY, `added` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);", wardID));

            bostedCon.update(MessageFormat.format("CREATE TABLE `grp14bosted`.`users_{0}` "
                    + "( `userID` VARCHAR(36) NOT NULL PRIMARY KEY, `added` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP );", wardID));

            bostedCon.update(MessageFormat.format("CREATE TABLE IF NOT EXISTS `grp14bosted`.`diaries_{0}` "
                    + "( `residentID` VARCHAR(36) NOT NULL PRIMARY KEY "
                    + ", `authorID` VARCHAR(36) NOT NULL "
                    + ", `topic` VARCHAR(100) NOT NULL "
                    + ", `text` VARCHAR(1000) NOT NULL "
                    + ", `date` DATE NOT NULL "
                    + ", `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP );", wardID));
        } catch (SQLException ex) {
        } finally {
            bostedCon.closeConnection();
        }
    }

    public ObservableList<Residence> getResidences() {
        try {
            bostedCon.openConnection();
            ObservableList<Residence> returnList = FXCollections.observableArrayList();
            ResultSet rs = bostedCon.query("SELECT * FROM residences");

            while (rs.next()) {
                returnList.add(new Residence(
                        UUID.fromString(rs.getString("residenceID")), rs.getString("name"), rs.getString("phone"), rs.getString("email"), rs.getString("address")));
            }
            return returnList;
        } catch (Exception e) {
        } finally {
            bostedCon.closeConnection();
        }
        return null;
    }

    public ObservableList<Ward> getWards(String ResidenceID) {
        ObservableList<Ward> returnList = FXCollections.observableArrayList();
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("SELECT * FROM `wards` WHERE `residenceID` = ?");
            pstmt.setString(1, ResidenceID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                returnList.add(new Ward(
                        UUID.fromString(rs.getString("wardID")), rs.getString("description"), rs.getString("name")));
            }
        } catch (SQLException e) {
        } finally {
            bostedCon.closeConnection();
        }
        return returnList;
    }
    
    public ObservableList<Prescription> getPrescriptions(ObservableList<Resident> residents){
        ObservableList<Prescription> retrunList = FXCollections.observableArrayList();
        try {
            medicinCon.openConnection();
            PreparedStatement pstmt = medicinCon.getPreparedstmt(
                    "SELECT `drug`, `form`, `strength`, `dosage`, `indication` "
                    + "FROM `ordination` WHERE `cpr` = ? AND `start_date` <= ? AND `end_date` >= ?;");
            for (Resident resident : residents) {
                pstmt.setString(1, resident.getCPR());//TO DO: Change to resident.getCPR
                pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    retrunList.add(new Prescription(
                            resident.getID()
                            , resident.getName()
                            , resident.getImage()
                            , rs.getString("drug")
                            , rs.getString("form")
                            , rs.getString("strength")
                            , rs.getString("dosage")
                            , rs.getString("indication")));
                }
                rs.close();
            }
            pstmt.close();
            medicinCon.closeConnection();
        } catch (SQLException ex) { ex.printStackTrace();}
        return retrunList;
    }
  
    public UUID getCurrentWardID() {
        return currentWardID;
    }

    @Override
    public String toString() {
        return "" + this.currentUser + this.currentWardID;
    }

    public String getCPR(String cpr) {
        try {
            borgerCon.openConnection();
            ResultSet rs = borgerCon.query("SELECT `cpr` From borger WHERE cpr = "+ cpr);
            String s = rs.getString("cpr");
            rs.close();
            return s;

        } catch (SQLException ex) {
        } finally {
            borgerCon.closeConnection();
        }
        return null;
    }

    public String getjournal(String s) {
        try {
            borgerCon.openConnection();

            String journal = "";

            ResultSet rs = borgerCon.query("SELECT `journal` FROM `borger` WHERE cpr = " + s);
            while (rs.next()) {
                journal = rs.getString("journal");
            }
            rs.close();
            return journal;

        } catch (SQLException ex) {
        } finally {
            borgerCon.closeConnection();
        }
        return null;
    }

    public void newUser(UUID wardNumber, String text, String text0, String text1, String text2, boolean selected, boolean selected0, boolean selected1, boolean selected2, boolean selected3, boolean selected4) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
