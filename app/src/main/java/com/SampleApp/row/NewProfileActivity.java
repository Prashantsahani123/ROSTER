package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.PopupCallRVAdapter;
import com.SampleApp.row.Adapter.PopupEmailRVAdapter;
import com.SampleApp.row.Adapter.PopupMsgRVAdapter;
import com.SampleApp.row.Adapter.ProfileRVAdapter;
import com.SampleApp.row.Data.profiledata.AddressData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.PhotoData;
import com.SampleApp.row.Data.profiledata.PopupEmailData;
import com.SampleApp.row.Data.profiledata.PopupPhoneNumberData;
import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.croputility.Crop;
import com.SampleApp.row.sql.ProfileModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by USER on 18-12-2015.
 */
public class NewProfileActivity extends Activity {
    private String fromMainDirectory;
    private boolean updated = false;
    private final static int EDIT_PROFILE_REQUEST = 10, CAPTURE_CAMERA_REQUEST = 11;
    private MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String hasimageflag = "0";
    private String picturePath;
    private String responsefromimageupload = "0";
    private Bitmap updatedProfileImage;
    private boolean updateProfileImage = false;
    private static Uri mCapturedImageURI;
    private Context context;
    private ProfileRVAdapter adapter;
    private ArrayList<Object> list;

    private ArrayList<PopupPhoneNumberData> myCallList = new ArrayList<>();
    private ArrayList<PopupPhoneNumberData> myMsgList = new ArrayList<>();
    private ArrayList<PopupEmailData> myMailList = new ArrayList<>();

    // Action Bar component
    private TextView tvTitle;
    private ImageView ivEdit, ivDelete;

    private String groupId, memberProfileId;
    private RecyclerView rvProfile;
    private TextView tvMemberName;

    private ProfileMasterData profileData;
    private ImageView ivNewProfileImage, ivNewCallButton, ivNewMessageButton, ivNewEmailButton, iv_imageedit;

    private ProgressBar pbPic;


    private String isAdmin = "", sessionProfileId = "";

