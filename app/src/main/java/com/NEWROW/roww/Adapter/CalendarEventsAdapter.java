package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.NewProfileActivity;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.holders.CalendarEventsHolder;
import com.NEWROW.row.holders.EmptyViewHolder;
import com.NEWROW.row.sql.DirectoryDataModel;

import java.util.ArrayList;

/**
 * Created by user on 13-02-2017.
 */
public class CalendarEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public ArrayList<CalendarData> list;
    private static final int View_TYPE_EMPTYLIST = 1;
    private static final int View_TYPE_EVENTS = 2;
    DirectoryDataModel directoryDataModel;
    String type;

    public CalendarEventsAdapter(Context context, ArrayList<CalendarData> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
        directoryDataModel = new DirectoryDataModel(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if(type.equalsIgnoreCase("A")||type.equalsIgnoreCase("B")){
            View v = LayoutInflater.from(context).inflate(R.layout.calender_birhday_anniversary_item, parent, false);
            CalendarEventsHolder holder = new CalendarEventsHolder(v);
            Log.d("♦♦♦♦ ", "Inside Bithday or Anniversary ");
            return holder;
        }else if(type.equalsIgnoreCase("E")) {
           View v = LayoutInflater.from(context).inflate(R.layout.calender_event_item, parent, false);
           CalendarEventsHolder holder = new CalendarEventsHolder(v);
           Log.d("♦♦♦♦ ", "Inside Event ");
           return holder;
       }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_calendar_recyclerview, parent, false);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(type.equals("A")||type.equals("B")||type.equals("E")){
            bindNonEmptyView(holder, position);
        }else{
            bindEmptyView(holder, position);
        }

    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        // ((EmptyViewHolder) holder).getEmptyView().setText("No Record Found For This Period");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        CalendarEventsHolder hol = (CalendarEventsHolder) holder;

        hol.tv_name.setText(list.get(position).getTitle());

    }


    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return View_TYPE_EMPTYLIST;
        } else {
            return View_TYPE_EVENTS;
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return View_TYPE_EMPTYLIST;
        }
        return list.size();
    }

    public void getProfileData(int profileId, int groupId) {
        Intent intent = new Intent(context, NewProfileActivity.class);
        intent.putExtra("memberProfileId", "" + profileId);
        intent.putExtra("groupId", "" + groupId);
        intent.putExtra("fromMainDirectory", "no");
        try {
            if ( PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY).equals("2")) {
                intent.putExtra("fromDTDirectory", "yes");
            }
        } catch(Exception e) {
            intent.putExtra("fromDTDirectory", "no");
        }

        context.startActivity(intent);
    }


}
