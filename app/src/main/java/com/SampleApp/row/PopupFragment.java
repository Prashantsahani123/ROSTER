package com.SampleApp.row;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by USER on 26-09-2016.
 */
public class PopupFragment extends Fragment {
    ImageView ivFragmentClose;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_fragment, container, false);

        ivFragmentClose = (ImageView)view.findViewById(R.id.iv_fragment_close);
        ivFragmentClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }


}
