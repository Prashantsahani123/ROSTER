package com.NEWROW.row;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Adapter.SettingsGroupListAdapter;
import com.NEWROW.row.Data.GroupData;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    TextView tv_title;
    ImageView iv_backbutton;
    RecyclerView rv_grpList;
    ArrayList<GroupData> grplist = new ArrayList<>();
    Bundle bundle;
    SettingsGroupListAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bundle = getIntent().getExtras();

        grplist = (ArrayList<GroupData>) bundle.getSerializable("grpList");

        init();
    }

    private void init() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Settings");
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        rv_grpList = (RecyclerView)findViewById(R.id.rv_grpListForSetting);

        rv_grpList.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new SettingsGroupListAdapter(SettingActivity.this, grplist);
        rv_grpList.setAdapter(rvAdapter);
    }
}
