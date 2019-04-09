package Domain;

import java.util.ArrayList;

public class Ward {

    private ArrayList<Resident> residents;
    private ArrayList<User> users;
    private String description;
    private int wardNumber;

    public Ward(int wardnumber, String description) {
        this.wardNumber = wardnumber;
        this.description = description;
    }

    public boolean addResident(Resident resident) {
        if (!residents.contains(resident) && resident != null) {
            residents.add(resident);
            return true;
        }
        return false;
    }

    public boolean addUser(User user) {
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
}
