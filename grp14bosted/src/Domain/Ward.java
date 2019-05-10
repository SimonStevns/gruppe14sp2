package Domain;

import java.util.ArrayList;
import java.util.UUID;

public class Ward {

    private ArrayList<UUID> residents;
    private ArrayList<UUID> users;
    private String description, residenceName;
    private UUID wardID;

    public Ward(UUID wardID, String description, String residenceName) {
        this.wardID = wardID;
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

    public UUID getWardNumber() {
        return this.wardID;
    }
    
    @Override
    public String toString(){
        return residenceName + ": " + description;
    }
}
