package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.NEWROW.row.Data.profiledata.PopupPhoneNumberData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.PopupMsgHolder;

import java.util.ArrayList;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupMsgRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PopupPhoneNumberData> list;

    public PopupMsgRVAdapter(Context context, ArrayList<PopupPhoneNumberData> list) {
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

        final PopupPhoneNumberData phoneNumber = list.get(position);

        PopupMsgHolder pHolder = (PopupMsgHolder) holder;

        String extraData = phoneNumber.getExtra();

        pHolder.getTvExtra().setText(phoneNumber.getExtra());
        pHolder.getTvPhoneNo().setText(phoneNumber.getNumber());
        pHolder.getTvTitle().setText(phoneNumber.getName());
        //pHolder.getCbSelected().setChecked(phoneNumber.isSelected());

        // added by satish on 24-05-2019 because tester give issue extra should not display
        if(extraData.equalsIgnoreCase("member_mobile_no") || extraData.equalsIgnoreCase("secondry_mobile_no") || extraData.equalsIgnoreCase("phone_no")){
            pHolder.getTvExtra().setText("");
        }

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

                    Utils.log("List is : "+list
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

    public void setOnPhoneNumberClickedListener(OnPhoneNumberClickedListener onPhoneNumberClickedListener) {
        this.onPhoneNumberClickedListener = onPhoneNumberClickedListener;
    }

    OnPhoneNumberClickedListener onPhoneNumberClickedListener;
    public interface OnPhoneNumberClickedListener {
        public void phoneNumberClicked(PopupPhoneNumberData pnd, int position);
    }

}
