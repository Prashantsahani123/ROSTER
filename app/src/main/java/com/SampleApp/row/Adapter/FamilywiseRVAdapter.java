package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.SampleApp.row.Data.FamilywiseMemberData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.FamilywiseMemberHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 25-03-2017.
 */
public class FamilywiseRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<FamilywiseMemberData> list;
    String flag; // 0-Directroy 1-globalsearch --> To hide the contents


    public FamilywiseRVAdapter(Context context, ArrayList<FamilywiseMemberData> list) {
        this.context = context;
        this.list = list;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.familywise_directory_item, parent, false);
            FamilywiseMemberHolder holder = new FamilywiseMemberHolder(view);
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
        if ( list.get(position) instanceof FamilywiseMemberData) {
            FamilywiseMemberHolder pHolder = (FamilywiseMemberHolder) holder;
            final FamilywiseMemberData data = list.get(position);

            pHolder.getTvMemberName().setText(data.getFamilyMemberName()+" ("+data.getMemberName()+")");
            pHolder.getTvNameRelation().setText(data.getRelation() + " ( "+ data.getFamilyMemberName() +" )");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( onMemberSelectedListener != null ) {

                        onMemberSelectedListener.onMemberSelected(data, position);

                    } else {
                        Toast.makeText(context, "Event not registered", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getMemberId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnMemberSelectedListener{
        public void onMemberSelected(FamilywiseMemberData data, int position);
    }

    public void setOnMemberSelectedListener(OnMemberSelectedListener onMemberSelectedListener) {
        this.onMemberSelectedListener = onMemberSelectedListener;
    }

    OnMemberSelectedListener onMemberSelectedListener;
}
