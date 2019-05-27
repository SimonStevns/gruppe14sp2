package Domain;

import java.util.Date;

public class Diary {

    private String topic, text;
    private Date date;

    public Diary(String topic, String text, Date date) {
        this.topic = topic;
        this.text = text;
        this.date = date;
    }
    
    public Date getDate(){
        return this.date;
    }
    public String getText(){
        return this.text;
    }
    public String getTopic(){
        return this.topic;
    }
    
    @Override
    public String toString(){
        return this.topic + " Dato:  " + date.toString();
    }
}
