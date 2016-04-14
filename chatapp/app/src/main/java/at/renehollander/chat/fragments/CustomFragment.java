package at.renehollander.chat.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public abstract class CustomFragment extends Fragment {

    public View findViewById(int id) {
        return getActivity().findViewById(id);
    }

    public void replace(int id, Fragment newFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(id, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
