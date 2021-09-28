package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NEWROW.row.Data.ContactCallInfo;
import com.NEWROW.row.R;

import java.util.ArrayList;

/**
 * Created by USER on 26-09-2016.
 */
public class PopupCallAdapter extends ArrayAdapter<ContactCallInfo> {


    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ContactCallInfo> contactCallInfos = new ArrayList<ContactCallInfo>();
    String flag_addsub = "0";
    String communicationType;
    public static ArrayList checkboxArrayList = new ArrayList<String>();
    private String chetimeslot;
    private TabLayout.Tab chk;
    String num;
    int pos=0;

    public PopupCallAdapter(Context mContext, int layoutResourceId, ArrayList<ContactCallInfo> contactCallInfos, String communicationType) {
        super(mContext, layoutResourceId, contactCallInfos);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.contactCallInfos = contactCallInfos;
        this.communicationType = communicationType;


    }


    public void setGridData(ArrayList<ContactCallInfo> contactCallInfos) {
        this.contactCallInfos = contactCallInfos;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.popup_call_list_item, parent, false);
            holder = new ViewHolder();

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.category = (TextView) row.findViewById(R.id.tv_category);
            holder.name = (TextView) row.findViewById(R.id.tv_personal_mem_name);
            holder.number = (TextView) row.findViewById(R.id.tv_personal_mobile);
            holder.label = (TextView) row.findViewById(R.id.label_mobile);
            holder.cbBox = (CheckBox) row.findViewById(R.id.cb_popup_checkbox);
            holder.callButton = (ImageView) row.findViewById(R.id.iv_popup_family_mobile);
            holder.tv_second_mobile = (TextView) row.findViewById(R.id.tv_second_mobile);
            holder.ll_one = (LinearLayout) row.findViewById(R.id.ll_one);
            holder.rl_primary = (RelativeLayout) row.findViewById(R.id.rl_primary);


            holder.rl_secondary = (RelativeLayout) row.findViewById(R.id.rl_secondary);
            holder.category_sec = (TextView) row.findViewById(R.id.tv_category);
            holder.name_sec = (TextView) row.findViewById(R.id.tv_personal_mem_name_sec);
            holder.number_sec = (TextView) row.findViewById(R.id.tv_personal_mobile_sec);
            holder.label_sec = (TextView) row.findViewById(R.id.label_mobile_sec);
            holder.cbBox_sec = (CheckBox) row.findViewById(R.id.cb_popup_checkbox_sec);
            holder.callButton_sec = (ImageView) row.findViewById(R.id.iv_popup_family_mobile_sec);
            holder.tv_second_mobile_sec = (TextView) row.findViewById(R.id.tv_second_mobile_sec);

            holder.label.setText("Mobile Number");
            //holder.cbBox.setOnCheckedChangeListener(myCheckChangList);

            if (communicationType.equals("Message")) {
                holder.cbBox.setVisibility(View.VISIBLE);
                holder.callButton.setVisibility(View.GONE);

            }
          /*  if (communicationType.equals("Email")) {
                holder.label.setText("Email Id");
                holder.callButton.setVisibility(View.GONE);

            }*/

           /* if (communicationType.equals("Call")) {
                 Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(holder.number.getText().toString()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return row ;
                }
                mContext.startActivity(in);
            }
*/

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ContactCallInfo data = contactCallInfos.get(position);

        if(communicationType.equals("Email")) {
            if (data.getContactEmail().equals("") && data.getContactEmail().equals(null)) {
                holder.label.setText("No Email Id Available");
            }
        }


        holder.category.setText(data.getContactCategory());
        holder.name.setText(data.getContactName());
        holder.number.setText(data.getContactMob());
        //holder.tv_second_mobile.setText(data.getContactSecondMob());
        if(data.getContactCategory().equals("Personal:")){
            if(communicationType.equals("Call") || communicationType.equals("Message")) {
                if(data.getContactSecondMob().equals("") || data.getContactSecondMob().equals(null))
                {
                    holder.rl_secondary.setVisibility(View.GONE);
                }
                else {
                    holder.rl_secondary.setVisibility(View.VISIBLE);
                    holder.name_sec.setText(data.getContactName());
                    holder.number_sec.setText(data.getContactSecondMob());
                    holder.label_sec.setText("Secondary Mobile No");
                }
            }
        }else {
            holder.rl_secondary.setVisibility(View.GONE);

        }

        //=====================================================================
       /* if(data.getContactCategory().equals("Family:")){
            if(communicationType.equals("Call") || communicationType.equals("Message")) {
                if(data.getContactMob().equals("") || data.getContactMob().equals(null))
                {
                    holder.rl_primary.setVisibility(View.GONE);
                }
                else {
                    holder.rl_primary.setVisibility(View.VISIBLE);
                    holder.name.setText(data.getContactName());
                    holder.number.setText(data.getContactMob());
                   // holder.label_sec.setText("Secondary Mobile No");
                }
            }
        }else {
            holder.rl_primary.setVisibility(View.GONE);

        }*/

        //=====================================================================
        if(communicationType.equals("Message"))
        {
            holder.cbBox.setVisibility(View.VISIBLE);
            holder.callButton.setVisibility(View.GONE);

            holder.cbBox_sec.setVisibility(View.VISIBLE);
            holder.callButton_sec.setVisibility(View.GONE);

        }

