package at.renehollander.chat.model;

import java.util.Date;

public class Message {

    public static enum Flag {
        SENT, RECIEVED;
    }

    private Flag flag;
    private String room;
    private String user;
    private Date date;
    private String text;

    public Message(Flag flag, String room, String user, Date date, String text) {
        this.flag = flag;
        this.room = room;
        this.user = user;
        this.date = date;
        this.text = text;
    }

    public Flag getFlag() {
        return flag;
    }

    public String getRoom() {
        return room;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "flag=" + flag +
                ", room='" + room + '\'' +
                ", user='" + user + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
