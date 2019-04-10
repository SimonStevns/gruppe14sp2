package Domain;

import java.util.UUID;

public class Resident {

    //Attributes
    private String name, phoneNumber, CPR;
    private UUID ID;

    //Constructors
    public Resident(String name, String phoneNumber, String CPR) {
        this.ID = UUID.randomUUID();
        this.CPR = CPR;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //Methods
    public UUID ID() {
        return UUID.randomUUID();
    }

    public String getName() {
        return this.name;
    }

    public String phoneNumber() {
        return this.phoneNumber;
    }

    public String getCPR() {
        return this.CPR;
    }
}
