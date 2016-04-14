package at.renehollander.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.renehollander.chat.R;
import at.renehollander.chat.model.ChatRoom;
import at.renehollander.chat.model.Message;
import at.renehollander.chat.util.ObservableListChangeListener;

public class MessageAdapter extends BaseAdapter {

    private ChatRoom room;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, ChatRoom room) {
        this.room = room;
        this.room.getMessages().addOnListChangedCallback(new ObservableListChangeListener(this));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        }

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(message.getText());
        return view;
    }
}
