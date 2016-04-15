package at.renehollander.chat.util;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.widget.BaseAdapter;

import at.renehollander.chat.model.ChatRoom;

public class ObservableListChangeListener extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<ChatRoom>> {

    private final BaseAdapter adapter;
    private final Activity activity;

    public ObservableListChangeListener(Activity activity, BaseAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void onChanged(ObservableArrayList<ChatRoom> chatRooms) {
        activity.runOnUiThread(adapter::notifyDataSetChanged);
    }

    @Override
    public void onItemRangeChanged(ObservableArrayList<ChatRoom> chatRooms, int i, int i1) {
        activity.runOnUiThread(adapter::notifyDataSetChanged);
    }

    @Override
    public void onItemRangeInserted(ObservableArrayList<ChatRoom> chatRooms, int i, int i1) {
        activity.runOnUiThread(adapter::notifyDataSetChanged);
    }

    @Override
    public void onItemRangeMoved(ObservableArrayList<ChatRoom> chatRooms, int i, int i1, int i2) {
        activity.runOnUiThread(adapter::notifyDataSetChanged);
    }

    @Override
    public void onItemRangeRemoved(ObservableArrayList<ChatRoom> chatRooms, int i, int i1) {
        activity.runOnUiThread(adapter::notifyDataSetChanged);
    }
}
