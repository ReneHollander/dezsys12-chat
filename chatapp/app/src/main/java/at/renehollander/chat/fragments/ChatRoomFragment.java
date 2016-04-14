package at.renehollander.chat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import at.renehollander.chat.Application;
import at.renehollander.chat.R;
import at.renehollander.chat.adapter.MessageAdapter;
import at.renehollander.chat.model.ChatRoom;

public class ChatRoomFragment extends CustomFragment {

    private ListView messages;

    public ChatRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String roomName = getArguments().getString("room");
        ChatRoom room = ((Application) getActivity().getApplication()).getChatRoomMap().get(roomName);

        getActivity().setTitle(room.getName());
        messages = (ListView) findViewById(R.id.listView);
        messages.setAdapter(new MessageAdapter(getActivity(), room));
    }

}
