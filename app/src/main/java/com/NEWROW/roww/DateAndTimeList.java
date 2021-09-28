package com.NEWROW.row;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by USER on 24-12-2015.
 */
public class DateAndTimeList extends Activity {

    TextView tv_date,tv_time;
    ArrayAdapter<String> adapter;
    ListView listview;
    final String[] date = new String[] {"17 Jan 2016","20 Jan 2016","25 Jan 2016","28 Jan 2016","30 Jan 2016","31 Jan 2016","1 Jan 2016"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.date_and_time_list);
        listview = (ListView) findViewById(R.id.listView);




        adapter = new ArrayAdapter<String>(this,R.layout.date_and_time_list_item,R.id.tv_date,date );
        listview.setAdapter(adapter);







    }
}
