package com.NEWROW.row;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.NEWROW.row.Adapter.GroupsPageAdapter;
import com.NEWROW.row.Data.GroupData;

import java.util.ArrayList;



/**
 * Created by user on 23-12-2015.
 */
public class  GroupsListingDashboard extends AppCompatActivity {
    ViewPager viewPager;
    GroupsPageAdapter adapter;
    TextView tv_title;
    ImageView iv_backbutton,iv_actionbtn,iv_actionbtn2;
    TabLayout tabLayout;
    ArrayList<GroupData> grplist = new ArrayList<>();
    Intent myintent;
    // String grpid;
    /*BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction().equals(Constant.GROUP_DATA_LOADED)) {
                if ( getIntent().getStringExtra("openGroup") != null ) {
                    Intent intent = new Intent(GroupsListingDashboard.this, GroupDashboard.class);
                    startActivity(intent);
                }
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplistingdashboard);

//        startService(new Intent(this, XmppConnectService.class));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        actionbarfunction();
        tablayout();
        //Log.d("TOUCHBASE", "UDID ---" + PreferenceManager.getPreference(GroupsListingDashboard.this, PreferenceManager.UDID));

        /*
        * From SplashImageSlider, "openGroup" data is passed when user comes inside SplashImageSlider by clicking invitation link. If use is already
        * logged in it is expected that he/she must be displayed with the dashboard of that group to which he/she has been added by group admin
        * */
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        tv_title.setText("Roster On Wheels");
        iv_actionbtn.setVisibility(View.GONE);
        iv_actionbtn2.setVisibility(View.VISIBLE);
        iv_backbutton.setVisibility(View.GONE);
        iv_actionbtn.setImageResource(R.drawable.search_btn);


        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupsListingDashboard.this, Global_Search.class);
                startActivity(i);
            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(GroupsListingDashboard.this, iv_actionbtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.dashboard_main_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                             case R.id.settings:
                                Intent ii = new Intent(getApplicationContext(),Settings.class);
                                startActivity(ii);
                                // read the listItemPosition here
                                return true;
                            case R.id.aboutus:
                                startActivity(new Intent(GroupsListingDashboard.this, AboutActivity.class));
                                return true;
                            default:
                                return false;
                        }
                        //return true;
                    }
                });

                popup.show();
            }
        });

    }

    private void tablayout() {
        tabLayout.addTab(tabLayout.newTab().setText(" All "));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new GroupsPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    tv_title.setText("All");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showAboutUsDialog(){

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
}
