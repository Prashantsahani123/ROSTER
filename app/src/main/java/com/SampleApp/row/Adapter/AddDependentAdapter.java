package com.SampleApp.row.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.SampleApp.row.Data.DependentData;
import com.SampleApp.row.Inteface.AttendanceItemClick;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.AddDependentHolder;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by USER1 on 25-03-2017.
 */
public class AddDependentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<DependentData> list =new ArrayList<>();
    String type;
    private AttendanceItemClick attendanceItemClick;
    ProgressDialog progressDialog;
    public AddDependentAdapter(Context context, ArrayList<DependentData> list,String type) {
        this.context = context;
        this.list = list;
        this.type=type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        try {
            if(type.equalsIgnoreCase(Constant.Dependent.ANNS) || type.equalsIgnoreCase(Constant.Dependent.ANNETS)) {
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_dependent_item, parent, false);
            }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_visitors_item, parent, false);
            }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_rotarian_item, parent, false);
            }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_delegates_item, parent, false);
            }
            AddDependentHolder holder = new AddDependentHolder(view);
            return holder;
        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AddDependentHolder addDependentHolder=(AddDependentHolder)holder;
        final DependentData data=list.get(position);

       if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
           addDependentHolder.tvDependentType.setText(data.getTitle());
           addDependentHolder.tvDependentNo.setText(String.valueOf(position+1));
           addDependentHolder.et_DependentName.setTag(position);
           addDependentHolder.et_DependentName.setText(data.getAnnsName());

           addDependentHolder.ll_removeRecord.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (attendanceItemClick != null) attendanceItemClick.onClick(v, position);
               }
           });

           MyTextWatcher myTextWatcher=new MyTextWatcher(addDependentHolder.et_DependentName);
           addDependentHolder.et_DependentName.addTextChangedListener(myTextWatcher);

       } else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
           addDependentHolder.tvDependentType.setText(data.getTitle());
           addDependentHolder.tvDependentNo.setText(String.valueOf(position+1));
           addDependentHolder.et_DependentName.setTag(position);
           addDependentHolder.et_DependentName.setText(data.getAnnetsName());

           addDependentHolder.ll_removeRecord.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (attendanceItemClick != null) attendanceItemClick.onClick(v, position);
               }
           });

           MyTextWatcher myTextWatcher=new MyTextWatcher(addDependentHolder.et_DependentName);
           addDependentHolder.et_DependentName.addTextChangedListener(myTextWatcher);
       }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
           addDependentHolder.tvDependentType.setText(data.getTitle());
           addDependentHolder.tvDependentNo.setText(String.valueOf(position+1));
           addDependentHolder.et_visitorsName.setTag(position);
           addDependentHolder.et_visitorsName.setText(data.getVisitorName());

           addDependentHolder.et_visitorsInvitorsName.setTag(position);
           addDependentHolder.et_visitorsInvitorsName.setText(data.getBrought());

           addDependentHolder.ll_visitor_remove.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (attendanceItemClick != null) attendanceItemClick.onClick(v, position);
               }
           });

           MyTextWatcher myTextWatcher=new MyTextWatcher(addDependentHolder.et_visitorsName);
           addDependentHolder.et_visitorsName.addTextChangedListener(myTextWatcher);

           MyTextWatcher myTextWatcher1=new MyTextWatcher(addDependentHolder.et_visitorsInvitorsName);
           addDependentHolder.et_visitorsInvitorsName.addTextChangedListener(myTextWatcher1);
       }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){

           addDependentHolder.et_RotarianID.setTag(position);
           addDependentHolder.et_RotarianID.setText(data.getRotarianID());

           addDependentHolder.et_RotarianName.setTag(position);
           addDependentHolder.et_RotarianName.setText(data.getRotarianName());

           addDependentHolder.et_clubName.setTag(position);
           addDependentHolder.et_clubName.setText(data.getClubName());

           addDependentHolder.ll_rotarian_removeRecord.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (attendanceItemClick != null) attendanceItemClick.onClick(v, position);
               }
           });

           EditorAction editorAction=new EditorAction(addDependentHolder.et_RotarianID);
           addDependentHolder.et_RotarianID.setOnEditorActionListener(editorAction);

           MyTextWatcher myTextWatcher=new MyTextWatcher(addDependentHolder.et_RotarianName);
           addDependentHolder.et_RotarianName.addTextChangedListener(myTextWatcher);

           MyTextWatcher myTextWatcher1=new MyTextWatcher(addDependentHolder.et_clubName);
           addDependentHolder.et_clubName.addTextChangedListener(myTextWatcher1);

       }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){

           addDependentHolder.et_delegates_rotarianName.setTag(position);
           addDependentHolder.et_delegates_rotarianName.setText(data.getRotarianName());

           addDependentHolder.et_districtDesignation.setTag(position);
           addDependentHolder.et_districtDesignation.setText(data.getDistrictDesignation());

           addDependentHolder.et_clubName.setTag(position);
           addDependentHolder.et_clubName.setText(data.getClubName());

           addDependentHolder.ll_delegatesRemove.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (attendanceItemClick != null) attendanceItemClick.onClick(v, position);
               }
           });

           MyTextWatcher myTextWatcher=new MyTextWatcher(addDependentHolder.et_delegates_rotarianName);
           addDependentHolder.et_delegates_rotarianName.addTextChangedListener(myTextWatcher);

           MyTextWatcher myTextWatcher1=new MyTextWatcher(addDependentHolder.et_districtDesignation);
           addDependentHolder.et_districtDesignation.addTextChangedListener(myTextWatcher1);

           MyTextWatcher myTextWatcher2=new MyTextWatcher(addDependentHolder.et_clubName);
           addDependentHolder.et_clubName.addTextChangedListener(myTextWatcher2);
       }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public ArrayList<DependentData> getList(){
        return list;
    }

    public void setClickListener(AttendanceItemClick itemClickListener) {
        this.attendanceItemClick = itemClickListener;
    }



    public class MyTextWatcher implements TextWatcher {
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (int) editText.getTag();

            if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
                DependentData data=list.get(position);
                data.setAnnsName(s.toString());
                data.setEdited(true);
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
                DependentData data=list.get(position);
                data.setAnnetsName(s.toString());
                data.setEdited(true);
            }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                DependentData data=list.get(position);
                data.setEdited(true);
                if(editText.getId()==R.id.et_visitorsName){
                    data.setVisitorName(s.toString());
                }else {
                    data.setBrought(s.toString());
                }

            }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                DependentData data=list.get(position);
                data.setEdited(true);
                if(editText.getId()==R.id.et_rotarianName){
                    data.setRotarianName(s.toString());
                }else if(editText.getId()==R.id.et_districtDesignation){
                    data.setDistrictDesignation(s.toString());
                }else {
                    data.setClubName(s.toString());
                }

            }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                DependentData data=list.get(position);
                data.setEdited(true);
                if(editText.getId()==R.id.et_RotarianName){
                    data.setRotarianName(s.toString());
                }else if(editText.getId()==R.id.et_clubName){
                    data.setClubName(s.toString());
                }
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class EditorAction implements TextView.OnEditorActionListener{

        EditText editText;
        public EditorAction(EditText editText) {
            this.editText=editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                int position= (int) editText.getTag();
                for(DependentData data: list){
                    if(data.getRotarianID().equalsIgnoreCase(editText.getText().toString())){
                        Utils.showToastWithTitleAndContext(context,"Rotarian ID already exist");
                        editText.setText("");
                        return true;
                    }
                }
                getRotarianDetails(editText,position);
                return true;
            }
            return false;
        }
    }

    private void getRotarianDetails(final EditText editText, final int position){
        if (InternetConnection.checkConnection(context)) {
            try {
                JSONObject requestObject=new JSONObject();
                requestObject.put("RotarianID",editText.getText().toString());
                Utils.log(Constant.GetrotarianDetailsbyRotarianID+" / "+requestObject.toString());
                progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetrotarianDetailsbyRotarianID, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.log(response.toString());
                        setRotarianDetails(response,position);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        editText.setText("");
                        progressDialog.dismiss();
                        Utils.showToastWithTitleAndContext(context,context.getString(R.string.msgRetry));
                        Utils.log("VollyError:- " + error);
                    }
                });

                AppController.getInstance().addToRequestQueue(context, request);
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }else {
            Utils.showToastWithTitleAndContext(context,context.getString(R.string.noInternet));
        }


    }


    private void setRotarianDetails(JSONObject response,int position){
        progressDialog.dismiss();
        try {
            JSONObject TBGetRotarianResult=response.getJSONObject("TBGetRotarianResult");
            String status=TBGetRotarianResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray Result=TBGetRotarianResult.getJSONArray("Result");
                DependentData data=list.get(position);
                data.setEdited(true);
                if(Result.length()>0){
                    for(int i=0;i<Result.length();i++){
                        JSONObject object=Result.getJSONObject(i);

                        data.setRotarianName(object.getString("Rotarian_Name"));
                        data.setRotarianID(object.getString("Rotarian_ID"));
                        data.setClubName(object.getString("Club_Name"));

                    }
                    notifyDataSetChanged();
                }else {
                    data.setRotarianName("");
                    data.setRotarianID("");
                    data.setClubName("");
                    Utils.showToastWithTitleAndContext(context,context.getString(R.string.msg_no_records_found));
                    notifyDataSetChanged();
                }

            }else {

                Utils.showToastWithTitleAndContext(context,context.getString(R.string.msg_no_records_found));

            }
        } catch (JSONException e) {
            Utils.showToastWithTitleAndContext(context,context.getString(R.string.msgRetry));
            e.printStackTrace();
        }
    }

}
