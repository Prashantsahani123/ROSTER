package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.profiledata.PopupPhoneNumberData;
import com.SampleApp.row.R;
import com.SampleApp.row.holders.PopupCallHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupCallRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PopupPhoneNumberData> list;

    public PopupCallRVAdapter(Context context, ArrayList<PopupPhoneNumberData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_phone_number, parent, false);
        PopupCallHolder holder = new PopupCallHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PopupPhoneNumberData phoneNumber = list.get(position);
        PopupCallHolder pHolder = (PopupCallHolder) holder;

        pHolder.getTvExtra().setText(phoneNumber.getExtra());
        pHolder.getTvPhoneNo().setText(phoneNumber.getNumber());
        pHolder.getTvTitle().setText(phoneNumber.getName());

        try {
            pHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhoneNumberClickedListener.phoneNumberClicked(phoneNumber, position);
                }
            });
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setOnPhoneNumberClickedListener(OnPhoneNumberClickedListener onPhoneNumberClickedListener) {
        this.onPhoneNumberClickedListener = onPhoneNumberClickedListener;
    }

    OnPhoneNumberClickedListener onPhoneNumberClickedListener;
    public interface OnPhoneNumberClickedListener {
        public void phoneNumberClicked(PopupPhoneNumberData pnd, int position);
    }
}
