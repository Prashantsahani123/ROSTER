package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.SampleApp.row.DTCalendarEventDetails;
import com.SampleApp.row.Data.CalendarData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.EventDetails;
import com.SampleApp.row.NewProfileActivity;
import com.SampleApp.row.ProfileActivityV4;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.CalendarEventsHolder;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.sql.DirectoryDataModel;

/**
 * Created by user on 13-02-2017.
 */
public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public ArrayList<CalendarData> list;
    private static final int View_TYPE_EMPTYLIST = 1;
    private static final int View_TYPE_EVENTS = 2;
    DirectoryDataModel directoryDataModel;

    public EventsAdapter(Context context, ArrayList<CalendarData> list) {
        this.context = context;
        this.list = list;
        directoryDataModel = new DirectoryDataModel(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == View_TYPE_EMPTYLIST) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_calendar_recyclerview, parent, false);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if (viewType == View_TYPE_EVENTS) {
            View v = LayoutInflater.from(context).inflate(R.layout.holder_calendar_events, parent, false);
            CalendarEventsHolder holder = new CalendarEventsHolder(v);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_EVENTS ");
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == View_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        } else {
            bindNonEmptyView(holder, position);
        }

    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        // ((EmptyViewHolder) holder).getEmptyView().setText("No Record Found For This Period");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        CalendarEventsHolder hol = (CalendarEventsHolder) holder;
        String date = list.get(position).getEventDate();
        String dd = "";
        String time = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date n = df.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
            SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a");
            dd = sdf.format(n);
            time = sdf_time.format(n);

        } catch (Exception e) {
            e.printStackTrace();
        }

        hol.tv_date.setText(dd);
        hol.tv_tile.setText(list.get(position).getTitle());
        hol.tv_type.setText(list.get(position).getType());
        if (list.get(position).getType().equalsIgnoreCase("Event")) {
            hol.tv_eventtime.setText(time);
            hol.tv_eventtime.setPadding(3, 3, 3, 3);
        } else {
            hol.tv_eventtime.setPadding(0, 0, 0, 0);
            hol.tv_eventtime.setVisibility(View.GONE);
        }

        hol.ll_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getType().equalsIgnoreCase("Event")) {
                    String groupType = PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY, "1");
                    Utils.log("Group Type : "+groupType);
                    if  ( groupType.equals(""+Constant.GROUP_CATEGORY_DT)) {
                        Intent i = new Intent(context, DTCalendarEventDetails.class);
                        i.putExtra("eventid", String.valueOf(list.get(position).getTypeId()));
                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, EventDetails.class);
                        i.putExtra("eventid", String.valueOf(list.get(position).getTypeId()));
                        context.startActivity(i);
                    }
                } else {
                    getProfileData(list.get(position).getTypeId(), list.get(position).getGroupId());
//
                }
            }
        });

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
            if ( PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY).equals(Constant.GROUP_CATEGORY_DT)) {
                intent.putExtra("fromDTDirectory", "no");
            }
        } catch(Exception e) {
            intent.putExtra("fromDTDirectory", "no");
        }

        context.startActivity(intent);
    }


}
