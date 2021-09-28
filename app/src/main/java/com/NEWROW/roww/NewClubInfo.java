package com.NEWROW.row;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.NEWROW.row.Data.FindAClubResultData;


/**
 * Created by user on 23-12-2015.
 */
public class NewClubInfo extends AppCompatActivity {
    ViewPager viewPager;
    ClubInfoAdapter adapter;
    TextView tv_title;
    //ImageView iv_backbutton,iv_actionbtn,iv_actionbtn2;
    TabLayout tabLayout;
    FindAClubResultData clubData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_club_info);

        Intent intent = getIntent();

        if (intent.hasExtra("clubData")) {
            try {
                clubData = (FindAClubResultData) intent.getExtras().getSerializable("clubData");
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        actionbarfunction();
        tablayout();
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Clubs");
    }

    private void tablayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));//(" Communication "));
        tabLayout.addTab(tabLayout.newTab().setText("Activity "));//(" Showcase "));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new ClubInfoAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    public class ClubInfoAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        public ClubInfoAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    FragmentClubInfo clubInfoTab = new FragmentClubInfo();
                    Bundle extras = new Bundle();
                    extras.putSerializable("clubData", clubData);
                    //clubInfoTab.getArguments().putSerializable("clubData", clubData);
                    clubInfoTab.setArguments(extras);
                    return clubInfoTab;
                case 1:
                    FragmentClubMembers clubMembersTab = new FragmentClubMembers();
                    Bundle extras1 = new Bundle();
                    extras1.putSerializable("clubData", clubData);
                    clubMembersTab.setArguments(extras1);
                    return clubMembersTab;
                case 2:
//                  FragmentCommunication communicationTab = new FragmentCommunication();
                    FragmentEvents communicationTab = new FragmentEvents();
                    Bundle extras3 = new Bundle();
                    extras3.putSerializable("clubData", clubData);
                    communicationTab.setArguments(extras3);
                    return communicationTab;
                case 3:
                    // FragmentGalleryNew galleryTab = new FragmentGalleryNew();
                    FragmentClubGallery galleryTab = new FragmentClubGallery();
                    Bundle extras4 = new Bundle();
                    extras4.putSerializable("clubData", clubData);
                    galleryTab.setArguments(extras4);
                    return galleryTab;
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
