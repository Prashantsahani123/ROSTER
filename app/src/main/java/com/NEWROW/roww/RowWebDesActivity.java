package com.NEWROW.row;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RowWebDesActivity extends AppCompatActivity {

    String typ;
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6,ll7;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_web_des);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Features");

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        //ll3 = findViewById(R.id.ll3);
     /*   ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);*/

        Intent i = getIntent();
        typ = i.getStringExtra("type");
        Log.d("TypeValue", "*************** " + typ);

        if(typ.equals("type1")){

            ll1.setVisibility(View.VISIBLE);

            ll2.setVisibility(View.GONE);
            /*ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);*/

        }else if(typ.equals("type2")){

            ll1.setVisibility(View.GONE);

            ll2.setVisibility(View.VISIBLE);
           /* ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll6.setVisibility(View.VISIBLE);
            ll7.setVisibility(View.VISIBLE);
*/
        }
    }
}
