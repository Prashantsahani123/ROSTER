package com.SampleApp.row;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppBarCalendarFragment extends Fragment {

    public static TextView tv_title,txt_month;
    public static LinearLayout ll_button;
    public static ImageView iv_backbutton,iv_arrow;

    public AppBarCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_app_bar_calendar, container, false);
        iv_backbutton=(ImageView)view.findViewById(R.id.iv_backbutton);
        iv_arrow=(ImageView)view.findViewById(R.id.iv_arrow);
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        txt_month=(TextView)view.findViewById(R.id.txt_month);
        ll_button=(LinearLayout) view.findViewById(R.id.ll_button);
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalenderActivity.act.finish();
            }
        });

        return view;
    }

}
