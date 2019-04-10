package Domain;

import java.util.ArrayList;
import java.util.UUID;

public class User 
{
    private String name, phoneNumber, password, email;
    private Privileges priv;
    private UUID id;
    private Ward primaryWard;
    private ArrayList<Ward> secondaryWards;

    public User(UUID iD, Privileges priv, String name, String email, String password, String phoneNumber, Ward primaryWard, ArrayList<Ward> secondaryWards) 
    {
        this.id = iD;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;      
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
    
    public UUID getID(){
        return this.id;
    }      
}