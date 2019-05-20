package Domain;

import java.util.UUID;

public class Resident {

    //Attributes
    private String name, phoneNumber,cpr;
    private UUID ID;

    //Constructors
    public Resident(UUID id, String name, String phoneNumber, String cpr) {
        this.ID = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cpr = cpr;
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

    public String getCPR() {
        return this.cpr;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
