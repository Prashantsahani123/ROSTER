package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.EditMemberAdapter;
import com.SampleApp.row.Data.ContactData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.Newmember;
import com.SampleApp.row.Data.PostNewContact;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.DirectoryDataModel;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 29-12-2015.
 */
public class EditMember extends Activity  {

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title,ib_add;
    ImageView iv_backbutton;
    EditText et_serach_ebulletin;
    DirectoryDataModel directoryDataModel;

    private static final int STATIC_INTEGER_VALUE = 100;

    String Name,Number,CountryCode;
    private long masterUid, grpId;

    private EditMemberAdapter adapter_contactData;
    public static List<ContactData> list_contactData = new ArrayList<ContactData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_members);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));


            listview = (ListView) findViewById(R.id.listView);
            tv_title = (TextView) findViewById(R.id.tv_title);
            ib_add = (TextView) findViewById(R.id.ib_add);
            iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
            tv_title.setText("Edit Contacts");


            final TextView tv_name = (TextView) findViewById(R.id.tv_name);

            et_serach_ebulletin = (EditText) findViewById(R.id.et_serach_ebulletin);

             et_serach_ebulletin.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
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

                    EditMemberAdapter adapter = new EditMemberAdapter(EditMember.this, EditMember.this, R.layout.edit_member_list_item, tempArrayList);
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

            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();

            list_contactData = (List<ContactData>) bundle.getSerializable("value");
            adapter_contactData = new EditMemberAdapter(EditMember.this, EditMember.this, R.layout.edit_member_list_item, (ArrayList<ContactData>) list_contactData);
            listview.setAdapter(adapter_contactData);
            listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        /*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(EditMember.this, EditContact.class);
                startActivity(i);
            }
        });*/

            iv_backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*  Intent intent = new Intent(EditMember.this, Contact_Import.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("value", (Serializable) list_contactData);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);*/
                    Utils.popupback(EditMember.this);
                    //finish();
                }
            });

            ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   DirectoryData insertData = null;
                    insertData = new DirectoryData();
                    Long grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                    Long masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));
                    directoryDataModel = new DirectoryDataModel(getApplicationContext());

                    for (int i = 0; i < list_contactData.size(); i++) {
                        insertData.setMasterUID("");
                        insertData.setGrpID("");
                        insertData.setProfileID("");
                        insertData.setGroupName("");
                        CountryCode = list_contactData.get(i).getCountryCode();
                        insertData.setMemberName("" + list_contactData.get(i).getContactName());
                        Name = list_contactData.get(i).getContactName();
                        insertData.setPic("");
                        insertData.setMembermobile("" + list_contactData.get(i).getContactNumber());
                        Number = list_contactData.get(i).getContactNumber();

                        insertData.setGrpCount("");
                        directoryDataModel.insertPhoneContact(masterUid, list_contactData.get(i).getContactName(), CountryCode + " " + list_contactData.get(i).getContactNumber(), grpId, insertData);
                    }*/
                    addMultiPleMember();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter_contactData.onActivityResult(requestCode, resultCode, data);
    }



    public void myStartActivityForResult(Intent intent, int requestCode) {
        // do pre-processing here if you need to
        startActivityForResult(intent, STATIC_INTEGER_VALUE);


    }


    //-------------------  Add Multiple members to the list -----------------------------



    private void addMultiPleMember() {

        DirectoryData insertData = null;
        insertData = new DirectoryData();
        boolean isValid = true;
        Long grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
        Long masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));
        directoryDataModel = new DirectoryDataModel(getApplicationContext());

        for (int i = 0; i < list_contactData.size(); i++) {
            if (list_contactData.get(i).getCountryCode().equalsIgnoreCase("0")) {

                isValid = false;
            }


        }

        if (isValid) {
            for (int i = 0; i < list_contactData.size(); i++) {
                insertData.setMasterUID("");
                insertData.setGrpID("");
                insertData.setProfileID("");
                insertData.setGroupName("");
                CountryCode = list_contactData.get(i).getCountryCode();
                insertData.setMemberName("" + list_contactData.get(i).getContactName());
                Name = list_contactData.get(i).getContactName();
                insertData.setPic("");
                insertData.setMembermobile("" + list_contactData.get(i).getContactNumber());
                Number = list_contactData.get(i).getContactNumber();

                insertData.setGrpCount("");
                //  directoryDataModel.insertPhoneContact(masterUid, list_contactData.get(i).getContactName(), CountryCode + " " + list_contactData.get(i).getContactNumber(), grpId, insertData);


                String body = "";
                Newmember newmembers = new Newmember();
                newmembers.setCountryId(CountryCode);
                newmembers.setMobile(Number);
                newmembers.setGroupId(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                newmembers.setMasterID(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));
                newmembers.setUserName(Name);
                newmembers.setMemberEmail("");

                PostNewContact postNewContact = new PostNewContact();
                List<Newmember> addMember = new ArrayList<Newmember>();

               /* for (int j = 0;j<directoryDataComparison.size();j++) {
                    if (directoryDataComparison.get(j).getMembermobile().equals(addMember.get(j).getMobile()))
                    {
                        Toast.makeText(getApplicationContext(),addMember.get(j).getUserName()+" is Already Exist", Toast.LENGTH_LONG).show();

                    }
//                }*/

                addMember.add(newmembers);
                postNewContact.setNewmembers(addMember);

                Gson gson = new Gson();
                body = gson.toJson(postNewContact);
                new PostMessageThread(body).execute();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Country Code", Toast.LENGTH_LONG).show();
        }

    }

    class PostMessageThread extends AsyncTask<Void, Void, String>
    {

        String body;
        ProgressDialog progressDialog;

        public PostMessageThread(String body)
        {
         this.body=body;
        }
        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(EditMember.this,R.style.TBProgressBar);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            //String tokenID=pref.getString(TAG_TOKEN, "");
            String response="";
            HttpURLConnection connection = null;

            String url=Constant.AddMultipleMemberToGroup;
            URL mUrl = null;
            try {
                mUrl = new URL(url);
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                byte[] data = body.getBytes("UTF-8");
                wr.write(data);
                wr.flush();
                wr.close();
                int responseCode = connection.getResponseCode();
                Log.e("code>", "" + responseCode);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result)
        {

            Utils.log(""+result);

            if(progressDialog!=null)
            {
                if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
            }

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

            super.onPostExecute(result);

          /*  if(result!="") {

            }*/

        }

    }


}



















