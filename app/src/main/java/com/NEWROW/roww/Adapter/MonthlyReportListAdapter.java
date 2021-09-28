package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NEWROW.row.MonthlyReportActivity;
import com.NEWROW.row.R;

import java.util.List;

public class MonthlyReportListAdapter extends RecyclerView.Adapter<MonthlyReportListAdapter.ViewHolder> {

    private Context context;
    private final List<String> allItems;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView tv_month_title;

        public ViewHolder(View view) {
            super(view);

            this.mView = view;

            this.tv_month_title = (TextView) view.findViewById(R.id.tv_month_title);

            this.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

//                    SurveyPollingData item = allItems.get(getAdapterPosition());

                    /*if(item.getSurveyStatus().equalsIgnoreCase("Expired")){
                        Intent intent = new Intent(context, SurveyResultActivity.class);
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, SurveyDetailsActivity.class);
                        intent.putExtra("status",item.getSurveyStatus());
                        context.startActivity(intent);
                    }*/

                    Intent intent = new Intent(context, MonthlyReportActivity.class);
                    intent.putExtra("month name",allItems.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });
        }
    }

    public MonthlyReportListAdapter(Context context, List<String> items) {
        allItems = items;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_report_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_month_title.setText(allItems.get(position));
    }

    public int getItemCount() {
        return this.allItems.size();
    }

}