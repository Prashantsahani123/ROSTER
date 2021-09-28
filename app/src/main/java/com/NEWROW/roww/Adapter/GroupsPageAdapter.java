package com.NEWROW.row.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.NEWROW.row.FragmentALL;
/**
 * Created by user on 23-12-2015.
 */
public class GroupsPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public GroupsPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentALL tab1 = new FragmentALL();
                return tab1;
            default:
                return null;
        }
       /* Log.d("ARRAYLIST", " POSITION " + position);
        if (position == 0) // if the position is 0 we are returning the First tab
        {
            FragmentALL tab1 = new FragmentALL();
            return tab1;
        } else if (position == 1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            FragmentPersonal tab2 = new FragmentPersonal();
            return tab2;
        }else if (position == 2)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            FragmentSocial tab3 = new FragmentSocial();
            return tab3;
        } else {
            FragmentBusiness tab4 = new FragmentBusiness();
            return tab4;

        }*/
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
