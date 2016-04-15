package at.renehollander.chat.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import at.renehollander.chat.Application;
import at.renehollander.chat.R;
import at.renehollander.chat.adapter.ChatRoomListListAdapter;
import at.renehollander.chat.model.ChatRoom;

public class ChatRoomListFragment extends CustomFragment {

    private ListView chatRooms;
    private FloatingActionButton addRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroomlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Application app = (Application) getActivity().getApplication();

        getActivity().setTitle(getResources().getString(R.string.chatrooms_fragment_title));
        chatRooms = (ListView) findViewById(R.id.listView);
        addRoom = (FloatingActionButton) findViewById(R.id.add_room);

        addRoom.setOnClickListener((btn -> {

            final EditText txtName = new EditText(getActivity());

            // Set the default text to a link of the Queen
            txtName.setHint("Roomname");

            new AlertDialog.Builder(getActivity())
                    .setTitle("Create new Room")
                    .setMessage("Create a new chatroom!")
                    .setView(txtName)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String name = txtName.getText().toString();
                            app.createRoom(name);
                            Log.d("roomlist", "addroom");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();

        }));

        chatRooms.setAdapter(new ChatRoomListListAdapter(getActivity(), app.getChatrooms()));
        chatRooms.setOnItemClickListener((adapterView, view2, position, id) -> {
            ChatRoomFragment crf = new ChatRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("room", ((ChatRoom) chatRooms.getAdapter().getItem(position)).getName());
            crf.setArguments(bundle);
            replace(R.id.fragment_container, crf);
        });
    }
}
