package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.Data.AttendenceDetailsData;
import com.SampleApp.row.Inteface.AttendanceItemClick;
import com.SampleApp.row.R;

import java.util.ArrayList;

/**
 * Created by Admin on 10-04-2018.
 */

public class AttendenceDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<AttendenceDetailsData> list=new ArrayList<>();
    public Context context;
    private static LayoutInflater inflater = null;
    boolean isSelectedAll;
    private AttendanceItemClick attendanceItemClick;
    public AttendenceDetailsAdapter(ArrayList<AttendenceDetailsData> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.attendence_details_item, parent, false);
        AttendenceDetailsHolder holder = new AttendenceDetailsHolder(v);
        return holder;
    }

    public void setClickListener(AttendanceItemClick itemClickListener) {
        this.attendanceItemClick = itemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final AttendenceDetailsData data=list.get(position);
        final AttendenceDetailsHolder attendenceHolder= (AttendenceDetailsHolder) holder;

        attendenceHolder.txt_title.setText(data.getCount()+" "+data.getTitle());





    }

    public class AttendenceDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_title;
        public LinearLayout ll_item;


        public AttendenceDetailsHolder(View itemView) {
            super(itemView);
            txt_title=(TextView)itemView.findViewById(R.id.txt_title);
            ll_item=(LinearLayout)itemView.findViewById(R.id.ll_catitem);
            ll_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (attendanceItemClick != null) attendanceItemClick.onClick(v, getAdapterPosition());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
