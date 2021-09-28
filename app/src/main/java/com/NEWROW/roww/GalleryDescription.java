package com.NEWROW.row;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.AlbumPhotoAdapter;
import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.Data.AlbumPhotoData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.sql.AlbumPhotosMasterModel;
import com.NEWROW.row.sql.GalleryMasterModel;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GalleryDescription extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final int STATE_UP = 1, STATE_DOWN = 2;
    int buttonState = STATE_DOWN;
    Context context;
    View descriptionWrapper;

    String fabOpen = "close";
/*    AppBarLayout layout;
    CollapsingToolbarLayout toolbarLayout;*/

    TextView tv_title, tv_minititle, tv_description, tv_dop, tv_cop, tv_beneficiary, tv_manPower, tv_noOfRotarians, txt_galleryTitle, tv_rotractors;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addPhoto, deletePhoto, editAlbum;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission( this );

    ArrayList<String> selectedImageList;

    public static final int SELECT_PICTURE = 2;
    static Bitmap bitmap;
    String updatedOn = "";
    String albumId = "";
    //    ImageView ivUpDown;
    FloatingActionButton ivUpDown;
    AlbumPhotosMasterModel albumPhotosMasterModel;
    private long grpId;
    ArrayList<AlbumPhotoData> albumPhotolist = new ArrayList<AlbumPhotoData>();
    AlbumPhotoAdapter adapter;
    GridView gv;
    String title, fromNoti = "0";
    String description, shareType, attendance, attendancePer, meetType;
    String albumimage;

    int mode = 1;
    String modeAttachment = "";
    boolean isinUpdatemode = false;
    static final int EDITALBUM = 1;
    GalleryMasterModel albumModel;
    String moduleId = "", fromShowcase = "1";
    LinearLayout ll_desc, ll_mom, ll_agenda;
    AlbumData data = new AlbumData();
    ArrayList<AlbumPhotoData> newAlbums;
    String currencyType = "";
    LinearLayout ll_noOfRotarians, ll_timespent, ll_bene, ll_cop, ll_dop, ll_details, ll_rotractors;

    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
    SimpleDateFormat sdf1 = new SimpleDateFormat( "dd MMM yyyy" );
    ImageView iv_actionbtn, iv_view, iv_view_mom, iv_download, iv_download_mom;

    DownloadManager downloadmanager;
    ProgressDialog progress;
    private String filePath = "";
    private long enqueId;
    FileDownloadReceiver fileReceiver;
    public static String MODE_VIEW = "VIEW", MODE_DOWNLOAD = "DOWNLOAD";
    String link = "", titleDoc = "";
    View view;
    //Added By Gaurav
    LinearLayout media_photo_layout, photo_title_layout;
    ImageView iv_printmedia;
    private String mediaPhotoPath = "";
    private String media_discription = "";
    private String Ismedia = "0";
    private String storageFileName;
    private String messageId_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_gallery_description_new );
        Log.e( "Album", "Inside album now" );
        context = this;
        if (PreferenceManager.getPreference( this, PreferenceManager.GROUP_ID ) != null) {
            grpId = Long.parseLong( PreferenceManager.getPreference( this, PreferenceManager.GROUP_ID ) );
        }


        fileReceiver = new FileDownloadReceiver();
        //  this.registerReceiver(fileReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        getApplicationContext().registerReceiver( fileReceiver, new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE ) );
        downloadmanager = (DownloadManager) GalleryDescription.this.getSystemService( Context.DOWNLOAD_SERVICE );

        albumPhotosMasterModel = new AlbumPhotosMasterModel( this );
        descriptionWrapper = findViewById( R.id.descriptionWrapper );

        gv = (GridView) findViewById( R.id.gv );
        ll_desc = (LinearLayout) findViewById( R.id.ll );

//        title = data.getTitle().toString();
//        description = data.getDescription().toString();
        //  albumId = data.getAlbumId();
//        albumimage = data.getImage();
//        shareType=data.getShareType();

        selectedImageList = new ArrayList<String>();

        tv_title = (TextView) findViewById( R.id.tv_title );
        tv_minititle = (TextView) findViewById( R.id.tv_minititle );
        iv_actionbtn = (ImageView) findViewById( R.id.iv_actionbtn );
        iv_actionbtn.setImageResource( R.drawable.edit );
        // iv_actionbtn.setVisibility(View.VISIBLE);

        iv_view = (ImageView) findViewById( R.id.iv_view );
        iv_view_mom = (ImageView) findViewById( R.id.iv_view_mom );
        iv_download = (ImageView) findViewById( R.id.iv_download );
        iv_download_mom = (ImageView) findViewById( R.id.iv_download_mom );

        view = (View) findViewById( R.id.view );
        ll_dop = (LinearLayout) findViewById( R.id.ll_dop );
        ll_cop = (LinearLayout) findViewById( R.id.ll_cop );
        ll_bene = (LinearLayout) findViewById( R.id.ll_bene );
        ll_timespent = (LinearLayout) findViewById( R.id.ll_timespent );
        ll_noOfRotarians = (LinearLayout) findViewById( R.id.ll_noOfRotarians );
        ll_rotractors = (LinearLayout) findViewById( R.id.ll_rotractors );
        ll_details = (LinearLayout) findViewById( R.id.ll_details );
        ll_mom = (LinearLayout) findViewById( R.id.ll_mom );
        ll_agenda = (LinearLayout) findViewById( R.id.ll_agenda );

        tv_description = (TextView) findViewById( R.id.tv_description );
        tv_description.setMovementMethod( new ScrollingMovementMethod() );
        tv_dop = (TextView) findViewById( R.id.tv_dop );

        /* Added By Gaurav*/
        media_photo_layout = (LinearLayout) findViewById( R.id.media_photo_layout );
        photo_title_layout = (LinearLayout) findViewById( R.id.photo_title_layout );
        iv_printmedia = findViewById( R.id.iv_printmedia );


        iv_printmedia.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( GalleryDescription.this, ViewMediaPhoto.class );
                intent.putExtra( "imgURL", mediaPhotoPath );
                intent.putExtra( "description", media_discription );
                startActivity( intent );
            }
        } );

        /*closed By Gaurav*/

//        if (data.getProject_date().toString().isEmpty()) {
//            ll_dop.setVisibility(View.GONE);
//        }

//        if (data.getShareType().equalsIgnoreCase("0")) {
//            ll_details.setVisibility(View.GONE);
//        } else {
//            ll_details.setVisibility(View.VISIBLE);
//        }

//        String date = data.getProject_date().toString();
//        if (date != null && !date.isEmpty()) {
//            try {
//                Date copDate = sdf.parse(date);
//                tv_dop.setText(sdf1.format(copDate));
//            } catch (ParseException e) {
//                tv_dop.setText(data.getProject_date().toString());
//                e.printStackTrace();
//            }
//        }

        tv_cop = (TextView) findViewById( R.id.tv_cop );

//        if (data.getCost_of_project_type().equalsIgnoreCase("1")) {
//            currencyType = "\u20B9";
//        } else if (data.getCost_of_project_type().equalsIgnoreCase("2")) {
//            currencyType = "$";
//        } else {
//            currencyType = "";
//        }
//        if (data.getProject_cost().toString().isEmpty()) {
//            ll_cop.setVisibility(View.GONE);
//        }
//
//        if (data.getBeneficiary().toString().isEmpty()) {
//            ll_bene.setVisibility(View.GONE);
//        }
//        if (data.getWorking_hour().toString().isEmpty()) {
//            ll_timespent.setVisibility(View.GONE);
//        }
//
//        if (data.getNoOfRotarians().toString().isEmpty()) {
//            ll_noOfRotarians.setVisibility(View.GONE);
//        }

//        tv_cop.setText(data.getProject_cost().toString());
        // tv_cop.setText(data.getProject_cost().toString() + " " + currencyType);
        tv_beneficiary = (TextView) findViewById( R.id.tv_beneficiary );
        // tv_beneficiary.setText(data.getBeneficiary().toString());
        tv_manPower = (TextView) findViewById( R.id.tv_manHrSpent );
