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
    private PassChecker passChecker;

    public User(Privileges priv, UUID id, String name, String email, String password, String phoneNumber) 
    {
        this.id = id;
        this.priv = priv;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;      
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
    public boolean hasPrivlege(Privilege priv) {
        return this.priv.hasPrivlege(priv);
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}