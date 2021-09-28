package com.NEWROW.row;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarGalleryFragment extends Fragment {

    ImageView iv_backbutton,iv_search;
    LinearLayout ll_search,ll_main;
    TextView txt_cancel;
    EditText et_search;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.actionbar_gallery, container, false);

        iv_backbutton = (ImageView) view.findViewById(R.id.iv_backbutton);

        iv_search = (ImageView) view.findViewById(R.id.iv_search);

        //this serach button closed By Gaurav For Not Showing currently via this search option
        //As per discussion with Naren Sir
        iv_search.setVisibility(View.GONE);

        ll_main=(LinearLayout)view.findViewById(R.id.relative_actionbar);
        ll_search=(LinearLayout)view.findViewById(R.id.ll_searchBar);
        txt_cancel=(TextView)view.findViewById(R.id.txt_cancel);
        et_search=(EditText)view.findViewById(R.id.et_searchBar);

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

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_main.setVisibility(View.GONE);
                ll_search.setVisibility(View.VISIBLE);
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_main.setVisibility(View.VISIBLE);
                ll_search.setVisibility(View.GONE);
                et_search.getText().clear();
            }
        });

        return view;
    }
}
