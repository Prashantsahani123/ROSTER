package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Data.ProfileData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 18-03-2016.
 */
public class ProfileAdapter_EditDetails extends ArrayAdapter<ProfileData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ProfileData> profileListDatas = new ArrayList<ProfileData>();
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();

    public ProfileAdapter_EditDetails(Context mContext, int layoutResourceId, ArrayList<ProfileData> profileListDatas) {
        super(mContext, layoutResourceId, profileListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.profileListDatas = profileListDatas;
    }

    public void setGridData(ArrayList<ProfileData> profileListDatas) {
        this.profileListDatas = profileListDatas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.tv_key = (TextView) row.findViewById(R.id.tv_key);
            holder.et_value = (EditText) row.findViewById(R.id.et_value);
            holder.tv_value = (TextView) row.findViewById(R.id.tv_value);
            holder.tv_country_code = (TextView) row.findViewById(R.id.tv_country_code);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ProfileData item = profileListDatas.get(position);
        holder.ref = position;
        holder.tv_key.setText(item.getKey());




        //   Log.d("TEST", "----" + item.getColType());
        if (item.getColType().equals("Text") || item.getColType().equals("mobile")) {
            holder.et_value.setText(item.getValue());
            holder.et_value.setVisibility(View.VISIBLE);
            holder.tv_country_code.setVisibility(View.GONE);
            holder.tv_value.setVisibility(View.GONE);
            if (item.getColType().equals("mobile")) {
                if (item.getUniquekey().equals("member_mobile_no")) {
                    //  holder.et_value.setClickable(false);
                    holder.et_value.setText(item.getValue());
                    holder.tv_value.setVisibility(View.GONE);
                    holder.tv_country_code.setVisibility(View.GONE);
                    holder.et_value.setClickable(false);
                    holder.et_value.setVisibility(View.VISIBLE);
                    holder.tv_value.setClickable(false);

                    if(item.getKey().equals("Mobile Number"))
                    {
                        holder.et_value.setEnabled(false);
                    }


                } else {
                    holder.et_value.setClickable(true);
                    holder.et_value.setInputType(InputType.TYPE_CLASS_NUMBER);

                    holder.tv_country_code.setVisibility(View.VISIBLE);
                    holder.et_value.setVisibility(View.GONE);
                    holder.et_value.setVisibility(View.VISIBLE);

                    // --SPIT TO COUNTRY CODE
                    //      Log.d("TOUCHBAASE", " SPLIT ---" + item.getValue());
                    String[] separated_eventDate = item.getValue().trim().split(" ");
                    //       Log.d("TOUCHBAASE", " SPLIT ---" + separated_eventDate.length +"--"+separated_eventDate[0]);
                    if (separated_eventDate.length == 1) {
                        holder.et_value.setText(separated_eventDate[0]);
                    } else if (separated_eventDate.length == 2) {
                        holder.et_value.setText(separated_eventDate[1]);
                        holder.tv_country_code.setText(separated_eventDate[0]);
                    }
                    // --SPIT TO COUNTRY CODE
                    holder.tv_country_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(mContext, holder.tv_country_code);

                            addcountry(popup);

                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    String c_name = String.valueOf(item.getTitle());

                                    int pos = 0;
                                    for (int i = 0; i < Utils.countryarraylist.size(); i++) {
                                        // int pos = 0;
                                        List<CountryData> list = new ArrayList<CountryData>();
                                        list.add(new CountryData(Utils.countryarraylist.get(i).getCountryId(), Utils.countryarraylist.get(i).getCountryCode(), Utils.countryarraylist.get(i).getCountryName()));
                                        // Log.d("TOUCHBAASE", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ---" + list);
                                        if (c_name.equals(Utils.countryarraylist.get(i).getCountryName())) {
                                            pos = Integer.parseInt(Integer.toString(i));
                                            // Log.d("-----Position", "....................................... Country CODE POP ---" + pos);
                                        }
                                    }
                                    holder.tv_country_code.setText(Utils.countryarraylist.get(pos).getCountryCode());
                                    String[] separated = item.getTitle().toString().split("-");
                                    /*if (separated.length > 1) {
                                        holder.tv_country_code.setText(separated[0]);
                                    }else{
                                        holder.tv_country_code.setText("+91");
                                    }*/
                                    String temp = holder.tv_country_code.getText().toString() + " " + holder.et_value.getText().toString();
                                    //      Log.d("TOUCHBAASE", " Country CODE POP ---" + temp);
                                    profileListDatas.get(holder.ref).setValue(temp);
                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });


                    holder.et_value.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                            //profileListDatas[holder.ref] = arg0.toString();
                            String temp = "";
                            if (holder.et_value.getText().toString().trim().length() > 0) {
                                temp = holder.tv_country_code.getText().toString() + " " + holder.et_value.getText().toString();
                            } else {
                                temp = "";
                            }
                            //     Log.d("TOUCHBAASE", " Country CODE ---" + temp);
                            profileListDatas.get(holder.ref).setValue(temp);
                        }
                    });
                }
            } else {
                holder.et_value.setClickable(true);
                holder.et_value.setInputType(InputType.TYPE_CLASS_TEXT);


                holder.et_value.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        ///profileListDatas[holder.ref] = arg0.toString();
                        profileListDatas.get(holder.ref).setValue(arg0.toString());
                    }
                });
            }
        }
        else if(item.getUniquekey().equals("BusinessName"))
        {
            holder.et_value.setVisibility(View.VISIBLE);
            holder.et_value.setText(item.getValue());
            holder.tv_country_code.setVisibility(View.GONE);
            holder.tv_value.setVisibility(View.GONE);
        }

        else if (item.getColType().equals("Date")) {
            holder.tv_country_code.setVisibility(View.GONE);
            //holder.tv_value.setText(item.getValue());
            if (item.getValue().isEmpty() || item.getValue() == null) {
                holder.tv_value.setText("");
                holder.tv_value.setHint("Enter Date");
            } else {
                holder.tv_value.setText(date_formatter(item.getDate()));
                profileListDatas.get(holder.ref).setValue(date_formatter(item.getDate()));
            }
            holder.tv_value.setVisibility(View.VISIBLE);
            holder.et_value.setVisibility(View.GONE);

            if (item.getValue().isEmpty()) {
                holder.tv_value.setHint("Enter Date");
            }
            holder.tv_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datepicker(holder.tv_value, holder.ref);
                    //profileListDatas.get(holder.ref).setValue(holder.tv_value.getText().toString());
                }
            });
        } else if (item.getColType().equals("bloodGroup")) {
            holder.tv_country_code.setVisibility(View.GONE);
            holder.tv_value.setVisibility(View.VISIBLE);
            holder.et_value.setVisibility(View.GONE);
            holder.tv_value.setText(item.getValue());
            holder.tv_value.setHint("Enter Blood Group");
            holder.tv_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Log.d("TOUCHBASE", "CLICK CLICK");
                    PopupMenu popup = new PopupMenu(mContext, holder.tv_value);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.blood_group, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            // Toast.makeText(AddFamilyDetailToProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            holder.tv_value.setText(item.getTitle());
                            profileListDatas.get(holder.ref).setValue(item.getTitle().toString());
                            return true;
                        }
                    });
                    popup.show();
                }
            });

        } else {
            holder.tv_value.setText(item.getValue());
            holder.tv_value.setVisibility(View.VISIBLE);
            holder.et_value.setVisibility(View.GONE);
        }

     /*   holder.et_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (lastFocussedPosition == -1 || lastFocussedPosition == position) {
                                lastFocussedPosition = position;
                                holder.et_value.requestFocus();
                            }
                        }
                    }, 200);

                } else {
                    lastFocussedPosition = -1;
                }
            }
        });*/

        return row;

    }


    static class ViewHolder {
        TextView tv_key, tv_value, tv_country_code;
        EditText et_value;
        int ref;

    }

    public void datepicker(final TextView setdatetext, final int pos) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setdatetext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        profileListDatas.get(pos).setValue(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public String date_formatter(String inputdate) {
        //String date = "2011/11/12";
        String date = inputdate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    private void addcountry(PopupMenu popup) {
        for (int i = 0; i < Utils.countryarraylist.size(); i++) {
            //  popup.getMenu().add("+1");
            //  popup.getMenu().add(Utils.countryarraylist.get(i).getCountryCode() + "-" + Utils.countryarraylist.get(i).getCountryName());
            popup.getMenu().add(Utils.countryarraylist.get(i).getCountryName());

        }


    }

}