        if(communicationType.equals("Email"))
        {

            holder.label.setText(data.getLabelemail1());
            holder.callButton.setVisibility(View.GONE);

            if(data.getContactEmail().equals("")&&data.getContactEmail().equals(null))
            {
                holder.name.setText("No Email Id Available");
            }
            else {
                holder.cbBox.setVisibility(View.GONE);
                holder.name.setVisibility(View.GONE);
                holder.callButton.setVisibility(View.GONE);

                holder.cbBox_sec.setVisibility(View.GONE);
                holder.name_sec.setVisibility(View.GONE);
                holder.callButton_sec.setVisibility(View.GONE);

                holder.number.setText(data.getContactEmail());
                holder.rl_secondary.setVisibility(View.GONE);
            }

        }

        /*holder.rl_primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", holder.number_sec.getText().toString(), null));
                Log.d("===","==="+holder.number.getText().toString());
                mContext.startActivity(intent);
            }
        });
*/

        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (communicationType.equals("Email"))
                {

                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("plain/text");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{holder.number.getText().toString()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
                else if(communicationType.equals("Call"))
                {

                    holder.rl_primary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", holder.number.getText().toString(), null));
                            Log.d("===","==="+holder.number.getText().toString());
                            mContext.startActivity(intent);
                        }
                    });



                    holder.rl_secondary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!holder.number_sec.getText().toString().equals("")||!holder.number_sec.getText().toString().equals(null)) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", holder.number_sec.getText().toString(), null));
                                Log.e("===", "=*****************************==" + holder.number_sec.getText().toString());
                                mContext.startActivity(intent);
                            }
                        }
                    });


                    if(!holder.number.getText().toString().equals("")||!holder.number.getText().toString().equals(null)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", holder.number.getText().toString(), null));
                        Log.d("===", "===" + holder.number.getText().toString());
                        mContext.startActivity(intent);
                    }
                    else  if(!holder.number_sec.getText().toString().equals("")||!holder.number_sec.getText().toString().equals(null)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", holder.number_sec.getText().toString(), null));
                        Log.d("===", "===" + holder.number_sec.getText().toString());
                        mContext.startActivity(intent);
                    }

                }
            }
        });




        holder.ll_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cbBox.isChecked()==true)
                {
                    holder.cbBox.setChecked(false);
                }
                else
                    holder.cbBox.setChecked(true);
                checkboxArrayList.remove(num);


            }
        });



        holder.cbBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(position==1)
                {
                    pos=position;
                    pos++;
                }
                else {
                    pos=position;
                }

                if(holder.cbBox.isChecked()){


                    String num = contactCallInfos.get(pos).getContactMob();
                    // chetimeslot = (String)holder.cbBox.getTag();
                    checkboxArrayList.add(num);
                    Log.e("array value", "" + checkboxArrayList);
                    Log.e("checked slo value", "" + chetimeslot);

                } else{

                    if(contactCallInfos.size()== 2) {
                        pos = pos -1;
                        String num = contactCallInfos.get(pos).getContactMob();
                        checkboxArrayList.remove(num);
                        // pos = pos -1;
                    }
                    else {
                        String num = contactCallInfos.get(pos).getContactMob();
                        checkboxArrayList.remove(num);
                    }
                    //checkboxArrayList.remove(pos);
                    // chetimeslot = (String)holder.cbBox.getTag();
                    // checkboxArrayList.remove(num);
                    // Log.e("else array value", ""+checkboxArrayList);

                }
            }
        });




        if(data.getContactCategory().equals("Personal:"))
        {
            holder.name.setVisibility(View.GONE);
            holder.name_sec.setVisibility(View.GONE);
        }

        holder.cbBox_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(holder.cbBox_sec.isChecked()){


                    String num = contactCallInfos.get(position).getContactSecondMob();
                    // chetimeslot = (String)holder.cbBox.getTag();
                    checkboxArrayList.add(num);
                    Log.e("array value", ""+checkboxArrayList);
                    Log.e("checked slo value", ""+chetimeslot);

                } else{

                    //                   checkboxArrayList.remove(position);

                    String num = contactCallInfos.get(position).getContactMob();
                    checkboxArrayList.remove(num);

                    // chetimeslot = (String)holder.cbBox.getTag();
                    // checkboxArrayList.remove(num);
                    // Log.e("else array value", ""+checkboxArrayList);

                }
            }
        });

        return row;
    }




    static class ViewHolder {
        TextView category,name,number,label,tv_second_mobile;
        CheckBox cbBox;
        ImageView callButton;
        RelativeLayout rl_secondary,rl_primary;
        LinearLayout ll_one;
        TextView category_sec,name_sec,number_sec,label_sec,tv_second_mobile_sec;
        CheckBox cbBox_sec;
        ImageView callButton_sec;
           /* ImageView iv_arrow;
            CheckBox cbBox;*/

    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            getMember((Integer) buttonView.getTag()).box = isChecked;

        }
    };

    ContactCallInfo getMember(int position) {
        return getItem(position);
    }
}



