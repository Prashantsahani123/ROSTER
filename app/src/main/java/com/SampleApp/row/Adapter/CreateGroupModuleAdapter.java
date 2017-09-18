package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.SampleApp.row.Data.CreateModuleData;


/**
 * Created by USER on 05-02-2016.
 */
public class CreateGroupModuleAdapter extends ArrayAdapter<CreateModuleData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CreateModuleData> list_createmodule = new ArrayList<CreateModuleData>();
    Dialog dialog_edit;
    EditText newModulename;
    TextView cancel,confirm,modulename;
    ImageView close,image;
    String strnewModuleName;

    public CreateGroupModuleAdapter(Context mContext, int layoutResourceId, ArrayList<CreateModuleData> list_createmodule) {
        super(mContext, layoutResourceId, list_createmodule);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_createmodule = list_createmodule;
    }

    public void setGridData(ArrayList<CreateModuleData> list_createmodule) {
        this.list_createmodule = list_createmodule;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list_createmodule.size();
    }

    @Override
    public CreateModuleData getItem(int position) {
        return list_createmodule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView name,moduleid;
        RelativeLayout info,edit;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final  ViewHolder holder;

        final CreateModuleData p = getMember(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.name = (TextView) row.findViewById(R.id.tv_name);
            holder.info = (RelativeLayout)row.findViewById(R.id.info);
            holder.edit = (RelativeLayout)row.findViewById(R.id.edit);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final CreateModuleData item = list_createmodule.get(position);

        holder.name.setText(item.getNewName());



        final CheckBox cbBuy = (CheckBox) row.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.box);

        LinearLayout name = (LinearLayout)row.findViewById(R.id.linear_name);

        name.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                if (cbBuy.isChecked() == true) {
                    cbBuy.setChecked(false);
                } else
                    cbBuy.setChecked(true);

            }

        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.create_group_module_popup);

                TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
                TextView tv_alert_info = (TextView)dialog.findViewById(R.id.tv_alert_info);
                TextView tv_modulename = (TextView)dialog.findViewById(R.id.tv_modulename);

                 tv_alert_info.setText(item.getModuleInfo());
                tv_modulename.setText(item.getNewName());


                tv_ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_edit = new Dialog(mContext, android.R.style.Theme_Translucent);
                dialog_edit.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_edit.setContentView(R.layout.popup_modulename_edit);
                dialog_edit.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_edit.show();

                newModulename = (EditText) dialog_edit.findViewById(R.id.et_new_modulename);
                modulename= (TextView)dialog_edit.findViewById(R.id.tv_modulename);
                modulename.setText(item.getNewName());
                cancel = (TextView)dialog_edit.findViewById(R.id.tv_cancel);
                close = (ImageView)dialog_edit.findViewById(R.id.iv_close);
                confirm = (TextView) dialog_edit.findViewById(R.id.tv_confirm);
                image=(ImageView)dialog_edit.findViewById(R.id.iv_moduleimage);
                Picasso.with(mContext).load(item.getImage())
                        .placeholder(R.drawable.diretory_icon)
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_edit.dismiss();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_edit.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        strnewModuleName = newModulename.getText().toString();
                        if(newModulename.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(mContext,"Please Enter module name",Toast.LENGTH_SHORT).show();
                        }else {
                            dialog_edit.dismiss();
                            item.setNewName(strnewModuleName);
                            holder.name.setText(item.getNewName());
                        }

                    }
                });
            }
        });
        return row;

    }

    /////////////////////////////////////////////////////////////////////

    CreateModuleData getMember(int position) {
        return ((CreateModuleData) getItem(position));
    }

    public ArrayList<CreateModuleData> getBox() {
        ArrayList<CreateModuleData> box = new ArrayList<CreateModuleData>();
        for (CreateModuleData p : list_createmodule)
        {
            if (p.box)
                box.add(p);

        }

        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            getMember((Integer) buttonView.getTag()).box = isChecked;

        }
    };


}