    // family pic code
    private String hasfamilypicflag = "0";
    public String flag = "profilePic";
    private TextView tvRotaryDesignation,tvClubDesignation;
    ProgressDialog dialog;
    String profileId,pic="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile_activity);
        context = this;
        isAdmin = PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN, "");
        sessionProfileId = PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID, "");
        try {
            fromMainDirectory = getIntent().getStringExtra("fromMainDirectory");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        Bundle b=getIntent().getExtras();
        if(b!=null){
            if(getIntent().hasExtra("fromDTDirectory")){
                if(getIntent().getStringExtra("fromDTDirectory").equalsIgnoreCase("yes")){
                   // pic=getIntent().getStringExtra("pic");
                    getProfileDetails();
                }
                else {
                    loadProfile();

                }
            }else if(getIntent().hasExtra("fromDistrictCommitteeList")){
                if(getIntent().getStringExtra("fromDistrictCommitteeList").equalsIgnoreCase("yes")){
                   // pic=getIntent().getStringExtra("pic");
                    getProfileDetails();
                }
                else {
                    loadProfile();

                }
            }else if(getIntent().hasExtra("fromSubGroup")){
                if(getIntent().getStringExtra("fromSubGroup").equalsIgnoreCase("yes")){
                    // pic=getIntent().getStringExtra("pic");
                    getProfileDetails();
                }
                else {
                    loadProfile();

                }
            }
            else {
                loadProfile();

            }
        }

        initEvents();
        initActionBar();
        checkAdminActions();
    }

    public void initComponents() {
        rvProfile = (RecyclerView) findViewById(R.id.rvProfileDetails);
        rvProfile.setLayoutManager(new LinearLayoutManager(context));
        tvMemberName = (TextView) findViewById(R.id.tv_member_name);
        ivNewCallButton = (ImageView) findViewById(R.id.ivNewCallButton);
        ivNewEmailButton = (ImageView) findViewById(R.id.ivNewMail);
        ivNewMessageButton = (ImageView) findViewById(R.id.ivNewMessage);

        groupId = getIntent().getStringExtra("groupId");
        memberProfileId = getIntent().getStringExtra("memberProfileId");
        ivNewProfileImage = (ImageView) findViewById(R.id.ivNewProfileImage);
        pbPic = (ProgressBar) findViewById(R.id.pbPic);
        iv_imageedit = (ImageView) findViewById(R.id.iv_imageedit);
        tvRotaryDesignation = (TextView) findViewById(R.id.tvRotaryDesignation);
        tvClubDesignation = (TextView) findViewById(R.id.tvClubDesignation);
    }

    public void initActionBar() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivEdit = (ImageView) findViewById(R.id.iv_actionbtn);
        ivDelete = (ImageView) findViewById(R.id.iv_actionbtn2);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deleteProfile();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("profileId", profileData.getProfileId());
                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            }
        });
        tvTitle.setText("Profile");

        ivDelete.setVisibility(View.VISIBLE);
        ivDelete.setImageDrawable(getResources().getDrawable(R.drawable.delete));
        ivEdit.setVisibility(View.VISIBLE);
        ivEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));
    }

    private void deleteProfile() {
        if (isAdmin.equalsIgnoreCase("yes") && sessionProfileId.equalsIgnoreCase(memberProfileId)) {
            //Toast.makeText(context, "")
            Toast.makeText(context, "You are the " + PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME) + "'s Admin, you cannot delete yourself.", Toast.LENGTH_LONG).show();
            return;
        }

        Hashtable<String, String> params = new Hashtable<>();

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        params.put("typeID", memberProfileId);   // Profile Id of the member which is to be deleted.
        params.put("type", "Member");            // type : Name of module

        params.put("profileID", sessionProfileId);  // deleted by

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        try {
            JSONObject jsonParams = new JSONObject(new Gson().toJson(params));
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting member. Please wait");
            dialog.setCancelable(false);

            if (InternetConnection.checkConnection(context)) {
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Constant.DeleteByModuleName,
                        jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Toast.makeText(context, "Failed to delete profile", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                );
                AppController.getInstance().addToRequestQueue(context, request);
                dialog.show();
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void checkAdminActions() {
        try {
            if (fromMainDirectory.equalsIgnoreCase("no")) {
                ivEdit.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                iv_imageedit.setVisibility(View.GONE);
                adapter.setEditable(false);
                adapter.notifyDataSetChanged();
                return;
            }

            if (isAdmin.equalsIgnoreCase("No")) {
                if (sessionProfileId.equals(profileData.getProfileId())) {
                    ivEdit.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.GONE);
                    iv_imageedit.setVisibility(View.VISIBLE);
                    adapter.setEditable(true);
                    adapter.notifyDataSetChanged();
                } else {
                    ivEdit.setVisibility(View.GONE);
                    ivDelete.setVisibility(View.GONE);
                    iv_imageedit.setVisibility(View.GONE);
                    adapter.setEditable(false);
                    /*if ( list.get(list.size()-1) instanceof PhotoData ) {
                        list.remove(list.size()-1);
                    }*/

                    adapter.notifyDataSetChanged();
                }
            } else {
                ivEdit.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(View.VISIBLE);
                iv_imageedit.setVisibility(View.VISIBLE);
                adapter.setEditable(true);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                loadProfile();
                checkAdminActions();
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                beginCrop(data.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            } else if (requestCode == CAPTURE_CAMERA_REQUEST && resultCode == RESULT_OK) {
                //----- Correct Image Rotation ----//
                Uri correctedUri = null;
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCapturedImageURI);
                    //imageBitmap = imageOrientationValidator(imageBitmap, getRealPathFromURI(mCapturedImageURI));
                    correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());
                    Log.d("==== Uri ===", "======" + correctedUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                beginCrop(correctedUri);
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

//    private void handleCrop(int resultCode, Intent result) {
//        if (resultCode == RESULT_OK) {
//
//            Uri croppedUri = Crop.getOutput(result);
//
//            if (croppedUri != null) {
//
//                Bitmap csBitmap = null;
//                ImageCompression imageCompression = new ImageCompression();
//                String path = "";
//                try {
//                    csBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
//                    path = Utils.getRealPathFromURI(croppedUri, getApplicationContext());
//                    Log.d("==== Path ===", "======" + path);
//
//                    imageCompression = new ImageCompression();
//                    picturePath = imageCompression.compressImage(path, getApplicationContext());
//                    Log.d("==picturePath====", "0000...." + picturePath);
//                    if (flag.equalsIgnoreCase("profilePic")) {
//                        responsefromimageupload = Utils.doFileUploadForProfilePic(NewProfileActivity.this,new File(picturePath.toString()), memberProfileId, groupId, "profile"); // Upload File to server
//                    } else {
//                        responsefromimageupload = Utils.doFileUploadForProfilePic(NewProfileActivity.this,new File(picturePath.toString()), memberProfileId, groupId, "family"); // Upload File to server
//                    }
//                    Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + responsefromimageupload);
//                    updated = true;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                updatedProfileImage = csBitmap;
//
//              //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                // csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
//                //  csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                //   iv_profileimage.setImageBitmap(csBitmap);
//
//               // picturePath = imageCompression.compressImage(path, getApplicationContext());
//                //ivNewProfileImage.setImageDrawable(Drawable.createFromPath(picturePath));
//                //Utils.log(picturePath);
//
//
//
//
//                if (responsefromimageupload.equalsIgnoreCase("0") && flag.equalsIgnoreCase("profilePic")) {
//                    Picasso.with(context)
//                            .load("file://" + picturePath)
//                            .transform(new CircleTransform())
//
//                            .placeholder(R.drawable.profile_pic)
//                            .into(ivNewProfileImage);
//
//                    hasimageflag="1";
//                    updateProfileImage = true;
//                    Utils.showToastWithTitleAndContext(context,"Profile Photo Updated Successfully");
//                } else if(responsefromimageupload.equalsIgnoreCase("0")) {
//                    ((PhotoData) list.get(list.size() - 1)).setFamilyPic("file://" + picturePath);
//                    adapter.notifyDataSetChanged();
//                    hasfamilypicflag="1";
//                    profileData.setFamilyPic("file://" + picturePath);
//                    Utils.showToastWithTitleAndContext(context,"Family Photo Updated Successfully");
//                }
//            }
//
//        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Uri croppedUri = Crop.getOutput(result);

            if (croppedUri != null) {

                Bitmap csBitmap = null;
                ImageCompression imageCompression = new ImageCompression();
                String path = "";
                try {
                    csBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                    path = Utils.getRealPathFromURI(croppedUri, getApplicationContext());
                    Log.d("==== Path ===", "======" + path);

                    imageCompression = new ImageCompression();
                    picturePath = imageCompression.compressImage(path, getApplicationContext());
//                    picturePath = imageCompression.getImage(path, getApplicationContext());

                    Log.d("==picturePath====", "0000...." + picturePath);
                    final ProgressDialog pd = ProgressDialog.show(NewProfileActivity.this, "", "Uploading...", false);

                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag.equalsIgnoreCase("profilePic")) {
                                responsefromimageupload = Utils.doFileUploadForProfilePic(NewProfileActivity.this,new File(picturePath.toString()), memberProfileId, groupId, "profile"); // Upload File to server
                            } else {
                                responsefromimageupload = Utils.doFileUploadForProfilePic(NewProfileActivity.this,new File(picturePath.toString()), memberProfileId, groupId, "family"); // Upload File to server
                            }
                            Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + responsefromimageupload);
                            updated = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID  " + responsefromimageupload);
                                    if (responsefromimageupload.equals("0")) {
                                        if (responsefromimageupload.equalsIgnoreCase("0") && flag.equalsIgnoreCase("profilePic")) {

                                            Picasso.with(context)
                                                    .load("file://" + picturePath)
                                                    .transform(new CircleTransform())

                                                    .placeholder(R.drawable.profile_pic)
                                                    .into(ivNewProfileImage);

                                            hasimageflag="1";
                                            updateProfileImage = true;
                                            Utils.showToastWithTitleAndContext(context,"Profile Photo Updated Successfully");
                                        } else if(responsefromimageupload.equalsIgnoreCase("0")) {
                                            ((PhotoData) list.get(list.size() - 1)).setFamilyPic("file://" + picturePath);
                                            adapter.notifyDataSetChanged();
                                            hasfamilypicflag="1";
                                            profileData.setFamilyPic("file://" + picturePath);
                                            Utils.showToastWithTitleAndContext(context,"Family Photo Updated Successfully");
                                        }

                                        loadProfile();
                                    }else {
                                        Toast.makeText(NewProfileActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });

                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
                //  csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //   iv_profileimage.setImageBitmap(csBitmap);

                // picturePath = imageCompression.compressImage(path, getApplicationContext());
                //ivNewProfileImage.setImageDrawable(Drawable.createFromPath(picturePath));
                //Utils.log(picturePath);





            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadProfile() {
        try {
            ProfileModel model = new ProfileModel(context);

            list = model.getProfileInfo(Long.parseLong(memberProfileId));
            Utils.log("List1 : "+list);
            if (list.size() == 0) {
                Toast.makeText(context, "First please load Directory contacts to view your profile", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            // First object in the list is Basic profile information
            Utils.log("List : "+list);
            profileData = (ProfileMasterData) list.remove(0);
            myCallList = getPhoneNumbers();
            myMsgList.addAll(myCallList);
            myMailList = getEmailIds();

            if (myMailList.size() == 0) {
                ivNewEmailButton.setEnabled(false);
                ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.p_g_mail));
            } else {
                ivNewEmailButton.setEnabled(true);
                ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_mail));
            }


            /*
            * Member name should not be displayed in the list.
            * Therefore removing member_name object followed by removing
            * separator object*/
            int n = list.size();
            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("member_name")) {
                        list.remove(i);
                        n = list.size();
                        try {
                            if (list.get(i) instanceof Separator) {
                                list.remove(i);
                            }
                        } catch (ArrayIndexOutOfBoundsException aie) {

                        }
                        break;
                    }
                }
            }
            // End of removing the member_name object

            n = list.size();
            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("member_master_designation")) {
                        tvClubDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue()+" (Club)");
                        tvClubDesignation.setVisibility(View.VISIBLE);
                        break;
                    }else {
                        tvClubDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue());
                        tvClubDesignation.setVisibility(View.GONE);
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("dg_master_designation")) {
                        tvRotaryDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue()+" (District)");
                        tvRotaryDesignation.setVisibility(View.VISIBLE);
                        break;
                    }else {
                        tvRotaryDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue());
                        tvRotaryDesignation.setVisibility(View.GONE);
                    }
                }
            }

            n = list.size();
            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("profileID")) {
                        list.remove(i);
                        n = list.size();
                        try {
                            if (list.get(i) instanceof Separator) {
                                list.remove(i);
                            }
                        } catch (ArrayIndexOutOfBoundsException aie) {

                        }
                        break;
                    }
                }
            }

            if ( ! getIntent().hasExtra("fromDistrictCommitteeList") && ! getIntent().hasExtra("fromDTDirectory")) {
                if (profileData.getFamilyPic().trim().length() == 0 || profileData.getFamilyPic().equalsIgnoreCase("null") || profileData.getFamilyPic().isEmpty()) {
                    PhotoData data = new PhotoData("");
                    data.setFamilyPic("");
                    hasfamilypicflag="0";
                    list.add(data);
                } else {
                    PhotoData data = new PhotoData(profileData.getFamilyPic());
                    data.setFamilyPic(profileData.getFamilyPic());
                    hasfamilypicflag="1";
                    list.add(data);
                }
            }
            adapter = new ProfileRVAdapter(context, list);

            rvProfile.setAdapter(adapter);
            adapter.setOnFamilyPicSelectedListener(new ProfileRVAdapter.OnFamilyPicSelectedListener() {
                @Override
                public void onFamilyPicSelected(int position) {
                    if (profileData.getFamilyPic().trim().length() == 0 || profileData.getFamilyPic().equalsIgnoreCase("null") || profileData.getFamilyPic().isEmpty()) {
                        hasfamilypicflag = "0";
                    } else {
                        hasfamilypicflag = "1";
                        Intent zoomImage = new Intent(context, ImageZoom.class);
                        zoomImage.putExtra("imgageurl",profileData.getFamilyPic());
                        startActivity(zoomImage);
                    }

//                    if (InternetConnection.checkConnection(getApplicationContext())) {
//                        if (!marshMallowPermission.checkPermissionForCamera()) {
//                            marshMallowPermission.requestPermissionForCamera();
//                        } else {
//                            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
//                                marshMallowPermission.requestPermissionForExternalStorage();
//                            } else {
//                                selectFamilyImage();
//                            }
//                        }
//                    } else {
//                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//                        // Not Available...
//                    }
                }
            });

            adapter.setOnFamilyPicEditedListener(new ProfileRVAdapter.OnFamilyPicEditedListener() {
                @Override
                public void onFamilyPicEdited(int position) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        if (!marshMallowPermission.checkPermissionForCamera()) {
                            marshMallowPermission.requestPermissionForCamera();
                        } else {
                            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                marshMallowPermission.requestPermissionForExternalStorage();
                            } else {
                                selectFamilyImage();
                            }
                        }
                    }
                    else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }
            });

            tvMemberName.setText(profileData.getMemberName());
            if (!profileData.getProfilePic().trim().equals("")) {
                Picasso.with(context)
                        .load(profileData.getProfilePic())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.profile_pic)
                        .into(ivNewProfileImage);
                ivNewProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent zoomImage = new Intent(context, ImageZoom.class);
                        zoomImage.putExtra("imgageurl",profileData.getProfilePic());
                        startActivity(zoomImage);
                    }
                });
                hasimageflag = "1";
                pbPic.setVisibility(View.GONE);
            } else {
                pbPic.setVisibility(View.GONE);
                hasimageflag = "0";
            }

            checkAdminActions();

            //model.printTable();

        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void getProfileDetails(){

        dialog=new ProgressDialog(context,R.style.TBProgressBar);
        dialog.setCancelable(false);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.show();

        Hashtable<String, String> paramTable = new Hashtable<>();
        paramTable.put("memberProfileId", memberProfileId);
        paramTable.put("groupId", "" + groupId);

        JSONObject jsonRequestData = null;
        try {
            jsonRequestData = new JSONObject(new Gson().toJson(paramTable));
            Utils.log("Url : " + Constant.GetMemberWithDynamicFields+ " Data : " + jsonRequestData);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.GetMemberWithDynamicFields,
                    jsonRequestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log("Success : " + response);
                            //handleSyncInfo(response);
                            if(dialog!=null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            setProfile(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(dialog!=null && dialog.isShowing()){
                                dialog.dismiss();
                            }

                            Utils.log("Error is : " + error);
                            error.printStackTrace();
                        }
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    1200000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> parseJson(JSONObject response){
        ArrayList<Object> list = new ArrayList<>();

        try {
            JSONObject memberListDetailResult = response.getJSONObject("MemberListDetailResult");
            String status= memberListDetailResult.getString("status");
            if(status.equals("0")){
                JSONObject memberDetails = memberListDetailResult.getJSONObject("MemberDetails");
                ProfileMasterData data=new ProfileMasterData();
                data.setMasterId(memberDetails.getString("masterID"));
                data.setGrpId(memberDetails.getString("grpID"));
                String profileId=memberDetails.getString("profileID");
                data.setProfileId(profileId);
                data.setIsAdmin(memberDetails.getString("isAdmin"));
                data.setMemberName(memberDetails.getString("memberName"));
                data.setMemberEmail(memberDetails.getString("memberEmail"));
                data.setMemberMobile(memberDetails.getString("memberMobile"));
                data.setMemberCountry(memberDetails.getString("memberCountry"));
                data.setProfilePic(memberDetails.getString("profilePic"));
                data.setFamilyPic(memberDetails.getString("familyPic"));
                data.setIsPersonalDetVisible(memberDetails.getString("isPersonalDetVisible"));
                data.setIsBussinessDetVisible(memberDetails.getString("isBusinDetVisible"));
                data.setIsFamilyDetVisible(memberDetails.getString("isFamilDetailVisible"));

                list.add(data);

                // Get all mobile number from personal and business
                if ( memberDetails.getString("isPersonalDetVisible").equalsIgnoreCase("yes")) {
                    JSONArray personalMemberDetails=memberDetails.getJSONArray("personalMemberDetails");
                    for(int i=0;i<personalMemberDetails.length();i++){
                        JSONObject jsonObject=personalMemberDetails.getJSONObject(i);
                        String fieldID = jsonObject.getString("fieldID");
                        String uniquekey = jsonObject.getString("uniquekey");
                        String key = jsonObject.getString("key");
                        String value = jsonObject.getString("value");
                        String colType = jsonObject.getString("colType");
                        String isEditable = jsonObject.getString("isEditable");
                        String isVisible = jsonObject.getString("isVisible");

                        if ( ! value.trim().equals("")) {
                            list.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                        }
                    }
                }

                if ( memberDetails.getString("isBusinDetVisible").equalsIgnoreCase("yes")) {
                    JSONArray businessMemberDetails=memberDetails.getJSONArray("businessMemberDetails");
                    for(int i=0;i<businessMemberDetails.length();i++){
                        JSONObject jsonObject=businessMemberDetails.getJSONObject(i);
                        String fieldID = jsonObject.getString("fieldID");
                        String uniquekey = jsonObject.getString("uniquekey");
                        String key = jsonObject.getString("key");
                        String value = jsonObject.getString("value");
                        String colType = jsonObject.getString("colType");
                        String isEditable = jsonObject.getString("isEditable");
                        String isVisible = jsonObject.getString("isVisible");

                        if ( ! value.trim().equals("")) {
                            list.add(new BusinessMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                        }
                    }
                }

                if ( list.size() > 1) {
                    list.add(new Separator());
                }

//                // Get all email ids from personal and business information
//                if ( isPersonalDetVisible.equals("yes")) {
//                    list.addAll(getEmailIds(profileId));
//                }
//
//                if ( isBussinessDetVisible.equals("yes")) {
//                    list.addAll(getBusinessEmails(profileId));
//                }

                if ( ! (list.get(list.size()-1) instanceof Separator) )
                    list.add(new Separator());
                //-------------------End of loading email ids--------------------//
                try {
                    if (!(list.get(list.size() - 1) instanceof Separator))
                        list.add(new Separator());
                } catch(ArrayIndexOutOfBoundsException ae) {}
//
//                if (isBussinessDetVisible.equals("yes")) {
//                    list.addAll(getOtherBusinessInfo(profileId));
//                }
                //-------------------Loading business name and designation------------//

                JSONObject addressDetails=memberDetails.getJSONObject("addressDetails");

                String isResidanceAddrVisible=addressDetails.getString("isResidanceAddrVisible");
                String isBusinessAddrVisible=addressDetails.getString("isBusinessAddrVisible");

                ArrayList<AddressData> addrList=new ArrayList<>();

                if(isBusinessAddrVisible.equalsIgnoreCase("y")){

                    JSONArray addressResult=addressDetails.getJSONArray("addressResult");
                    for(int i=0;i<addressResult.length();i++){
                        JSONObject object=addressResult.getJSONObject(i);

                        AddressData addressData=new AddressData();
                        addressData.setAddressID(object.getString("addressID"));
                        addressData.setAddressType(object.getString("addressType"));
                        addressData.setAddress(object.getString("address"));
                        addressData.setCity(object.getString("city"));
                        addressData.setState(object.getString("state"));
                        addressData.setCountry(object.getString("country"));
                        addressData.setPincode(object.getString("pincode"));
                        addressData.setPhoneNo(object.getString("phoneNo"));
                        addressData.setFax(object.getString("fax"));
                        addressData.setProfileID(object.getString("profileID"));

                        addrList.add(addressData);
                    }
                }



                for(int i=0;i<addrList.size();i++) {
                    if ( addrList.get(i).getAddressType().equals("Business")) {

                        AddressData addressData=addrList.get(i);
                        if(!addressData.getAddress().trim().isEmpty() || !addressData.getCity().trim().isEmpty() || !addressData.getState().trim().isEmpty() || !addressData.getCountry().trim().isEmpty() || !addressData.getPincode().trim().isEmpty() || !addressData.getFax().trim().isEmpty()){
                            list.add(addrList.get(i));
                            list.add(new Separator());
                        }
//                        list.add(addrList.get(i));
//                        list.add(new Separator());
                        if ( addrList.get(i).getPhoneNo().trim().equals("")) {

                        } else {
                            String countryCode = new ProfileModel(context).searchForCountryCode(addrList.get(i).getCountry());
                            BusinessMemberDetails businessContact = new BusinessMemberDetails("" + profileId, "0", "phone_no", "Business Contact No.", countryCode+addrList.get(i).getPhoneNo(), "Contact", "1", "y");
                            list.add(businessContact);
                        }
                        break;
                    }
                }
                try {
                    if (!(list.get(list.size() - 1) instanceof Separator))
                        list.add(new Separator());
                } catch(ArrayIndexOutOfBoundsException ae) {}

                // Get all addresses from personal and business information
                // list.addAll(addrList);
                try {
                    if (!(list.get(list.size() - 1) instanceof Separator))
                        list.add(new Separator());
                } catch(ArrayIndexOutOfBoundsException ae) {}

//                if ( isPersonalDetVisible.equals("yes")) {
//                    list.addAll(getOtherPersonalInfo(profileId));
//                }

                for(int i=0;i<addrList.size();i++) {
                    if ( addrList.get(i).getAddressType().equals("Residence")) {

                        AddressData addressData=addrList.get(i);
                        if(!addressData.getAddress().trim().isEmpty() || !addressData.getCity().trim().isEmpty() || !addressData.getState().trim().isEmpty() || !addressData.getCountry().trim().isEmpty() || !addressData.getPincode().trim().isEmpty() || !addressData.getFax().trim().isEmpty()){
                            list.add(addrList.get(i));
                            list.add(new Separator());
                        }

//                        list.add(addrList.get(i));
//                        list.add(new Separator());
                        if ( addrList.get(i).getPhoneNo().trim().equals("")) {

                        } else {
                            String countryCode = new ProfileModel(context).searchForCountryCode(addrList.get(i).getCountry());
                            PersonalMemberDetails residenceContact = new PersonalMemberDetails("" + profileId, "0", "phone_no", "Residential Contact No.", countryCode+addrList.get(i).getPhoneNo(), "Contact", "1", "y");
                            list.add(residenceContact);
                        }
                        break;
                    }
                }
                try {
                    if (!(list.get(list.size() - 1) instanceof Separator))
                        list.add(new Separator());
                } catch(ArrayIndexOutOfBoundsException ae) {}


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setProfile(JSONObject response) {
        try {

            list = parseJson(response);
            if (list.size() == 0) {
                Toast.makeText(context, "Something went wrong, Try again.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            // First object in the list is Basic profile information
            Utils.log("List : "+list);
            profileData = (ProfileMasterData) list.remove(0);
            myCallList = getPhoneNumbers();
            myMsgList.addAll(myCallList);
            myMailList = getEmailIds();

            if (myMailList.size() == 0) {
                ivNewEmailButton.setEnabled(false);
                ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.p_g_mail));
            } else {
                ivNewEmailButton.setEnabled(true);
                ivNewEmailButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_mail));
            }


            /*
            * Member name should not be displayed in the list.
            * Therefore removing member_name object followed by removing
            * separator object*/
            int n = list.size();
            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("member_name")) {
                        list.remove(i);
                        n = list.size();
                        try {
                            if (list.get(i) instanceof Separator) {
                                list.remove(i);
                            }
                        } catch (ArrayIndexOutOfBoundsException aie) {

                        }
                        break;
                    }
                }
            }
            // End of removing the member_name object

            n = list.size();
            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("member_master_designation")) {
                        tvClubDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue()+" (Club)");
                        tvClubDesignation.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                if (list.get(i) instanceof PersonalMemberDetails) {
                    if (((PersonalMemberDetails) list.get(i)).getUniquekey().equals("dg_master_designation")) {
                        tvRotaryDesignation.setText(((PersonalMemberDetails) list.get(i)).getValue()+" (District)");
                        tvRotaryDesignation.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            if (profileData.getFamilyPic().trim().length() == 0 || profileData.getFamilyPic().equalsIgnoreCase("null") || profileData.getFamilyPic().isEmpty()) {

                hasfamilypicflag="0";
            } else {
                PhotoData data = new PhotoData(profileData.getFamilyPic());
                data.setFamilyPic(profileData.getFamilyPic());
                hasfamilypicflag="1";
                list.add(data);
            }

            if ( ! getIntent().hasExtra("fromDistrictCommitteeList") && ! getIntent().hasExtra("fromDTDirectory")) {

            }
            adapter = new ProfileRVAdapter(context, list);
            rvProfile.setAdapter(adapter);
            adapter.setOnFamilyPicSelectedListener(new ProfileRVAdapter.OnFamilyPicSelectedListener() {
                @Override
                public void onFamilyPicSelected(int position) {
                    if (profileData.getFamilyPic().trim().length() == 0 || profileData.getFamilyPic().equalsIgnoreCase("null") || profileData.getFamilyPic().isEmpty()) {
                        hasfamilypicflag = "0";
                    } else {
                        hasfamilypicflag = "1";
                        Intent zoomImage = new Intent(context, ImageZoom.class);
                        zoomImage.putExtra("imgageurl",profileData.getFamilyPic());
                        startActivity(zoomImage);
                    }
//
//                    if (InternetConnection.checkConnection(getApplicationContext())) {
//                        if (!marshMallowPermission.checkPermissionForCamera()) {
//                            marshMallowPermission.requestPermissionForCamera();
//                        } else {
//                            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
//                                marshMallowPermission.requestPermissionForExternalStorage();
//                            } else {
//                                selectFamilyImage();
//                            }
//                        }
//                    } else {
//                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//                        // Not Available...
//                    }
                }
            });

            adapter.setOnFamilyPicEditedListener(new ProfileRVAdapter.OnFamilyPicEditedListener() {
                @Override
                public void onFamilyPicEdited(int position) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        if (!marshMallowPermission.checkPermissionForCamera()) {
                            marshMallowPermission.requestPermissionForCamera();
                        } else {
                            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                marshMallowPermission.requestPermissionForExternalStorage();
                            } else {
                                selectFamilyImage();
                            }
                        }
                    }
                    else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }
            });
            tvMemberName.setText(profileData.getMemberName());
            if (!profileData.getProfilePic().trim().equals("")) {
                Picasso.with(context)
                        .load(profileData.getProfilePic())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.profile_pic)
                        .into(ivNewProfileImage);
                ivNewProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent zoomImage = new Intent(context, ImageZoom.class);
                        zoomImage.putExtra("imgageurl",profileData.getProfilePic());
                        startActivity(zoomImage);
                    }
                });
                hasimageflag = "1";
                pbPic.setVisibility(View.GONE);
            }
