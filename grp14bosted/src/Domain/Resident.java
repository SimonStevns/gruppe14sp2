package Domain;

import java.util.UUID;

public class Resident {

    //Attributes
    private String name, phoneNumber;
    private UUID ID;

    //Constructors
    public Resident(String name, String phoneNumber, UUID ID) {
        this.ID = ID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //Methods
    public UUID ID() {
        return UUID.randomUUID();
    }
    
    public String getName(){
        return this.name;
    }
    
    public String phoneNumber(){
        return this.phoneNumber;
    }
}
