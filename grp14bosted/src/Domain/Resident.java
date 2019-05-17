package Domain;

import java.util.UUID;
import javafx.scene.image.Image;

public class Resident {

    //Attributes
    private String name, phoneNumber;
    private Image picture;
    private UUID ID;

    //Constructors
    public Resident(UUID id, Image picture, String name, String phoneNumber) {
        this.ID = id;
        this.picture = picture;
        this.name = name;
        this.phoneNumber = phoneNumber;
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
    
    public Image getImage(){
        return this.picture;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
