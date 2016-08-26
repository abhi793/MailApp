package in.main.mailapp;

import java.util.ArrayList;

/**
 * Created by Abhishek Pc on 20-08-2016.
 */
public class MailModel {
    private String subject;
    private String body;
    private ArrayList<String> participants;
    private Boolean isStarred;
    private Boolean isRead;
    private String id;
   public MailModel()
    {

    }
    public MailModel(ArrayList<String> participants,String subject,String body,Boolean isStarred,Boolean isRead,String id)
    {
        this.participants = participants;
        this.subject = subject;
        this.body = body;
        this.isStarred = isStarred;
        this.isRead = isRead;
        this.id = id;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public Boolean getStarred() {
        return isStarred;
    }

    public void setStarred(Boolean starred) {
        isStarred = starred;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
