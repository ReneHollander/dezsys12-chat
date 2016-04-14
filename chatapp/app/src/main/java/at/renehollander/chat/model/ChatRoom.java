package at.renehollander.chat.model;

import android.databinding.ObservableArrayList;

import java.util.List;

public class ChatRoom {

    private String name;
    private ObservableArrayList<Message> messages;

    public ChatRoom(String name, ObservableArrayList<Message> messages) {
        this.name = name;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public ObservableArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "name='" + name + '\'' +
                ", messages=" + messages +
                '}';
    }
}
