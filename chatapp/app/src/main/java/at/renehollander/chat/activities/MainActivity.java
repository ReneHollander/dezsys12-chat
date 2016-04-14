package at.renehollander.chat.activities;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import at.renehollander.chat.Application;
import at.renehollander.chat.R;
import at.renehollander.chat.fragments.ChatRoomListFragment;
import at.renehollander.chat.model.ChatRoom;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ChatRoomListFragment()).commit();
        }
    }

}
