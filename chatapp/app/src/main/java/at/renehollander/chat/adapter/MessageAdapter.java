package at.renehollander.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import at.renehollander.chat.R;
import at.renehollander.chat.model.ChatRoom;
import at.renehollander.chat.model.Message;
import at.renehollander.chat.util.ObservableListChangeListener;

public class MessageAdapter extends BaseAdapter {

    private ChatRoom room;
    private LayoutInflater inflater;

    public MessageAdapter(Activity activity, ChatRoom room) {
        this.room = room;
        this.room.getMessages().addOnListChangedCallback(new ObservableListChangeListener(activity, this));
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return room.getMessages().size();
    }

    @Override
    public Message getItem(int i) {
        return room.getMessages().get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Message message = getItem(position);
        if (view == null) {
            if (message.getFlag() == Message.Flag.RECIEVED) {
                view = inflater.inflate(R.layout.message_recieved, null);
            } else if (message.getFlag() == Message.Flag.SENT) {
                view = inflater.inflate(R.layout.message_sent, null);
            } else {
                throw new RuntimeException("unknown message type");
            }
        } else {
            if (view.getId() != R.layout.message_recieved && message.getFlag() == Message.Flag.RECIEVED) {
                view = inflater.inflate(R.layout.message_recieved, null);
            } else if (view.getId() != R.layout.message_sent && message.getFlag() == Message.Flag.SENT) {
                view = inflater.inflate(R.layout.message_sent, null);
            }
        }
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView username = (TextView) view.findViewById(R.id.username);
        username.setText(message.getUser());
        date.setText(SDF.format(message.getDate()));
        text.setText(message.getText());
        return view;
    }

    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
