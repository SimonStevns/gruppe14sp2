package Domain;

import java.util.Date;
import java.util.UUID;

public class Diary {

    String topic, text;
    UUID authorID, residentID, lastID;
    Date date;

    public Diary(String topic, String text, Date date) {
        this.topic = topic;
        this.text = text;
        this.date = date;
    }
    
    @Override
    public String toString(){
        return this.topic + " : Dato:  " + date.toString()+" "+this.text;
    }
}