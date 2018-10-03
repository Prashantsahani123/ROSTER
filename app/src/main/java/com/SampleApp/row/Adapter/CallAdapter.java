package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.CalendarData;
import com.SampleApp.row.Data.profiledata.PopupPhoneNumberData;
import com.SampleApp.row.R;
import com.SampleApp.row.holders.MobileViewHolder;

import java.util.ArrayList;

/**
 * Created by Admin on 21-02-2018.
 */

public class CallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CalendarData> list;

    public CallAdapter() {
    }

    public CallAdapter(Context context, ArrayList<CalendarData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mobile_number_item, parent, false);
        MobileViewHolder holder = new MobileViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CalendarData phoneNumber = list.get(position);
        MobileViewHolder pHolder = (MobileViewHolder) holder;

        pHolder.txt_title.setText(phoneNumber.getMemberName());
        pHolder.txt_mobile.setText(phoneNumber.getMemberNumber());

        try {
            pHolder.ll_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phoneNumber.getMemberNumber()));

                    context.startActivity(callIntent);
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
         void phoneNumberClicked(PopupPhoneNumberData pnd, int position);
    }
}
