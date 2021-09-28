package com.NEWROW.row;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class ActionBarFragmentPDFView extends Fragment {

    ImageView iv_backbutton;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.actionbar_pdf_view_fragment, container, true);

        iv_backbutton = (ImageView) view.findViewById(R.id.iv_backbutton);


        iv_backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                //Toast.makeText(getActivity(), "Here", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();

            }
        });


        return view;
    }


}
