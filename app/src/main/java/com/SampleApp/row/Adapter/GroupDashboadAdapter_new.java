package com.SampleApp.row.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Announcement;
import com.SampleApp.row.AttendenceActivity;
import com.SampleApp.row.BoardOfDirectorsActivity;
import com.SampleApp.row.CalenderActivity;
import com.SampleApp.row.ClubHistoryActivity;
import com.SampleApp.row.DTDirectoryActivity;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.DirectoryActivity;
import com.SampleApp.row.DistrictClubActivity;
import com.SampleApp.row.DistrictCommittee;
import com.SampleApp.row.Documents;
import com.SampleApp.row.E_Bulletin;
import com.SampleApp.row.Events;
import com.SampleApp.row.ExternalLinkActivity;
import com.SampleApp.row.FeedbackActivity;
import com.SampleApp.row.FindAClubActivity;
import com.SampleApp.row.FindRotatrianActivity;
import com.SampleApp.row.Gallery;
import com.SampleApp.row.GroupDashboard;
import com.SampleApp.row.GroupInfo_New;
import com.SampleApp.row.LeaderBoardActivity;
import com.SampleApp.row.MonthlyReportListActivity;
import com.SampleApp.row.MyApplication;
import com.SampleApp.row.PastPresidentActivity;
import com.SampleApp.row.R;
import com.SampleApp.row.RotaryLibraryActivity;
import com.SampleApp.row.ServiceCattegoryList;
import com.SampleApp.row.SubGroupList;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.WebLinkActivity;
import com.SampleApp.row.sql.ReplicaInfoModel;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.SampleApp.row.FragmentALL.notificationCountDatas;
import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;


/**
 * Created by user on 01-02-2016.
 */
public class GroupDashboadAdapter_new extends BaseAdapter {

    public static final ArrayList<String> groupIds = new ArrayList<String>();
    public static final ArrayList<String> serviceDirectoryIds = new ArrayList<String>();
    public static final ArrayList<String> attendanceIds = new ArrayList<String>();

    final long groupId = 0;

    static {
        groupIds.add(Constant.Module.INFO);     // Info
        groupIds.add(Constant.Module.FAQ);     // FAQ
        groupIds.add(Constant.Module.PROGRAM_SCHEDULE);     // Program Schedule
        serviceDirectoryIds.add(Constant.Module.SERVICE_DIRECTORY);
        serviceDirectoryIds.add(Constant.Module.VVIP);
        attendanceIds.add(Constant.Module.ATTENDANCE);
    }

    public static final String IMPROVEMENT = "18";
    Context context;
    ArrayList<ModuleData> listmodule = new ArrayList<>();
    private static LayoutInflater inflater = null;
    public static int count_read_module = 0;
    GroupDashboard groupDashboard;
    int dashPosition;
    String grpId;
    int cnt = 0, cnt_eve = 0, cnt_ann = 0, cnt_bulletin = 0, cnt_doc = 0;
    String finalcount_event, finalcount_announcement, finalcount_document;
    long masterUid;

    public GroupDashboadAdapter_new(GroupDashboard cntx, ArrayList<ModuleData> list, String grpId) {
        // TODO Auto-generated constructor stub

        listmodule = list;
        context = cntx;
        groupDashboard = cntx;
        this.grpId = grpId;

        Log.e("Touchbase", "satish GroupDashboardAdapter New Group ID : " + this.grpId);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listmodule.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
        TextView group_count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder = new Holder();

        View rowView;

        ReplicaInfoModel model = new ReplicaInfoModel(context);

        final String replicaOf = model.getReplicaOf(listmodule.get(position).getModuleId());

        rowView = inflater.inflate(R.layout.groupdashboardgrid_item, null);

        holder.tv = (TextView) rowView.findViewById(R.id.text);
        holder.img = (ImageView) rowView.findViewById(R.id.picture);
        holder.group_count = (TextView) rowView.findViewById(R.id.group_count);

        holder.tv.setText(listmodule.get(position).getModuleName());

        //------------------ Count

        try {

            if (replicaOf.equals(Constant.Module.EVENTS)) {

                if (notificationCountDatas.getModuleCount(this.grpId, listmodule.get(position).getModuleId()) <= 0) {
                    holder.group_count.setVisibility(View.GONE);
                } else {
                    int count_read_event = EventListAdapter.count_read_events;
                    int total_event = notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId());
                    finalcount_event = String.valueOf(total_event - count_read_event);

                    holder.group_count.setVisibility(View.VISIBLE);

                    holder.group_count.setText(finalcount_event);
                    cnt_eve = Integer.parseInt(holder.group_count.getText().toString());
                    Log.d("======", "........" + cnt_eve);
                    holder.group_count.setText("" + notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId()));
                    holder.group_count.setBackgroundResource(R.drawable.notification_count_event);
                }
            }

