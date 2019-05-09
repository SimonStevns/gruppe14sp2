package Domain;

import Persistens.Connect;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import javafx.collections.ObservableList;
import java.util.UUID;
import javafx.collections.FXCollections;
import java.time.LocalDate;

public class Facade {

    final private Connect bostedCon = new Connect(Connect.BOSTED_URL, "root", "");
    final private Connect borgerCon = new Connect(Connect.BORGER_URL, "root", "");
    final private Connect medicinCon = new Connect(Connect.MEDICIN_URL, "root", "");

    private User user;

    public void newResident(String name, String phone, String email, File pic) throws SQLException {
        bostedCon.openConnection();

        PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO `residents` (`residentID`, `caseworkerID`, `name`, `email`, `phone`) VALUES (?, ?, ?, ?, ?);");

        pstmt.setString(1, UUID.randomUUID().toString());
        pstmt.setString(2, UUID.randomUUID().toString());
        pstmt.setString(3, name);
        pstmt.setString(4, email);
        pstmt.setString(5, phone);

        pstmt.executeUpdate();
        bostedCon.closeConnection();

    }

    public void newUser(String name, String pass, String email, String phone, boolean own,
            boolean all, boolean find, boolean write, boolean drug, boolean admin) throws SQLException {
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
            pstmt = bostedCon.getPreparedstmt("INSERT INTO users (uuid, pass, email, name, priv, phone, prime) VALUES (?, ?, ?, ?, ?, ?, ?);");

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, pass);
            pstmt.setString(3, email);
            pstmt.setString(4, name);
            pstmt.setInt(5, rs.getInt("privlegeID"));
            pstmt.setString(6, phone);
            pstmt.setString(7, "1");
            pstmt.executeUpdate();        
            pstmt.close();          
            bostedCon.closeConnection();

        }
    }

    public ObservableList<Resident> getResidents() {
        try {
            bostedCon.openConnection();
            ResultSet rs = bostedCon.query("SELECT residentID, name, phone FROM residents;");
            ObservableList<Resident> returnList = FXCollections.observableArrayList();
            while (rs.next()) {
                Resident resident = new Resident(
                        UUID.fromString(rs.getString("residentID")), rs.getString("name"), rs.getString("phone"));
                returnList.add(resident);
            }
            return returnList;

        } catch (SQLException ex) {
        } finally {
            bostedCon.closeConnection();
        }
        return null;
    }

    public boolean setUser(String email, String pass) throws SQLException {
        bostedCon.openConnection();

        PreparedStatement pstmt = bostedCon.getPreparedstmt(
                "SELECT users.uuid, users.pass, users.name ,users.email, users.phone, users.prime, "
                + "privleges.VIEWOWN, privleges.VIEWALL, privleges.FIND, privleges.WRITEDIARY, privleges.DRUG, privleges.ADMIN "
                + "FROM users "
                + "INNER JOIN privleges ON users.priv = privleges.privlegeID "
                + "WHERE email = ? AND pass = ?;");
        pstmt.setString(1, email);
        pstmt.setString(2, pass);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            boolean[] b = {
                rs.getBoolean("VIEWOWN"), rs.getBoolean("VIEWALL"), rs.getBoolean("FIND"), rs.getBoolean("WRITEDIARY"), rs.getBoolean("DRUG"), rs.getBoolean("ADMIN")
            };
            user = new User(
                    new Privileges(b), UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getString("email"), rs.getString("pass"), rs.getString("phone"));
            bostedCon.closeConnection();
            return true;
        }
        bostedCon.closeConnection();
        return false;

    }

    public boolean hasPrivlege(Privilege p) {
        return this.user.hasPrivlege(p);
    }

    public void addDiaryEntry(UUID residentID, String topic, String text) {
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO diaries (topic, text, author, residentID, date) VALUES (? ,? ,? ,? ,?)");

            pstmt.setString(1, topic);
            pstmt.setString(2, text);
            pstmt.setString(3, user.getID().toString());
            pstmt.setString(4, residentID.toString());
            pstmt.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            bostedCon.closeConnection();
        }
    }

    public void addDiaryEntry(UUID residentID, String topic, String text, LocalDate date) {
        try {
            bostedCon.openConnection();
            PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO diaries (topic, text, author, residentID, date ) VALUES (? ,? ,? ,?, ?)");
            System.out.println(date.toString());
            pstmt.setString(1, topic);
            pstmt.setString(2, text);
            pstmt.setString(3, user.getID().toString());
            pstmt.setString(4, residentID.toString());
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
            PreparedStatement pstmt = bostedCon.getPreparedstmt("SELECT topic, text, date FROM diaries where residentID = ? ORDER BY diaries.date DESC ;");
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

    public void addPriv(boolean own, boolean all, boolean find, boolean write, boolean drug, boolean admin, int id) throws SQLException {
        bostedCon.openConnection();
        PreparedStatement pstmt = bostedCon.getPreparedstmt("INSERT INTO privleges (VIEWOWN, VIEWALL, FIND, WRITEDIARY, DRUG, ADMIN, privlegeID) VALUES (? ,? ,? ,? ,? ,? , ?);");

        pstmt.setBoolean(1, own);
        pstmt.setBoolean(2, all);
        pstmt.setBoolean(3, find);
        pstmt.setBoolean(4, write);
        pstmt.setBoolean(5, drug);
        pstmt.setBoolean(6, admin);
        pstmt.setInt(7, id);
        pstmt.executeUpdate();

        bostedCon.closeConnection();
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

            bostedCon.query(MessageFormat.format("CREATE TABLE IF NOT EXISTS `residents_{0}` ( `residentID` VARCHAR(36) NOT NULL );", wardID));

            bostedCon.query(MessageFormat.format("CREATE TABLE IF NOT EXISTS `grp14bosted`.`diaries_{0}` "
                    + "( `residenceID` VARCHAR(36) NOT NULL "
                    + ", `author` VARCHAR(36) NOT NULL "
                    + ", `topic` VARCHAR(100) NOT NULL "
                    + ", `text` VARCHAR(1000) NOT NULL "
                    + ", `date` DATE NOT NULL ) ;", wardID));
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

}