//        tv_manPower.setText(data.getWorking_hour().toString());
        // tv_manPower.setText(data.getWorking_hour().toString() + " " + data.getWorking_hour_type());
        tv_noOfRotarians = (TextView) findViewById( R.id.tv_noOfRotarians );
        tv_rotractors = (TextView) findViewById( R.id.tv_rotractors );

        // tv_noOfRotarians.setText(data.getNoOfRotarians().toString());
        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));

        txt_galleryTitle = (TextView) findViewById( R.id.txt_galleryTitle );
        //Log.e("Description", description);


        // tv_description.setText(description);

        /*layout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);*/

        ivUpDown = (FloatingActionButton) findViewById( R.id.fab1 );

        albumModel = new GalleryMasterModel( this );
        moduleId = PreferenceManager.getPreference( this, PreferenceManager.MODULE_ID );

        // final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (fabOpen.equalsIgnoreCase("close")) {
//                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
//                    layoutParams.height = layoutParams.WRAP_CONTENT;
//                    layout.setLayoutParams(layoutParams);
//                    fabOpen = "open";
//                    fab.setImageResource(R.drawable.f_up);
//                    toolbarLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                } else {
//                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
//                    layoutParams.height = 160;
//                    layout.setLayoutParams(layoutParams);
//                    fabOpen = "close";
//                    fab.show();
//                    fab.setImageResource(R.drawable.f_down);
//                    toolbarLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                }
//            }
//        });

        materialDesignFAM = (FloatingActionMenu) findViewById( R.id.material_design_android_floating_action_menu );
        addPhoto = (com.github.clans.fab.FloatingActionButton) findViewById( R.id.material_design_floating_action_menu_item1 );
        deletePhoto = (com.github.clans.fab.FloatingActionButton) findViewById( R.id.material_design_floating_action_menu_item2 );
        editAlbum = (com.github.clans.fab.FloatingActionButton) findViewById( R.id.material_design_floating_action_menu_item3 );


      /*  if (fromShowcase.equalsIgnoreCase("0")) {
            iv_actionbtn.setVisibility(View.GONE);
            tv_title.setText(title);
            txt_galleryTitle.setVisibility(View.GONE);
            //materialDesignFAM.setVisibility(View.GONE);
        } else {

            iv_actionbtn.setVisibility(View.VISIBLE);
            String catid=PreferenceManager.getPreference(GalleryDescription.this,PreferenceManager.MY_CATEGORY);
            if(catid.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){
                view.setVisibility(View.VISIBLE);
                txt_galleryTitle.setText(title);
                if(shareType.equalsIgnoreCase("0")){
                    tv_title.setText("Club Service");

                    TextView lbl_cost=(TextView)findViewById(R.id.lbl_cost);
                    TextView lbl_ben=(TextView)findViewById(R.id.lbl_ben);
                    TextView lbl_man=(TextView)findViewById(R.id.lbl_man);

                    lbl_cost.setText("Attendance");
                    lbl_ben.setText("Attendance(%)");
                    lbl_man.setText("Meeting Type");

                    if (data.getAttendance().toString().isEmpty()) {
                        ll_cop.setVisibility(View.GONE);
                    }else {
                        ll_cop.setVisibility(View.VISIBLE);
                    }

                    if (data.getAttendancePer().toString().isEmpty()) {
                        ll_bene.setVisibility(View.GONE);
                    }else {
                        ll_bene.setVisibility(View.VISIBLE);
                    }

                    if (data.getMeetType().toString().isEmpty()) {
                        ll_timespent.setVisibility(View.GONE);
                    }else {
                        ll_timespent.setVisibility(View.VISIBLE);
                    }

                    if (data.getAgendaDocID().toString().isEmpty()) {
                        ll_agenda.setVisibility(View.GONE);
                    }else {
                        ll_agenda.setVisibility(View.VISIBLE);
                    }

                    if (data.getMomDocID().toString().isEmpty()) {
                        ll_mom.setVisibility(View.GONE);
                    }else {
                        ll_mom.setVisibility(View.VISIBLE);
                    }

                    attendance=data.getAttendance();

                    tv_cop.setText(data.getAttendance());
                    tv_beneficiary.setText(data.getAttendancePer());
                    meetType=data.getMeetType();

                    if(meetType!=null && !meetType.isEmpty()){
                        if(meetType.equalsIgnoreCase("0")){
                            tv_manPower.setText("Regular");

                        }else if(meetType.equalsIgnoreCase("1")){
                            tv_manPower.setText("BOD");
                        }else {
                            tv_manPower.setText("Assembly");
                        }
                    }

                }else {
                    tv_title.setText("Rotary Service");
                }
            }else {
                tv_title.setText(title);
                txt_galleryTitle.setVisibility(View.GONE);
            }


            //materialDesignFAM.setVisibility(View.VISIBLE);
        }*/


        addPhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        materialDesignFAM.close( false );

//                        Toast.makeText(GalleryDescription.this, "Maximum 5 images are allowed", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//
//
////                      Intent i = new Intent(GalleryDescription.this,AlbumFolderPage.class);
////                      startActivity(i);
                        if (gv.getCount() < 5) {
                            Intent i = new Intent( GalleryDescription.this, AddPhotoActivity.class );
                            i.putExtra( "albumId", albumId );
                            i.putExtra( "count", gv.getCount() );
                            startActivity( i );
                        } else {
                            Toast.makeText( GalleryDescription.this, "Maximum 5 photos are allowed.", Toast.LENGTH_SHORT ).show();
                        }
                    }
                }
            }

        } );


        Intent i = getIntent();

        if (i.hasExtra( "fromNoti" )) {
            fromNoti = i.getStringExtra( "fromNoti" );
            albumId = i.getStringExtra( "albumID" );
            //update Data into Database
            messageId_temp=i.getStringExtra("messageId");
            if (messageId_temp!=null){
                //Create Database Helper Class Object
                DatabaseHelper databaseHelpers = new DatabaseHelper( this );

                boolean notificationInsert = databaseHelpers.updateData( messageId_temp );
                Log.d("messageId_temp", "messageID ID ID AFTER :- " + messageId_temp);
                Log.d("messageId_temp", "Is Data Updated :- " + notificationInsert);


            }


        } else {
            title = i.getStringExtra( "albumname" );
            description = i.getStringExtra( "albumDescription" );
            albumId = i.getStringExtra( "albumId" );
            albumimage = i.getStringExtra( "albumImage" );
            try {


                fromShowcase = i.getExtras().getString( "fromShowcase" );


                data = (AlbumData) i.getSerializableExtra("albumData");


                //  setAlbumData(data);

                albumId = data.getAlbumId();
            }
            catch (Exception e)
            {
                albumId = i.getExtras().getString( "albumData" );
                e.printStackTrace();
            }

          //  albumId = "8";

            if (InternetConnection.checkConnection( this )) {
                //checkForUpdateForAlbums();
                loadData();
            } else {
                Toast.makeText( this, "No internet connection.Cannot fetch Updated Album Records", Toast.LENGTH_LONG ).show();
            }

            checkadminrights();


        }

        init();


        //loadFromDB();
