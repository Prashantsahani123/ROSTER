package com.SampleApp.row;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by user on 02-02-2017.
 */
public class ActionBarFragmentCalendar extends Fragment {
    ImageView iv_backbutton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actionbar_fragment_calendar, container, false);


        iv_backbutton = (ImageView) view.findViewById(R.id.iv_backbutton);

        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }
}