//            else if(!pic.trim().isEmpty()){
//                Picasso.with(context)
//                        .load(pic)
//                        .transform(new CircleTransform())
//                        .placeholder(R.drawable.profile_pic)
//                        .into(ivNewProfileImage);
//                ivNewProfileImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent zoomImage = new Intent(context, ImageZoom.class);
//                        zoomImage.putExtra("imgageurl",profileData.getProfilePic());
//                        startActivity(zoomImage);
//                    }
//                });
//                hasimageflag = "1";
//                pbPic.setVisibility(View.GONE);
//            }

            else {
                pbPic.setVisibility(View.GONE);
                hasimageflag = "0";
            }

            //model.printTable();

        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void initEvents() {
        ivNewCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCallList.size() > 1) {
                    showCallPopup();
                } else {
                    if (myCallList.size() == 1) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + myCallList.get(0).getNumber()));
                        startActivity(callIntent);
                    }
                }
            }
        });

        ivNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMsgList.size() > 1) {
                    showMsgPopup();
                } else {
                    if (myMsgList.size() == 1) {
                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//                        msgIntent.setType("vnd.android-dir/mms-sms");
//                        msgIntent.putExtra("address", myMsgList.get(0).getNumber());
                        msgIntent.setData(Uri.parse("smsto: "+Uri.encode(myMsgList.get(0).getNumber())));
                        startActivity(msgIntent);
                    }
                }
            }
        });

        ivNewEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMailList.size() > 1) {
                    showEmailPopup();
                } else {
                    if (myMailList.size() == 1) {
                        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setType("plain/text");
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{myMailList.get(0).getEmailId().toString()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(emailIntent);
                    }
                }
            }
        });

        iv_imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    if (!marshMallowPermission.checkPermissionForCamera()) {
                        marshMallowPermission.requestPermissionForCamera();
                    } else {
                        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                            marshMallowPermission.requestPermissionForExternalStorage();
                        } else {
                            selectImage();
                        }
                    }
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options;
        Drawable.ConstantState constantState;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            constantState = context.getResources()
                    .getDrawable(R.drawable.profile_pic, context.getTheme())
                    .getConstantState();
        } else {
            constantState = context.getResources().getDrawable(R.drawable.profile_pic)
                    .getConstantState();
        }

        if (ivNewProfileImage.getDrawable().getConstantState() == constantState) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        }else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
        }
