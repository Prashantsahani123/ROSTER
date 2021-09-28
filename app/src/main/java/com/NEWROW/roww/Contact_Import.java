package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.ContactAdapter;
import com.NEWROW.row.Data.ContactData;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by USER on 30-12-2015.
 */

public class Contact_Import extends Activity  {

    ListView listview;
    ArrayAdapter<String> adapter;
    EditText et_serach_directory;
    TextView tv_title, ib_add;
    ImageView iv_backbutton;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ArrayList<ContactData> n = new ArrayList<ContactData>();
    private static final int CONTACT_IMPORT = 101;

    ArrayList<String> selectedItems = new ArrayList<String>();
    private ContactAdapter adapter_contactData;
    private ArrayList<ContactData> list_contactData = new ArrayList<ContactData>();
    ContactData contactData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_selection);
        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Add Contacts");
        ib_add = (TextView) findViewById(R.id.ib_add);

        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);

        // adapter = new ArrayAdapter<String>(this,R.layout.create_group_selection_list_item,R.id.tv_name,ebulletine );
        // listview.setAdapter(adapter);
//        if (!marshMallowPermission.checkPermissionForContacts()) {
//            // Apply this permission on click of the add contact button at previous activity
//            marshMallowPermission.requestPermissionForContacts();
//        } else {
//            LoadContactsAyscn lca = new LoadContactsAyscn();
//            lca.execute();
//        }

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        LoadContactsAyscn lca = new LoadContactsAyscn();
                        lca.execute();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(Contact_Import.this,"Oops! Permission denied",Toast.LENGTH_SHORT).show();
                        finish(); //added by satish suhas told to do this for fixed issue #3 of 'ROW Issues 16/1' sheet
                    }
                })

                .setPermissions(android.Manifest.permission.READ_CONTACTS)
                .check();

        init();

    }


    private void init() {
        et_serach_directory.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // Contact_Import.this.adapter.getFilter().filter(cs);

                int textlength = cs.length();
                ArrayList<ContactData> tempArrayList = new ArrayList<ContactData>();
                for (ContactData c : list_contactData) {
                    if (textlength <= c.getContactName().length()) {
                        if (c.getContactName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                ContactAdapter adapter = new ContactAdapter(Contact_Import.this, R.layout.create_group_selection_list_item, tempArrayList);
                listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter);


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  Intent i = new Intent(Contact_Import.this, Flow.class);
                startActivity(i);*/
            }
        });

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n.clear();
                int count = 0;
                if(adapter_contactData!=null){
                    for (ContactData p : adapter_contactData.getBox()) {
                        if (p.box) {
                            count = count + 1;
                        }


                        int pos = adapter_contactData.getPosition(p);
                        n.add(new ContactData(p.getContactName(), p.getContactNumber(), p.getCountryCode(), p.getIdNumber(), p.box, "" + pos));


                    }
                }


              /* Intent intent = new Intent(getApplicationContext(), Directory.class);
                //Intent intent = new Intent(getApplicationContext(),EditMember.class);
                //Intent intent = new Intent(Contact_Import.this,Directory.class);
                intent.putExtra("value", n);
               // startActivityForResult(intent, CONTACT_IMPORT);
                setResult(Activity.RESULT_OK, intent);
                finish();*/


                if (count <= 0) {
                    Toast.makeText(Contact_Import.this, "Please Select at least 1 Member ", Toast.LENGTH_LONG).show();
                } else {


                    Intent intent = new Intent(getApplicationContext(), EditMember.class);
                    //Intent intent = new Intent(Contact_Import.this,Directory.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("value", n);
                    startActivityForResult(intent, CONTACT_IMPORT);
                    // setResult(Activity.RESULT_OK, intent);
                    //finish();
                }
            }
        });
    }

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(Contact_Import.this, "Loading Contacts", "Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<String> contacts = new ArrayList<String>();
           /* try
            {
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                if (cursor.getCount() > 0)
                {
                    while (cursor.moveToNext())
                    {
                        String phone = "";
                        String strContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String strContactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor pCur = contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[] { strContactId }, null);

                            while (pCur.moveToNext())
                            {
                                phone = pCur .getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            pCur.close();


                            ContactData gd = new ContactData();
                            gd.setContactName(strContactName);
                            gd.setContactNumber(""+phone);
                            gd.setCountryCode(String.valueOf(91));
                            gd.setId(strContactId);

                            // adding data to arrayList
                            // contacts.add(countryCode + ":" + nationalNumber);

                            list_contactData.add(gd);

                                                   }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

*/


            //----


            Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {
                String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String id = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                Log.d("TOUCHBASE", " NAME :- " + contactName + " NUMBER :- " + phNumber);
                // contacts.add(contactName + ":" + phNumber);
                if (contactName == null || phNumber == null) {

                } else {
                    if (contactName.equals("") || phNumber.equals("")) {
                    } else {
                        int countryCode = 0;
                        String nationalNumber = "";
                        // phone must begin with '+'
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        Phonenumber.PhoneNumber numberProto = null;
                        try {
                            numberProto = phoneUtil.parse(phNumber, "");
                            countryCode = numberProto.getCountryCode();
                            nationalNumber = "" + numberProto.getNationalNumber();


                        } catch (NumberParseException e) {
                            e.printStackTrace();
                            countryCode = 0;
                            nationalNumber = phNumber;
                        }


                        Log.i("code", "national number " + nationalNumber);

                        ContactData gd = new ContactData();
                        gd.setContactName(contactName);
                        gd.setContactNumber("" + nationalNumber);
//                            if(contactName.equalsIgnoreCase("Shona"))
//                            {
//                                Log.e("Country Code",""+contactName);
//
//                                Log.e("Country Code",""+numberProto.getCountryCode());
//                            }
                        gd.setCountryCode(String.valueOf(countryCode));
                        gd.setId(id);

                        // adding data to arrayList
                        // contacts.add(countryCode + ":" + nationalNumber);

                        list_contactData.add(gd);


                        //contacts.add(contactName + ":" + phNumber);
                    }


                }
            }
            c.close();


            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);

            pd.cancel();
            Log.d("TOUcHbase", "@@@@@@ " + contacts.toString());

          /*  adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.create_group_selection_list_item, R.id.tv_name, contacts);
            listview.setAdapter(adapter);
            */

            adapter_contactData = new ContactAdapter(Contact_Import.this, R.layout.create_group_selection_list_item, list_contactData);
            listview.setAdapter(adapter_contactData);
            TextView empty = ((TextView) findViewById(R.id.tv_no_records_found));
            empty.setText("No records found");
            listview.setEmptyView(empty);
            Collections.sort(list_contactData, new MemberSort());

        }

    }



    public void readContactDetail() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CONTACT_IMPORT:
                if (requestCode == CONTACT_IMPORT && resultCode == RESULT_OK) {
                    if (data != null) {

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

         /*               Bundle bundle = data.getExtras();
                        ArrayList<ContactData> modifyContactDatas= (ArrayList<ContactData>) bundle.getSerializable("value");

                        for (int i=0;i<modifyContactDatas.size();i++)
                        {

                            //  list_contactData.add(new ContactData(p.getContactName(), p.getContactNumber() , p.getCountryCode() , p.getIdNumber() ,p.box,p.getId() ));
*//*
                            ContactData contactData = new ContactData();
                            contactData.setContactName(modifyContactDatas.get(i).getContactName());
                            contactData.setContactNumber(modifyContactDatas.get(i).getContactNumber());
                            contactData.setId(modifyContactDatas.get(i).getId());
                            int pos =Integer.parseInt(modifyContactDatas.get(i).getId());
                            list_contactData.set(pos,contactData);*//*
                        }
                        adapter_contactData.notifyDataSetChanged();*/


                    }

                }
                break;
        }
    }

    // --------------- Sorting member list in ascending order --------
    static class MemberSort implements Comparator<ContactData>
    {
        public int compare(ContactData c1, ContactData c2)
        {
            return c1.getContactName().compareTo(c2.getContactName());
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MarshMallowPermission.CONTACTS_PERMISSION_REQUEST_CODE) {
//            if (!marshMallowPermission.checkPermissionForContacts()) {
//                Toast.makeText(Contact_Import.this, "Contacts permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//
//                finish();
//                // Apply this permission on click of the add contact button at previous activity
////                marshMallowPermission.requestPermissionForContacts();
//            } else {
//                LoadContactsAyscn lca = new LoadContactsAyscn();
//                lca.execute();
//            }
//
//        }
//    }
}
