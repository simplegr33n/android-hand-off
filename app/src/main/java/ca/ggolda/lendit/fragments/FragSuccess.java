package ca.ggolda.lendit.fragments;

/**
 * Created by gcgol on 11/04/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.ggolda.lendit.R;

public class FragSuccess extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_logo, container, false);

        TextView resultText = (TextView) v.findViewById(R.id.result_text);
        resultText.setVisibility(View.VISIBLE);


        return v;
    }

}