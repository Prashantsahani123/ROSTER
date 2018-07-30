package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.SampleApp.row.Data.profiledata.PopupEmailData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.PopupEmailHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupEmailRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PopupEmailData> list;

    public PopupEmailRVAdapter(Context context, ArrayList<PopupEmailData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mail_item, parent, false);
        PopupEmailHolder holder = new PopupEmailHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PopupEmailData emailData = list.get(position);
        PopupEmailHolder pHolder = (PopupEmailHolder) holder;

        pHolder.getTvEmail().setText(emailData.getEmailId());
        pHolder.getTvTitle().setText(emailData.getName());
        //pHolder.getCbSelected().setChecked(emailData.isSelected());


        try {
            pHolder.getTvEmail().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEmailIdClickedListener.onEmailIdClickListener(emailData, position);
                }
            });

            pHolder.getCbSelected().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    list.get(position).setSelected(isChecked);
                    Utils.log(
                            "List is : "+list
                    );
                }
            });

        } catch (NullPointerException npe) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnEmailIdClickedListener(OnEmailIdClickedListener onEmailIdClickedListener) {
        this.onEmailIdClickedListener= onEmailIdClickedListener;
    }

    OnEmailIdClickedListener onEmailIdClickedListener;
    public interface OnEmailIdClickedListener {
        public void onEmailIdClickListener(PopupEmailData pnd, int position);
    }

    public ArrayList<PopupEmailData> getList(){
        return list;
    }
}
