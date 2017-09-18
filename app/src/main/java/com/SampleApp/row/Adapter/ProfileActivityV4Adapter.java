package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.ProfileData;
import com.SampleApp.row.R;


/**
 * Created by user on 02-03-2016.
 */
public class ProfileActivityV4Adapter extends ArrayAdapter<ProfileData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ProfileData> profileListDatas = new ArrayList<ProfileData>();
    String listType; // wheather it us personal or business

    /*public ProfileActivityV4Adapter(Context mContext, int layoutResourceId, ArrayList<ProfileData> profileListDatas) {
        super(mContext, layoutResourceId, profileListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.profileListDatas = profileListDatas;
    }*/

    public ProfileActivityV4Adapter(Context mContext, int layoutResourceId, ArrayList<ProfileData> profileListDatas, String listType) {
        super(mContext, layoutResourceId, profileListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.profileListDatas = profileListDatas;
        this.listType = listType;
    }

    public void setGridData(ArrayList<ProfileData> profileListDatas) {
        this.profileListDatas = profileListDatas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.profileTitle = (TextView) row.findViewById(R.id.tv_name);
            holder.profileDesc = (TextView) row.findViewById(R.id.tv_mobile);
            holder.ll_list_item = (LinearLayout) row.findViewById(R.id.ll_list_item);
            holder.ivLeftimage = (ImageView) row.findViewById(R.id.iv_leftside_image);
            holder.ivRightimage = (ImageView) row.findViewById(R.id.iv_rightside_image);
            holder.ll_cell = (LinearLayout) row.findViewById(R.id.ll_cell);
            holder.vw_divider = (View) row.findViewById(R.id.vw_divider);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ProfileData item = profileListDatas.get(position);
        holder.vw_divider.setVisibility(View.GONE);

        if (item.getKey().equals("Name")) {

            holder.ll_cell.setVisibility(View.GONE);
        } else if (item.getValue().equals("") || item.getValue().equals(null)) {
            holder.ll_cell.setVisibility(View.GONE);
        } else {
            holder.vw_divider.setVisibility(View.GONE);
            holder.ll_cell.setVisibility(View.VISIBLE);
         //   holder.profileTitle.setText(item.getKey());
        //    holder.profileDesc.setText(item.getValue());

            if (listType.equals("business")) {
                holder.vw_divider.setVisibility(View.GONE);
            }

            if (listType.equals("business")) {
                holder.vw_divider.setVisibility(View.GONE);

                if (item.getKey().equals("Business Email")) {
                  /*holder.profileTitle.setVisibility(View.GONE);
                    holder.profileDesc.setVisibility(View.GONE);
                    holder.ll_cell.setVisibility(View.GONE);
                    holder.vw_divider.setVisibility(View.GONE);*/
                }
                else
                holder.ll_cell.setVisibility(View.VISIBLE);
                holder.profileTitle.setText(item.getKey());
                holder.profileDesc.setText(item.getValue());
                holder.vw_divider.setVisibility(View.GONE);
            }
           else {
                holder.vw_divider.setVisibility(View.GONE);
                holder.ll_cell.setVisibility(View.VISIBLE);
                holder.profileTitle.setText(item.getKey());
                holder.profileDesc.setText(item.getValue());
            }
        }

        //------------------------------Blue Color Field --------------------
        if (item.getKey().equals("Mobile Number")) {
            holder.profileDesc.setTextColor(Color.parseColor("#4868D2"));
            holder.profileDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String number = holder.profileDesc.getText().toString();
                    Log.e("----", "---NUMBER ---- " + number);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", number, null));

                    mContext.startActivity(intent);
                }
            });
        }

        if (item.getKey().equals("Secondary Mobile No")) {
            holder.vw_divider.setVisibility(View.VISIBLE);
            holder.profileDesc.setTextColor(Color.parseColor("#4868D2"));
            holder.profileDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = holder.profileDesc.getText().toString();
                    Log.e("----", "---NUMBER ---- " + number);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", number, null));

                    mContext.startActivity(intent);
                }
            });
        }

        if (item.getKey().equals("Business Email")) {

            String email = holder.profileDesc.getText().toString();
            if (email.equals("") && email.equals(null)) {
                holder.vw_divider.setVisibility(View.GONE);
            }
            else if (listType.equals("business")) {
                holder.vw_divider.setVisibility(View.GONE);
            }
            else if (listType.equals("personal")) {
                holder.vw_divider.setVisibility(View.VISIBLE);
            }
        }

        if (item.getKey().equals("Email ID")) {
            if (item.getValue().equals("") || item.getValue().equals(null)) {
                holder.vw_divider.setVisibility(View.GONE);
            }
        }

        if (item.getKey().equals("Secondry Mobile No")) {
            String number = holder.profileDesc.getText().toString();
            if (number.equals("") && number.equals(null)) {
                holder.vw_divider.setVisibility(View.GONE);
            }
        }

        if (item.getKey().equals("Email ID")) {
            holder.profileDesc.setTextColor(Color.parseColor("#4868D2"));
            holder.profileDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("plain/text");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{holder.profileDesc.getText().toString()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                }
            });

        }
        if (item.getKey().equals("Business Email")) {
            holder.profileDesc.setTextColor(Color.parseColor("#4868D2"));
            holder.profileDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("plain/text");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{holder.profileDesc.getText().toString()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });

        }

        if (item.getKey().equals("Date of Birth")) {
            holder.profileDesc.setTextColor(Color.parseColor("#000000"));
        }
        //-------------------------------------------------------------------------
        return row;
    }

    static class ViewHolder {
        TextView profileTitle, profileDesc;
        LinearLayout ll_list_item, ll_cell;
        ImageView ivLeftimage, ivRightimage;
        View vw_divider;
    }


}
