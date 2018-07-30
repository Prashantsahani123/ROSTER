package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SampleApp.row.Data.AttendanceData;
import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by USER on 06-10-2016.
 */
public class AttendanceListAdapter extends ArrayAdapter<AttendanceData> {

    private String[] names;
    private String[] id;
    private Integer[] percentage;
    private Activity context;


    private Context mContext;
    private int layoutResourceId;

    private ArrayList<AttendanceData> attendanceDataArrayList = new ArrayList<AttendanceData>();
    private int pos;
    //String flag; //data is available in list or not



   /* public AttendanceListAdapter(Activity context, String[] names, String[] id, Integer[] percentage) {
        super(context, R.layout.attendance_list_item, names);
        this.context = context;
        this.names = names;
        this.id = id;
        this.percentage = percentage;

    }*/



    public AttendanceListAdapter(Context mContext, int layoutResourceId, ArrayList<AttendanceData> attendanceDataArrayList) {
        super(mContext, layoutResourceId, attendanceDataArrayList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.attendanceDataArrayList = attendanceDataArrayList;
        //this.flag = flag;

    }

    public void setGridData(ArrayList<AttendanceData> attendanceDataArrayList) {
        this.attendanceDataArrayList = attendanceDataArrayList;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();


            holder.tvName = (TextView) row.findViewById(R.id.tv_name);
            holder.tvId = (TextView) row.findViewById(R.id.tv_id);
            holder.tvPercentage = (TextView) row.findViewById(R.id.compliance_percentage);
            holder.progress = (ProgressBar) row.findViewById(R.id.pd_fore);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.pb_back);

            holder.progressbackRed = (ProgressBar) row.findViewById(R.id.pb_redback);
            holder.progressforeRed = (ProgressBar) row.findViewById(R.id.pb_redfore);


            holder.progressgray = (ProgressBar) row.findViewById(R.id.pb_gray);
            holder.tv_percentage_sign = (TextView)row.findViewById(R.id.tv_percentage_sign);
            holder.tvLabelId = (TextView)row.findViewById(R.id.tv_labelId);
            //progress.setProgress(58);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        AttendanceData data = attendanceDataArrayList.get(position);

            try {

                if(data.getName().equals("") || data.getName().equals(0))
                {
                    holder.progressgray.setVisibility(View.VISIBLE);
                    holder.progress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.progressforeRed.setVisibility(View.GONE);
                    holder.progressbackRed.setVisibility(View.GONE);
                    holder.tvName.setVisibility(View.GONE);
                    holder.tvId.setVisibility(View.GONE);
                    holder.tvPercentage.setText("N.A.");
                    holder.tvLabelId.setText("");
                    holder.tvPercentage.setTextColor(Color.parseColor("#747474"));
                    holder.tv_percentage_sign.setText("");
                }
                else if (data.getAttendance().equals("") || data.getAttendance().equals(0)) {
                    holder.progressgray.setVisibility(View.VISIBLE);
                    holder.progress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.progressforeRed.setVisibility(View.GONE);
                    holder.progressbackRed.setVisibility(View.GONE);
                    holder.tvPercentage.setText("N.A.");
                    holder.tvLabelId.setText("");
                    holder.tvPercentage.setTextColor(Color.parseColor("#747474"));
                    holder.tv_percentage_sign.setTextColor(Color.parseColor("#747474"));
                    holder.tv_percentage_sign.setText("");

                } else {

                    holder.tvName.setText(data.getName());
                    holder.tvId.setText(data.getId());
                    holder.tvPercentage.setText(data.getAttendance());
                    holder.progressforeRed.setVisibility(View.INVISIBLE);
                    holder.progressbackRed.setVisibility(View.INVISIBLE);
                    holder.progress.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.VISIBLE);
                   // holder.tvPercentage.setTextColor(Color.GREEN);
                    holder.tvPercentage.setTextColor(Color.parseColor("#00a651"));
                    holder.tv_percentage_sign.setTextColor(Color.parseColor("#00a651"));
                    holder.progress.setProgress(Math.round( Float.parseFloat(data.getAttendance())));
                    holder.tvLabelId.setText("ID : ");

                    if (Float.parseFloat(data.getAttendance()) < 75) {
                        holder.progressforeRed.setVisibility(View.VISIBLE);
                        holder.progressbackRed.setVisibility(View.VISIBLE);
                        holder.progressforeRed.setProgress(Math.round( Float.parseFloat(data.getAttendance())));
                        holder.tvPercentage.setTextColor(Color.RED);
                        holder.progress.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                        holder.tvPercentage.setTextColor(Color.parseColor("#FF3333"));
                        holder.tv_percentage_sign.setTextColor(Color.parseColor("#FF3333"));
                        holder.tvLabelId.setText("ID : ");
                    }
                }
            }catch(NumberFormatException ex){
                ex.printStackTrace();
                holder.progress.setProgress(0);
                holder.tvPercentage.setText("N.A.");
                holder.tv_percentage_sign.setText("");
            }


        //=== Schools are close------

        return row;


    }

    static class ViewHolder {
        TextView tvName;
        TextView tvId,tvLabelId;
        TextView tvPercentage,tv_percentage_sign;
        ProgressBar progress;
        ProgressBar progressBar;
        ProgressBar progressbackRed;
        ProgressBar progressforeRed;
        ProgressBar progressgray;

    }
}