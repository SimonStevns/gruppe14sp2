package Domain;

import java.util.ArrayList;

public class Residence {
     //Atributes
    private String name, phoneNumber, email, addres;
    private ArrayList<Ward> wards;
    
    
    public Residence(String name, String phoneNumber, String email, String addres) 
    {
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
    
    @Override
    public String toString(){
        String returnString = "" + this.name + "\n";
        returnString += "";
        returnString = wards.stream().map((ward) -> ward.toString() + "\n").reduce(returnString, String::concat);
        return returnString;
    }
}
