package com.NEWROW.row.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by Admin on 22-02-2018.
 */

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CalendarData> list;

    public MsgAdapter(Context context, ArrayList<CalendarData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_msg_phone_number, parent, false);
        PopupMsgHolder holder = new PopupMsgHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CalendarData phoneNumber = list.get(position);
        PopupMsgHolder pHolder = (PopupMsgHolder) holder;

        pHolder.getTvExtra().setVisibility(View.GONE);
        pHolder.getTvPhoneNo().setText(phoneNumber.getMemberNumber());
        pHolder.getTvTitle().setText(phoneNumber.getMemberName());
        //pHolder.getCbSelected().setChecked(phoneNumber.isSelected());

        try {
            pHolder.getTvPhoneNo().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhoneNumberClickedListener.phoneNumberClicked(phoneNumber, position);
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

    public void setOnPhoneNumberClickedListener(MsgAdapter.OnPhoneNumberClickedListener onPhoneNumberClickedListener) {
        this.onPhoneNumberClickedListener = onPhoneNumberClickedListener;
    }

    MsgAdapter.OnPhoneNumberClickedListener onPhoneNumberClickedListener;
    public interface OnPhoneNumberClickedListener {
        public void phoneNumberClicked(CalendarData pnd, int position);
    }

    public ArrayList<CalendarData> getList(){
        return list;
    }

}