            if (replicaOf.equals(Constant.Module.ANNOUNCEMENTS)) {

                if (notificationCountDatas.getModuleCount(this.grpId, listmodule.get(position).getModuleId()) <= 0) {
                    holder.group_count.setVisibility(View.GONE);
                } else {
                    int count_read_announcement = AnnouncementListAdapter.count_read_announcements;
                    int total_announcement = notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId());
                    finalcount_announcement = String.valueOf(total_announcement - count_read_announcement);

                    holder.group_count.setVisibility(View.VISIBLE);
                    holder.group_count.setText(finalcount_announcement);
                    cnt_ann = Integer.parseInt(holder.group_count.getText().toString());

                    holder.group_count.setText("" + notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId()));
                    holder.group_count.setBackgroundResource(R.drawable.notification_count_announcement);
                }
            }

            if (replicaOf.equals(Constant.Module.E_BULLETINS)) {
                if (notificationCountDatas.getModuleCount(this.grpId, listmodule.get(position).getModuleId()) <= 0) {
                    holder.group_count.setVisibility(View.GONE);
                } else {

                    int count_read_ebulletine = E_BulletineAdapter.count_read_ebulletines;
                    int total_ebulletine = notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId());
                    finalcount_announcement = String.valueOf(total_ebulletine - count_read_ebulletine);

                    holder.group_count.setVisibility(View.VISIBLE);
                    holder.group_count.setText("" + notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId()));
                    holder.group_count.setBackgroundResource(R.drawable.notification_count_ebulletine);
                }
            }

            if (replicaOf.equals(Constant.Module.DOCUMENTS)) {

                if (notificationCountDatas.getModuleCount(this.grpId, listmodule.get(position).getModuleId()) <= 0) {
                    holder.group_count.setVisibility(View.GONE);
                } else {

                    int count_read_document = DocumentAdapter.count_read_documents;
                    int total_document = notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId());
                    finalcount_document = String.valueOf(total_document - count_read_document);

                    holder.group_count.setText("" + notificationCountDatas.getModuleCount(grpId, listmodule.get(position).getModuleId()));

                    holder.group_count.setBackgroundResource(R.drawable.notification_count_document);
                }
            }

        } catch (Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
            e.printStackTrace();
        }

        cnt = cnt_ann + cnt_bulletin + cnt_eve + cnt_doc;

        count_read_module = cnt;

        Log.d("....", "//////" + count_read_module);

        //holder.img.setImageResource(imageId[position]);

        //   Log.d("TOUCHBASE", "@@@@@@" + context.getResources().getDisplayMetrics().density);

        if (listmodule.get(position).getImage().equals("") || listmodule.get(position).getImage() == null || listmodule.get(position).getImage().isEmpty()) {

        } else {

            if ((context.getResources().getDisplayMetrics().density) <= 2.0) {

                Picasso.with(context).load(listmodule.get(position).getImage())
                        .placeholder(R.drawable.dashboardplaceholder)
                        //.resize(150,150)
                        .into(holder.img);
            } else {
                Picasso.with(context).load(listmodule.get(position).getImage())
                        .placeholder(R.drawable.dashboardplaceholder)
                        .resize(150, 150)
                        .into(holder.img);
            }
        }

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Toast.makeText(context, "You Clicked " , Toast.LENGTH_LONG).show();
                Log.e("Touchbase", "Replica of value is : " + replicaOf);

                if (replicaOf.equals(Constant.Module.DIRECTORY)) {
                    //Intent i = new Intent(context, Directory.class);
                    String myCategory = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);

                    if (myCategory.equals("2")) {
                        Intent i = new Intent(context, DTDirectoryActivity.class);
                        i.putExtra("moduleName", listmodule.get(position).getModuleName());
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                        context.startActivity(i);

                    } else {
                        Intent i = new Intent(context, DirectoryActivity.class);
                        i.putExtra("moduleName", listmodule.get(position).getModuleName());
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                        context.startActivity(i);
                    }

                    return;

                } else if (replicaOf.equals(Constant.Module.SERVICE_DIRECTORY)) {
                    Intent i = new Intent(context, ServiceCattegoryList.class);
                    //Intent i = new Intent(context, ServiceDirectoryList.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.GALLERY)) {  // 8 means gallery
                    Intent i = new Intent(context, Gallery.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                }
                if (replicaOf.equals(Constant.Module.ATTENDANCE)) {  // 17 means Attendance
                    Intent i = new Intent(context, AttendenceActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("moduleId", listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());

                    context.startActivity(i);
                    return;
                }

                if (replicaOf.equals(Constant.Module.CHAT)) {
//                    Intent i = new Intent(context, ChatHome.class);
//                    i.putExtra("moduleName",listmodule.get(position).getModuleName());
//                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
//                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
//                    context.startActivity(i);
//                    return;
                } else if (replicaOf.equals(Constant.Module.PAST_PRESIDENTS)) {
                    Intent i = new Intent(context, PastPresidentActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.CLUBHISTORY)) {
                    Intent i = new Intent(context, ClubHistoryActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.ROTARY_LIBRARY)) {
                    Intent i = new Intent(context, RotaryLibraryActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.WEBLINKS)) {
                    Intent i = new Intent(context, WebLinkActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.BOARD_OF_DIRECTORS)) {
                    Intent i = new Intent(context, BoardOfDirectorsActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.CELEBRATIONS)) {
//                    Intent i = new Intent(context, MonthActivity.class);
//                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
//                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
//                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
//                    context.startActivity(i);

                    Intent i = new Intent(context, CalenderActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.DISTRICT_COMMITTEE)) {
                    Intent i = new Intent(context, DistrictCommittee.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                } else if (replicaOf.equals(Constant.Module.CLUBS)) {
                    Intent i = new Intent(context, DistrictClubActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                    return;
                }

                if (!InternetConnection.checkConnection(context)) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                if (replicaOf.equals(Constant.Module.FEEDBACK)) {   // 16 means Suggestions
                    //webservices(listmodule.get(position).getModuleId());

                    Intent i = new Intent(context, FeedbackActivity.class);
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                } else if (replicaOf.equals(Constant.Module.EVENTS)) {
                    Intent i = new Intent(context, Events.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    groupDashboard.startActivityForResult(i, Constant.REQUEST_EVENT);
                } else if (replicaOf.equals(Constant.Module.ANNOUNCEMENTS)) {
                    Intent i = new Intent(context, Announcement.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    i.putExtra("moduleID", String.valueOf(listmodule.get(position).getModuleId()));
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    groupDashboard.startActivityForResult(i, Constant.REQUEST_ANNOUNCEMENT);
                } else if (replicaOf.equals(Constant.Module.E_BULLETINS)) {
                    Intent i = new Intent(context, E_Bulletin.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    groupDashboard.startActivityForResult(i, Constant.REQUEST_EBULLITION);
                } else if (replicaOf.equals(Constant.Module.DOCUMENTS)) {
                    Intent i = new Intent(context, Documents.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    groupDashboard.startActivityForResult(i, Constant.REQUEST_DOC);
                } else if (replicaOf.equals(Constant.Module.SUB_GROUPS)) {
                    Intent i = new Intent(context, SubGroupList.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    i.putExtra("parentId", "0");
                    i.putExtra("subgroupname", "Sub Groups");
                    context.startActivity(i);

                /*} else if (listmodule.get(position).getModuleStaticRef().equals("servicedirectory")) {
                    Intent i = new Intent(context, ServiceDirectoryList.class);
                    context.startActivity(i);*/
                } else if (replicaOf.equals(Constant.Module.INFO)) {
                    Intent i = new Intent(context, GroupInfo_New.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("moduleId", listmodule.get(position).getModuleId());

                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                } else if (replicaOf.equals(Constant.Module.IMPROVEMENT)) {
                    Intent i = new Intent(context, Announcement.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    i.putExtra("moduleID", String.valueOf(listmodule.get(position).getModuleId()));
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    groupDashboard.startActivityForResult(i, Constant.REQUEST_ANNOUNCEMENT);

//                   Intent i = new Intent(context, Improvement.class);
//                   i.putExtra("moduleName",listmodule.get(position).getModuleName());
//                   PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
//                   PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
//                   context.startActivity(i);
                } else if (replicaOf.equals(Constant.Module.EXTERNAL_LINK)) {
                    Intent i = new Intent(context, ExternalLinkActivity.class);
//                   i.putExtra("moduleName",listmodule.get(position).getModuleName());
//                   i.putExtra("moduleId",listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());

                    //String moduleId = listmodule.get(position).getModuleId();
                    //String grpId = PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID);
                    // new GetExternalLinkTask(grpId, moduleId).execute();

                    //PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                }

                // new Module added for ROW known as find a Rotarian.
                else if (replicaOf.equals(Constant.Module.FIND_A_ROTARIAN)) {
                    Intent i = new Intent(context, FindRotatrianActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                } else if (replicaOf.equals(Constant.Module.NEAR_ME)) {
                    Intent i = new Intent(context, FindAClubActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                }else if(replicaOf.equals(Constant.Module.CLUB_MONTHLY_REPORT_New)){
                    Intent i = new Intent(context, MonthlyReportListActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    i.putExtra("GroupID", String.valueOf(listmodule.get(position).getGroupId()));
                    i.putExtra("moduleID", String.valueOf(listmodule.get(position).getModuleId()));
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    groupDashboard.startActivity(i);
                }  else if (replicaOf.equals(Constant.Module.LEADERBOAD)){
                    // Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, LeaderBoardActivity.class);
                    i.putExtra("moduleName", listmodule.get(position).getModuleName());
                    PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, listmodule.get(position).getModuleId());
                    PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, listmodule.get(position).getModuleName());
                    context.startActivity(i);
                } else {
                    Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                }

             /*   if (result[position] == "Directory") {
                    Intent i = new Intent(context,Directory.class);
                    //  context.startActivity(i);
                } else if (result[position] == "e-bulletin") {
                    Intent i = new Intent(context,E_Bulletin.class);
                    //  context.startActivity(i);
                }else if (result[position] == "Announcement") {
                    Intent i = new Intent(context,Announcement.class);
                    //  context.startActivity(i);
                }else if (result[position] == "Events") {
                    Intent i = new Intent(context,Events.class);
                    context.startActivity(i);
                }else if (result[position] == "Document Safe") {
                    Intent i = new Intent(context,Documents.class);
                    // context.startActivity(i);
                }else if (result[position] == "Celebrations") {
                    Intent i = new Intent(context,Celebration.class);
                    context.startActivity(i);
                }*/
            }
        });

        return rowView;
    }


    private void webservices(String moduleId) {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("memberMainId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpId", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleID", moduleId));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        //arrayList.add(new BasicNameValuePair("memberProfileID", "1"));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetEmail + " :- " + arrayList.toString());
        new GetEmailIdAsyncTask(Constant.GetEmail, arrayList, MyApplication.getContext()).execute();
    }

    public class GetEmailIdAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(GroupDashboadAdapter_new.this.context, R.style.TBProgressBar);
        //Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public GetEmailIdAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            //context = ctx;
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
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d("Response", "calling getGroupInfoItem");
                /*{
                    "GrpEmailResult": {
                    "status": "0"
                    "message": "success"
                    "email": "ghjgh@hfghfh..Ll"
                }
                    -
                }*/
                try {
                    JSONObject jsonResult = new JSONObject(result.toString());
                    String status = jsonResult.getJSONObject("GrpEmailResult").getString("status");
                    if (status.equals("0")) {
                        String email = jsonResult.getJSONObject("GrpEmailResult").getString("email");
                        Log.e("EmailID", "Email ID : " + email);
                        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setType("plain/text");
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        if (context == null) {
                            Log.e("ContextNull", "Context is null");
                        } else
                            context.startActivity(Intent.createChooser(emailIntent, "Send Suggestions"));
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(MyApplication.getContext(), "Sorry. But something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }


    public class GetExternalLinkTask extends AsyncTask<Void, Void, String> {

        String grpId = "0", moduleId = "0";
        ArrayList<NameValuePair> args;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        public GetExternalLinkTask(String grpId, String moduleId) {
            this.grpId = grpId;
            this.moduleId = moduleId;
            args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("grpId", grpId));
            args.add(new BasicNameValuePair("moduleId", moduleId));
            Log.e("TouchBase", "♦♦♦♦Parameters " + Constant.GET_EXTERNAL_LINK + " :- " + args);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = HttpConnection.postData(Constant.GET_EXTERNAL_LINK, args);
                return result;
            } catch (Exception e) {
                Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("TouchBase", "♦♦♦♦Response : " + s);
                progressDialog.hide();
                JSONObject json = new JSONObject(s).getJSONObject("TBGetLinkResult");
                if (json.getString("status").equals("0")) {
                    if (json.has("link")) {
                        String link = json.getString("link");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        context.startActivity(intent);
                    } else if (json.has("message")) {
                        Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Failed to get information. Please try again", Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException npe) {
                Log.e("TouchBase", "♦♦♦♦NullPointerException");
                npe.printStackTrace();
                Toast.makeText(context, "No response from server. Please try again", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(context, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }


}