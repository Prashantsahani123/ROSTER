package com.SampleApp.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.SpinnerAdapter;
import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Data.AlbumPhotoData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Data.UploadPhotoData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.services.UploadPhotoService;
import com.SampleApp.row.sql.UploadedPhotoModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 18-11-2016.
 */
public class DTEditAlbumActivity extends Activity {
    TextView tv_title, tv_add, tv_getCount, tv_cancel, tv_clubServiceInfo;
    String albumId, albumName, albumDescription;
    String albumImage = "";
    ImageView iv_backbutton, iv_image, iv_edit, iv_album_photo, iv_album_photo2, iv_album_photo3, iv_album_photo4;
    ImageView close1, close2, close3, close4, close5;
    EditText edt_title, edt_description;
    EditText et_categoryName, et_noOfRotarians, et_COP, et_Beneficiary, et_manPower;
    Spinner sp_category, et_currency, sp_timeType;
    TextView et_DOP;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ProgressDialog pd;
    private String uploadedimgid = "0";
    String galleryType = "0";
    RadioButton d_radio0, d_radio1, d_radio2;
    private RadioButton rbInClub, rbPublic;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    private String edit_gallery_selectedids = "0";
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    String inputids = "";
    ArrayList<String> selectedsubgrp;
    private String edit_album_selectedids = "0";
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    String categoryID, projectDate;
    SpinnerAdapter adapter;
    //private RadioButton rbInClub, rbPublic;
    SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    LinearLayout ll_rotaryServicecontent,ll_category;
    int imgFlag = 0, deletePhotoflag = 0;
    EditText et_coverPhoto, et_album_photo3, et_album_photo2, et_album_photo1, et_album_photo4;
    int flag = 1;
    String groupId = "";
    String createdBy = "";
    String isdelete = "false";
    String photoId1 = "", photoId2 = "", photoId3 = "", photoId4 = "", photoId5 = "";
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    ArrayList<String> PhotoIdList = new ArrayList<>();
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    LinearLayout ll_photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_edit_album);

        addPhotoModel = new UploadedPhotoModel(this);
        groupId = PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID);
        createdBy = PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID);


        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Edit Album");
        edt_title = (EditText) findViewById(R.id.et_galleryTitle);

        edt_description = (EditText) findViewById(R.id.et_evetDesc);


        iv_image = (ImageView) findViewById(R.id.iv_event_photo);
        tv_add = (TextView) findViewById(R.id.tv_done);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_getCount = (TextView) findViewById(R.id.getCount);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);


        iv_album_photo = (ImageView) findViewById(R.id.iv_album_photo);
        iv_album_photo2 = (ImageView) findViewById(R.id.iv_album_photo2);
        iv_album_photo3 = (ImageView) findViewById(R.id.iv_album_photo3);
        iv_album_photo4 = (ImageView) findViewById(R.id.iv_album_photo4);

        et_coverPhoto = (EditText) findViewById(R.id.et_coverPhoto);
        et_album_photo1 = (EditText) findViewById(R.id.et_album_photo1);
        et_album_photo2 = (EditText) findViewById(R.id.et_album_photo2);
        et_album_photo3 = (EditText) findViewById(R.id.et_album_photo3);
        et_album_photo4 = (EditText) findViewById(R.id.et_album_photo4);

        close1 = (ImageView) findViewById(R.id.close1);
        close2 = (ImageView) findViewById(R.id.close2);
        close3 = (ImageView) findViewById(R.id.close3);
        close4 = (ImageView) findViewById(R.id.close4);
        close5 = (ImageView) findViewById(R.id.close5);


        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        // d_radio0.setChecked(true);
        rbInClub = (RadioButton) findViewById(R.id.rbInClub);
        rbPublic = (RadioButton) findViewById(R.id.rbPublic);

        Intent i = getIntent();
        albumId = i.getStringExtra("albumId");
        albumName = i.getStringExtra("albumname");
        albumDescription = i.getStringExtra("description");
        albumImage = i.getStringExtra("albumImage");
        //loadFromServer();
        edt_title.setText(albumName);
        edt_description.setText(albumDescription);
        et_categoryName = (EditText) findViewById(R.id.et_categoryName);
        et_DOP = (TextView) findViewById(R.id.et_DOP);
        et_DOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(et_DOP);
            }
        });
        et_COP = (EditText) findViewById(R.id.et_COP);
        et_Beneficiary = (EditText) findViewById(R.id.et_Beneficiary);
        et_manPower = (EditText) findViewById(R.id.et_manPower);
        et_noOfRotarians = (EditText) findViewById(R.id.et_noOfRotarians);

        tv_clubServiceInfo = (TextView) findViewById(R.id.tv_clubServiceInfo);
        ll_rotaryServicecontent = (LinearLayout) findViewById(R.id.ll_rotaryServicecontent);
        ll_category= (LinearLayout) findViewById(R.id.ll_category);
        ll_photos = (LinearLayout)findViewById(R.id.ll_photos);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = categoryList.get(position);
                categoryID = data.getDistrict_id();
                if (data.getDistrict_Name().equalsIgnoreCase("others")) {
                    et_categoryName.setVisibility(View.VISIBLE);
                } else {
                    et_categoryName.setText("");
                    et_categoryName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (rbInClub.isChecked()) {
            ll_rotaryServicecontent.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
            tv_clubServiceInfo.setVisibility(View.VISIBLE);
        }

        rbInClub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility(View.VISIBLE);
                ll_category.setVisibility(View.VISIBLE);
                tv_clubServiceInfo.setVisibility(View.GONE);
            }
        });

        rbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility(View.GONE);
                ll_category.setVisibility(View.GONE);
                tv_clubServiceInfo.setVisibility(View.VISIBLE);
            }
        });

        for (int j = 0; j < 5; j++) {
            imageList.add("");
            PhotoIdList.add("0");
            descList.add("");
        }

        et_currency = (Spinner) findViewById(R.id.et_currency);
        currencyList.add("\u20B9");
        currencyList.add("$");
        ArrayAdapter adapter = new ArrayAdapter(DTEditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, currencyList);
        et_currency.setAdapter(adapter);

        sp_timeType = (Spinner) findViewById(R.id.sp_timeType);
        timeType.add("Hours");
        timeType.add("Days");
        timeType.add("Months");
        timeType.add("Years");
        ArrayAdapter adapter1 = new ArrayAdapter(DTEditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, timeType);
        sp_timeType.setAdapter(adapter1);


