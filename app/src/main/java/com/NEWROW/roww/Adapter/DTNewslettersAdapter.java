package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.DTNewslettersData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.DTNewslettersHolder;
import com.NEWROW.row.holders.EmptyViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by user on 13-02-2017.
 */
public class DTNewslettersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public ArrayList<DTNewslettersData> list;
    private static final int VIEW_TYPE_EMPTYLIST = 1;
    private static final int VIEW_TYPE_NEWSLETTERS = 2;

    public DTNewslettersAdapter(Context context, ArrayList<DTNewslettersData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTYLIST) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            Log.d("♦♦♦♦ ", "Inside VIEW_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if (viewType == VIEW_TYPE_NEWSLETTERS) {
            View v = LayoutInflater.from(context).inflate(R.layout.holder_dt_newsletters, parent, false);
            DTNewslettersHolder holder = new DTNewslettersHolder(v);
            Log.d("♦♦♦♦ ", "Inside VIEW_TYPE_NEWSLETTERS ");
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        } else {
            bindNonEmptyView(holder, position);
        }

    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
         ((EmptyViewHolder) holder).getEmptyView().setText("No Record Found For This Period");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        DTNewslettersHolder hol = (DTNewslettersHolder) holder;
        String[] date = list.get(position).getPublishDateTime().split(" ");
        String dd = "";
        String time = "";
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        try {
            dd = date[0]+" "+date[1]+" "+date[2];
            time = date[3]+" "+date[4];
            /*Date n = df.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a");
            dd = sdf.format(n);
            time = sdf_time.format(n);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        hol.getTvNewsletterTitle().setText(list.get(position).getEbulletinTitle());
        hol.getTvDateTime().setText(dd+"   |   "+time);

        hol.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemSelectedListener.onItemSelected(position);
                } catch (NullPointerException npe) {
                    Utils.log("OnItemSelectedListener not registered");
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return VIEW_TYPE_EMPTYLIST;
        } else {
            return VIEW_TYPE_NEWSLETTERS;
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return VIEW_TYPE_EMPTYLIST;
        }
        return list.size();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public ArrayList<DTNewslettersData> getItems() {
        return list;
    }

    private OnItemSelectedListener onItemSelectedListener;

    public OnItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
