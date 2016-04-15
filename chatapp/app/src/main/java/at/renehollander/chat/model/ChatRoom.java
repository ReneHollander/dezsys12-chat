package at.renehollander.chat.model;

import android.databinding.ObservableArrayList;

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

    public Message getLast() {
        if (getMessages().size() == 0) return null;
        else return getMessages().get(getMessages().size() - 1);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "name='" + name + '\'' +
                ", messages=" + messages +
                '}';
    }
}
