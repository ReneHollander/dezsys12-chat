package at.renehollander.chat.adapter;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.renehollander.chat.R;
import at.renehollander.chat.model.ChatRoom;
import at.renehollander.chat.util.ObservableListChangeListener;

public class ChatRoomListListAdapter extends BaseAdapter {

    private ObservableArrayList<ChatRoom> chatrooms;
    private LayoutInflater inflater;

    public ChatRoomListListAdapter(Context context, ObservableArrayList<ChatRoom> chatrooms) {
        this.chatrooms = chatrooms;
        this.chatrooms.addOnListChangedCallback(new ObservableListChangeListener(this));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatrooms.size();
    }

    @Override
    public ChatRoom getItem(int i) {
        return chatrooms.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.chatroomlist_row, null);
        }
        TextView header = (TextView) view.findViewById(R.id.header);
        TextView text = (TextView) view.findViewById(R.id.text);
        header.setText(getItem(position).getName());
        text.setText("This is room " + getItem(position).getName());
        return view;
    }



}
