package at.renehollander.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.renehollander.chat.R;
import at.renehollander.chat.model.ChatRoom;
import at.renehollander.chat.model.Message;
import at.renehollander.chat.util.ObservableListChangeListener;

public class ChatRoomListListAdapter extends BaseAdapter {

    private final Activity activity;
    private ObservableArrayList<ChatRoom> chatrooms;
    private LayoutInflater inflater;
    private ObservableListChangeListener changeListener;

    public ChatRoomListListAdapter(Activity activity, ObservableArrayList<ChatRoom> chatrooms) {
        this.activity = activity;
        this.chatrooms = chatrooms;
        changeListener = new ObservableListChangeListener(this.activity, this);
        for (ChatRoom room : this.chatrooms) {
            room.getMessages().addOnListChangedCallback(changeListener);
        }
        this.chatrooms.addOnListChangedCallback(changeListener);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        Message last = getItem(position).getLast();
        if (last != null)
            text.setText(last.getUser() + ": " + last.getText());
        else text.setText("");

        return view;
    }


    private class ChangeListener extends android.databinding.ObservableList.OnListChangedCallback<ObservableList<ChatRoom>> {

        @Override
        public void onChanged(ObservableList<ChatRoom> sender) {
            System.out.println("here");
            for (ChatRoom room : sender) {
                room.getMessages().addOnListChangedCallback(changeListener);
            }
            activity.runOnUiThread(ChatRoomListListAdapter.this::notifyDataSetChanged);
        }

        @Override
        public void onItemRangeChanged(ObservableList<ChatRoom> sender, int positionStart, int itemCount) {
            activity.runOnUiThread(ChatRoomListListAdapter.this::notifyDataSetChanged);
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                sender.get(i).getMessages().addOnListChangedCallback(changeListener);
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList<ChatRoom> sender, int positionStart, int itemCount) {
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                sender.get(i).getMessages().addOnListChangedCallback(changeListener);
            }
            activity.runOnUiThread(ChatRoomListListAdapter.this::notifyDataSetChanged);
        }

        @Override
        public void onItemRangeMoved(ObservableList<ChatRoom> sender, int fromPosition, int toPosition, int itemCount) {
            activity.runOnUiThread(ChatRoomListListAdapter.this::notifyDataSetChanged);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<ChatRoom> sender, int positionStart, int itemCount) {
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                sender.get(i).getMessages().removeOnListChangedCallback(changeListener);
            }
            activity.runOnUiThread(ChatRoomListListAdapter.this::notifyDataSetChanged);
        }
    }
}
