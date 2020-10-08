package app.customtenant.messenger.models;

import java.util.Date;

public class WSocketGetMessage {

    private long time;
    private java.util.Date date;
    private String from;
    private String msg;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return from;
    }

    public void setUsername(String username) {
        this.from = username;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

}
