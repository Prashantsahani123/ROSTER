package com.SampleApp.row;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.SlidingImage_Adapter;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

//import com.viewpagerindicator.CirclePageIndicator;

public class SplashImageSlider extends AppCompatActivity {
    private static ViewPager mPager;

    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.screen1,R.drawable.screen2,R.drawable.screen3,R.drawable.screen4,R.drawable.screen5};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    TextView tv_term;
    private boolean exit = false;
    private SlidingImage_Adapter sliderAdapter;
    private Handler sliderHandler;
    private int currentPage = 0;
    CirclePageIndicator indicator;
    private TextView tv_exit,tv_accept,tv_margin;
    private View view_terms_and_condition,view_margin;
    WebView webview;
    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash_image_slider);

        tv_term = (TextView)findViewById(R.id.tv_terms);
        tv_exit = (TextView)findViewById(R.id.tv_exit);
        tv_accept = (TextView)findViewById(R.id.tv_accept);
        view_margin = (View) findViewById(R.id.margin);
        view_terms_and_condition = (View)findViewById(R.id.view_terms_and_condition);


        sliderHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if ( currentPage < IMAGES.length ) {
                    mPager.setCurrentItem(currentPage);
                    currentPage++;
                    sendEmptyMessageDelayed(0, 6000);
                }
            }
        };

        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID) == null) {


        } else {  // means user is already logged in to the app
            Intent intent = getIntent();
            if ( intent != null && intent.getStringExtra("fromApp") == null ) {  // fromApp = null means splashimageslider is launched by clicking on invitation link
                Uri data = intent.getData();
                String groupId = data.getQueryParameter("groupId");
                String masterUID = data.getQueryParameter("masterUID");
                String isGroupAdmin = data.getQueryParameter("isAdmin");
                String groupName = data.getQueryParameter("groupName");

                String sessionMasterId = PreferenceManager.getPreference(SplashImageSlider.this, PreferenceManager.MASTER_USER_ID);
                boolean otherUserTryingToLogin = false;
                if ( sessionMasterId == null ){

                } else if ( ! sessionMasterId.equals(masterUID)){
                    otherUserTryingToLogin = true;
                }

                if ( otherUserTryingToLogin ) {
                    Utils.simpleMessage(SplashImageSlider.this, "Other user is already logged in on this device. Please try on some other device", "Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                } else {
                    PreferenceManager.savePreference(SplashImageSlider.this, PreferenceManager.MASTER_USER_ID, masterUID);
                    PreferenceManager.savePreference(SplashImageSlider.this, PreferenceManager.GROUP_ID, groupId);
                    PreferenceManager.savePreference(SplashImageSlider.this, PreferenceManager.IS_GRP_ADMIN, isGroupAdmin);
                    PreferenceManager.savePreference(SplashImageSlider.this, PreferenceManager.GROUP_NAME, groupName);
                    Intent i = new Intent(SplashImageSlider.this, GroupsListingDashboard.class);
                    i.putExtra("openGroup", "yes");  // this means first show entity listing and then launch dashboard of that entity to which user is invited
                    startActivity(i);
                    finish();
                }

            } else {
                Intent i = new Intent(getApplicationContext(), GroupsListingDashboard.class);
                startActivity(i);
                finish();
            }


        }

        init();
    }



    private void init() {
        view_margin.setVisibility(View.GONE);
        view_terms_and_condition.setVisibility(View.GONE);

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SplashImageSlider.this,LoginPage_Row.class);
                startActivity(i);
                finish();
            }
        });

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);

        sliderAdapter = new SlidingImage_Adapter(SplashImageSlider.this, ImagesArray);
        mPager.setAdapter(sliderAdapter);

        //mPager.setClipChildren(false);

        indicator = (CirclePageIndicator)
        findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);


        NUM_PAGES =IMAGES.length;



        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
       /* Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        */
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (currentPage == 4) {
                    tv_term.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.GONE);
                    tv_exit.setVisibility(View.VISIBLE);
                    tv_accept.setVisibility(View.VISIBLE);
                    view_margin.setVisibility(View.VISIBLE);
                    view_terms_and_condition.setVisibility(View.VISIBLE);

                    tv_term.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           popup();
                        }
                    });
                } else {
                    tv_term.setVisibility(View.INVISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    tv_exit.setVisibility(View.GONE);
                    tv_accept.setVisibility(View.GONE);
                    view_margin.setVisibility(View.GONE);
                    view_terms_and_condition.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        sliderHandler.sendEmptyMessage(0);
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void popup(){
        final Dialog dialog = new Dialog(SplashImageSlider.this,android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_terms_and_conditions);
        Button close = (Button)dialog.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        webview = (WebView)dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadData(readFilefromPath(R.raw.termscondition),"text/html","utf-8");
        dialog.show();
    }

    private String readFilefromPath(int id){

        InputStream raw = getResources().openRawResource(id);
        ByteArrayOutputStream stream   = new ByteArrayOutputStream();
        int i;
        try{
            i = raw.read();
            while (i!=-1){
                stream.write(i);
                i = raw.read();
            }raw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return stream.toString();
    }
}
