package Domain;

import java.util.UUID;

public class Resident {

    //Attributes
    private String name, phoneNumber, ROW_NUMBER;
    private UUID ID;

    //Constructors
    public Resident(UUID id, String name, String phoneNumber, String ROW_NUMBER) {
        this.ID = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ROW_NUMBER = ROW_NUMBER;
    }

    //Methods
    public UUID getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String phoneNumber() {
        return this.phoneNumber;
    }

    public String GetROW() {
        return this.ROW_NUMBER;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
