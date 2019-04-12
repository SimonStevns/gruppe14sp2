package Domain;

import java.util.ArrayList;
import Persistens.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Testclasses {


    public static void main(String[] args) {
        Residence r = new Residence("Humlehuset","8gange8","test@test.dk", "Cortex park 24");
        
        Ward w1 = new Ward(r.numberOfWards(), "Afvening", r.getName());
        
        w1.addResident(new Resident("Andreas", "50509433", "999999-9999"));
        w1.addResident(new Resident("Andreas1", "505094331", "999999-99991"));
        
        boolean[] b = {true,true,true,true,true,true};
        Privileges w1p = new Privileges(b);
        User u1 = new User(w1p, "Simon Stevns","Simon@test.dk", "test", "88888888", w1,new ArrayList<>());
        w1.addUser(u1);
        System.out.println(u1.hasPrivlege(Privilege.ADMIN));
        
        
        r.addWard(w1);
        
        Ward w2 = new Ward(r.numberOfWards(), "Stoffer", r.getName());
        
        w2.addResident(new Resident("Andreas", "50509433", "999999-9999"));
        w2.addResident(new Resident("Andreas1", "505094331", "999999-99991"));
        
        r.addWard(w2);
        
        System.out.println(r.toString());
        
        Connect con = new Connect(Connect.BOSTED_URL,"root","");
        con.openConnection();
        try {
            ResultSet rs = con.query("SELECT * FROM test");
            while (rs.next()) {                
                System.out.println(rs.getInt("tal"));
            }
            
                    } catch (SQLException ex) {
            System.out.println("ups");
        }
        con.closeConnection();
    }
    
}
