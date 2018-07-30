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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.SpinnerAdapter;
import com.SampleApp.row.Data.AlbumData;
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

import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * Created by user on 06-09-2016.
 */
public class AddAlbum extends Activity {
    ArrayList<String> selectedsubgrp;
    TextView tv_title, tv_cancel,tv_clubServiceInfo;
    ImageView iv_backbutton, iv_edit, iv_event_photo,iv_album_photo,iv_album_photo2,iv_album_photo3,iv_album_photo4;
    RadioButton d_radio0, d_radio1, d_radio2;
    TextView tv_getCount, tv_add,et_DOP;
    String galleryType = "0";
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    EditText et_coverPhoto,et_album_photo3,et_album_photo2,et_album_photo1,et_album_photo4;
    private String groupType = "1";
    private LinearLayout llShareWrapper,ll_rotaryServicecontent,ll_category,ll_photos;
    private RadioGroup rgShare;
    private RadioButton rbInClub, rbPublic;

    private String hasimageflag = "0";
    private String galleryId = "0";
    private String edit_gallery_selectedids = "0";
    ProgressDialog pd;
    private String uploadedimgid = "0";
    EditText et_gallery_title, et_description,et_categoryName,et_noOfRotarians,et_COP,et_Beneficiary,et_manPower;
    String inputids = "",albumId="";
    private String flag_callwebsercie = "0";
    private String moduleName = "";
    private Context context;
    Spinner sp_category,et_currency,sp_timeType;
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ProgressDialog progressDialog;
    SpinnerAdapter adapter;
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    String categoryID,projectDate;
    TextView tv_TimeCountType;
    int imgFlag=0;
    String groupId = "";
    String createdBy = "";
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    int flag = 1, deletePhotoflag = 0;
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    ImageView close1, close2, close3, close4, close5;
    String isdelete = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_album_new);
        context = this;
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        groupId= PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID);
        createdBy= PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID);


        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Album");
        tv_title.setText("New " + moduleName);

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        d_radio0.setChecked(true);

        addPhotoModel = new UploadedPhotoModel(this);

        tv_getCount = (TextView) findViewById(R.id.getCount);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        iv_event_photo = (ImageView) findViewById(R.id.iv_event_photo);
        iv_album_photo = (ImageView)findViewById(R.id.iv_album_photo);
        iv_album_photo2 = (ImageView)findViewById(R.id.iv_album_photo2);
        iv_album_photo3 = (ImageView)findViewById(R.id.iv_album_photo3);
        iv_album_photo4 = (ImageView)findViewById(R.id.iv_album_photo4);

        et_coverPhoto = (EditText)findViewById(R.id.et_coverPhoto);
        et_album_photo1 = (EditText)findViewById(R.id.et_album_photo1);
        et_album_photo2 = (EditText)findViewById(R.id.et_album_photo2);
        et_album_photo3 = (EditText)findViewById(R.id.et_album_photo3);
        et_album_photo4 = (EditText)findViewById(R.id.et_album_photo4);
        
        close1 = (ImageView) findViewById(R.id.close1);
        close2 = (ImageView) findViewById(R.id.close2);
        close3 = (ImageView) findViewById(R.id.close3);
        close4 = (ImageView) findViewById(R.id.close4);
        close5 = (ImageView) findViewById(R.id.close5);


        tv_add = (TextView) findViewById(R.id.tv_done);
        tv_clubServiceInfo = (TextView)findViewById(R.id.tv_clubServiceInfo);

        et_gallery_title = (EditText) findViewById(R.id.et_galleryTitle);


        et_description = (EditText) findViewById(R.id.et_evetDesc);

        et_categoryName = (EditText)findViewById(R.id.et_categoryName);
        et_DOP = (TextView)findViewById(R.id.et_DOP);
        et_DOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(et_DOP);
            }
        });
        et_COP = (EditText)findViewById(R.id.et_COP);
        et_Beneficiary = (EditText)findViewById(R.id.et_Beneficiary);
        et_manPower = (EditText)findViewById(R.id.et_manPower);
        et_manPower.setKeyListener(DigitsKeyListener.getInstance(true,true));
        et_noOfRotarians = (EditText)findViewById(R.id.et_noOfRotarians);

        sp_category = (Spinner)findViewById(R.id.sp_category);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = categoryList.get(position);
                categoryID = data.getDistrict_id();
                if(categoryList.get(position).getDistrict_Name().equalsIgnoreCase("others")){
                    et_categoryName.setVisibility(View.VISIBLE);
                }else{
                    et_categoryName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_currency = (Spinner)findViewById(R.id.et_currency);
        currencyList.add("\u20B9");
        currencyList.add("$");
        ArrayAdapter adapter = new ArrayAdapter(AddAlbum.this,android.R.layout.simple_spinner_dropdown_item,currencyList);
        et_currency.setAdapter(adapter);

        sp_timeType = (Spinner)findViewById(R.id.sp_timeType);
        timeType.add("Hours");
        timeType.add("Days");
        timeType.add("Months");
        timeType.add("Years");
        ArrayAdapter adapter1 = new ArrayAdapter(AddAlbum.this,android.R.layout.simple_spinner_dropdown_item,timeType);
        sp_timeType.setAdapter(adapter1);

        llShareWrapper = (LinearLayout) findViewById(R.id.llShareWrapper);
        ll_rotaryServicecontent = (LinearLayout)findViewById(R.id.ll_rotaryServicecontent);
        ll_category = (LinearLayout)findViewById(R.id.ll_category);
        ll_photos = (LinearLayout)findViewById(R.id.ll_photos);
        rbInClub = (RadioButton) findViewById(R.id.rbInClub);
        if(rbInClub.isChecked()){
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

        rbPublic = (RadioButton)findViewById(R.id.rbPublic);
        rbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility(View.GONE);
                ll_category.setVisibility(View.GONE);
                tv_clubServiceInfo.setVisibility(View.VISIBLE);
            }
        });

        rgShare = (RadioGroup) findViewById(R.id.rgShare);

        for(int i=0;i<5;i++){
            imageList.add("");
            descList.add("");
        }

        groupType = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
        Utils.log("Group type is : " + groupType);
        clearselectedtext();
        init();
        getCategoryList();
    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }

    private void init() {
        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i  = new Intent(AddAlbum.this,Gallery.class);
                startActivity(i);
            }

        });*/



        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals("2")) {
                    Intent i = new Intent(AddAlbum.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
                    startActivityForResult(i, 3);
                } else if (galleryType.equals("1")) {
                    /*Intent i = new Intent(AddAlbum.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
                    startActivityForResult(i, 1);*/

                    Intent subgrp = new Intent(AddAlbum.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    subgrp.putExtra("selected", selectedsubgrp);
                    subgrp.putExtra("edit", "1");
                    startActivityForResult(subgrp, 1);
                }

            }
        });

        iv_event_photo.setOnClickListener(new View.OnClickListener() {
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

        iv_album_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag=2;
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
                        flag=3;
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
                        flag=4;
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
                        flag=5;
                        selectAlbumImages();
                    }
                }

            }
        });




        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d("-----------------", "add is clicked");
                if (validation() == true) {

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }
            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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


                            deletePhotoflag = 1;
                            if (!imageList.get(0).isEmpty()) {
                                imageList.remove(0);
                                imageList.add(0, "");
                                iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                                iv_event_photo.setImageResource(0);
                                close1.setVisibility(View.GONE);
                            }

                    }
                });

                dialog.show();
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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
                        deletePhotoflag = 1;
                        if (!imageList.get(1).isEmpty()) {
                            imageList.remove(1);
                            imageList.add(1, "");
                            iv_album_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo.setImageResource(0);
                            close2.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(2).isEmpty()) {
                            imageList.remove(2);
                            imageList.add(2, "");
                            iv_album_photo2.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo2.setImageResource(0);
                            close3.setVisibility(View.GONE);
                        }
                    }
                });

                dialog.show();
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(3).isEmpty()) {
                            imageList.remove(3);
                            imageList.add(3, "");
                            iv_album_photo3.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo3.setImageResource(0);
                            close4.setVisibility(View.GONE);
                        }
                    }
                });

                dialog.show();
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(4).isEmpty()) {
                            imageList.remove(4);
                            imageList.add(4, "");
                            iv_album_photo4.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo4.setImageResource(0);
                            close5.setVisibility(View.GONE);
                        }
                    }
                });

                dialog.show();
            }
        });



    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:

                if (checked) {
                    //set inch button to unchecked
                    galleryType = "0";
                    llShareWrapper.setVisibility(View.VISIBLE);
                } else {
                    llShareWrapper.setVisibility(View.VISIBLE);
                }

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
                       /*d_radio0.setChecked(false);
                        d_radio2.setChecked(false);
                        d_radio1.setEnabled(false);
                        d_radio2.setEnabled(true);*/
                    d_radio1.setChecked(false);
                llShareWrapper.setVisibility(View.GONE);
                Intent subgrp = new Intent(AddAlbum.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);

                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(AddAlbum.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }
                llShareWrapper.setVisibility(View.GONE);

                // d_radio2.setEnabled(false);
                /*
                d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }

        if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
            llShareWrapper.setVisibility(View.GONE);
        }
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(AddAlbum.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("albumId", "0"));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID)));
        arrayList.add(new BasicNameValuePair("type", galleryType));
        arrayList.add(new BasicNameValuePair("memberIds", inputids));
        arrayList.add(new BasicNameValuePair("albumTitle", et_gallery_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumDescription", et_description.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumImage", uploadedimgid));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        String shareType = "";
        arrayList.add(new BasicNameValuePair("dateofproject",projectDate ));

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
            arrayList.add(new BasicNameValuePair("costofprojecttype",""));
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
            arrayList.add(new BasicNameValuePair("costofprojecttype",(et_currency.getSelectedItemPosition()+1)+""));
        }


        flag_callwebsercie = "0";

        Log.d("Response", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddUpdateAlbum_New, arrayList, AddAlbum.this).execute();
//        Log.d("Response", "PARAMETERS " + Constant.AddAlbum + " :- " + arrayList.toString());
//        new WebConnectionAsyncLogin(Constant.AddAlbum, arrayList, AddAlbum.this).execute();
    }


    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);

        public WebConnectionAsyncLogin(String url, List<NameValuePair> argList, Context ctx) {
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
                if (flag_callwebsercie.equals("0")) {
                    getresult(result.toString());
                } else if (flag_callwebsercie.equals("1")) {
                    // getresult_addeddata(result.toString());
                } else if (flag_callwebsercie.equals("4")) {
                    //getresultOfRemovephoto(result.toString());
                }
            } else {
                Log.d("Response", "Null Resposnse"+result);
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
                setResult(1, intent);

                albumId = ActivityResult.getString("galleryid");

                if (iv_event_photo.getDrawable() != null) {
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

//                if (tv_add.getText().equals("Done"))
//                    Toast.makeText(AddAlbum.this, "Album Added successfully.", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(AddAlbum.this, "Album updated successfully.", Toast.LENGTH_SHORT).show();

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);
                //finish();//finishing activity

            } else {
                if (tv_add.getText().equals("Add"))
                    Toast.makeText(AddAlbum.this, "Failed to add album.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddAlbum.this, "Failed to update album.", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getAlbumImagesData() {
        for(int i = 0;i< imageList.size();i++) {
            if(!imageList.get(i).equals("")) {
                data = new UploadPhotoData("0", imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                long id = addPhotoModel.insert(data);

//            if(id>0 && imageList.size()-1 == i){
//
//                //   Toast.makeText(AddPhotoActivity.this,"Data saved Successfully",Toast.LENGTH_SHORT).show();
//            }
            }
        }


            Toast.makeText(AddAlbum.this, "Album Added successfully.", Toast.LENGTH_SHORT).show();


        finish();
        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(AddAlbum.this, UploadPhotoService.class);
        startService(intent);
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

                            projectDate = year+"-"+ (monthOfYear + 1) + "-"+dayOfMonth;
                            setdatetext.setText(date);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public boolean validation() {
        if(rbInClub.isChecked()) {
            if (et_gallery_title.getText().toString().trim().matches("") || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_description.getText().toString().trim().matches("") || et_description.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (imgFlag == 0) {
                Toast.makeText(AddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

        return true;
        }else if(rbPublic.isChecked()) {

            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_COP.getText().toString().trim().matches("") || et_COP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter cost.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches("") || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Beneficiaries.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches("") || et_manPower.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Man hours.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches("") || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Rotarians.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_gallery_title.getText().toString().trim().matches("") || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_description.getText().toString().trim().matches("") || et_description.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (imgFlag == 0) {
                Toast.makeText(AddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }else{
            return false;
        }
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);

        final CharSequence[] options;
        options = new CharSequence[]{"Choose from Gallery"};
//        if (hasimageflag.equals("0")) {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
//            hasimageflag = "1";
//        } else {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddAlbum.this);
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
                } else if (options[item].equals("Remove Photo")) {
                    if (galleryId.equals("0")) {
                        iv_event_photo.setImageResource(R.drawable.edit_image);
                        imgFlag=0;
                    } else {
                        //remove_photo_webservices();
                    }

                }
            }
        });
        //builder.show();
    }
    private void selectAlbumImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 6);
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
                Log.d("Touchnase", "@@@ " + listaddsubgrp);
                String result = "";
                int count = 0;
                /*ArrayList<String> selectedsubgrp = new ArrayList<>();

                selectedsubgrp.clear();
                inputids = "";

                for (SubGoupData d : listaddsubgrp) {
                    if (d.isBox() == true) {
                        result = result + d.getSubgrpId();
                        count = count + 1;
                        selectedsubgrp.add(d.getSubgrpId());
                    }
                    //something here
                }
                for (int i = 0; i < selectedsubgrp.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedsubgrp.get(i);

                    if (i != selectedsubgrp.size() - 1) {

                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }*/

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


        /***************Image Capture***********/
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
                    iv_event_photo.setImageBitmap(bt);
                    iv_event_photo.setBackground(null);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    Log.d("TOUCHBASE", "FILE PATH " + f.toString());
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show(AddAlbum.this, "", "Loading...", false);
                    final File finalF = f;
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            imgFlag=1;
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "gallery"); // Upload File to server
                            imageList.remove(0);
                            imageList.add(0,finalF.toString());
                            Utils.log(imageList.toString());
                            ll_photos.setVisibility(View.VISIBLE);
                            Utils.log("URL : "+finalF.toString());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(AddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        //iv_event_photo.setImageResource(R.drawable.edit_image);
                                        iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                                        imgFlag=0;
                                    }
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
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    Toast.makeText(context, "Failed to load captured image. Please try again.", Toast.LENGTH_LONG).show();
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

                Utils.log(imageList.toString());

                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(AddAlbum.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(picturePath), "gallery"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(AddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                                    ll_photos.setVisibility(View.GONE);
                                } else {
                                    imageList.remove(0);
                                    imageList.add(0,picturePath.toString());
                                    iv_event_photo.setImageBitmap(thumbnail);
                                    iv_event_photo.setBackground(null);
                                    ll_photos.setVisibility(View.VISIBLE);
                                    imgFlag=1;
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        }else if(requestCode==6){
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
                    Utils.log("Images "+ imageList.toString());
                    if(flag==2){
                        imageList.remove(1);
                        imageList.add(1,picturePath.toString());
                        iv_album_photo.setImageBitmap(thumbnail);
                        iv_album_photo.setBackground(null);
                        close2.setVisibility(View.VISIBLE);
                    }else if(flag==3){
                        imageList.remove(2);
                        imageList.add(2,picturePath.toString());
                        iv_album_photo2.setImageBitmap(thumbnail);
                        iv_album_photo2.setBackground(null);
                        close3.setVisibility(View.VISIBLE);
                    }else if(flag==4){
                        imageList.remove(3);
                        imageList.add(3,picturePath.toString());
                        iv_album_photo3.setImageBitmap(thumbnail);
                        iv_album_photo3.setBackground(null);
                        close4.setVisibility(View.VISIBLE);
                    }else if(flag==5){
                        imageList.remove(4);
                        imageList.add(4,picturePath.toString());
                        iv_album_photo4.setImageBitmap(thumbnail);
                        iv_album_photo4.setBackground(null);
                        close5.setVisibility(View.VISIBLE);
                    }
                }
        }

        if (groupType.equals("2")) {
            llShareWrapper.setVisibility(View.GONE);
        } else if (d_radio0.isChecked()) {
            llShareWrapper.setVisibility(View.VISIBLE);
        }
    }


    private void getCategoryList() {

        try {
            progressDialog=new ProgressDialog(AddAlbum.this,R.style.TBProgressBar);
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
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    Utils.log("VollyError:- " + error.toString());
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAllShowcaseDetails(JSONObject response) {
        try {
            JSONObject ShowcaseDetails = response.getJSONObject("ShowcaseDetails");
            String status = ShowcaseDetails.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONObject result = ShowcaseDetails.getJSONObject("Result");
                JSONArray Categories = result.getJSONArray("Categories");
                for (int i=1;i<Categories.length();i++){
                    JSONObject categoryObj = Categories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setDistrict_id(categoryObj.getString("ID"));
                    data.setDistrict_Name(categoryObj.getString("Name"));
                    categoryList.add(i-1,data);
                }
                adapter=new SpinnerAdapter(context,categoryList);
                sp_category.setAdapter(adapter);
                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }
}
