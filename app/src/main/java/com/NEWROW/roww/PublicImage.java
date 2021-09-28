package com.NEWROW.row;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PublicImage extends AppCompatActivity {

    LinearLayout ll1,ll11,ll2,ll22,ll3,ll33,ll4,ll44,ll5,ll6,ll7;
    TextView tv_title;
    String typ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_image);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Features");

        ll1 = findViewById(R.id.ll1);
        ll11 = findViewById(R.id.ll11);
        ll2 = findViewById(R.id.ll2);
        ll22 = findViewById(R.id.ll22);
        ll3 = findViewById(R.id.ll3);
        ll33 = findViewById(R.id.ll33);
        ll4 = findViewById(R.id.ll4);
        ll44 = findViewById(R.id.ll44);

        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);

        Intent i = getIntent();
        typ = i.getStringExtra("type");
        Log.d("TypeValue", "*************** " + typ);

        if(typ.equals("type1")){

            ll1.setVisibility(View.VISIBLE);
            ll11.setVisibility(View.VISIBLE);

            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
        }
        else if(typ.equals("type2")){

            ll2.setVisibility(View.VISIBLE);
            ll22.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);

        }
        else if(typ.equals("type3")){

            ll3.setVisibility(View.VISIBLE);
            ll33.setVisibility(View.VISIBLE);

            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);

        }
        else if(typ.equals("type4")){
            ll4.setVisibility(View.VISIBLE);
            ll44.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);

        }else if(typ.equals("dist1")){

            ll5.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);

        }else if(typ.equals("dist2")){

            ll6.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);

        }else if(typ.equals("dist3")){

            ll7.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll11.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll22.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll33.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll44.setVisibility(View.GONE);
        }
    }
}
