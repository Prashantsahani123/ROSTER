package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.BuildConfig;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.SampleApp.row.DashboardActivity.drawer;


/**
 * Created by root on 11/12/15.
 */
public class NavAdapter extends BaseAdapter {

    public LayoutInflater minflater;
    public Activity activity;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title

    private ArrayList<Integer> images;

    public NavAdapter(Context context, List<String> listDataHeader, ArrayList<Integer> images) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.images = images;
    }


    @Override
    public int getCount() {
        return _listDataHeader.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.drawer_list_item, null);

        LinearLayout llTitle = (LinearLayout)convertView.findViewById(R.id.llTitle);
        LinearLayout llList = (LinearLayout)convertView.findViewById(R.id.llList);
        LinearLayout ll_version = (LinearLayout)convertView.findViewById(R.id.ll_version);
        TextView txt_version = (TextView)convertView.findViewById(R.id.txt_version);

        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        ImageView img_drawer_img = (ImageView) convertView.findViewById(R.id.drawer_img);
        ImageView imgCloseDrawer = (ImageView)convertView.findViewById(R.id.drawerClose);
        if(position==0){
            llTitle.setVisibility(View.VISIBLE);
            llList.setVisibility(View.GONE);
            ll_version.setVisibility(View.GONE);
            title.setText(_listDataHeader.get(position));
        }else if(position==_listDataHeader.size()-1){
            llTitle.setVisibility(View.GONE);
            llList.setVisibility(View.GONE);
            ll_version.setVisibility(View.VISIBLE);

            if(Constant.BaseURL_V3.contains(Constant.LIVE_URL)){
                txt_version.append(" "+ BuildConfig.VERSION_NAME);

            }else {
                txt_version.append("  T"+ BuildConfig.VERSION_NAME);
            }
        }
        else{
            llTitle.setVisibility(View.GONE);
            llList.setVisibility(View.VISIBLE);
            ll_version.setVisibility(View.GONE);
            txtListChild.setText(_listDataHeader.get(position));
            img_drawer_img.setImageResource(images.get(position));
        }

        imgCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(Gravity.START)){
                    drawer.closeDrawer(Gravity.START);
                }
            }
        });

        return convertView;
    }


}