package com.NEWROW.row;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RowWeb extends AppCompatActivity {

    Button dist_btn,club_btn;
    LinearLayout ll1,ll2,ll3;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_web);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Features");

        dist_btn = findViewById(R.id.dist_btn);
        club_btn = findViewById(R.id.club_btn);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);


        dist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RowWeb.this,RowWebDesActivity.class);
                intent.putExtra("type", "type1");
                startActivity(intent);
            }
        });

        club_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RowWeb.this,RowWebDesActivity.class);
                intent.putExtra("type", "type2");
                startActivity(intent);
            }
        });

    }
}
