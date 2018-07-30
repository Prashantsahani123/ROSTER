package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.ContactData;
import com.SampleApp.row.EditMember;
import com.SampleApp.row.Inteface.StartActivityForResultInterface;
import com.SampleApp.row.R;
import com.SampleApp.row.SelectCountry;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.SampleApp.row.EditMember.list_contactData;

/**
 * Created by USER1 on 17-06-2016.
 */
public class EditMemberAdapter extends ArrayAdapter<ContactData>    {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ContactData> list_contact = new ArrayList<ContactData>();
    private int mRequestCode = 100;
    private static final int STATIC_INTEGER_VALUE = 100;
    // String flag_addsub = "0";
    String country_code,country_id;
    private StartActivityForResultInterface myInterface;
    private EditMember activity;
    EditText et_name ;
    TextView tv_country_code ;
    TextView et_mobile;
    int currentPos=0;
    String nameEdit,numberEdit,countryCodeEdit;
    final Pattern MOBILE_PATTERN = Pattern.compile("[0-9]{3,20}");


    public EditMemberAdapter(EditMember activity,Context mContext, int layoutResourceId, ArrayList<ContactData> list_contact) {
        super(mContext, layoutResourceId, list_contact);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.activity = activity;
        this.list_contact = list_contact;
    }


    public void setGridData(ArrayList<ContactData> list_contact) {
        this.list_contact = list_contact;
        notifyDataSetChanged();
    }

    public void setCallback (StartActivityForResultInterface myInterface) {
        this.myInterface = myInterface;
    }


