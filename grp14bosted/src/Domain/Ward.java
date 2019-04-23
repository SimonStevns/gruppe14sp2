package Domain;

import java.util.ArrayList;
import java.util.UUID;

public class Ward {

    private ArrayList<UUID> residents;
    private ArrayList<UUID> users;
    private String description, residenceName;
    private int wardNumber;

    public Ward(int wardnumber, String description, String residenceName) {
        this.wardNumber = wardnumber;
        this.description = description;
        this.residenceName = residenceName;
        this.residents = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public boolean addResident(UUID resident) {
        if (!residents.contains(resident) && resident != null) {
            residents.add(resident);
            return true;
        }
        return false;
    }

    public boolean addUser(UUID user) {
        if (!users.contains(user) && user != null) {
            users.add(user);
            return true;
        }
        return false;
    }

    public String getWardDescription() {
        return this.description;
    }

    public int getWardNumber() {
        return this.wardNumber;
    }
    
    @Override
    public String toString(){
        return "Afdelling nummer: " + this.wardNumber + " beboer: " + this.residents.size() + " medarbejderer: " + this.users.size();
    }
}
