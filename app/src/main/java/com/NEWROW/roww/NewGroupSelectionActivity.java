package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.SubGoupData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TreeNode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class NewGroupSelectionActivity extends AppCompatActivity {

    ListView lvGroups;
    Context context;
    ArrayList<SubGoupData> currentList;

    ArrayList<String> selected;
    public static Stack<TreeNode> stk = new Stack<TreeNode>();
    public static TreeNode root;
    public static TreeNode currentNode;
    String parentId;
    TextView tv_no_records_found, tv_addbtn;
    String profileId;
    GroupSelectionAdapter adapter;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_group_selection);

        context = this;
        selected = new ArrayList<String>();
        tv_title = (TextView) findViewById(R.id.tv_title);
        lvGroups = (ListView) findViewById(R.id.lvGroups);
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);

        parentId = getIntent().getExtras().getString("parentId", "0");
        profileId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        tv_addbtn = (TextView) findViewById(R.id.tv_addbtn);
        currentList = new ArrayList<SubGoupData>();

        if ( root == null ) {
            SubGoupData rootData = new SubGoupData("", "0", "0", false, "0");
            root = new TreeNode(rootData);
            currentNode = root;
            setTitle("Subgroups");
        } /*else {
            currentNode = new TreeNode(parentId);
        }*/

        selected.clear();

        webservices();

        if ( getIntent().getExtras().getString("edit", "0").equals("1")) {
            selected = getIntent().getExtras().getStringArrayList("selected");
        }

        Log.e("sub gruop ","selected list=>"+selected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        root = null;
        stk.clear();
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
       /* arrayList.add(new BasicNameValuePair("parentID", parentId));
        arrayList.add(new BasicNameValuePair("profileId", profileId));*/

        Log.e("Response", "PARAMETERS " + Constant.GetSubGroupList + " :- " + arrayList.toString());

        if (InternetConnection.checkConnection(context)){
            new WebConnectionAsyncAnnouncement(Constant.GetSubGroupList, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {
        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(NewGroupSelectionActivity.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionAsyncAnnouncement(String url, List<NameValuePair> argList, Context ctx) {
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
                Log.d("Response", "@@ " + result.toString());
                currentList.clear();
                getSubGroup(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

    }


    private void getSubGroup(String result) {

        try {

            JSONObject object = new JSONObject(result);

            JSONObject jsonObj = object.getJSONObject("TBGetSubGroupListResult");

            final String status = jsonObj.getString("status");

            if (status.equals("0")) {

                JSONArray SubGroupResult = jsonObj.getJSONArray("SubGroupResult");

                int n = SubGroupResult.length();

                /*if ( n == 0 ) {
                    Intent intent = new Intent(SubGroupSelectionList.this, SubGroupDetails.class);
                    intent.putExtra("subgroupid", parentId);
                    intent.putExtra("subgroupname", groupName);
                    startActivity(intent);
                    finish();
                }*/

                for (int i = 0; i < n; i++) {

                    JSONObject obj = SubGroupResult.getJSONObject(i);

                    JSONObject objects = obj.getJSONObject("Subgroup");

                    SubGoupData data = new SubGoupData();

                    data.setSubgroup_name(objects.getString("subgrpTitle"));
                    data.setNo_of_members(objects.getString("noOfmem"));
                    data.setSubgrpId(objects.getString("subgrpId"));
                    //data.setHasSubgroups(objects.getString("hasSubgroup"));
                    data.setHasSubgroups("");

                    Log.d("sub", "current Subgrp id "+data.getSubgrpId());

                    if ( selected.contains(data.getSubgrpId()) ) {
                        data.setBox(true);
                    }

                    currentNode.addChildNode(new TreeNode(data));
                }

                adapter =  new GroupSelectionAdapter(context, R.layout.subgroup_selection_list_item, currentNode.children, "0");

                lvGroups.setAdapter(adapter);

                if(n>0) {
                    tv_no_records_found.setVisibility(View.GONE);
                }else {
                    tv_no_records_found.setVisibility(View.VISIBLE);
                }

            } else {
                tv_no_records_found.setVisibility(View.VISIBLE);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }


    }


/*
    comment by satish on 07-06-2019 becuase api change
private void getSubGroup(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);

            JSONObject SubGroupListResult = jsonObj.getJSONObject("result");

            final String status = jsonObj.getString("status");

            if (status.equals("0")) {

                JSONArray SubGroupResult = SubGroupListResult.getJSONArray("subGrpList");

                int n = SubGroupResult.length();

                *//*if ( n == 0 ) {
                    Intent intent = new Intent(SubGroupSelectionList.this, SubGroupDetails.class);
                    intent.putExtra("subgroupid", parentId);
                    intent.putExtra("subgroupname", groupName);
                    startActivity(intent);
                    finish();
                }*//*
                for (int i = 0; i < n; i++) {

                    JSONObject objects = SubGroupResult.getJSONObject(i);
                    //JSONObject objects = object.getJSONObject("Subgroup");

                    SubGoupData data = new SubGoupData();

                    data.setSubgroup_name(objects.getString("subgrpTitle").toString());
                    data.setNo_of_members(objects.getString("noOfmem").toString());
                    data.setSubgrpId(objects.getString("subgrpId").toString());
                    data.setHasSubgroups(objects.getString("hasSubgroup").toString());

                    if ( selected.contains(data.getSubgrpId()) ) {
                        data.setBox(true);
                    }

                    currentNode.addChildNode(new TreeNode(data));
                }

                adapter =  new GroupSelectionAdapter(context, R.layout.subgroup_selection_list_item, currentNode.children, "0");
                lvGroups.setAdapter(adapter);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }


    }*/


    @Override
    public void onBackPressed() {
        if ( stk.isEmpty() ) {
            super.onBackPressed();
        } else {
            currentNode = stk.pop();
            //Log.e("PoppedList", currentList.toString());
            adapter =  new GroupSelectionAdapter(context, R.layout.subgroup_selection_list_item, currentNode.children, "0");
            lvGroups.setAdapter(adapter);
            String title = ((SubGoupData)currentNode.getData()).getSubgroup_name();
            if ( title.equals("")) {
                setTitle("Subgroups");
            } else {
                setTitle(title);
            }

        }
    }

    public class GroupSelectionAdapter extends ArrayAdapter<TreeNode> {
        private Context mContext;
        private int layoutResourceId;
        private ArrayList list_subGroup = new ArrayList();
        String flag_addsub = "0";

        public GroupSelectionAdapter (Context mContext, int layoutResourceId, ArrayList list_subGroup, String flag_addsub) {
            super(mContext, layoutResourceId, list_subGroup);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.list_subGroup = list_subGroup;
            this.flag_addsub = flag_addsub;
        }

        public void setGridData(ArrayList<SubGoupData> list_subGroup) {
            this.list_subGroup = list_subGroup;
            notifyDataSetChanged();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            View row = convertView;
            final ViewHolder holder;
            final SubGoupData item = (SubGoupData) ((TreeNode) list_subGroup.get(position)).getData();
            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();

                // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
                holder.subgroupName = (TextView) row.findViewById(R.id.tv_subgroupName);
                holder.member_number = (TextView) row.findViewById(R.id.tv_member_number);
                holder.iv_arrow = (ImageView) row.findViewById(R.id.iv_arrow);
                holder.cbBox = (CheckBox) row.findViewById(R.id.cbBox);

                holder.cbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        getsubgrps((Integer) buttonView.getTag()).box = isChecked;
                        if ( ! isChecked ) {
                            selected.remove(item.getSubgrpId());
                        }
                    }
                });

                holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ArrayList<SubGoupData> list = new ArrayList<SubGoupData>(currentList);
                        stk.push(currentNode);
                        currentNode = (TreeNode)list_subGroup.get(position);
                        parentId = item.getSubgrpId();
                        setTitle(item.getSubgroup_name());
                        if ( currentNode.children.isEmpty()) {
                            webservices();
                        } else {
                            adapter =  new GroupSelectionAdapter(context, R.layout.subgroup_selection_list_item, currentNode.children, "0");
                            lvGroups.setAdapter(adapter);
                        }
                    }
                });
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.cbBox.toggle();
                    }
                });
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }


            holder.cbBox.setTag(position);
            holder.cbBox.setChecked(item.box);

            if (item.getHasSubgroups().equals("1")) {
                //holder.cbBox.setVisibility(View.VISIBLE);
                holder.iv_arrow.setVisibility(View.VISIBLE);
            } else {
                holder.iv_arrow.setVisibility(View.GONE);
            }

            holder.subgroupName.setText(item.getSubgroup_name());
            holder.member_number.setText("No of Members :- "+item.getNo_of_members());
            return row;
        }

        /*public ArrayList<SubGoupData> getBox() {
            ArrayList<SubGoupData> box = new ArrayList<SubGoupData>();
            for (SubGoupData p : list_subGroup) {
                if (p.box)
                    box.add(p);
            }
            return box;
        }*/

        SubGoupData getsubgrps(int position) {
            return (SubGoupData)(getItem(position).getData());
        }

        /*CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        };*/


    }

    public void sendSelectedData(View v) {
        if ( selected == null ) {
            selected = new ArrayList<String>();
        }
        Stack<TreeNode> tempStk = new Stack<TreeNode>();
        tempStk.push(root);

        while ( ! tempStk.isEmpty()) {
            TreeNode t = tempStk.pop();

            Iterator iterator = t.children.iterator();

            while ( iterator.hasNext() ) {
                try {
                    TreeNode node = (TreeNode) iterator.next();
                    SubGoupData data = (SubGoupData) node.getData();
                    if ( data.isBox() ) {
                        if ( ! selected.contains(data.getSubgrpId() ) )
                            selected.add(data.getSubgrpId());
                    }

                    if ( ! node.children.isEmpty() ) {
                        tempStk.push(node);
                    }
                } catch(ClassCastException cce) {
                    cce.printStackTrace();
                }
            }
            /*SubGoupData data = (SubGoupData) t.getData();

            if ( data.isBox() ) {
                selected.add(data.getSubgrpId());
            }

            if ( t.children.size() != 0 ) {
                tempStk.push(t);
            }*/
        }

        if ( selected.isEmpty() ) {
            Toast.makeText(NewGroupSelectionActivity.this, "Please select at least one subgroup", Toast.LENGTH_LONG).show();
            return;
        }
        stk.clear();
        Intent intent = new Intent();
        intent.putExtra("result", selected);

        setResult(RESULT_OK, intent);
        finish();
    }

    public static class ViewHolder {
        TextView subgroupName, member_number;
        ImageView iv_arrow;
        CheckBox cbBox;
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