   /* public void updateResults(ArrayList<ContactData> results) {
        list_contact = results;
        //Triggers the list update
        notifyDataSetChanged();
    }
*/


    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case STATIC_INTEGER_VALUE:
                if (requestCode == STATIC_INTEGER_VALUE && resultCode == activity.RESULT_OK) {
                    if (data != null) {

                        String country_code = data.getStringExtra("countryCode");
                    //    String country_id = data.getStringExtra("countryid");
                    //    String pos = data.getStringExtra("pos");



                   /*   ContactData contactData = new ContactData();
                        contactData.setCountryCode(countryCodeEdit);
                        contactData.setContactName(nameEdit);
                        contactData.setContactNumber(numberEdit);
                        contactData.setBox(list_contactData.get(currentPos).isBox());
                        contactData.setIdNumber(list_contactData.get(currentPos).getIdNumber());*/

                   /*     for (ContactData model : latestDealsModels) {
                            ContactData m = new ContactData();
                            m.setLatitude(model.getPosition().get(0));
                            m.setLongitude(model.getPosition().get(1));
                            m.setShopName(model.getShopName());
                            //  city = manager.getAddress(model.getPosition().get(0), model.getPosition().get(1));

                            String city = "";
                            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(model.getPosition().get(0), model.getPosition().get(1), 1);
                                if (addresses.size() > 0) {
                                    city = addresses.get(0).getSubLocality();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            String dist = String.format("%.2f", manager.distFrom(model.getPosition().get(0), model.getPosition().get(1), Double.parseDouble(lat), Double.parseDouble(lon)));
                            model.setCity(city);
                            model.setDist(Double.parseDouble(dist) + " km");
                            Constants.MAP_MODELS.add(m);
                            Constants.DealType = TAG;
                            arrayList.add(model);}*/

                        countryCodeEdit =country_code;
                        tv_country_code.setText(""+country_code);

                        //   list_contactData.set(currentPos,contactData);
                        notifyDataSetChanged();
                    }

                }
                break;
        }

    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
            holder.tv_number = (TextView) row.findViewById(R.id.tv_number);
            holder.country_code = (TextView) row.findViewById(R.id.tv_country_code);
            holder.iv_edit = (ImageView) row.findViewById(R.id.iv_edit);
            holder.ll_contact = (LinearLayout) row.findViewById(R.id.ll_contact);
            holder.iv_delete = (ImageView)row.findViewById(R.id.iv_delete);

            //holder.country_code = (TextView) row.findViewById(R.id.country_code);
            //  holder.cbBox = (CheckBox) row.findViewById(R.id.cbBox);
            //  holder.cbBox.setOnCheckedChangeListener(myCheckChangList);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ContactData item = list_contact.get(position);

        holder.tv_name.setText(item.getContactName());
        holder.tv_number.setText(item.getContactNumber());
        holder.country_code.setText(item.getCountryCode());

        if(item.getCountryCode().equalsIgnoreCase("0")||item.getCountryCode().equalsIgnoreCase(""))
        {
            holder.ll_contact.setBackground(mContext.getResources().getDrawable(R.drawable.contact_border));
        }
        else {
            holder.ll_contact.setBackground(null);
        }

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

              /*  Intent i = new Intent(mContext, EditContact.class);
                i.putExtra("name", item.getContactName());
                i.putExtra("number", item.getCountryCode() + item.getContactNumber());
                i.putExtra("pos",""+position);
                ((Activity) mContext).startActivityForResult(i, STATIC_INTEGER_VALUE);
              */

                final Dialog dialog = new Dialog(getContext(),android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_edit_member);
               // holder.country_code.setText(country_code);


                final String name = item.getContactName();
                final String number = item.getContactNumber();
                final String countryCode = item.getCountryCode();

                 et_name = (EditText) dialog.findViewById(R.id.et_name);
                 tv_country_code = (TextView) dialog.findViewById(R.id.tv_country_code);
                 et_mobile = (TextView) dialog.findViewById(R.id.et_mobile);
                TextView tv_confirm = (TextView)dialog.findViewById(R.id.tv_confirm);
                ImageView iv_close = (ImageView)dialog.findViewById(R.id.iv_close);

                et_name.setText(name);
                et_mobile.setText(number);
                tv_country_code.setText(countryCode);

                tv_country_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  Intent i = new Intent(mContext, SelectCountry.class);
                        //((Activity) mContext).startActivityForResult(i, mRequestCode);
                        i.putExtra("pos",""+position);
                        myInterface.myStartActivityForResult(i, STATIC_INTEGER_VALUE);*/

                        Intent i = new Intent((Activity) mContext, SelectCountry.class);
                        activity.startActivityForResult(i, mRequestCode);
                    }
                });


                iv_close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                tv_confirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        if (validation() == true) {
                            currentPos = position;


                            holder.tv_name.setText(et_name.getText().toString());
                            holder.tv_number.setText(et_mobile.getText().toString());
                            holder.country_code.setText(tv_country_code.getText().toString());


                            Log.d("-------", "--------" + list_contact);

                            nameEdit = et_name.getText().toString();
                            numberEdit = et_mobile.getText().toString();
                            countryCodeEdit = tv_country_code.getText().toString();

                            dialog.dismiss();

                            tv_country_code.setText("" + countryCodeEdit);

                            ContactData contactData = new ContactData();
                            contactData.setCountryCode(countryCodeEdit);
                            contactData.setContactName(nameEdit);
                            contactData.setContactNumber(numberEdit);
                            contactData.setBox(list_contactData.get(currentPos).isBox());
                            contactData.setIdNumber(list_contactData.get(currentPos).getIdNumber());
                            list_contactData.set(currentPos, contactData);
                            notifyDataSetChanged();

                        }
                    }

                });

                dialog.show();

            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_contact.remove(position);
                notifyDataSetChanged();
            }
        });

        return row;
    }



   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ( mContext).onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (STATIC_INTEGER_VALUE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);


                }
                break;
            }
        }
    }*/


     static class ViewHolder {
        TextView tv_name, tv_number, country_code;
        ImageView iv_edit,iv_delete;
         LinearLayout ll_contact;

         //ImageView iv_arrow;


    }

    public boolean validation()
    {
        if(et_name.getText().toString().trim().matches("") || et_name.getText().toString().trim() == null) {
            Toast.makeText(mContext, "Please Enter Name", Toast.LENGTH_LONG).show();
            return false;
        }
        if(et_mobile.getText().toString().trim().matches("") || et_mobile.getText().toString().trim()== null ) {

            Toast.makeText(mContext, "Please Enter  Mobile Number ", Toast.LENGTH_LONG).show();
            return false;
        }
       /* if(MOBILE_PATTERN.matcher(et_mobile.getText().toString()).matches()==false)
        {
            Toast.makeText(mContext, "Please enter a valid Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        }
*/
        if(tv_country_code.getText().toString().trim().matches("0") ||tv_country_code.getText().toString().trim().matches("") || tv_country_code.getText().toString().trim()== null )
        {
            Toast.makeText(mContext, "Please select Country Code ", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

}

