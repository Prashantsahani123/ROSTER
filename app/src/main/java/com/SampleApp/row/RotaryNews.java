package com.SampleApp.row;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Adapter.FragmentALLAdapter_new;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Data.NewsFeed;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.GroupMasterModel;
import com.SampleApp.row.sql.ModuleDataModel;
import com.SampleApp.row.sql.RSSFeedsModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RotaryNews extends AppCompatActivity {

    RecyclerView rv_Blog;
    TextView tv_title;
    ImageView iv_backbutton;
    public FragmentALLAdapter_new rv_adapter;
    private RSSFeedsModel feedModel;
    ArrayList<NewsFeed> feedList;
    ArrayList<Object> grplist = new ArrayList<>();
    private long masterUid;
    GroupMasterModel groupModel;
    private String onresume_flag = "0";
    String updatedOn = "";
    ArrayList<ModuleData> moduleList = new ArrayList<ModuleData>();
    ModuleDataModel moduleDataModel;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_blog);

        //masterUid = Long.parseLong(PreferenceManager.getPreference(this.getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by pp
        masterUid = Long.parseLong("157542"); // line added by pp

        groupModel = new GroupMasterModel(this.getApplicationContext()); // line by prasad
        moduleDataModel = new ModuleDataModel(this.getApplicationContext());
        dialog = new ProgressDialog(RotaryNews.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setCanceledOnTouchOutside(false);

        rv_Blog = (RecyclerView) findViewById(R.id.rv_Blog);
        rv_Blog.setLayoutManager(new LinearLayoutManager(this));
        rv_adapter = new FragmentALLAdapter_new(RotaryNews.this, grplist, "1");
        rv_Blog.setAdapter(rv_adapter);


        actionbarfunction();
        init();
       loadRssFeeds();
    }

    public void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Rotary News");
    }

    private void init() {
        feedList = new ArrayList<>();
        feedModel = new RSSFeedsModel(this);
    }

    public void loadRssFeeds() {
        new AsyncTask<Void, Void, String>() {
            boolean isFound = false;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    isFound = feedModel.isFeedAvailable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (!isFound) {
                        return "failed";
                    }
                    return "success";
                } catch (Exception e) {
                    Utils.log("RSS Feeds File are not present in local database");
                    return "failed";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("failed")) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        dialog.show();
                        new LoadRssTask().execute();
                    }
                } else {
                    feedList = new ArrayList<>();
                    feedList = feedModel.getNewsFeedList();
                    grplist.add("Rotary News & Updates");
                    grplist.addAll(feedList);
                    rv_adapter.notifyDataSetChanged();
                    checkForFeedsUpdate();
                }
            }
        }.execute();

    }

    public class LoadRssTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://my.rotary.org/en/rss.xml");
                //URL url = new URL("http://rosteronwheels.com/resources/rss.xml");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                //HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setConnectTimeout(60000);
                InputStream in = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                in.close();

                return new String(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utils.log("http://rosteronwheels.com/resources/rss.xml");
            Log.e("RSS", "doInBackground: " + s);
            saveRssFeeds(s);

        }
    }

    public void saveRssFeeds(String s) {
        try {
            /*FileOutputStream fout = this.getContext().openFileOutput(RSS_FEEDS_FILE, MODE_PRIVATE);
            fout.write(s.getBytes());
            fout.close();*/
            feedList = parseFeeds(s);

            Handler RssFeedHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    boolean saved = feedModel.syncData(feedList);
                    if (!saved) {
                        Log.e("SyncFailed------->", "Failed to update data in local db for RSS Feeds. Retrying in 2 seconds");
                        sendEmptyMessageDelayed(0, 2000);
                    } else {
                        grplist.clear();
                        grplist.addAll(groupModel.getGroups(masterUid));
                        feedList = new ArrayList<>();
                        feedList = feedModel.getNewsFeedList();
                        grplist.add("Rotary News & Updates");
                        grplist.addAll(feedList);
                        rv_adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        //checkForBlogsUpdate();
                    }
                }
            };

            if (feedList.size() > 0) {
                RssFeedHandler.sendEmptyMessageDelayed(0, 1000);
                dialog.dismiss();
            } else {

            }
        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
            dialog.dismiss();
        }

    }

    NewsFeed feed = new NewsFeed();

    public ArrayList<NewsFeed> parseFeeds(String feedData) {
        ArrayList<NewsFeed> feedList = new ArrayList<>();
        try {
            InputStream in = new ByteArrayInputStream(feedData.getBytes());
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(in, null);
            String text = "";

            int eventType = parser.getEventType();
            int ctr = 1;
            endParsing:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("item")) {
                            feed = new NewsFeed();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equals("item")) {
                            feedList.add(feed);
                            ctr++;
                            if (ctr > 10) {
                                break endParsing;
                            }
                        } else if (tagName.equals("title")) {
                            feed.setTitle(text);
                        } else if (tagName.equals("link")) {
                            feed.setLink(text);
                        } else if (tagName.equals("pubDate")) {
                            feed.setPubDate(text);
                        } else if (tagName.equals("description")) {
                            feed.setDescription(text);
                        }
                        break;
                }

                eventType = parser.next();
            }

            if (feedList.size() != 0) {
                //grplist.add("Rotary News & Updates");
                return feedList;
                /*grplist.addAll(feedList);
                rv_adapter.notifyDataSetChanged();*/
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return feedList;
    }

    public void checkForFeedsUpdate() {
        if (InternetConnection.checkConnection(this)) {
            new LoadRssTask().execute();
        }
    }

}