//        if (InternetConnection.checkConnection(this)) {
//            webservices();
//            Log.d("---------------", "Check for update gets called------");
//        } else {
//            Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
//        }
    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN ) != null) {
            if (PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN ).equals( "No" )) {
                iv_actionbtn.setVisibility( View.GONE );
                //materialDesignFAM.setVisibility(View.GONE);
            } else {
                iv_actionbtn.setVisibility( View.VISIBLE );

            }
        } else {
            iv_actionbtn.setVisibility( View.GONE );
            //materialDesignFAM.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InternetConnection.checkConnection( this )) {
            if (fromNoti.equalsIgnoreCase( "1" )) {
                loadData();
            } else {
                webservices();
            }

            Log.d( "---------------", "Check for update gets called------" );
        } else {
            Toast.makeText( this, "No internet connection to get Updated Records", Toast.LENGTH_LONG ).show();
        }
    }


    public void init() {
        iv_actionbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupType = PreferenceManager.getPreference( GalleryDescription.this, PreferenceManager.MY_CATEGORY, "1" );
                if (groupType.equals( "" + Constant.GROUP_CATEGORY_DT )) {

                   // Toast.makeText(getApplicationContext(),"ccccc",Toast.LENGTH_LONG).show();
                    Intent i = new Intent( GalleryDescription.this, DTEditAlbumActivity.class );
                    i.putExtra( "albumId", albumId );
                    i.putExtra( "description", description );
                    i.putExtra( "albumImage", albumimage );
                    i.putExtra( "albumname", title );

                    if (shareType.equalsIgnoreCase( "0" )) {

                        i.putExtra( "header", "District Event" );

                    } else {
                        i.putExtra( "header", "District Project" );

                    }

                    startActivityForResult( i, EDITALBUM );
                    // finish();
                } else {

                  //  Toast.makeText(getApplicationContext(),"ssss",Toast.LENGTH_LONG).show();

                    if (InternetConnection.checkConnection( GalleryDescription.this )) {
                        Intent i = new Intent( GalleryDescription.this, EditAlbumActivity.class );
                        i.putExtra( "albumId", albumId );
                        i.putExtra( "description", description );
                        i.putExtra( "albumImage", albumimage );
                        i.putExtra( "albumname", title );
                        i.putExtra( "header", tv_title.getText().toString() );

                        startActivityForResult( i, EDITALBUM );
                        //  finish();
                    } else {
                        Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_LONG ).show();
                    }
                }
            }
        } );

        ivUpDown.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == STATE_UP) {
                    ivUpDown.setImageDrawable( getResources().getDrawable( R.drawable.g_down ) );
                    buttonState = STATE_DOWN;
                    int dimensionInDp = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics() );
                    ll_desc.getLayoutParams().height = dimensionInDp;
                    ll_desc.requestLayout();
//                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.75f );
//                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.25f );
//                    descriptionWrapper.setLayoutParams(descParams);
//                    gv.setLayoutParams(gvParams);
                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                } else {
                    ivUpDown.setImageDrawable( getResources().getDrawable( R.drawable.g_up ) );
                    buttonState = STATE_UP;
                    int dimensionInDp = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics() );
                    ll_desc.getLayoutParams().height = dimensionInDp;
                    ll_desc.requestLayout();
