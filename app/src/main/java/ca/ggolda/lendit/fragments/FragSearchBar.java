package ca.ggolda.lendit.fragments;

/**
 * Created by gcgol on 11/04/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import ca.ggolda.lendit.R;
import ca.ggolda.lendit.activities.MainSearchActivity;

public class FragSearchBar extends Fragment {

    private EditText searchEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = (EditText) v.findViewById(R.id.search_edittext);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    searchForText();
                    // Hide the stage view if it has been expanded for an item
                    // on click/enter to search
                    MainSearchActivity MActivity = (MainSearchActivity) getContext();
                    FragWrap fragWrap = new FragWrap();
                    MActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragWrap)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }
                return false;
            }
        });



        ImageView searchButton = (ImageView) v.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchForText();
                // Hide the stage view if it has been expanded for an item
                // on click/enter to search
                MainSearchActivity MActivity = (MainSearchActivity) getContext();
                FragWrap fragWrap = new FragWrap();
                MActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragWrap)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return v;
    }

    private void searchForText() {

    }
}