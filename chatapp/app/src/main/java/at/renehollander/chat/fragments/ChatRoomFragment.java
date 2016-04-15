package at.renehollander.chat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import at.renehollander.chat.Application;
import at.renehollander.chat.R;
import at.renehollander.chat.adapter.MessageAdapter;
import at.renehollander.chat.model.ChatRoom;

public class ChatRoomFragment extends CustomFragment {

    private ListView messages;
    private EditText newmsg;
    private ImageButton newmsgsend;

    public ChatRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Application app = ((Application) getActivity().getApplication());

        String roomName = getArguments().getString("room");
        ChatRoom room = app.getChatRoomMap().get(roomName);

        getActivity().setTitle(room.getName());
        messages = (ListView) findViewById(R.id.listView);
        messages.setAdapter(new MessageAdapter(getActivity(), room));

        newmsg = (EditText) findViewById(R.id.newmsg);
        newmsgsend = (ImageButton) findViewById(R.id.newmsgsend);

        newmsgsend.setOnClickListener((btn) -> {
            String msg = newmsg.getText().toString();
            if (!msg.isEmpty()) {
                app.sendMessage(room.getName(), msg);
            }
            newmsg.setText("");
        });
    }

}
