package com.NEWROW.row;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Adapter.RotaryLyoutAdapter;

public class RotaryWorld extends AppCompatActivity {

    GridView gv;
    TextView tv_title;
    ImageView iv_backbutton;

    //ArrayList of Rotary World
    String[] appName_RW = {"Library", "Rotary News", "Rotary Blog", "Rotary.org"};
    Integer[] appImg_RW = {R.drawable.library, R.drawable.news, R.drawable.blog, R.drawable.rotary};
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_world);

        gv = (GridView) findViewById(R.id.gridView1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        tv_title.setText(title);

        if (tv_title.getText().equals("Rotary World")) {

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {

                        case 0:
                            Intent i = new Intent(RotaryWorld.this, RotaryLibraryActivity.class);
                            startActivity(i);
                            break;
                        case 1:
                            Intent i1 = new Intent(RotaryWorld.this, RotaryNews.class);
                            startActivity(i1);
                            break;
                        case 2:
                            Intent i2 = new Intent(RotaryWorld.this, RotaryBlog.class);
                            startActivity(i2);
                            break;
                        case 3:
                            Intent i3 = new Intent(RotaryWorld.this, OpenLinkActivity.class);
                            i3.putExtra("modulename", "Rotary.org");
                            i3.putExtra("link", "https://my.rotary.org/en");
                            startActivity(i3);
                    }

                }
            });

            RotaryLyoutAdapter adapter = new RotaryLyoutAdapter(RotaryWorld.this, appName_RW, appImg_RW);
            gv.setAdapter(adapter);
        }

    }
}
