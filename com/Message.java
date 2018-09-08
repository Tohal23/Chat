package com;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;


public class Message implements Serializable {
    private String login;
    private String message;
    private TypeOfMessage action;
    private String[] users;
    private Date time;

    public Message(String log, String mess) {
        login = log;
        message = mess;
        time = java.util.Calendar.getInstance().getTime();
    }

    public Message(String log, TypeOfMessage act) {
        action = act;
        login = log;
        time = java.util.Calendar.getInstance().getTime();
    }

    public Message(String log, String mes, TypeOfMessage act) {
        action = act;
        login = log;
        message = mes;
        time = java.util.Calendar.getInstance().getTime();
    }

    public Message(String log, String mess, String[] users) {
        login = log;
        message = mess;
        time = java.util.Calendar.getInstance().getTime();
        this.users = users;
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

    public TypeOfMessage getType() {
        return action;
    }

    public String getTime() {
        Time t = new Time(time.getTime());
        return t.toString();
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public void setType(TypeOfMessage action) {
        this.action = action;
    }
}