//
//        final CharSequence[] options;
//        if (hasimageflag.equals("0")) {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
//        } else {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
//
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    flag = "profilePic";
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, CAPTURE_CAMERA_REQUEST);


                } else if (options[item].equals("Choose from Gallery")) {
                    flag = "profilePic";
                    Crop.pickImage(NewProfileActivity.this);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    remove_photo_webservices();
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectFamilyImage() {
        final CharSequence[] options_family;
        if (hasfamilypicflag.equals("0")) {
            options_family = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        } else {
            options_family = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options_family, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options_family[item].equals("Take Photo")) {
                    flag = "family";
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, CAPTURE_CAMERA_REQUEST);


                } else if (options_family[item].equals("Choose from Gallery")) {
                    flag = "family";
                    Crop.pickImage(NewProfileActivity.this);

                } else if (options_family[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options_family[item].equals("Remove Photo")) {
                    remove_familyphoto_webservices();
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", memberProfileId));
        arrayList.add(new BasicNameValuePair("type", "Member"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));


        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new RemovePhotoService(Constant.DeleteImage, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void remove_familyphoto_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", memberProfileId));
        arrayList.add(new BasicNameValuePair("type", "family"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));


        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new RemoveFamilyPhotoService(Constant.DeleteImage, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class RemoveFamilyPhotoService extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(NewProfileActivity.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public RemoveFamilyPhotoService(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (this.progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresultOfRemovefamilyphoto(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getresultOfRemovefamilyphoto(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {

                ((PhotoData) list.get(list.size() - 1)).setFamilyPic("");
                adapter.notifyDataSetChanged();
                profileData.setFamilyPic("");
                hasfamilypicflag = "0";
                updated = true;
                new ProfileModel(context).updateProfilePic(memberProfileId, "", "family",PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                loadProfile();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public class RemovePhotoService extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(NewProfileActivity.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public RemovePhotoService(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }


        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (this.progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresultOfRemovephoto(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getresultOfRemovephoto(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                ivNewProfileImage.setImageResource(R.drawable.profile_pic);
                hasimageflag = "0";
                updated = true;
                new ProfileModel(context).updateProfilePic(memberProfileId, "", "profile",PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                loadProfile();
                Utils.showToastWithTitleAndContext(context,"Photo Removed Successfully");
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
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
