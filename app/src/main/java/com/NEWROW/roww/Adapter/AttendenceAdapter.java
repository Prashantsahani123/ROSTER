package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.AttendenceActivity;
import com.NEWROW.row.AttendenceDetails;
import com.NEWROW.row.Data.AttendanceData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.AttendenceHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 25-03-2017.
 */
public class AttendenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AttendanceData> list;
    String flag; // 0-Directroy 1-globalsearch --> To hide the contents


    public AttendenceAdapter(Context context, ArrayList<AttendanceData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.attendence_item, parent, false);
            AttendenceHolder holder = new AttendenceHolder(view);
            return holder;
        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindProfileData(holder, position);
    }

    public void bindProfileData(RecyclerView.ViewHolder holder, final int position) {
        if ( list.get(position) instanceof AttendanceData) {
            AttendenceHolder pHolder = (AttendenceHolder) holder;
            final AttendanceData data = list.get(position);

            pHolder.getTvEventName().setText(data.getName());
            pHolder.getTvDate().setText(data.getDate());
            pHolder.getTvTime().setText(data.getTime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AttendenceDetails.class);
                    i.putExtra("id",data.getId());
                    i.putExtra("moduleName", AttendenceActivity.title);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
