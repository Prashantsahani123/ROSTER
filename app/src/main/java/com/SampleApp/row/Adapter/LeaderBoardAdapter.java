package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.SampleApp.row.Data.LeaderBoardData;
import com.SampleApp.row.R;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private Context context;
    private final List<LeaderBoardData> allItems;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final TextView tv_count,tv_name,tv_score;

        public ViewHolder(View view) {
            super(view);

            this.mView = view;
            this.tv_count = (TextView) view.findViewById(R.id.tv_count);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_score = (TextView) view.findViewById(R.id.tv_score);

            this.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                   /* Intent intent = new Intent(context, MonthlyReportActivity.class);
                    intent.putExtra("month name",allItems.get(getAdapterPosition()));
                    context.startActivity(intent);*/
                }
            });
        }
    }

    public LeaderBoardAdapter(Context context, List<LeaderBoardData> items) {
        allItems = items;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_borad_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        LeaderBoardData leaderBoardData = allItems.get(position);
        holder.tv_name.setText(leaderBoardData.getName());
        holder.tv_score.setText(leaderBoardData.getScore());
        holder.tv_count.setText(""+(position+1));

    }

    public int getItemCount() {
        return this.allItems.size();
    }

}