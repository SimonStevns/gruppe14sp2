package Domain;

import java.util.ArrayList;

public class Caseworker 
{
    private String name, phoneNumber, password, email, iD;
    private Ward primaryWard;
    private ArrayList<Ward> secondaryWards;

    public Caseworker(String name, String email, String password, String phoneNumber, String iD, Ward primaryWard, ArrayList<Ward> secondaryWards) 
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.iD = iD;
        this.primaryWard = primaryWard;
        this.secondaryWards = secondaryWards;
    }

    public Ward getPrimaryWard(){
        return this.primaryWard;
    }
    
    public ArrayList<Ward> getSecondaryWards(){
        return this.secondaryWards;
    }
    
    public ArrayList<Ward> getAllWards(){
        ArrayList<Ward> returnList = this.secondaryWards;
        returnList.add(primaryWard);
        return returnList;
    }
    
    public String getID(){
        return this.iD;
    }
    
    
    
}
