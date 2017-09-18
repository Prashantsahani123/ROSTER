package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.SampleApp.row.Data.profiledata.ClassificationData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.ClassificationHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 25-03-2017.
 */
public class ClassificationRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ClassificationData> list;
    String flag; // 0-Directroy 1-globalsearch --> To hide the contents


    public ClassificationRVAdapter(Context context, ArrayList<ClassificationData> list, String flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.classification_layout, parent, false);
            ClassificationHolder holder = new ClassificationHolder(view);
            return holder;
        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindClassificationData(holder, position);
    }

    public void bindClassificationData(RecyclerView.ViewHolder holder, final int position) {
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

    public OnClassificationSelectedListener getOnClassificationSelectedListener() {
        return onClassificationSelectedListener;
    }

    public void setOnClassificationSelectedListener(OnClassificationSelectedListener onClassificationSelectedListener) {
        this.onClassificationSelectedListener = onClassificationSelectedListener;
    }

    OnClassificationSelectedListener onClassificationSelectedListener;
}
