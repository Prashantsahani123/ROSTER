package com.SampleApp.row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.PopupCallRVAdapter;
import com.SampleApp.row.Adapter.PopupEmailRVAdapter;
import com.SampleApp.row.Adapter.PopupMsgRVAdapter;
import com.SampleApp.row.Adapter.ProfileRVAdapter;
import com.SampleApp.row.Data.DistrictCommitteeData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.PopupEmailData;
import com.SampleApp.row.Data.profiledata.PopupPhoneNumberData;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER on 18-12-2015.
 */
public class DistrictCommiteeProfile extends Activity {

    private boolean updated = false;

    private Context context;
    private ProfileRVAdapter adapter;
    private ArrayList<Object> list;

    private ArrayList<PopupPhoneNumberData> myCallList = new ArrayList<>();
    private ArrayList<PopupPhoneNumberData> myMsgList = new ArrayList<>();
    private ArrayList<PopupEmailData> myMailList = new ArrayList<>();
    private LinearLayout ll_mobile,ll_email,ll_clubName,ll_districtDesignation,ll_classification,ll_keyword,ll_businessName,ll_designation,ll_businessAddress,ll_rotaryId,ll_donarRecognition;

    private TextView tvTitle,tv_member_name,tvRotaryDesignation,tv_mobile,tv_email,tv_clubName,tv_districtDesig,tv_classification,tv_keyword,tv_businessName,tv_designation,tv_businessAddress,tv_rotaryId,tv_donarRegcognition;

    private RecyclerView rvProfile;

    private ImageView ivNewProfileImage, ivNewCallButton, ivNewMessageButton, ivNewEmailButton, iv_imageedit;

    private ProgressBar pbPic;
    DistrictCommitteeData districtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_commitee_profile);
        context = this;

        initComponents();
        Bundle b=getIntent().getExtras();

        districtData = (DistrictCommitteeData) b.getSerializable("districtData");

        initEvents();
        initActionBar();
        setData();
    }

    private void setData() {

        if(districtData.getPic()!=null && !districtData.getPic().equals("")){
            Picasso.with(context)
                    .load(districtData.getPic())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(ivNewProfileImage);
        }

        tv_member_name.setText(districtData.getMemberName());
        if(districtData.getDistrictDesignation()!=null && !districtData.getDistrictDesignation().equals("")){
            tvRotaryDesignation.setVisibility(View.VISIBLE);
            tvRotaryDesignation.setText(districtData.getDistrictDesignation());
        }else {
            tvRotaryDesignation.setVisibility(View.GONE);
        }

        if(districtData.getMembermobile()!=null && !districtData.getMembermobile().equals("")){
            ll_mobile.setVisibility(View.VISIBLE);
            tv_mobile.setText(districtData.getMembermobile());
        }else {
            ll_mobile.setVisibility(View.GONE);
        }

        if(districtData.getMailId()!=null && !districtData.getMailId().equals("")){
            ll_email.setVisibility(View.VISIBLE);
            tv_email.setText(districtData.getMailId());
        }else {
            ll_email.setVisibility(View.GONE);
        }


        if(districtData.getClubName()!=null && !districtData.getClubName().equals("")){
            ll_clubName.setVisibility(View.VISIBLE);
            tv_clubName.setText(districtData.getClubName());
        }else {
            ll_clubName.setVisibility(View.GONE);
        }

        if(districtData.getDistrictDesignation()!=null && !districtData.getDistrictDesignation().equals("")){
            ll_districtDesignation.setVisibility(View.VISIBLE);
            tv_districtDesig.setText(districtData.getDistrictDesignation());
        }else {
            ll_districtDesignation.setVisibility(View.GONE);
        }

        if (districtData.getMailId()==null || districtData.getMailId().equals("")) {
            ivNewEmailButton.setEnabled(false);
            ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.p_g_mail));
        } else {
            ivNewEmailButton.setEnabled(true);
            ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_mail));
        }



    }

    public void initComponents() {

        ll_mobile = (LinearLayout)findViewById(R.id.ll_mobile);
        ll_email = (LinearLayout)findViewById(R.id.ll_email);
        ll_clubName = (LinearLayout)findViewById(R.id.ll_clubName);
        ll_districtDesignation = (LinearLayout)findViewById(R.id.ll_districtDesignation);
        ll_classification = (LinearLayout)findViewById(R.id.ll_classification);
        ll_keyword = (LinearLayout)findViewById(R.id.ll_keyword);
        ll_businessName = (LinearLayout)findViewById(R.id.ll_businessName);
        ll_designation = (LinearLayout)findViewById(R.id.ll_designation);
        ll_businessAddress = (LinearLayout)findViewById(R.id.ll_businessAddress);
        ll_rotaryId = (LinearLayout)findViewById(R.id.ll_rotaryId);
        ll_donarRecognition = (LinearLayout)findViewById(R.id.ll_donarRecognition);


        tv_member_name = (TextView) findViewById(R.id.tv_member_name);
        tvRotaryDesignation = (TextView) findViewById(R.id.tvRotaryDesignation);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_clubName = (TextView) findViewById(R.id.tv_clubName);
        tv_districtDesig = (TextView) findViewById(R.id.tv_districtDesig);
        tv_classification = (TextView) findViewById(R.id.tv_classification);
        tv_keyword = (TextView) findViewById(R.id.tv_keyword);
        tv_businessName = (TextView) findViewById(R.id.tv_businessName);
        tv_designation = (TextView) findViewById(R.id.tv_designation);
        tv_businessAddress = (TextView) findViewById(R.id.tv_businessAddress);
        tv_rotaryId = (TextView) findViewById(R.id.tv_rotaryId);
        tv_donarRegcognition = (TextView) findViewById(R.id.tv_donarRegcognition);

        ivNewCallButton = (ImageView) findViewById(R.id.ivNewCallButton);
        ivNewEmailButton = (ImageView) findViewById(R.id.ivNewMail);
        ivNewMessageButton = (ImageView) findViewById(R.id.ivNewMessage);


        ivNewProfileImage = (ImageView) findViewById(R.id.ivNewProfileImage);



    }

    public void initActionBar() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("Profile");
    }


    public void initEvents() {
        ivNewCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + tv_mobile.getText().toString()));
                        startActivity(callIntent);

            }
        });

        ivNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//                        msgIntent.setType("vnd.android-dir/mms-sms");
