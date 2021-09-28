package com.NEWROW.row;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by USER on 23-12-2015.
 */
public class Celebration extends Activity {

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton;
    EditText et_search;
    final String[] ebulletine = new String[] { "Apricot","Asparagus",
            " Broad Bean","Beet root","Courgette","Date",
            "Endive" ,"Fennel",
            "Grapes","Green Beans","Garlic","Guava","Haricot Bean","Leamon","Mango","Paper","Sweet Potato","Tomato","Yam","Watermelon"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.celebration);

        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Celebrations");


        adapter = new ArrayAdapter<String>(this, R.layout.celebration_list_item, R.id.tv_name, ebulletine);
        listview.setAdapter(adapter);

     /*   listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Celebration.this, EventDetails.class);
                startActivity(i);
            }
        });
*/
    }
}


