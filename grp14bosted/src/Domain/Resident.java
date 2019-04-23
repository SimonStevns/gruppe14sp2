package Domain;

import java.util.UUID;

public class Resident {

    //Attributes
    private String name, phoneNumber;
    private UUID ID;

    //Constructors
    public Resident(UUID id, String name, String phoneNumber) {
        this.ID = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //Methods
    public UUID ID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String phoneNumber() {
        return this.phoneNumber;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