//                        msgIntent.putExtra("address", myMsgList.get(0).getNumber());
                        msgIntent.setData(Uri.parse("smsto: "+Uri.encode(tv_mobile.getText().toString())));
                        startActivity(msgIntent);

            }
        });

        ivNewEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setType("plain/text");
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tv_email.getText().toString()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(emailIntent);
            }
        });


    }



    public void showCallPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_call, null);
        builder.setView(view);

        final AlertDialog callDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.hide();
            }
        });
        Utils.log("Phone numbers are : " + myCallList);
        PopupCallRVAdapter popupCallRVAdapter = new PopupCallRVAdapter(context, myCallList);
        popupCallRVAdapter.setOnPhoneNumberClickedListener(new PopupCallRVAdapter.OnPhoneNumberClickedListener() {
            @Override
            public void phoneNumberClicked(PopupPhoneNumberData pnd, int position) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + myCallList.get(position).getNumber()));
                callDialog.hide();
                startActivity(callIntent);
            }
        });
        RecyclerView rvPhoneNumbers = (RecyclerView) view.findViewById(R.id.rvCallList);
        rvPhoneNumbers.setLayoutManager(new LinearLayoutManager(context));
        rvPhoneNumbers.setAdapter(popupCallRVAdapter);
        callDialog.show();
    }

    public void showEmailPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.new_mail_popup, null);
        builder.setView(view);

        final RecyclerView rvMail = (RecyclerView) view.findViewById(R.id.rvMail);
        rvMail.setLayoutManager(new LinearLayoutManager(context));

        final AlertDialog mailDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<PopupEmailData> myMsgList =((PopupEmailRVAdapter)rvMail.getAdapter()).getList();
                int n = myMsgList.size();
                ArrayList<String> selectedList = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    PopupEmailData pnd = myMsgList.get(i);
                   pnd.setSelected(false);
                }

                mailDialog.hide();
            }
        });

        PopupEmailRVAdapter popupMailRVAdapter = new PopupEmailRVAdapter(context, myMailList);
        popupMailRVAdapter.setOnEmailIdClickedListener(new PopupEmailRVAdapter.OnEmailIdClickedListener() {
            @Override
            public void onEmailIdClickListener(PopupEmailData pnd, int position) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pnd.getEmailId().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }
        });


        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                ArrayList<PopupEmailData> myMsgList =((PopupEmailRVAdapter)rvMail.getAdapter()).getList();
                int n = myMsgList.size();
                ArrayList<String> selectedList = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    PopupEmailData pnd = myMsgList.get(i);
                    if (pnd.isSelected()) {
                        selectedList.add(pnd.getEmailId());
                        count++;
                    }
                }

                if (count == 0) {
                    Toast.makeText(context, "Please select at least one email id to send mail", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    String address = Utils.implode(", ", selectedList);

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("plain/text");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        context.startActivity(emailIntent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
                    }

                    int m = myMsgList.size();
                    for (int i = 0; i < m; i++) {
                        myMsgList.get(i).setSelected(false);
                    }
                }
                mailDialog.hide();

            }
        });


        rvMail.setAdapter(popupMailRVAdapter);
        mailDialog.show();
    }

    public void showMsgPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.new_popup_msg, null);
        builder.setView(view);

        final AlertDialog msgDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = myMsgList.size();
                for (int i = 0; i < n; i++) {
                    PopupPhoneNumberData pnd = myMsgList.get(i);

                   pnd.setSelected(false);
                }

                msgDialog.hide();
            }
        });

        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        Utils.log("Phone numbers are : " + myCallList);
        PopupMsgRVAdapter popupMsgRVAdapter = new PopupMsgRVAdapter(context, myCallList);

        RecyclerView rvPhoneNumbers = (RecyclerView) view.findViewById(R.id.rvCallList);
        rvPhoneNumbers.setLayoutManager(new LinearLayoutManager(context));
        popupMsgRVAdapter.setOnPhoneNumberClickedListener(new PopupMsgRVAdapter.OnPhoneNumberClickedListener() {
            @Override
            public void phoneNumberClicked(PopupPhoneNumberData pnd, int position) {
                Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                //msgIntent.setType("vnd.android-dir/mms-sms");
                //msgIntent.putExtra("address", pnd.getNumber());
                msgIntent.setData(Uri.parse("smsto: "+Uri.encode(pnd.getNumber())));
                startActivity(msgIntent);
                msgDialog.hide();
            }
        });

        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                int n = myMsgList.size();
                for (int i = 0; i < n; i++) {
                    PopupPhoneNumberData pnd = myMsgList.get(i);

                    if (pnd.isSelected()) {
                        count++;
                    }
                }

                if (count == 0) {
                    Toast.makeText(context, "Please select at least one mobile number to send message", Toast.LENGTH_LONG).show();
                    return;
                }
                msgDialog.hide();
                sendMessage();
            }
        });

        rvPhoneNumbers.setAdapter(popupMsgRVAdapter);
        msgDialog.show();
    }

    public void sendMessage() {

        ArrayList<String> selectedList = new ArrayList<>();

        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
        //msgIntent.setType("vnd.android-dir/mms-sms");
        //msgIntent.setType("vnd.android-dir/mms-sms");
        int n = myMsgList.size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            PopupPhoneNumberData pnd = myMsgList.get(i);
            if (pnd.isSelected()) {
                selectedList.add(pnd.getNumber());
                count++;
            }
        }

        if (count == 0) {
            Toast.makeText(context, "Please select at least one mobile number to send message", Toast.LENGTH_LONG).show();
            return;
        }

        String address = Utils.implode(", ", selectedList);
        //msgIntent.putExtra("address", address);
        msgIntent.setData(Uri.parse("smsto: "+Uri.encode(address)));
        try {
            startActivity(msgIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        }
        clearMsgSelection();
    }

    public void clearMsgSelection() {
        int n = myMsgList.size();
        for (int i = 0; i < n; i++) {
            myMsgList.get(i).setSelected(false);
        }
    }

    public ArrayList<PopupPhoneNumberData> getPhoneNumbers() {
        ArrayList<PopupPhoneNumberData> callList = new ArrayList<>();

        int n = list.size();
        for (int i = 0; i < n; i++) {
            Object obj = list.get(i);
            if (obj instanceof PersonalMemberDetails) {
                PersonalMemberDetails pmd = (PersonalMemberDetails) obj;
                if (pmd.getColType().equals(ProfileRVAdapter.COLUMN_TYPE_PHONE)) {
                    callList.add(new PopupPhoneNumberData(pmd.getValue(), pmd.getKey()));
                }
            } else if (obj instanceof BusinessMemberDetails) {
                BusinessMemberDetails bmd = (BusinessMemberDetails) obj;
                if (bmd.getColType().equals(ProfileRVAdapter.COLUMN_TYPE_PHONE)) {
                    callList.add(new PopupPhoneNumberData(bmd.getValue(), bmd.getKey()));
                }
            } else if (obj instanceof FamilyMemberData) {
                FamilyMemberData fmd = (FamilyMemberData) obj;
                if (!fmd.getContactNo().trim().equals("")) {
                    callList.add(new PopupPhoneNumberData(fmd.getContactNo(), fmd.getRelationship(), fmd.getMemberName()));
                }
            }
        }
        return callList;
    }

    public ArrayList<PopupEmailData> getEmailIds() {
        ArrayList<PopupEmailData> callList = new ArrayList<>();

        int n = list.size();
        for (int i = 0; i < n; i++) {
            Object obj = list.get(i);
            if (obj instanceof PersonalMemberDetails) {
                PersonalMemberDetails pmd = (PersonalMemberDetails) obj;
                if (pmd.getColType().equals(ProfileRVAdapter.COLUMN_TYPE_EMAIL)) {
                    callList.add(new PopupEmailData(pmd.getValue(), pmd.getKey()));
                }
            } else if (obj instanceof BusinessMemberDetails) {
                BusinessMemberDetails bmd = (BusinessMemberDetails) obj;
                if (bmd.getColType().equals(ProfileRVAdapter.COLUMN_TYPE_EMAIL)) {
                    callList.add(new PopupEmailData(bmd.getValue(), bmd.getKey()));
                }
            } else if (obj instanceof FamilyMemberData) {
                FamilyMemberData fmd = (FamilyMemberData) obj;
                if (!fmd.getEmailID().equals("")) {
                    callList.add(new PopupEmailData(fmd.getEmailID(), fmd.getRelationship(), fmd.getMemberName()));
                }
            }
        }
        return callList;
    }

    @Override
    public void onBackPressed() {
        if (updated) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }


}