//                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.6f );
//                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.4f );
//                    descriptionWrapper.setLayoutParams(descParams);
//                    gv.setLayoutParams(gvParams);

                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                }
            }
        } );

        deletePhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 0;
                materialDesignFAM.close( false );
                materialDesignFAM.setVisibility( View.GONE );
                //adapter = new AlbumPhotoAdapter(GalleryDescription.this, albumPhotolist, "0");
                adapter = new AlbumPhotoAdapter( GalleryDescription.this, newAlbums, "0" );
                gv.setAdapter( adapter );
            }
        } );

        editAlbum.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDesignFAM.close( false );

                String groupType = PreferenceManager.getPreference( GalleryDescription.this, PreferenceManager.MY_CATEGORY, "1" );
                if (groupType.equals( "" + Constant.GROUP_CATEGORY_DT )) {
                    Intent i = new Intent( GalleryDescription.this, DTEditAlbumActivity.class );
                    i.putExtra( "albumId", albumId );
                    i.putExtra( "description", description );
                    i.putExtra( "albumImage", albumimage );
                    i.putExtra( "albumname", title );
                    i.putExtra( "header", tv_title.getText().toString() );
                    startActivityForResult( i, EDITALBUM );
                    finish();
                } else {
                    if (InternetConnection.checkConnection( GalleryDescription.this )) {
                        Intent i = new Intent( GalleryDescription.this, EditAlbumActivity.class );
                        i.putExtra( "albumId", albumId );
                        i.putExtra( "description", description );
                        i.putExtra( "albumImage", albumimage );
                        i.putExtra( "albumname", title );//title change with tv_tite
                        i.putExtra( "header", tv_title.getText().toString() );

                        startActivityForResult( i, EDITALBUM );
                        finish();
                    } else {
                        Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_LONG ).show();
                    }
                }
            }
        } );


        iv_download.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modeAttachment = MODE_DOWNLOAD;

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission( GalleryDescription.this );
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if (!InternetConnection.checkConnection( GalleryDescription.this )) {
                    Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (data.getAgendaDocID().equals( "" )) {
                    Toast.makeText( GalleryDescription.this, "Download Link not found.", Toast.LENGTH_SHORT ).show();
                    return;
                }

                progress = ProgressDialog.show( GalleryDescription.this, "Downloading",
                        "File Downloading", true );

                link = data.getAgendaDocID();
                titleDoc = "Agenda";
                Uri u = Uri.parse( link );
                File f = new File( "" + u );
                String fileName = f.getName();
                storageFileName = fileName;

                filePath = f.getPath();
                if (fileExists( fileName )) {
                    progress.dismiss();
                    Toast.makeText( GalleryDescription.this, "File is already downloaded", Toast.LENGTH_LONG ).show();
                    String filenameArray[] = fileName.split( "\\." );
                    String extension = filenameArray[filenameArray.length - 1];

                    File sdcard = Environment.getExternalStorageDirectory();
                    File myfile = new File( sdcard.getPath() + "/Touchbase", fileName );
                    Log.d( "***********", "-----" + extension );
                    String pdf = "pdf";
//                    if (extension.equals(pdf)) {
//                        Intent i = new Intent(GalleryDescription.this, PDFViewActivity.class);
//
//                        i.putExtra("fileName", myfile.getPath());
//                        i.putExtra("mode", "VIEW");
//                        i.putExtra("filePath", filePath);
//                        Log.e("TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode);
//                        GalleryDescription.this.startActivity(i);
//
//
//                    }

                    if (extension.equals( pdf )) {
                        Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                        i.putExtra( "title", titleDoc );
                        i.putExtra( "fileName", fileName );
                        i.putExtra( "mode", "VIEW" );
                        i.putExtra( "filePath", filePath );
                        i.putExtra( "ext", "pdf" );
                        i.putExtra( "mom_gaurav", "1" );
                        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                        GalleryDescription.this.startActivity( i );
                    } else if (extension.equalsIgnoreCase( "doc" ) || extension.equalsIgnoreCase( "docx" )) {
                        Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                        i.putExtra( "title", titleDoc );
                        i.putExtra( "fileName", fileName );
                        i.putExtra( "mode", "VIEW" );
                        i.putExtra( "filePath", link );
                        i.putExtra( "ext", "doc" );
                        i.putExtra( "mom_gaurav", "1" );
                        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                        GalleryDescription.this.startActivity( i );
                    }
                } else {
                    filePath = f.getPath();
                    Log.d( "-----FILE NAME---------", "-----Downloaded-----" + fileName );

                    File myDirectory = new File( Environment.DIRECTORY_DOWNLOADS );
                    String path = myDirectory.getAbsolutePath();
                    Log.e( "===Directory PaTH===", "=====" + path );

                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs();
                    }

                    downloadmanager = (DownloadManager) GalleryDescription.this.getSystemService( Context.DOWNLOAD_SERVICE );
                    Uri uri = Uri.parse( link );
                    DownloadManager.Request request = new DownloadManager.Request( uri );
                    enqueId = downloadmanager.enqueue(
                            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE )
                                    .setAllowedOverRoaming( false ).setTitle( fileName )
                                    .setDescription( "File Downloading" )
                                    .setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName ) );
                    Log.e( "TouchBase", "♦♦♦♦Enque ID : " + enqueId );
                }
            }
        } );

        iv_download_mom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modeAttachment = MODE_DOWNLOAD;

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission( GalleryDescription.this );
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if (!InternetConnection.checkConnection( GalleryDescription.this )) {
                    Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (data.getMomDocID().equals( "" )) {
                    Toast.makeText( GalleryDescription.this, "Download Link not found.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                progress = ProgressDialog.show( GalleryDescription.this, "Downloading",
                        "File Downloading", true );

                titleDoc = "Minutes of Meeting";
                link = data.getMomDocID();
                Uri u = Uri.parse( link );
                File f = new File( "" + u );
                String fileName = f.getName();
                storageFileName = fileName;

                filePath = f.getPath();
                if (fileExists( fileName )) {
                    progress.dismiss();
                    Toast.makeText( GalleryDescription.this, "File is already downloaded", Toast.LENGTH_LONG ).show();
                    String filenameArray[] = fileName.split( "\\." );
                    String extension = filenameArray[filenameArray.length - 1];

/*
                    File sdcard = Environment.getExternalStorageDirectory();
                    File myfile = new File( sdcard.getPath() + "/Touchbase", fileName );
*/
                    Log.d( "***********", "-----" + extension );
                    String pdf = "pdf";
//                    if (extension.equals(pdf)) {
//                        Intent i = new Intent(GalleryDescription.this, PDFViewActivity.class);
//
//                        i.putExtra("fileName", myfile.getPath());
//                        i.putExtra("mode", "VIEW");
//                        i.putExtra("filePath", filePath);
//                        Log.e("TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode);
//                        GalleryDescription.this.startActivity(i);
//                    }

                    if (extension.equals( pdf )) {
                        Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                        i.putExtra( "title", titleDoc );
                        i.putExtra( "fileName", fileName );
                        i.putExtra( "mode", "VIEW" );
                        i.putExtra( "filePath", filePath );
                        i.putExtra( "ext", "pdf" );
                        Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                        i.putExtra( "mom_gaurav", "1" );
                        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        GalleryDescription.this.startActivity( i );
                        // finishAffinity();
                    } else if (extension.equalsIgnoreCase( "doc" ) || extension.equalsIgnoreCase( "docx" )) {
                        Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                        i.putExtra( "title", titleDoc );
                        i.putExtra( "fileName", fileName );
                        i.putExtra( "mode", "VIEW" );
                        i.putExtra( "filePath", link );
                        i.putExtra( "ext", "doc" );
                        i.putExtra( "mom_gaurav", "1" );
                        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                        GalleryDescription.this.startActivity( i );
                        // finishAffinity();

                    }
                } else {
                    filePath = f.getPath();
                    Log.d( "-----FILE NAME---------", "-----Downloaded-----" + fileName );

                    File myDirectory = new File( Environment.DIRECTORY_DOWNLOADS );
                    String path = myDirectory.getAbsolutePath();
                    Log.e( "===Directory PaTH===", "=====" + path );

                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs();
                    }

                    downloadmanager = (DownloadManager) GalleryDescription.this.getSystemService( Context.DOWNLOAD_SERVICE );
                    Uri uri = Uri.parse( link );
                    DownloadManager.Request request = new DownloadManager.Request( uri );
                    enqueId = downloadmanager.enqueue(
                            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE )
                                    .setAllowedOverRoaming( false ).setTitle( fileName )
                                    .setDescription( "File Downloading" )
                                    .setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName ) );
                    Log.e( "TouchBase", "♦♦♦♦Enque ID : " + enqueId );
                }
            }
        } );


        iv_view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modeAttachment = MODE_VIEW;

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission( GalleryDescription.this );
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if (!InternetConnection.checkConnection( GalleryDescription.this )) {
                    Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (data.getAgendaDocID().equals( "" )) {
                    Toast.makeText( GalleryDescription.this, "Download Link not found.", Toast.LENGTH_SHORT ).show();
                    return;
                }


                progress = ProgressDialog.show( GalleryDescription.this, "Loading", "Loading file", true );

                titleDoc = "Agenda";
                link = data.getAgendaDocID();
                Uri u = Uri.parse( link );
                File f = new File( "" + u );
                String fileName = f.getName();
                storageFileName = fileName;

                filePath = f.getPath();
                Log.d( "-----FILE NAME---------", "-----Downloaded-----" + fileName );

                File myDirectory = new File( Environment.DIRECTORY_DOWNLOADS );
                String path = myDirectory.getAbsolutePath();
                Log.e( "===Directory PaTH===", "=====" + path );

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                downloadmanager = (DownloadManager) GalleryDescription.this.getSystemService( Context.DOWNLOAD_SERVICE );
                Uri uri = Uri.parse( link );
                // As file is taped for open purpose only, saving it in app directory. So that it will not be visible in file explorer of the system
                File appFolder = GalleryDescription.this.getFilesDir();
                File downloadFile = new File( appFolder, fileName );
                DownloadManager.Request request = new DownloadManager.Request( uri );
                enqueId = downloadmanager.enqueue(
                        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE )
                                .setAllowedOverRoaming( false ).setTitle( fileName )
                                .setDescription( "File Downloading" )
                                //.setDestinationUri(Uri.fromFile(downloadFile)));
                                .setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName ) );
                Log.e( "TouchBase", "♦♦♦♦Enque ID : " + enqueId );
            }
        } );

        iv_view_mom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modeAttachment = MODE_VIEW;

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission( GalleryDescription.this );
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if (!InternetConnection.checkConnection( GalleryDescription.this )) {
                    Toast.makeText( GalleryDescription.this, "No internet connection", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (data.getMomDocID().equals( "" )) {
                    Toast.makeText( GalleryDescription.this, "Download Link not found.", Toast.LENGTH_SHORT ).show();
                    return;
                }


                progress = ProgressDialog.show( GalleryDescription.this, "Loading", "Loading file", true );
                titleDoc = "Minutes of Meeting";
                link = data.getMomDocID();
                Uri u = Uri.parse( link );
                File f = new File( "" + u );
                String fileName = f.getName();
                storageFileName = fileName;

                filePath = f.getPath();
                Log.d( "-----FILE NAME---------", "-----Downloaded-----" + fileName );

                File myDirectory = new File( Environment.DIRECTORY_DOWNLOADS );
                String path = myDirectory.getAbsolutePath();
                Log.e( "===Directory PaTH===", "=====" + path );

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                downloadmanager = (DownloadManager) GalleryDescription.this.getSystemService( Context.DOWNLOAD_SERVICE );
                Uri uri = Uri.parse( link );
                // As file is taped for open purpose only, saving it in app directory. So that it will not be visible in file explorer of the system
                File appFolder = GalleryDescription.this.getFilesDir();
                File downloadFile = new File( appFolder, fileName );
                DownloadManager.Request request = new DownloadManager.Request( uri );
                enqueId = downloadmanager.enqueue(
                        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE )
                                .setAllowedOverRoaming( false ).setTitle( fileName )
                                .setDescription( "File Downloading" )
                                //.setDestinationUri(Uri.fromFile(downloadFile)));
                                .setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName ) );
                Log.e( "TouchBase", "♦♦♦♦Enque ID : " + enqueId );
            }
        } );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );


        if (requestCode == SELECT_PICTURE) {

            if (resultCode == Activity.RESULT_OK) {

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt( i );
                        Uri uri = item.getUri();
                        String path = uri.getPath();
                        //In case you need image's absolute path
                        // String path = getRealPathFromURI(GalleryDescription.this, uri);

                        if (selectedImageList.size() <= 5) {
                            selectedImageList.add( uri.toString() );
                        } else {

                        }
                    }
                }
            }
        } else if (requestCode == EDITALBUM) {
            if (resultCode == Activity.RESULT_OK) {

                if (InternetConnection.checkConnection( this )) {
                    //checkForUpdateForAlbums();
                    loadData();
                } else {
                    Toast.makeText( this, "No internet connection.Cannot fetch Updated Album Records", Toast.LENGTH_LONG ).show();
                }
            }
        }
    }

    public class FileDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (progress != null) progress.dismiss();

            String action = intent.getAction();


            if (modeAttachment.equals( MODE_DOWNLOAD )) {
                Toast.makeText( context, "File downloaded successfully", Toast.LENGTH_LONG ).show();
                Log.e( "Touchbase", "♦♦♦♦Mode : " + mode );
            }

            Log.e( "TouchBase", "♦♦♦♦Inside receiver" );
            if (action.equals( DownloadManager.ACTION_DOWNLOAD_COMPLETE )) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById( enqueId );
                Cursor cur = downloadmanager.query( query );
                //DownloadManager.COLUMN_LOCAL_FILENAME
                if (cur.moveToNext()) {
                    int status = cur.getInt( cur.getColumnIndex( DownloadManager.COLUMN_STATUS ) );
                    //Log.e("Touchbase", "♦♦♦♦URI : "+cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    String fileName = cur.getString( cur.getColumnIndex( DownloadManager.COLUMN_LOCAL_URI ) );

                    fileName = fileName.replace( "file://", "" );

                    if (fileName != null) {
                        fileName = fileName.replace( "file://", "" );

                    } else {
                        Log.e( "===URL===", "=====" + "The resource you are looking for has been removed, had its name changed, or is temporarily unavailable." );


                    }


//                    int colCount = cur.getColumnCount();
//                    for(int i=0;i<colCount;i++) {
//                        String columnName = cur.getColumnName(i);
//                        String value = cur.getString(i);
//                        Log.e("Touchbase", "♥♥♥♥Column Name : " + columnName+" - "+value);
//                    }
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //Log.i("FLAG", "done");
                        //Toast.makeText(mContext, "Download SUCCESSFULL!", Toast.LENGTH_SHORT).show();

                        String filenameArray[] = fileName.split( "\\." );
                        String extension = filenameArray[filenameArray.length - 1];

                        String pdf = "pdf";
                        if (extension.equals( pdf )) {

                            Log.d( "-----FILE NAME---------", "-----Call one time-----" );

                            Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                            i.putExtra( "title", titleDoc );
                            i.putExtra( "fileName", fileName );
                            i.putExtra( "mode", "VIEW" );
                            i.putExtra( "filePath", filePath );
                            i.putExtra( "ext", "pdf" );
                            Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                            i.putExtra( "mom_gaurav", "1" );
                            i.putExtra( "storageFileName", storageFileName );

                            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

                            GalleryDescription.this.startActivity( i );
                            // GalleryDescription.this.finish();


                        } else if (extension.equalsIgnoreCase( "doc" ) || extension.equalsIgnoreCase( "docx" )) {
                            Log.d( "-----FILE NAME---------", "-----Call second time-----" );

                            Intent i = new Intent( GalleryDescription.this, PDFViewActivity.class );
                            i.putExtra( "title", titleDoc );
                            i.putExtra( "fileName", fileName );
                            i.putExtra( "mode", "VIEW" );
                            i.putExtra( "filePath", link );
                            i.putExtra( "ext", "doc" );
                            Log.e( "TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode );
                            i.putExtra( "mom_gaurav", "1" );
                            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

                            GalleryDescription.this.startActivity( i );


                        }
                    } else {
                        Toast.makeText( context, "Failed to download file", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Log.e( "Touchbase", "♦♦♦♦No records found for download" );
                    // Toast.makeText(context, "Failed to download", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText( context, "Unable to download file", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    public boolean fileExists(String fileName) {
        String sdcard = Environment.DIRECTORY_DOWNLOADS;
        File myfile = new File( sdcard, fileName );
        return myfile.exists();
    }

//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                //if(c!= null&& c.getCount()>0) {
//                    c.moveToFirst();
//                    do {
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        if(selectedImageList.size()<=5) {
//                            selectedImageList.add(picturePath);
//                        }else{
//
//                        }
//                    }while (c.moveToNext());
    // }
    // else{
    //   Toast.makeText(GalleryDescription.this,"Unable to get Image and filepath of selected Image"+ c.getCount(),Toast.LENGTH_SHORT).show();
    // }


//        Intent i = new Intent(GalleryDescription.this, AddPhoto.class);
//        i.putStringArrayListExtra("SelectedPhotoList", selectedImageList);
//        startActivity(i);
//
//    }

//    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri, proj, null,
//                    null, null);
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String str = cursor.getString(column_index);
//            return cursor.getString(column_index);
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }


    public void loadFromDB() {

        Log.d( "Touchbase", "Trying to load Photos inside album from local db" );
        albumPhotolist = albumPhotosMasterModel.getAlbumsPhoto( albumId );

        boolean isDataAvailable = albumPhotosMasterModel.isDataAvailable( albumId );

        Log.e( "DataAvailable", "Data available : " + isDataAvailable );

        if (!isDataAvailable) {
            Log.d( "Touchbase---@@@@@@@@", "Loading from server" );
            if (InternetConnection.checkConnection( this ))
                webservices();
            else
                Toast.makeText( this, "No internet connection", Toast.LENGTH_LONG ).show();
        } else {
            adapter = new AlbumPhotoAdapter( this, albumPhotolist, "1" );
            gv.setAdapter( adapter );

            if (InternetConnection.checkConnection( this )) {
                checkForUpdate();
                Log.d( "---------------", "Check for update gets called------" );
            } else {
                Toast.makeText( this, "No internet connection to get Updated Records", Toast.LENGTH_LONG ).show();
            }
            gv.setOnItemClickListener( this );
        }
    }

    public void checkForUpdate() {
        isinUpdatemode = true;
        Log.e( "Touchbase", "------ checkForUpdate() called for update" );
        String url = Constant.GetAlbumPhotoList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "groupId", data.getGrpId() ) );
        arrayList.add( new BasicNameValuePair( "Financeyear", Gallery.year ) );



//        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId,"1970/01/01 00:00:00");
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
//        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
//
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);;
        Log.e( "request", arrayList.toString() );

        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask( url, arrayList, this );
        task.execute();
        Log.d( "Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + arrayList.toString() );
    }


    public void webservices() {

       // AlbumData data = new AlbumData();

        Log.e( "Touchbase", "------ webservices() called for 1st time" );
        //String url = Constant.GetAlbumPhotoList;
        String url = Constant.GetAlbumPhotoList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();


        String grpid1 = data.getGrpId();
        if(grpid1 == null)
        {

            Intent i = getIntent();
            if(i == null)
            {

            }
            else
            {
                try {
                    grpid1 = i.getExtras().getString( "grpid" );
                }
                catch (Exception e)
                {

                }

            }


        }
        else if(grpid1.equalsIgnoreCase("") || grpid1.isEmpty())
        {
            Intent i = getIntent();
            if(i == null)
            {

            }
            else
            {
                try {
                    grpid1 = i.getExtras().getString( "grpid" );
                }
                catch (Exception e)
                {

                }

            }


        }




        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "groupId",  grpid1) );//data.getGrpId()
        arrayList.add( new BasicNameValuePair( "Financeyear",  Gallery.year) );


