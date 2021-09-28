package com.NEWROW.row;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeaturesActivity extends AppCompatActivity {
    TextView tv_title,title;
    LinearLayout ll1,ll2,ll3,ll4;
    View v1,v2,v3,v4;
    ImageView iv_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Features");

        iv_backbutton = (ImageView)findViewById(R.id.iv_backbutton);

        title = findViewById(R.id.title);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);


        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(FeaturesActivity.this,FeaturesSubActivity.class);
                intent.putExtra("type", "type1");
                startActivity(intent);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeaturesActivity.this,FeaturesSubActivity.class);
                intent.putExtra("type", "type2");
                startActivity(intent);
            }
        });

        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeaturesActivity.this,PublicImage.class);
                intent.putExtra("type", "type4");
                startActivity(intent);
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeaturesActivity.this,RowWeb.class);
                startActivity(intent);
            }
        });




    }
}
