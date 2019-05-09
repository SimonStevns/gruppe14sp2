package Domain;

import java.util.ArrayList;
import java.util.UUID;

public class Residence {
    private UUID ID;
    private String name, phoneNumber, email, addres;
    private ArrayList<Ward> wards;
    
    
    public Residence(UUID ID, String name, String phoneNumber, String email, String addres) 
    {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addres = addres;
        this.wards = new ArrayList<>();
    }
    
    public String getName(){
        return this.name;
    }
    public boolean addWard(Ward ward){
        if (ward != null){
            this.wards.add(ward);
            return true;
        }
        return false;
    }
    public int numberOfWards(){
        
        return wards.size() + 1;
    }
    public UUID getId(){
        return this.ID;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
