package at.renehollander.chat;

import java.util.Date;

public class ChatMessage {

    private Date date;
    private String username;
    private String message;

    public ChatMessage(Date date, String username, String message) {
        this.date = date;
        this.username = username;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "date=" + date +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