//        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId, "1970/01/01 00:00:00");
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
//        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);


        Log.e( "request", arrayList.toString() );

        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask( url, arrayList, this );
        task.execute();
        Log.d( "Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + arrayList.toString() );
    }


    public class GalleryPhotosDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( GalleryDescription.this, R.style.TBProgressBar );
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GalleryPhotosDataAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            if (!isinUpdatemode) {
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData( url, argList );
                val = val.toString();
                Log.d( "Responsephoto", "we"  + val +"request" +argList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            isinUpdatemode = false;
            if (result != "" && result != null) {
                Log.d( "Response", "calling getAllAlbumList" );

                getGalleryPhotosData( result.toString() );

            } else {
                Log.d( "Response", "Null Resposnse" );
            }

        }
    }

    //===================== Get Data ========================================

    public void getGalleryPhotosData(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );

            JSONObject jsonTBAlbumPhotoListResult = jsonObj.getJSONObject( "TBAlbumPhotoListResult" );
            final String status = jsonTBAlbumPhotoListResult.getString( "status" );

            if (status.equals( "0" )) {

                // updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");
                newAlbums = new ArrayList<AlbumPhotoData>();

                //JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonTBAlbumPhotoListResult.getJSONArray( "Result" );

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();
                newAlbums.clear();


                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject( i );

                    data.setPhotoId( result_object.getString( "photoId" ).toString() );

                    data.setDescription( result_object.getString( "description" ).toString() );
                    data.setGrpId( String.valueOf( grpId ) );
                    data.setAlbumId( String.valueOf( albumId ) );

//                    if (result_object.has("url")) {
//                        data.setUrl(result_object.getString("url").toString());
//                    } else {
//                        data.setUrl("");
//                    }

                    if (!result_object.getString( "url" ).isEmpty()) {
                        data.setUrl( result_object.getString( "url" ).toString() );
                        newAlbums.add( data );
                    }

                    //   newAlbums.add(data);
                }
                if (newAlbums.size() == 0) {
                    photo_title_layout.setVisibility( View.GONE );
                } else {
                    photo_title_layout.setVisibility( View.VISIBLE );

                }

                adapter = new AlbumPhotoAdapter( GalleryDescription.this, newAlbums, "1" );
                gv.setAdapter( adapter );
                gv.setOnItemClickListener( GalleryDescription.this );


