package com.SampleApp.row;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by USER on 24-12-2015.
 */
public class AddEventDateTime extends Activity {

    TextView tv_title;
    ImageView iv_backbutton;
    ImageView call_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_event_date_time);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Events");

        ToggleButton switchCompat = (ToggleButton) findViewById(R.id.compatSwitch);


    }
}
