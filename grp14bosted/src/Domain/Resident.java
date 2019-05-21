package Domain;

import java.util.UUID;
import javafx.scene.image.Image;

public class Resident {

    //Attributes

    private String name, phoneNumber,cpr;
    private UUID ID;
    private Image picture;
    //Constructors
    public Resident(UUID id, Image picture, String name, String phoneNumber, String cpr) {

        this.ID = id;
        this.picture = picture;
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

    
    public Image getImage(){
        return this.picture;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
