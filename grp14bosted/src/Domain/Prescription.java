package Domain;

import java.util.UUID;
import javafx.scene.image.Image;

public class Prescription {

    private UUID ID;
    private String residentName, drug, form, strength, dosage, indication;
    private Image residentImage;

    public Prescription(UUID residentID, String residentName, Image residentImage, String drug, String form, String strength, String dosage, String indication) {
        this.ID = residentID;
        this.residentName = residentName;
        this.residentImage = residentImage;
        this.drug = drug;
        this.form = form;
        this.strength = strength;
        this.dosage = dosage;
        this.indication = indication;
    }

    public String getDrugDescription() {
        return "" + this.drug + " " + this.form + " " + this.strength;
    }

    public Image getImage() {
        return this.residentImage;
    }

    public String getName() {
        return this.residentName;
    }

    public String getDosage() {
        return this.dosage;
    }

    public String getIndication() {
        return this.indication;
    }

    public UUID getID() {
        return this.ID;
    }
    
    public String diaryString() {
        return "" + this.residentName + " har modtaget " + this.getDrugDescription() + " " + dosage;
    }

}
