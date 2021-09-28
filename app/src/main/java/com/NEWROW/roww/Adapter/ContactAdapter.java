package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.Contact_Import;
import com.NEWROW.row.Data.ContactData;
import com.NEWROW.row.R;

import java.util.ArrayList;


/**
 * Created by USER1 on 16-06-2016.
 */
public class ContactAdapter extends ArrayAdapter<ContactData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ContactData> list_contact = new ArrayList<ContactData>();
    Contact_Import contact_import;
   // String flag_addsub = "0";

    public ContactAdapter(Context mContext, int layoutResourceId, ArrayList<ContactData> list_contact) {
        super(mContext, layoutResourceId, list_contact);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_contact = list_contact;
    }

    public void setGridData(ArrayList<ContactData> list_contact) {
        this.list_contact = list_contact;
        notifyDataSetChanged();
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
            holder.tv_mobile = (TextView) row.findViewById(R.id.tv_mobile);
            holder.country_code = (TextView) row.findViewById(R.id.country_code);
            holder.ll_contact = (LinearLayout) row.findViewById(R.id.ll_contact);
          //  holder.cbBox = (CheckBox) row.findViewById(R.id.cbBox);
          //  holder.cbBox.setOnCheckedChangeListener(myCheckChangList);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ContactData item = list_contact.get(position);
       /* holder.cbBox.setTag(position);
        holder.cbBox.setChecked(item.box);

        if (flag_addsub.equals("1")) {
            holder.cbBox.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.GONE);
   holder.cbBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( holder.cbBox.isChecked()==true)
                    {
                        holder.cbBox.setChecked(false);
                    }
                    else
                        holder.cbBox.setChecked(true);

                }
            });

        }*/



        holder.tv_name.setText(item.getContactName());
        holder.tv_mobile.setText(item.getContactNumber());
        holder.country_code.setText(item.getCountryCode());




        CheckBox cbBuy = (CheckBox) row.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(item.box);

        return row;
    }

//---------------------------------------------------------------------------------------------
ContactData getProduct(int position) {
    return ((ContactData) getItem(position));
}

    public ArrayList<ContactData> getBox() {
        ArrayList<ContactData> box = new ArrayList<ContactData>();
        for (ContactData p : list_contact) {
            if (p.box)
                box.add(p);
        }


        Log.d("@@@@@@----------------@","----------" +box);
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };


//-------------------------------------------------------------------------------------------



  /*  public ArrayList<SubGoupData> getBox() {
        ArrayList<SubGoupData> box = new ArrayList<SubGoupData>();
        for (SubGoupData p : list_subGroup)
        {
            if (p.box)
                box.add(p);

        }
        return box;
    }

    SubGoupData getsubgrps(int position) {
        return ((SubGoupData) getItem(position));
    }
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getsubgrps((Integer) buttonView.getTag()).box = isChecked;

        }
    };
*/


    static class ViewHolder {
        TextView tv_name, tv_mobile,country_code;
        LinearLayout ll_contact;
        //ImageView iv_arrow;
        CheckBox cbBox;

    }


}