//                final ArrayList<AlbumPhotoData> UpdatedAlbumPhototList = new ArrayList<AlbumPhotoData>();
//                JSONArray jsonUpdatedAlbumPhotoList = jsonResult.getJSONArray("updatedPhotos");
//
//
//                int updateAlbumPhotoListCount = jsonUpdatedAlbumPhotoList.length();
//
//                for (int i = 0; i < updateAlbumPhotoListCount; i++) {
//
//                    AlbumPhotoData data = new AlbumPhotoData();
//
//                    JSONObject result_object = jsonUpdatedAlbumPhotoList.getJSONObject(i);
//
//                    data.setPhotoId(result_object.getString("photoId").toString());
//
//                    data.setDescription(result_object.getString("description").toString());
//                    data.setGrpId(String.valueOf(grpId));
//                    data.setAlbumId(String.valueOf(albumId));
//                    if (result_object.has("url")) {
//                        data.setUrl(result_object.getString("url").toString());
//                    } else {
//                        data.setUrl("");
//                    }
//                    UpdatedAlbumPhototList.add(data);
//
//                }
//
//                final ArrayList<AlbumPhotoData> DeletedAlbumPhotoList = new ArrayList<AlbumPhotoData>();
//                String jsonDeletedAlbumPhotoList = jsonResult.getString("deletedPhotos");
//                int deleteAlbumPhotoListCount = 0;
//                if(!jsonDeletedAlbumPhotoList.equalsIgnoreCase("")){
//
//                    String[]deletedAlbumArray = jsonDeletedAlbumPhotoList.split(",");
//                    deleteAlbumPhotoListCount = deletedAlbumArray.length;
//
//                    for (int i = 0; i < deleteAlbumPhotoListCount; i++) {
//                        AlbumPhotoData data = new AlbumPhotoData();
//                        data.setAlbumId(String.valueOf(albumId));
//                        data.setPhotoId(String.valueOf(deletedAlbumArray[i].toString()));
//                        DeletedAlbumPhotoList.add(data);
//
//                    }
//                }

//                Handler AlbumPhotohandler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//
//                        boolean saved = albumPhotosMasterModel.syncData(grpId,albumId, newAlbums, UpdatedAlbumPhototList, DeletedAlbumPhotoList);
//                        if (!saved) {
//                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
//                            sendEmptyMessageDelayed(0, 2000);
//                        } else {
//                            PreferenceManager.savePreference(GalleryDescription.this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId, updatedOn);
//
//                           albumPhotolist = new ArrayList<>();
//                            albumPhotolist = albumPhotosMasterModel.getAlbumsPhoto(albumId);
//                            adapter = new AlbumPhotoAdapter(GalleryDescription.this, albumPhotolist, "1");
//                            gv.setAdapter(adapter);
//                            gv.setOnItemClickListener(GalleryDescription.this);
//                        }
//                    }
//                };