//        if (albumImage.equalsIgnoreCase("") || albumImage.equalsIgnoreCase("null")) {
//
//        } else {
//            Picasso.with(DTEditAlbumActivity.this).load(albumImage)
//                    .placeholder(R.drawable.dashboardplaceholder)
//                    .into(iv_image);
//            imgFlag=1;
//        }
        init();
        getCategoryList();
    }


    public void datepicker(final TextView setdatetext) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                            Date newDate = format.parse(dayOfMonth + " " + (monthOfYear + 1) + " " + year);

                            format = new SimpleDateFormat("dd MMM yyyy");
                            String date = format.format(newDate);

                            projectDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            setdatetext.setText(date);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                inputids = "";
                d_radio1.setChecked(false);
                d_radio2.setChecked(false);
                d_radio2.setEnabled(true);

                d_radio1.setEnabled(true);

                clearselectedtext();
                break;
            case R.id.d_radio1:
                if (checked)
                    //set MM button to unchecked
               /*     d_radio0.setChecked(false);
                d_radio2.setChecked(false);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);*/
                    d_radio1.setChecked(false);
                Intent subgrp = new Intent(DTEditAlbumActivity.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(DTEditAlbumActivity.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }
                // d_radio2.setEnabled(false);
             /* d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }
    }

    public void init() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        selectImage();
                    }
                }


            }

        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals("2")) {
                    Intent i = new Intent(DTEditAlbumActivity.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_announcement_selectedids", edit_album_selectedids);
                    startActivityForResult(i, 3);
                } else if (galleryType.equals("1")) {
//                    Intent i = new Intent(EditAlbumActivity.this, SubGroupList.class);
//                    //i.putParcelableArrayListExtra("name1", memberData);
//                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
//                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
//                    startActivityForResult(i, 1);

                    Intent subgrp = new Intent(DTEditAlbumActivity.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    subgrp.putExtra("selected", selectedsubgrp);
                    subgrp.putExtra("edit", "1");
                    startActivityForResult(subgrp, 1);
                }

            }
        });


        iv_album_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 2;
                        selectAlbumImages();
                    }
                }

            }
        });
        iv_album_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 3;
                        selectAlbumImages();
                    }
                }

            }
        });
        iv_album_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 4;
                        selectAlbumImages();
                    }
                }

            }
        });

        iv_album_photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 5;
                        selectAlbumImages();
                    }
                }

            }
        });


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == true) {

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    }
                }
            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                            deletePhotoflag = 1;
                            deletePhoto(albumId, photoId1);
                        } else {
                            Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                            deletePhotoflag = 2;
                            deletePhoto(albumId, photoId2);
                        } else {
                            Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                            deletePhotoflag = 3;
                            deletePhoto(albumId, photoId3);
                        } else {
                            Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                            deletePhotoflag = 4;
                            deletePhoto(albumId, photoId4);
                        } else {
                            Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                            deletePhotoflag = 5;
                            deletePhoto(albumId, photoId5);
                        } else {
                            Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });


    }

    private void selectAlbumImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 6);
    }

    public void loadFromServer() {
        if (InternetConnection.checkConnection(this))
            loadData();
        else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    public void loadData() {

        Log.e("Touchbase", "------ loadData() called");
        String url = Constant.GetAlbumDetails_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));

        Log.d("Request", "PARAMETERS " + Constant.GetAlbumDetails_New + " :- " + arrayList.toString());
        GetAlbumDetailsAsynctask task = new GetAlbumDetailsAsynctask(url, arrayList, this);
        task.execute();

    }


    @Override
    public void onBackPressed() {
        Utils.popupback(DTEditAlbumActivity.this);
    }

    public class GetAlbumDetailsAsynctask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GetAlbumDetailsAsynctask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != "") {
                Log.d("Response", "calling GetAlbumDetails");
                getAlbumDetails(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    public void getAlbumDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject("TBAlbumDetailResult");
            final String status = jsonTBAlbumsListResult.getString("status");

            if (status.equals("0")) {

                JSONArray jsonNewAlbumList = jsonTBAlbumsListResult.getJSONArray("AlbumDetailResult");

                JSONObject object = jsonNewAlbumList.getJSONObject(0);
                JSONObject jsonAlbumDetail = object.getJSONObject("AlbumDetail");
//                    String id = jsonAlbumDetail.getString("albumId");
//                    String groupId = jsonAlbumDetail.getString("groupId");
//                    String tle = jsonAlbumDetail.getString("albumTitle");
//                    String desc = jsonAlbumDetail.getString("albumDescription");
                String type = jsonAlbumDetail.getString("type");


//                if (type.equalsIgnoreCase("0")) {
//                    d_radio0.setChecked(true);
//                    galleryType = "0";
//                    clearselectedtext();
//                } else if (type.equalsIgnoreCase("1")) {
//                    galleryType = "1";
//                    d_radio1.setChecked(true);
//                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] ids = edit_album_selectedids.split(",");
//                    selectedsubgrp = new ArrayList<String>();
//                    for (String id : ids) {
//                        selectedsubgrp.add(id);
//                    }
//                    inputids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] mystring = edit_album_selectedids.split(",");
//                    int size = mystring.length;
//                    tv_getCount.setText("You have added " + size + " sub groups");
//                    iv_edit.setVisibility(View.VISIBLE);
//                } else {
//                    galleryType = "2";
//                    d_radio2.setChecked(true);
//                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
//                    inputids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] mystring = edit_album_selectedids.split(",");
//                    int size = mystring.length;
//                    tv_getCount.setText("You have added " + size + " members");
//                    iv_edit.setVisibility(View.VISIBLE);
//                }

                d_radio0.setChecked(true);
                galleryType = "0";
                String shareType = jsonAlbumDetail.getString("shareType");
                if (shareType.equals("0")) {
                    rbInClub.setChecked(true);
                } else if (shareType.equals("1")) {
                    rbPublic.setChecked(true);
                }
                edt_title.setText(jsonAlbumDetail.getString("albumTitle"));
                edt_description.setText(jsonAlbumDetail.getString("albumDescription"));
                et_categoryName.setText(jsonAlbumDetail.getString("othercategorytext"));
                et_Beneficiary.setText(jsonAlbumDetail.getString("beneficiary"));
                et_COP.setText(jsonAlbumDetail.getString("project_cost"));

                projectDate = jsonAlbumDetail.getString("project_date");
                Date date = format1.parse(projectDate);
                et_DOP.setText(format.format(date));
                //et_DOP.setText(jsonAlbumDetail.getString("project_date"));

                et_manPower.setText(jsonAlbumDetail.getString("working_hour"));
                et_noOfRotarians.setText(jsonAlbumDetail.getString("NumberOfRotarian"));
                //sp_category.setSelection(Integer.parseInt(jsonAlbumDetail.getString("albumCategoryID"))-1);
                sp_category.setSelection(getIndex(categoryList, jsonAlbumDetail.getString("albumCategoryText")));
                Log.d("Index", getIndex(categoryList, jsonAlbumDetail.getString("albumCategoryText")) + "");
                et_currency.setSelection(Integer.parseInt(jsonAlbumDetail.getString("cost_of_project_type")) - 1);
                sp_timeType.setSelection(timeType.indexOf(jsonAlbumDetail.getString("working_hour_type")));
//


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }

    private int getIndex(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDistrict_Name().toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(DTEditAlbumActivity.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID)));
        arrayList.add(new BasicNameValuePair("type", galleryType));
        arrayList.add(new BasicNameValuePair("memberIds", inputids));
        arrayList.add(new BasicNameValuePair("albumTitle", edt_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumDescription", edt_description.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumImage", uploadedimgid));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        String shareType = "";
        arrayList.add(new BasicNameValuePair("dateofproject", projectDate));


        if (rbInClub.isChecked()) {
            shareType = "0";
            arrayList.add(new BasicNameValuePair("shareType", shareType));

            arrayList.add(new BasicNameValuePair("costofproject", ""));
            arrayList.add(new BasicNameValuePair("beneficiary", ""));
            arrayList.add(new BasicNameValuePair("manhourspent", ""));
            arrayList.add(new BasicNameValuePair("categoryId", ""));
            arrayList.add(new BasicNameValuePair("manhourspenttype", ""));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", ""));
            arrayList.add(new BasicNameValuePair("OtherCategorytext", ""));
            arrayList.add(new BasicNameValuePair("costofprojecttype", ""));

        } else if (rbPublic.isChecked()) {
            shareType = "1";
            arrayList.add(new BasicNameValuePair("shareType", shareType));
            arrayList.add(new BasicNameValuePair("costofproject", et_COP.getText().toString()));
            arrayList.add(new BasicNameValuePair("beneficiary", et_Beneficiary.getText().toString()));
            arrayList.add(new BasicNameValuePair("manhourspent", et_manPower.getText().toString()));
            arrayList.add(new BasicNameValuePair("categoryId", categoryID));
            arrayList.add(new BasicNameValuePair("manhourspenttype", "Hours"));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", et_noOfRotarians.getText().toString()));
            arrayList.add(new BasicNameValuePair("OtherCategorytext", et_categoryName.getText().toString()));
            arrayList.add(new BasicNameValuePair("costofprojecttype", (et_currency.getSelectedItemPosition() + 1) + ""));
        }
        Utils.log("Currency item position:  " + et_currency.getSelectedItemPosition() + "");

        Log.d("Response", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString());
        new EditAlbumAsyncTask(Constant.AddUpdateAlbum_New, arrayList, DTEditAlbumActivity.this).execute();
    }


    public class EditAlbumAsyncTask extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);

        public EditAlbumAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();
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
            progressDialog.dismiss();
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddGalleryResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Intent intent = new Intent();
                intent.putExtra("resultForEditAlbum", val);
                setResult(1, intent);


                albumId = ActivityResult.getString("galleryid");

                if (iv_image.getDrawable() != null) {
                    descList.remove(0);
                    descList.add(0,et_coverPhoto.getText().toString());
                }

                if (iv_album_photo.getDrawable() != null) {
                    descList.remove(1);
                    descList.add(1,et_album_photo1.getText().toString());
                }

                if (iv_album_photo2.getDrawable() != null) {
                    descList.remove(2);
                    descList.add(2,et_album_photo2.getText().toString());
                }

                if (iv_album_photo3.getDrawable() != null) {
                    descList.remove(3);
                    descList.add(3,et_album_photo3.getText().toString());
                }

                if (iv_album_photo4.getDrawable() != null) {
                    descList.remove(4);
                    descList.add(4,et_album_photo4.getText().toString());
                }

                getAlbumImagesData();


//                finish();//finishing activity
//
//                if (tv_add.getText().equals("Done")) {
//                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                }

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

            } else {
                Toast.makeText(DTEditAlbumActivity.this, "Failed to update album. Please retry.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getAlbumImagesData() {
        for (int i = 0; i < imageList.size(); i++) {
            if (!imageList.get(i).equals("")) {
                data = new UploadPhotoData(PhotoIdList.get(i).toString(), imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                long id = addPhotoModel.insert(data);

//            if(id>0 && imageList.size()-1 == i){
//
//                //   Toast.makeText(AddPhotoActivity.this,"Data saved Successfully",Toast.LENGTH_SHORT).show();
//            }
            }
        }


        Toast.makeText(DTEditAlbumActivity.this, "Album updated successfully.", Toast.LENGTH_SHORT).show();


        finish();
        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(DTEditAlbumActivity.this, UploadPhotoService.class);
        startService(intent);
    }

    private void getCategoryList() {

        try {
//            progressDialog=new ProgressDialog(AddAlbum.this,R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
            final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("DistrictID", "0");

            Log.d("Response", "PARAMETERS " + Constant.GetShowcaseDetails + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetShowcaseDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    setAllShowcaseDetails(response);
                    Utils.log(response.toString());
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if(progressDialog!=null){
//                        progressDialog.dismiss();
//                    }
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error.toString());
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAllShowcaseDetails(JSONObject response) {
        try {
            JSONObject ShowcaseDetails = response.getJSONObject("ShowcaseDetails");
            String status = ShowcaseDetails.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONObject result = ShowcaseDetails.getJSONObject("Result");
                JSONArray Categories = result.getJSONArray("Categories");
                for (int i = 0; i < Categories.length(); i++) {
                    JSONObject categoryObj = Categories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setDistrict_id(categoryObj.getString("ID"));
                    data.setDistrict_Name(categoryObj.getString("Name"));
                    categoryList.add(i, data);
                }
                categoryList.remove(0);
                adapter = new SpinnerAdapter(this, categoryList);
                sp_category.setAdapter(adapter);
                loadFromServer();
                getAlbumPhotos();
                //progressDialog.dismiss();
            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

    }

    private void getAlbumPhotos() {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("albumId", albumId);
            requestData.put("groupId", groupId);
            Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAlbumPhotoList_New, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    //globalResponse=response;
                    getPhotos(response);
                    //loadRssBlogs();
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log("VollyError:- " + error.toString());
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(DTEditAlbumActivity.this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPhotos(JSONObject response) {
        try {

            JSONObject jsonTBAlbumPhotoListResult = response.getJSONObject("TBAlbumPhotoListResult");
            final String status = jsonTBAlbumPhotoListResult.getString("status");

            if (status.equals("0")) {

                // updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");


                //JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonTBAlbumPhotoListResult.getJSONArray("Result");

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();


                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject(i);


                    if (i == 0) {
                        close1.setVisibility(View.GONE);
                        photoId1 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(0);
                        PhotoIdList.add(0, photoId1);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200,200)
                                .placeholder(R.drawable.dashboardplaceholder)
                                .into(iv_image);
                        iv_image.setBackground(null);
                        et_coverPhoto.setText(result_object.getString("description").toString());
                        imgFlag = 1;
                    } else if (i == 1) {
                        close2.setVisibility(View.VISIBLE);
                        photoId2 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(1);
                        PhotoIdList.add(1, photoId2);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200,200)
                                .placeholder(R.drawable.dashboardplaceholder)
                                .into(iv_album_photo);
                        iv_album_photo.setBackground(null);
                        et_album_photo1.setText(result_object.getString("description").toString());
                    } else if (i == 2) {
                        close3.setVisibility(View.VISIBLE);
                        photoId3 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(2);
                        PhotoIdList.add(2, photoId3);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200,200)
                                .placeholder(R.drawable.dashboardplaceholder)
                                .into(iv_album_photo2);
                        iv_album_photo2.setBackground(null);
                        et_album_photo2.setText(result_object.getString("description").toString());
                    } else if (i == 3) {
                        close4.setVisibility(View.VISIBLE);
                        photoId4 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(3);
                        PhotoIdList.add(3, photoId4);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200,200)
                                .placeholder(R.drawable.dashboardplaceholder)
                                .into(iv_album_photo3);
                        iv_album_photo3.setBackground(null);
                        et_album_photo3.setText(result_object.getString("description").toString());
                    } else if (i == 4) {
                        close5.setVisibility(View.VISIBLE);
                        photoId5 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(4);
                        PhotoIdList.add(4, photoId5);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200,200)
                                .placeholder(R.drawable.dashboardplaceholder)
                                .into(iv_album_photo4);
                        iv_album_photo4.setBackground(null);
                        et_album_photo4.setText(result_object.getString("description").toString());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Member Select
        if (requestCode == 3) {

            if (resultCode == Activity.RESULT_OK) {

                listaddmemberdata = data.getParcelableArrayListExtra("result");
                String result = "";
                int count = 0;
                ArrayList<String> selectedmemberid = new ArrayList<>();

                selectedmemberid.clear();
                inputids = "";

                for (DirectoryData d : listaddmemberdata) {
                    if (d.isBox() == true) {
                        result = result + d.getProfileID();
                        count = count + 1;
                        selectedmemberid.add(d.getProfileID());
                    }
                    //something here
                }
                for (int i = 0; i < selectedmemberid.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedmemberid.get(i);

                    if (i != selectedmemberid.size() - 1) {

                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);
                d_radio2.setChecked(true);

                d_radio0.setEnabled(true);
                d_radio1.setEnabled(true);
                d_radio2.setEnabled(false);

                galleryType = "2";
                Log.d("Touchnase", "Arrat " + inputids);
                tv_getCount.setText("You have added " + count + " members");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }
        // SubGroup Select
        if (requestCode == 1) {


            if (resultCode == Activity.RESULT_OK) {

                listaddsubgrp = data.getParcelableArrayListExtra("result");
                if (listaddsubgrp.size() == 0)
                    Log.d("Touchnase", "@@@ " + listaddsubgrp);
                String result = "";
                int count = 0;

                selectedsubgrp = new ArrayList<>();
                selectedsubgrp.clear();
                selectedsubgrp = data.getStringArrayListExtra("result");
                count = selectedsubgrp.size();

                inputids = "";
                for (int i = 0; i < selectedsubgrp.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedsubgrp.get(i);

                    if (i != selectedsubgrp.size() - 1) {

                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }
                d_radio0.setChecked(false);
                d_radio1.setChecked(true);
                d_radio2.setChecked(false);

                d_radio0.setEnabled(true);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);

                Log.d("Touchnase", "Arrat " + inputids);
                galleryType = "1";
                tv_getCount.setText("You have added " + count + " sub groups");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }
        if (requestCode == 4) {


            if (resultCode == Activity.RESULT_OK) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    iv_image.setImageBitmap(bt);
                    iv_image.setBackground(null);


                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    Log.d("TOUCHBASE", "FILE PATH " + f.toString());
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);
                    final File finalF = f;
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            imgFlag = 1;
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "gallery"); // Upload File to server
                            imageList.add(0, finalF.toString());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        //iv_image.setImageResource(R.drawable.edit_image);
                                        iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                                        imgFlag = 0;
                                        close1.setVisibility(View.GONE);
                                    }
                                    ll_photos.setVisibility(View.GONE);
                                    close1.setVisibility(View.GONE);
                                    //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    thread.start();
                    ///-------------------------------------------------------------------
                    //uploadedimgid = Utils.doFileUpload(new File(f.toString()), "announcement"); // Upload File to server


                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2) {

            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                final Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.d("TOUCHBASE", "FILE PATH " + picturePath.toString());
                imageList.add(0, picturePath.toString());
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(picturePath), "gallery"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    imgFlag = 0;
                                    Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_image.setImageResource(R.drawable.edit_image);
                                    iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                                    close1.setVisibility(View.GONE);
                                } else {
                                    imgFlag = 1;
                                    iv_image.setImageBitmap(thumbnail);
                                    iv_image.setBackground(null);
                                    close1.setVisibility(View.GONE);
                                    ll_photos.setVisibility(View.GONE);
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        } else if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                final Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.d("TOUCHBASE", "FILE PATH " + picturePath.toString());
                //imageList.add(picturePath.toString());
                Utils.log("Images " + imageList.toString());
                if (flag == 2) {
                    imageList.add(flag - 1, picturePath.toString());
                    iv_album_photo.setImageBitmap(thumbnail);
                    iv_album_photo.setBackground(null);
                } else if (flag == 3) {
                    imageList.add(flag - 1, picturePath.toString());
                    iv_album_photo2.setImageBitmap(thumbnail);
                    iv_album_photo2.setBackground(null);
                } else if (flag == 4) {
                    imageList.add(flag - 1, picturePath.toString());
                    iv_album_photo3.setImageBitmap(thumbnail);
                    iv_album_photo3.setBackground(null);
                } else if (flag == 5) {
                    imageList.add(flag - 1, picturePath.toString());
                    iv_album_photo4.setImageBitmap(thumbnail);
                    iv_album_photo4.setBackground(null);
                }
            }

        }
    }

    public void deletePhoto(String albumId, String photoId) {
        Log.e("Touchbase", "------ deletePhoto() is called");
        String url = Constant.DeletePhoto;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("photoId", photoId));
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("deletedBy", PreferenceManager.getPreference(DTEditAlbumActivity.this, PreferenceManager.GRP_PROFILE_ID)));

        DeletePhotoAsyncTask task = new DeletePhotoAsyncTask(url, arrayList, DTEditAlbumActivity.this);
        task.execute();

        Log.d("Request", "PARAMETERS " + Constant.DeletePhoto + " :- " + arrayList.toString());
    }


    public class DeletePhotoAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
        Context con = null;
        String url = null;
        List<NameValuePair> argList = null;


        public DeletePhotoAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            con = ctx;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
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
            progressDialog.dismiss();
            if (result != "") {
                getdeltedresult(result.toString());
                Log.d("Response", "calling DeleteAlbum");
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getdeltedresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBDelteAlbumPhoto");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");
                if (deletePhotoflag == 1) {
                    imageList.add(0,"");
                    iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_image.setImageResource(0);
                    close1.setVisibility(View.GONE);
                } else if (deletePhotoflag == 2) {
                    imageList.add(1,"");
                    iv_album_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo.setImageResource(0);
                    close2.setVisibility(View.GONE);
                } else if (deletePhotoflag == 3) {
                    imageList.add(2,"");
                    iv_album_photo2.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo2.setImageResource(0);
                    close3.setVisibility(View.GONE);
                } else if (deletePhotoflag == 4) {
                    imageList.add(3,"");
                    iv_album_photo3.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo3.setImageResource(0);
                    close4.setVisibility(View.GONE);
                } else if (deletePhotoflag == 5) {
                    imageList.add(4,"");
                    iv_album_photo4.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo4.setImageResource(0);
                    close5.setVisibility(View.GONE);
                }
                Toast.makeText(DTEditAlbumActivity.this, "Photo deleted successfully.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


        final CharSequence[] options;
        options = new CharSequence[]{"Choose from Gallery"};
        //options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DTEditAlbumActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        // builder.show();
    }

    public boolean validation() {
        if (rbInClub.isChecked()) {
            if (edt_title.getText().toString().trim().matches("") || edt_title.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }
            if (edt_description.getText().toString().trim().matches("") || edt_description.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (imgFlag == 0) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        } else if (rbPublic.isChecked()) {
            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_COP.getText().toString().trim().matches("") || et_COP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter cost.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches("") || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Beneficiaries.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches("") || et_manPower.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Man hours.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches("") || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Rotarians.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (edt_title.getText().toString().trim().matches("") || edt_title.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }

            if (edt_description.getText().toString().trim().matches("") || edt_description.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (imgFlag == 0) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }


    /*public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                if (checked)
                    //set inch button to unchecked
                    galleryType = "0";
                inputids = "";
                d_radio1.setChecked(false);
                d_radio2.setChecked(false);
                d_radio2.setEnabled(true);

                d_radio1.setEnabled(true);
                clearselectedtext();
                break;
            case R.id.d_radio1:
                if (checked)
                    //set MM button to unchecked
               *//*     d_radio0.setChecked(false);
                d_radio2.setChecked(false);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);*//*
                    d_radio1.setChecked(false);

                Intent subgrp = new Intent(EditAlbumActivity.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(EditAlbumActivity.this, AddMembers.class);
                    startActivityForResult(i, 3);


                }

                // d_radio2.setEnabled(false);
             *//*   d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*//*
                break;
        }
    }*/


    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }


}
