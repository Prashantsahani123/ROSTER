package com.NEWROW.row;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.FamilywiseRVAdapter;
import com.NEWROW.row.Data.FamilywiseMemberData;
import com.NEWROW.row.Data.profiledata.ProfileMasterData;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.sql.ProfileModel;

import java.util.ArrayList;


/**
 * Created by USER on 17-12-2015.
 */
public class FamilywiseDirectoryActivity extends Activity {
    private static final int DELETE_PROFILE_REQUEST = 100;
    private static final boolean NO_ANIMATE = false, ANIMATE = true;
    private static final int ADD_MANUALY_REQUEST = 1;
    private static final int PHONE_BOOK_LIST = 101;
    private String classification = "";
    private String title;
    private ProfileModel profileModel;
    Context context;
    EditText et_serach_directory;
    String updatedOn = "";
    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private long masterUid, grpId;
    //ImageView ivSearch;
    FamilywiseRVAdapter adapter;
    ArrayList<FamilywiseMemberData> list;
    private LinearLayout llCenterMessage;
    private TextView tvCenterButton;
    private TextView tvCenterMessage;
    private ProgressBar progressBar;

    String filter[] = {"Rotarian","Classification", "Family"};
    Spinner sp_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_directory);

        context = this;
        profileModel = new ProfileModel(context);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
        classification = getIntent().getStringExtra("classification");
        Utils.log("GroupId : "+grpId);
        initComponents();
        init();
        checkadminrights();
        refreshData("");
        // Code to set filter in Directory
        //setFilter();

    }

    public void initComponents() {
        //ivSearch = (ImageView) findViewById(R.id.btnSearch);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageDrawable(getResources().getDrawable(R.drawable.search_btn));

        tvCenterMessage = (TextView) findViewById(R.id.tv_no_records_found);
        llCenterMessage = (LinearLayout) findViewById(R.id.llCenterMessage);
        tvCenterButton = (TextView) findViewById(R.id.tvRetry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        title = getIntent().getExtras().getString("moduleName", "Directory");
        tv_title.setText(title);

        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);

        rvDirectory = (RecyclerView) findViewById(R.id.rvDirectory);

        list = new ArrayList<>();
        adapter = new FamilywiseRVAdapter(context, list);
        rvDirectory.setLayoutManager(new LinearLayoutManager(context));
        rvDirectory.setAdapter(adapter);
        rvDirectory.setVisibility(View.VISIBLE);
        sp_filter = (Spinner) findViewById(R.id.sp_filter);
        sp_filter.setSelection(2);
    }

    /*public void manageCenterMessage(int visibility, String message, int msgVisibility, String btnLabel, int btnVisibility, View.OnClickListener onClickListener) {
        llCenterMessage.setVisibility(visibility);
        tvCenterMessage.setText(message);
        tvCenterMessage.setVisibility(msgVisibility);
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener!=null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }
    }*/

    public void manageCenterMessage(int visibility, final String message, int msgVisibility, String btnLabel, int btnVisibility, View.OnClickListener onClickListener, boolean animateMessage) {
        llCenterMessage.setVisibility(visibility);
        tvCenterMessage.setText(message);
        tvCenterMessage.setVisibility(msgVisibility);

        if ( animateMessage ) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener!=null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }
    }
    private void checkadminrights() {
        String isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        if ( isAdmin.equals("No")) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadFromDB();
    }

    public void init() {

        et_serach_directory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_serach_directory.selectAll();
                }
            }
        });


        /*ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = et_serach_directory.getText().toString();
                try {
                    hideInput();



                } catch(Exception e) {
                    Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });*/


        et_serach_directory.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String q = et_serach_directory.getText().toString();

                if ( q.equals("")){
                    refreshData("");
                } else {
                    list = profileModel.getFamilyWiseMemberSerach(grpId, q);
                    Utils.log("MyList Size : "+list.size());
                    adapter = new FamilywiseRVAdapter(context, list);
                    rvDirectory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnMemberSelectedListener(memberSelectedListener);
                    rvDirectory.setVisibility(View.VISIBLE);

                    if ( list.size() == 0 ) {
                        manageCenterMessage(View.VISIBLE, "No results",  View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
                    } else {
                        manageCenterMessage(View.GONE, "",  View.GONE, "", View.GONE, null, NO_ANIMATE);
                    }
                }
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


        et_serach_directory.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return true;
                }
                return false;
            }
        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdvancedSearchActivity.class);
                intent.putExtra("groupId", ""+grpId);
                startActivity(intent);
            }
        });
        adapter.setOnMemberSelectedListener(memberSelectedListener);
        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Code for filter selection
    }

    FamilywiseRVAdapter.OnMemberSelectedListener memberSelectedListener = new FamilywiseRVAdapter.OnMemberSelectedListener() {

        @Override
        public void onMemberSelected(FamilywiseMemberData data, int position) {

           try {

               /* Intent intent = new Intent(context, FamilyProfileActivity.class);
                intent.putExtra("memberProfileId",""+data.getMemberId());
                intent.putExtra("groupId", ""+grpId);
                intent.putExtra("fromMainDirectory", "no");
                startActivityForResult(intent, DELETE_PROFILE_REQUEST);*/

               // done by satish on 27-05-2019 issue reported by tester 'Filter by Family ->  Display all the fields same as in directory' sheet 'Row bug sheet 01-04-2019'
               Intent intent = new Intent(context, NewProfileActivity.class);
               intent.putExtra("memberProfileId",""+data.getMemberId());
               intent.putExtra("groupId", ""+grpId);
               intent.putExtra("fromMainDirectory", "yes");
               startActivityForResult(intent, DELETE_PROFILE_REQUEST);

            } catch(Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }
    };

    public void finishActivity(View v) {
        finish();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TOUCHBASE", "REQUEST CODE" + resultCode);
        // check if the request code is same as what is passed  here it is 1
        if (requestCode == ADD_MANUALY_REQUEST) {
            if (InternetConnection.checkConnection(getApplicationContext())) {

            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
            // webservices();
        } else if (requestCode == PHONE_BOOK_LIST && resultCode ==RESULT_OK) {
            if (data != null) {

            }
        } else if ( resultCode == RESULT_OK && requestCode == DELETE_PROFILE_REQUEST ) {

        }
    }

    //-----------------------Offline Database ----------------------
    private void copyTextToClipBoard(String chatMessage) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(chatMessage);
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Link", chatMessage);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_LONG).show();
    }


    //-----------------------All new declarations -----------
    RecyclerView rvDirectory;

    ArrayList<ProfileMasterData> profileList = new ArrayList<>();

    public void refreshData(String msg) {

        list = profileModel.getFamilywiseMember(grpId);

        if ( list.size() > 0 ) {
            Utils.log("MyList Size : " + list.size());
            adapter = new FamilywiseRVAdapter(context, list);
            rvDirectory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnMemberSelectedListener(memberSelectedListener);
            rvDirectory.setVisibility(View.VISIBLE);
            Utils.log("Loaded from local db");
            manageCenterMessage(View.GONE, "", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        } else {
            manageCenterMessage(View.VISIBLE, "No new updates", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        }
    }
    public void changeAdapter(AdapterView<?> parent, View view, int position, long id) {
        String filter = sp_filter.getSelectedItem().toString();

        if ( filter.equalsIgnoreCase("Classification") ) {
            Intent intent = new Intent(context, ClassificationActivity.class);
            intent.putExtra("moduleName", "Directory");
            startActivity(intent);
            finish();
        } else if ( filter.equalsIgnoreCase("Rotarian")) {
            Intent i = new Intent(context, DirectoryActivity.class);
            i.putExtra("moduleName",title);
            context.startActivity(i);
            finish();
        } else if ( filter.equalsIgnoreCase("Family")) {

        }
    }
    boolean isFieldPresent = false;
    private static final String DYNAMIC_FIELDS_FILE = "dynamicField.json";
}