//                int overAllCount = newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount;
//
//                System.out.println("Number of records received for photos inside albums  : " + overAllCount);
//                if (newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount != 0) {
//
//                    AlbumPhotohandler.sendEmptyMessageDelayed(0, 1000);
//                } else {
//                    Log.e("NoUpdate", "No updates found");
//                }
            }
        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mode != 0) {
            Intent intent = new Intent( GalleryDescription.this, ImageDetailActivity.class );
            intent.putParcelableArrayListExtra( "photos", newAlbums );
            intent.putExtra( "photoid", "" + id );
            intent.putExtra( "position", position );
            intent.putExtra( "albumName", title );
            intent.putExtra( "albumId", albumId );
            intent.putExtra( "imgUrl", newAlbums.get( position ).getUrl() );
            intent.putExtra( "fromMain", "yes" );
            intent.putExtra( "fromShowcase", fromShowcase );
            startActivity( intent );
        }
    }

    @Override
    public void onBackPressed() {

        adapter = new AlbumPhotoAdapter( GalleryDescription.this, newAlbums, "1" );

        if (materialDesignFAM.isOpened()) {
            materialDesignFAM.close( false );
        } else if (mode == 0 && adapter.getIsdelete().equalsIgnoreCase( "true" )) {
            //checkForUpdate();
            webservices();

        } else if (mode == 0) {
            adapter.notifyDataSetChanged();
            gv.setAdapter( adapter );
            mode = 1;
            materialDesignFAM.setVisibility( View.VISIBLE );
        } else {
            super.onBackPressed();
        }
    }

    public void checkForUpdateForAlbums() {
        Log.e( "Touchbase", "------ checkForUpdateForAlbums() called" );
        String url = Constant.GetAlbumsList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "profileId", PreferenceManager.getPreference( this, PreferenceManager.GRP_PROFILE_ID ) ) );
        arrayList.add( new BasicNameValuePair( "groupId", PreferenceManager.getPreference( this, PreferenceManager.GROUP_ID ) ) );
        arrayList.add( new BasicNameValuePair( "moduleId", PreferenceManager.getPreference( this, PreferenceManager.MODULE_ID ) ) );
        updatedOn = PreferenceManager.getPreference( this, TBPrefixes.GALLERY_PREFIX + moduleId + "_" + grpId );
        Log.e( "UpdatedOn", "Last updated date is : " + updatedOn );
        arrayList.add( new BasicNameValuePair( "updatedOn", updatedOn ) );//updatedOn 1970-1-1 0:0:0
        Log.e( "UpdatedOn", "Last updated date is : " + updatedOn );
        ;
        Log.e( "request", arrayList.toString() );
        GetUpdatedAlbumRecordAsyncTask task = new GetUpdatedAlbumRecordAsyncTask( url, arrayList, this );
        task.execute();
        Log.d( "Response", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + arrayList.toString() );
    }

    public class GetUpdatedAlbumRecordAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( GalleryDescription.this, R.style.TBProgressBar );
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GetUpdatedAlbumRecordAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            if (!isinUpdatemode) {
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData( url, argList );
                val = val.toString();
                Log.d( "Response", "we" + val );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (result != "") {
                Log.d( "Response", "calling getAllAlbumList" );

                getAlbumData( result.toString() );

            } else {
                Log.d( "Response", "Null Resposnse" );
            }

        }
    }


    public void getAlbumData(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject( "TBAlbumsListResult" );
            final String status = jsonTBAlbumsListResult.getString( "status" );

            if (status.equals( "0" )) {

                updatedOn = jsonTBAlbumsListResult.getString( "updatedOn" );
                final ArrayList<AlbumData> newAlbums = new ArrayList<AlbumData>();

                JSONObject jsonResult = jsonTBAlbumsListResult.getJSONObject( "Result" );

                JSONArray jsonNewAlbumList = jsonResult.getJSONArray( "newAlbums" );

                int newAlbumListCount = jsonNewAlbumList.length();

                for (int i = 0; i < newAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonNewAlbumList.getJSONObject( i );

                    data.setAlbumId( result_object.getString( "albumId" ).toString() );
                    data.setTitle( result_object.getString( "title" ).toString() );
                    data.setDescription( result_object.getString( "description" ).toString() );
                    data.setGrpId( result_object.getString( "groupId" ).toString() );
                    data.setIsAdmin( result_object.getString( "isAdmin" ).toString() );
                    data.setModuleId( result_object.getString( "moduleId" ).toString() );

                    if (result_object.has( "image" )) {
                        data.setImage( result_object.getString( "image" ).toString() );
                    } else {
                        data.setImage( "" );
                    }

                    newAlbums.add( data );

                }

                final ArrayList<AlbumData> UpdatedAlbumList = new ArrayList<AlbumData>();
                JSONArray jsonUpdatedAlbumList = jsonResult.getJSONArray( "updatedAlbums" );

                int updateAlbumListCount = jsonUpdatedAlbumList.length();

                for (int i = 0; i < updateAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonUpdatedAlbumList.getJSONObject( i );

                    data.setAlbumId( result_object.getString( "albumId" ).toString() );
                    data.setTitle( result_object.getString( "title" ).toString() );
                    data.setDescription( result_object.getString( "description" ).toString() );
                    data.setGrpId( result_object.getString( "groupId" ).toString() );
                    data.setIsAdmin( result_object.getString( "isAdmin" ).toString() );
                    data.setModuleId( result_object.getString( "moduleId" ).toString() );

                    if (result_object.has( "image" )) {
                        data.setImage( result_object.getString( "image" ).toString() );
                    } else {
                        data.setImage( "" );
                    }
                    tv_title.setText( result_object.getString( "title" ).toString() );
                    tv_description.setText( result_object.getString( "description" ).toString() );
                    UpdatedAlbumList.add( data );

                }

                final ArrayList<AlbumData> DeletedAlbumList = new ArrayList<AlbumData>();
                String jsonDeletedAlbumList = jsonResult.getString( "deletedAlbums" );
                int deleteAlbumListCount = 0;
                if (!jsonDeletedAlbumList.equalsIgnoreCase( "" )) {

                    String[] deletedAlbumArray = jsonDeletedAlbumList.split( "," );
                    deleteAlbumListCount = deletedAlbumArray.length;

                    for (int i = 0; i < deleteAlbumListCount; i++) {
                        AlbumData data = new AlbumData();
                        data.setAlbumId( String.valueOf( deletedAlbumArray[i].toString() ) );
                        DeletedAlbumList.add( data );

                    }

                }

                Handler Albumdatahandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage( msg );
                        boolean saved = albumModel.syncData( grpId, newAlbums, UpdatedAlbumList, DeletedAlbumList );
                        if (!saved) {
                            Log.e( "SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds" );
                            sendEmptyMessageDelayed( 0, 2000 );
                        } else {
                            // PreferenceManager.savePreference(GalleryDescription.this, TBPrefixes.GALLERY_PREFIX+moduleId+"_"+grpId, updatedOn);
                        }
                    }
                };

                int overAllCount = newAlbumListCount + updateAlbumListCount + deleteAlbumListCount;

                System.out.println( "Number of records received for albums  : " + overAllCount );
                if (newAlbumListCount + updateAlbumListCount + deleteAlbumListCount != 0) {

                    Albumdatahandler.sendEmptyMessageDelayed( 0, 1000 );
                } else {
                    Log.e( "NoUpdate", "No updates found" );
                }
            }
        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
            e.printStackTrace();
        }

    }

    public void loadData() {

        Log.e( "Touchbase", "------ loadData() called" );
        String url = Constant.GetAlbumDetails_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "Financeyear", Gallery.year ) );

        Log.d( "Album Data", "PARAMETERS " + Constant.GetAlbumDetails_New + " :- " + arrayList.toString() );
        GetAlbumDetailsAsynctask task = new GetAlbumDetailsAsynctask( url, arrayList, this );
        task.execute();

    }

    public class GetAlbumDetailsAsynctask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( context, R.style.TBProgressBar );

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
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData( url, argList );
                val = val.toString();
                Log.d( "Response", "we" + val );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != "") {
                Log.d( "Response", "calling GetAlbumDetails" );

                getAlbumDetails( result.toString() );

                Log.d( "GetAlbumDetails", result.toString() );
            } else {
                Log.d( "Response", "Null Resposnse" );
                Toast.makeText( context, "Something went wrong", Toast.LENGTH_SHORT ).show();
            }

        }
    }

    public void getAlbumDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject( "TBAlbumDetailResult" );
            final String status = jsonTBAlbumsListResult.getString( "status" );

            if (status.equals( "0" )) {

                JSONArray jsonNewAlbumList = jsonTBAlbumsListResult.getJSONArray( "AlbumDetailResult" );

                if (jsonNewAlbumList.length() < 1) {
                    return;
                }

                JSONObject object = jsonNewAlbumList.getJSONObject( 0 );
                JSONObject albumObj = object.getJSONObject( "AlbumDetail" );

                data.setAlbumId( albumObj.getString( "albumId" ) );
                data.setTitle( albumObj.getString( "albumTitle" ) );
                data.setDescription( albumObj.getString( "albumDescription" ) );
                data.setImage( albumObj.getString( "albumImage" ) );
                data.setGrpId( albumObj.getString( "groupId" ) );
//                data.setModuleId(albumObj.getString("moduleId"));
                //  data.setIsAdmin(albumObj.getString("isAdmin"));
                data.setClub_Name( albumObj.getString( "clubname" ) );
                data.setProject_date( albumObj.getString( "project_date" ) );
                data.setProject_cost( albumObj.getString( "project_cost" ) );
                data.setBeneficiary( albumObj.getString( "beneficiary" ) );
                data.setWorking_hour( albumObj.getString( "working_hour" ) );
                data.setWorking_hour_type( albumObj.getString( "working_hour_type" ) );
                data.setCost_of_project_type( albumObj.getString( "cost_of_project_type" ) );
                data.setNoOfRotarians( albumObj.getString( "NumberOfRotarian" ) );
                data.setShareType( albumObj.getString( "shareType" ) );
                data.setAttendance( albumObj.getString( "Attendance" ) );
                data.setAttendancePer( albumObj.getString( "AttendancePer" ) );
                data.setMeetType( albumObj.getString( "MeetingType" ) );
                data.setAgendaDocID( albumObj.getString( "AgendaDoc" ) );
                data.setMomDocID( albumObj.getString( "MOMDoc" ) );
                //Add New Field
                data.setRotractors( albumObj.getString( "Rotaractors" ) );

                Ismedia = albumObj.getString( "Ismedia" );
                mediaPhotoPath = albumObj.getString( "Mediaphoto" );

                media_discription = albumObj.getString( "MediaDesc" ).toString();


                setAlbumData( data );


                if (fromNoti.equalsIgnoreCase( "1" )) {
                    webservices();
                    iv_actionbtn.setVisibility( View.GONE );
                }


            }

        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
            Toast.makeText( context, "Something went wrong", Toast.LENGTH_SHORT ).show();
            e.printStackTrace();
        }
    }

    private void setAlbumData(AlbumData data) {
        title = data.getTitle().toString();
        description = data.getDescription().toString();
        albumId = data.getAlbumId();
        albumimage = data.getImage();
        shareType = data.getShareType();
        // data.getRotractors();

        if (data.getProject_date().toString().isEmpty()) {
            ll_dop.setVisibility( View.GONE );
        }

//        if (data.getShareType().equalsIgnoreCase("0")) {
//            ll_details.setVisibility(View.GONE);
//        } else {
//            ll_details.setVisibility(View.VISIBLE);
//        }

        String date = data.getProject_date().toString();
        if (date != null && !date.isEmpty()) {
            try {
                Date copDate = sdf.parse( date );
                tv_dop.setText( sdf1.format( copDate ) );
            } catch (ParseException e) {
                tv_dop.setText( data.getProject_date().toString() );
                e.printStackTrace();
            }
        }


        if (data.getCost_of_project_type().equalsIgnoreCase( "1" )) {
            currencyType = "\u20B9";
        } else if (data.getCost_of_project_type().equalsIgnoreCase( "2" )) {
            currencyType = "$";
        } else {
            currencyType = "";
        }
        if (data.getProject_cost().toString().isEmpty()) {
            ll_cop.setVisibility( View.GONE );
        } else {
            ll_cop.setVisibility( View.VISIBLE );
        }

        if (data.getBeneficiary().toString().isEmpty()) {
            ll_bene.setVisibility( View.GONE );
        } else {
            ll_bene.setVisibility( View.VISIBLE );
        }
        if (data.getWorking_hour().toString().isEmpty()) {
            ll_timespent.setVisibility( View.GONE );
        } else {
            ll_timespent.setVisibility( View.VISIBLE );
        }

        if (data.getNoOfRotarians().toString().isEmpty()) {
            ll_noOfRotarians.setVisibility( View.GONE );
        } else {
            ll_noOfRotarians.setVisibility( View.VISIBLE );
        }


//        tv_cop.setText(data.getProject_cost().toString());
        tv_cop.setText( data.getProject_cost().toString() + " " + currencyType );

        tv_beneficiary.setText( data.getBeneficiary().toString() );


        tv_manPower.setText( data.getWorking_hour().toString() + " " + data.getWorking_hour_type() );

        tv_noOfRotarians.setText( data.getNoOfRotarians().toString() );
        //  tv_rotractors.setText(data.getRotractors().toString());
        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));


        Log.e( "Description", description );


        tv_description.setText( description );

        if (fromShowcase.equalsIgnoreCase( "0" )) {
            // iv_actionbtn.setVisibility(View.GONE);
            tv_title.setText( title );
            txt_galleryTitle.setVisibility( View.GONE );
            //materialDesignFAM.setVisibility(View.GONE);
        } else {

            //  iv_actionbtn.setVisibility(View.VISIBLE);
            String catid = PreferenceManager.getPreference( GalleryDescription.this, PreferenceManager.MY_CATEGORY );

            TextView lbl_cost = (TextView) findViewById( R.id.lbl_cost );
            TextView lbl_ben = (TextView) findViewById( R.id.lbl_ben );
            TextView lbl_man = (TextView) findViewById( R.id.lbl_man );
            if (catid.equalsIgnoreCase( String.valueOf( Constant.GROUP_CATEGORY_CLUB ) )) {
                view.setVisibility( View.VISIBLE );
                txt_galleryTitle.setText( title );

                if (shareType.equalsIgnoreCase( "0" )) {


                    //Club Meeting
                    tv_title.setText( "Club Meeting" );
                    lbl_cost.setText( "Attendance" );
                    lbl_ben.setText( "Attendance(%)" );
                    lbl_man.setText( "Meeting Type" );

                    if (data.getAttendance().toString().isEmpty()) {
                        ll_cop.setVisibility( View.GONE );
                        ll_bene.setVisibility( View.GONE );

                    } else {
                        ll_cop.setVisibility( View.VISIBLE );
                        ll_bene.setVisibility( View.VISIBLE );

                    }

                   /* if (data.getAttendancePer().toString().isEmpty()) {
                        ll_bene.setVisibility(View.GONE);
                    } else {
                        ll_bene.setVisibility(View.VISIBLE);
                    }*/

                    if (data.getMeetType().toString().isEmpty()) {
                        ll_timespent.setVisibility( View.GONE );
                    } else {
                        ll_timespent.setVisibility( View.VISIBLE );
                    }

                    if (data.getAgendaDocID().toString().isEmpty() || data.getAgendaDocID().equalsIgnoreCase( "0" )) {
                        ll_agenda.setVisibility( View.GONE );
                    } else {
                        ll_agenda.setVisibility( View.VISIBLE );
                    }

                    if (data.getMomDocID().toString().isEmpty() || data.getMomDocID().equalsIgnoreCase( "0" )) {
                        ll_mom.setVisibility( View.GONE );
                    } else {
                        ll_mom.setVisibility( View.VISIBLE );
                    }

                    attendance = data.getAttendance();

                    tv_cop.setText( data.getAttendance() );
                    tv_beneficiary.setText( data.getAttendancePer() );
                    meetType = data.getMeetType();

                    if (meetType != null && !meetType.isEmpty()) {
                        if (meetType.equalsIgnoreCase( "0" )) {
                            tv_manPower.setText( "Regular" );

                        } else if (meetType.equalsIgnoreCase( "1" )) {
                            tv_manPower.setText( "BOD" );
                        } else if (meetType.equalsIgnoreCase( "2" )) {
                            tv_manPower.setText( "Assembly" );
                        } else {
                            tv_manPower.setText( "Fellowship" );
                        }
                    }

                } else {

                    //Service Project
                    tv_title.setText( "Service Project" );
                    lbl_cost.setText( "Cost" );
                    lbl_ben.setText( "Direct Beneficiaries" );
                    lbl_man.setText( "Man hours" );

                    ll_agenda.setVisibility( View.GONE );
                    ll_mom.setVisibility( View.GONE );

                    if (data.getRotractors().toString().isEmpty()) {
                        ll_rotractors.setVisibility( View.GONE );
                    } else {
                        ll_rotractors.setVisibility( View.VISIBLE );
                        tv_rotractors.setText( data.getRotractors().toString() );


                    }


                    if (Ismedia.equals( "1" )) {
                        //Means Media Photo Uploaded
                        //media path is a URL for Image

                        if (mediaPhotoPath.equals( "" )) {
                            //Image is deleted
                        } else {
                            media_photo_layout.setVisibility( View.VISIBLE );

                            Picasso.with( GalleryDescription.this ).load( mediaPhotoPath )
                                    //.fit()
                                    //.resize(200, 200)
                                    .placeholder( R.drawable.placeholder_new )
                                    .into( iv_printmedia );
                            iv_printmedia.setBackground( null );


                        }

                    } else {
                        media_photo_layout.setVisibility( View.GONE );
                    }


                }
            } else {

                //District Module
                //   tv_title.setText(title);
                txt_galleryTitle.setVisibility( View.GONE );

                if (shareType.equalsIgnoreCase( "0" )) {
                    //District Event Data Displayed Add By Gaurav
                    //  tv_title.setText("District Event");
                    tv_title.setText( title );

                    ll_cop.setVisibility( View.GONE );
                    ll_timespent.setVisibility( View.GONE );
                } else if (shareType.equalsIgnoreCase( "1" )) {
                    //District Project Data Displayed Add By Gaurav
                    tv_title.setText( title );
                    // tv_title.setText("District Project");
                    ll_cop.setVisibility( View.VISIBLE );
                    ll_timespent.setVisibility( View.VISIBLE );

                    if (data.getRotractors().toString().isEmpty()) {
                        ll_rotractors.setVisibility( View.GONE );
                    } else {
                        ll_rotractors.setVisibility( View.VISIBLE );
                        tv_rotractors.setText( data.getRotractors().toString() );


                    }


                    if (Ismedia.equals( "1" )) {
                        //Means Media Photo Uploaded
                        //media path is a URL for Image
                        if (mediaPhotoPath.equals( "" )) {
                            //Image is deleted
                        } else {
                            media_photo_layout.setVisibility( View.VISIBLE );

                            Picasso.with( GalleryDescription.this ).load( mediaPhotoPath )
                                    //.fit()
                                    //.resize(200, 200)
                                    .placeholder( R.drawable.placeholder_new )
                                    .into( iv_printmedia );
                            iv_printmedia.setBackground( null );


                        }

                    } else {
                        media_photo_layout.setVisibility( View.GONE );
                    }


                }
            }
        }
    }


}
