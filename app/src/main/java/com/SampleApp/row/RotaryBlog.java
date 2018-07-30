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
import com.SampleApp.row.Data.BlogFeed;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.GroupMasterModel;
import com.SampleApp.row.sql.ModuleDataModel;
import com.SampleApp.row.sql.RotaryBlogsModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RotaryBlog extends AppCompatActivity {

    RecyclerView rv_Blog;
    TextView tv_title;
    ImageView iv_backbutton;
    public FragmentALLAdapter_new rv_adapter;
    private RotaryBlogsModel blogsModel;
    ArrayList<BlogFeed> blogList;
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
        dialog = new ProgressDialog(RotaryBlog.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setCanceledOnTouchOutside(false);

        rv_Blog = (RecyclerView) findViewById(R.id.rv_Blog);
        rv_Blog.setLayoutManager(new LinearLayoutManager(this));
        rv_adapter = new FragmentALLAdapter_new(RotaryBlog.this, grplist, "1");
        rv_Blog.setAdapter(rv_adapter);
        blogsModel = new RotaryBlogsModel(getApplicationContext());

        actionbarfunction();
        init();
        loadRssBlogs();
    }

    public void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Rotary Blog");
    }

    private void init() {
        blogList = new ArrayList<>();
    }

    public void loadRssBlogs() {
        new AsyncTask<Void, Void, String>() {
            FileInputStream fin;
            ArrayList<BlogFeed> list = new ArrayList<BlogFeed>();
            boolean isFound = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    isFound = blogsModel.isBlogAvailable();
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
                    Utils.log("RSS Blogs File are not present in local database");
                    return "failed";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("failed")) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        dialog.show();
                        new LoadBlogTask().execute();
                    }
                } else {
                    blogList = new ArrayList<>();
                    blogList = blogsModel.getBlogsList();
                    grplist.add("Rotary Blogs");
                    grplist.addAll(blogList);
                    rv_adapter.notifyDataSetChanged();
                }
            }
        }.execute();

        /*try {
            FileInputStream fin = this.getContext().openFileInput(RSS_BLOGS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            parseBlogs(fieldData);
        }catch (FileNotFoundException fne){
            Utils.log("RSS Blogs File are not present in local file");
            if (InternetConnection.checkConnection(this.getContext())) {
                new LoadBlogTask().execute();
            }else{
                Toast.makeText(this.getContext(), "No internet conenction", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Utils.log("RSS Blogs File are not present in local file");
            if (InternetConnection.checkConnection(this.getContext())) {
                new LoadBlogTask().execute();
            }
        }*/
    }
    public class LoadBlogTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://blog.rotary.org/feed/");
                //URL url = new URL("http://rosteronwheels.com/resources/feed.xml");

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
            Log.e("RSS", "doInBackground: " + s);
            saveRssBlogs(s);

        }
    }
    public void saveRssBlogs(String s) {
        try {

            blogList = parseBlogs(s);

            Handler RssBlogsHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    boolean saved = blogsModel.syncData(blogList);
                    if (!saved) {
                        Log.e("SyncFailed------->", "Failed to update data in local db for RSS Feeds. Retrying in 2 seconds");
                        sendEmptyMessageDelayed(0, 2000);
                    } else {
                        blogList = new ArrayList<>();
                        blogList = blogsModel.getBlogsList();
                        grplist.add("Rotary Blogs");

                        grplist.addAll(blogList);
                        rv_adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            };

            if (blogList.size() > 0) {
                RssBlogsHandler.sendEmptyMessageDelayed(0, 1000);
                dialog.dismiss();
            } else {

            }

        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
            dialog.dismiss();
        }

    }

    BlogFeed feedB = new BlogFeed();
    public ArrayList<BlogFeed> parseBlogs(String feedData) {
        ArrayList<BlogFeed> feedList = new ArrayList<>();
        try {
            /*feedData = feedData.replaceAll("<media:title>", "<media:title1>");
            feedData = feedData.replaceAll("</media:title>", "</media:title1>");
            */
            InputStream in = new ByteArrayInputStream(feedData.getBytes());
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(in, null);
            String text = "";

            int eventType = parser.getEventType();
            int ctr = 1;
            String prevTag = "";
            endParsing:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("item")) {
                            feedB = new BlogFeed();
                            //Utils.log("Tag name : "+tagName);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();

                        break;

                    case XmlPullParser.END_TAG:
                        prevTag = tagName;
                        if (tagName.equals("item")) {
                            feedList.add(feedB);
                            ctr++;
                            if (ctr > 10) {
                                break endParsing;
                            }
                        } else if (tagName.equals("title")) {
                            if (prevTag.equals("item")) {  // if prevTag is "item" then only its actual <title> otherwise its <media:title>
                                feedB.setTitle(text);
                            }
                        } else if (tagName.equals("link")) {
                            feedB.setLink(text);
                        } else if (tagName.equals("pubDate")) {
                            feedB.setPubDate(text);
                        } else if (tagName.equals("description")) {
                            Utils.log("Description : " + text);
                            /*if ( text.contains("<img")) {
                                int index = text.indexOf(">");
                                if ( index != -1 ) {
                                    String image = text.substring(text.indexOf("http"), index-2);
                                    Utils.log("Image Path is : "+image);
                                    text = text.substring(index+1);
                                }
                            }*/
                            feedB.setDescription(text);
                        }
                        break;
                }
                eventType = parser.next();
            }

            return feedList;
            /*if (feedList.size() != 0) {
                grplist.add("Rotary Blogs");
                grplist.addAll(feedList);
                rv_adapter.notifyDataSetChanged();
            }*/

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return feedList;
    }
}
