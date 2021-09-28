package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.CalendarNotifiationData;
import com.NEWROW.row.Data.LabelData;
import com.NEWROW.row.Data.profiledata.Separator;
import com.NEWROW.row.NewProfileActivity;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.CalendarLabelHolder;
import com.NEWROW.row.holders.CalendarNotificationHolder;
import com.NEWROW.row.holders.EmptyViewHolder;

import java.util.ArrayList;

;


/**
 * Created by user on 17-02-2017.
 */
public class CalendarNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Object> list;
    private static final int VIEW_TYPE_EMPTYLIST = 1;
    private static final int VIEW_TYPE_LIST = 2;
    private static final int VIEW_TYPE_LABEL = 3;
    private static final int VIEW_TYPE_SEPERATOR = 4;
    String mobileNo = "";

    public CalendarNotificationAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTYLIST) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_calendar_recyclerview, parent, false);
            return new EmptyViewHolder(v);
        } else if (viewType == VIEW_TYPE_LIST) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.holder_calendar_notification_details, parent, false);
            CalendarNotificationHolder holder = new CalendarNotificationHolder(v);
            return holder;
        } else if (viewType == VIEW_TYPE_LABEL) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.calendar_notification_label, parent, false);
            CalendarLabelHolder holder = new CalendarLabelHolder(v);
            return holder;

        } else if ( viewType == VIEW_TYPE_SEPERATOR) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.divider, parent, false);
            DividerHolder holder = new DividerHolder(v);
            return holder;
        }
        return null;
    }

    public class DividerHolder extends RecyclerView.ViewHolder {

        public DividerHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_EMPTYLIST) {
            Utils.log("View type is EMPTY");
            bindEmptyView(holder, position);

        } else if (getItemViewType(position) == VIEW_TYPE_LABEL) {
            Utils.log("View type is : LABEL");
            bindLabelView(holder, position);
        } else {
            Utils.log("View type is : EVENT");
            bindNonEmptyView(holder, position);
        }
    }


    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {
        try {
            CalendarNotificationHolder hol = (CalendarNotificationHolder) holder;
            final CalendarNotifiationData data = ((CalendarNotifiationData) list.get(position));
            mobileNo = data.getMemberMobile();
            hol.tv_name.setText(data.getMembername());
            if  (data.getRelation().equals("")) {
                hol.tv_relation.setVisibility(View.GONE);
            } else {
                hol.tv_relation.setVisibility(View.VISIBLE);
                hol.tv_relation.setText(data.getRelation());
            }
            hol.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewProfileActivity.class);
                    intent.putExtra("memberProfileId", "" + data.getProfileId());
                    intent.putExtra("groupId", "" + data.getGroupId());
                    intent.putExtra("fromMainDirectory", "no");
                    try {
                        if ( PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY).equals(Constant.GROUP_CATEGORY_DT)) {
                            intent.putExtra("fromDTDirectory", "no");
                        }
                    } catch(Exception e) {
                        intent.putExtra("fromDTDirectory", "no");
                    }
                    context.startActivity(intent);
                }
            });

            try {
                if ( list.get(position+1) instanceof Separator ) {
                    hol.itemView.findViewById(R.id.view).setVisibility(View.GONE);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {

            }
            /*hol.iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ((CalendarNotifiationData) list.get(position)).getMemberMobile(), null));
                    context.startActivity(intent);
                }
            });
            hol.iv_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", ((CalendarNotifiationData) list.get(position)).getMemberMobile());
                    context.startActivity(smsIntent);
                }
            });*/
        } catch (Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No Records Found");
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return VIEW_TYPE_EMPTYLIST;
        } else if (list.get(position) instanceof LabelData) {
            return VIEW_TYPE_LABEL;
        } else if ( list.get(position) instanceof Separator) {
            return VIEW_TYPE_SEPERATOR;
        } else {
            return VIEW_TYPE_LIST;
        }
    }

    public void bindLabelView(RecyclerView.ViewHolder holder, int position) {
        CalendarLabelHolder lHolder = (CalendarLabelHolder) holder;
        lHolder.getTvLabel().setText(((LabelData) list.get(position)).getLabel());
        lHolder.getTvCount().setText(((LabelData) list.get(position)).getCount());
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return VIEW_TYPE_EMPTYLIST;
        }
        return list.size();
    }


}
