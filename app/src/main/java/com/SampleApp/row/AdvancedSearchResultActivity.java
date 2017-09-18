package com.SampleApp.row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.Adapter.DirectoryRVAdapter;
import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ProfileModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by USER1 on 21-04-2017.
 */
public class AdvancedSearchResultActivity extends Activity {
    private static final String DYNAMIC_FIELDS_FILE = "dynamicField.json";
    private ImageView iv_backbutton, iv_actionbtn;
    private TextView tv_title, tvNoResults;
    private String groupId = "";
    private Context context;

    private Hashtable<String, String[]> searchData;

    private RecyclerView rvSearchResult;
    private DirectoryRVAdapter adapter;
    private ArrayList<ProfileMasterData> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search_result);

        context = this;
        groupId = getIntent().getStringExtra("groupId");

        searchData = new Hashtable<>();
        list = new ArrayList<>();
        Type type = new TypeToken<Hashtable<String, String[]>>(){}.getType();
        searchData = new Gson().fromJson(getIntent().getStringExtra("searchData"), type);
        //Utils.log("Search fields are : " + new Gson().toJson(searchData).toString());
        init();
        actionbarFunction();
        search();
    }

    public void init() {
        tvNoResults = (TextView) findViewById(R.id.tvNoResults);
        rvSearchResult = (RecyclerView) findViewById(R.id.rvSearchResult);
        adapter = new DirectoryRVAdapter(context, list, "0");
        rvSearchResult.setLayoutManager(new LinearLayoutManager(context));
        rvSearchResult.setAdapter(adapter);
        adapter.setOnMemberSelectedListener(memberSelectedListener);
    }

    public void search() {
        ProfileModel model = new ProfileModel(context);
        list = model.dynamicSearch(Long.parseLong(groupId), searchData);

        if ( list.size() == 0 ) {
            tvNoResults.setVisibility(View.VISIBLE);
            rvSearchResult.setVisibility(View.GONE);
        } else {
            adapter = new DirectoryRVAdapter(context, list, "0");
            rvSearchResult.setAdapter(adapter);
            adapter.setOnMemberSelectedListener(memberSelectedListener);
        }
    }

    private void actionbarFunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title.setText("Search Result");
        iv_actionbtn.setVisibility(View.GONE);

        /*iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    DirectoryRVAdapter.OnMemberSelectedListener memberSelectedListener = new DirectoryRVAdapter.OnMemberSelectedListener() {
        @Override
        public void onMemberSelected(ProfileMasterData data, int position) {
            try {
                Intent intent = new Intent(context, NewProfileActivity.class);
                intent.putExtra("memberProfileId", data.getProfileId());
                intent.putExtra("groupId", data.getGrpId());
                startActivity(intent);
            } catch(Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }
    };
}
