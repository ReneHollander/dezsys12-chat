package at.renehollander.chat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import at.renehollander.chat.Application;
import at.renehollander.chat.R;
import at.renehollander.chat.adapter.ChatRoomListListAdapter;
import at.renehollander.chat.model.ChatRoom;

public class ChatRoomListFragment extends CustomFragment {

    private ListView chatRooms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroomlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.chatrooms_fragment_title));
        chatRooms = (ListView) findViewById(R.id.listView);

        chatRooms.setAdapter(new ChatRoomListListAdapter(getActivity(), ((Application) getActivity().getApplication()).getChatrooms()));
        chatRooms.setOnItemClickListener((adapterView, view2, position, id) -> {
            ChatRoomFragment crf = new ChatRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("room", ((ChatRoom) chatRooms.getAdapter().getItem(position)).getName());
            crf.setArguments(bundle);
            replace(R.id.fragment_container, crf);
        });
    }

}
