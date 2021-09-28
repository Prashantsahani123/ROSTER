package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.NEWROW.row.Data.profiledata.ClassificationData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.ClassificationHolder;
import com.NEWROW.row.holders.ProfileMasterHolder;

import java.util.ArrayList;

/**
 * Created by Admin on 23-01-2018.
 */

public class ClassificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ClassificationData> list;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private String module="";
    public ClassificationAdapter(Context context, ArrayList<ClassificationData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            if(viewType==VIEW_TYPE_ITEM) {
                module = "ITEM";
                View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.classification_layout, parent, false);
                ClassificationHolder holder = new ClassificationHolder(view);
                return holder;
            }
            else {
                module="LOAD";
                View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_item, parent, false);
                ProfileMasterHolder holder = new ProfileMasterHolder(view);
                return holder;
            }
        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (module.equalsIgnoreCase("ITEM")) {
            if ( list.get(position) instanceof ClassificationData) {
                ClassificationHolder pHolder = (ClassificationHolder) holder;
                final ClassificationData data = list.get(position);
                pHolder.getTvClassificationName().setText(data.getClassificationName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if ( onClassificationSelectedListener != null ) {

                        onClassificationSelectedListener.onClassificationSelected(data, position);

                    } else {
                        Toast.makeText(context, "Event not registered", Toast.LENGTH_LONG).show();
                    }
                    }
                });
            }
        } else {

//            ClassificationHolder pHolder = (ClassificationHolder) holder;
//            pHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClassificationSelectedListener{
        public void onClassificationSelected(ClassificationData data, int position);
    }

    public void setonClassificationSelectedListener(OnClassificationSelectedListener onClassificationSelectedListener) {
        this.onClassificationSelectedListener = onClassificationSelectedListener;
    }


    OnClassificationSelectedListener onClassificationSelectedListener;
}
