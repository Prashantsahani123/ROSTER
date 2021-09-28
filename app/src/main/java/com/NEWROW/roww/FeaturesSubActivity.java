package com.NEWROW.row;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeaturesSubActivity extends AppCompatActivity {

    TextView tv_title;
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
    View v1,v2,v3,v4,v5,v6;
    String typ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features_sub);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Features");

        Intent i = getIntent();
        typ = i.getStringExtra("type");
        Log.d("TypeValue", "*************** " + typ);


        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v5 = findViewById(R.id.v5);
        v6 = findViewById(R.id.v6);


        if(typ.equals("type1")){

            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);


            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);

        }else if(typ.equals("type2")){

            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll6.setVisibility(View.VISIBLE);

            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);

        }

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "type1");
                startActivity(intent);

            }
        });


        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "type2");
                startActivity(intent);

            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "type3");
                startActivity(intent);

            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "dist1");
                startActivity(intent);

            }
        });

        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "dist2");
                startActivity(intent);

            }
        });

        ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesSubActivity.this,PublicImage.class);
                intent.putExtra("type", "dist3");
                startActivity(intent);

            }
        });



    }
}
