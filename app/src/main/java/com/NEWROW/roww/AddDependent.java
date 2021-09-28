package com.NEWROW.row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.Adapter.AddDependentAdapter;
import com.NEWROW.row.Data.DependentData;
import com.NEWROW.row.Inteface.AttendanceItemClick;
import com.NEWROW.row.Utils.Constant;

import java.util.ArrayList;

public class AddDependent extends AppCompatActivity implements AttendanceItemClick {

    Context context;
    TextView tv_title, tv_dependenntType,ib_continue;
    EditText et_noOfDependent;
    ImageView iv_backbutton;
    RecyclerView rv_dependentDetails;
    LinearLayout ll_increaseDependent,ll_decreaseDependent,ll_add;
    ArrayList<DependentData> list=new ArrayList<>();
    ArrayList<DependentData> newList,updatedList,deletedList;

    String type="";
    AddDependentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dependents);
        context=this;
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_dependenntType = (TextView) findViewById(R.id.tv_dependenntType);
        ib_continue=(TextView)findViewById(R.id.ib_continue);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        rv_dependentDetails = (RecyclerView)findViewById(R.id.rv_dependentDetails);
        rv_dependentDetails.setLayoutManager(new LinearLayoutManager(AddDependent.this));
        ll_add=(LinearLayout)findViewById(R.id.ll_add);


        init();

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
           // deletedList=(ArrayList<DependentData>) bundle.getSerializable("deletedList");
            type=bundle.getString("type");
            if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){

                list= (ArrayList<DependentData>) bundle.getSerializable("list");
                adapter=new AddDependentAdapter(context,list,type);
                rv_dependentDetails.setAdapter(adapter);
                adapter.setClickListener(this);
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){

                list= (ArrayList<DependentData>) bundle.getSerializable("list");
                adapter=new AddDependentAdapter(context,list,type);
                rv_dependentDetails.setAdapter(adapter);
                adapter.setClickListener(this);
            }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){

                list= (ArrayList<DependentData>) bundle.getSerializable("list");
                adapter=new AddDependentAdapter(context,list,type);
                rv_dependentDetails.setAdapter(adapter);
                adapter.setClickListener(this);
            }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){

                list= (ArrayList<DependentData>) bundle.getSerializable("list");
                adapter=new AddDependentAdapter(context,list,type);
                rv_dependentDetails.setAdapter(adapter);
                adapter.setClickListener(this);
            }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){

                list= (ArrayList<DependentData>) bundle.getSerializable("list");
                adapter=new AddDependentAdapter(context,list,type);
                rv_dependentDetails.setAdapter(adapter);
                adapter.setClickListener(this);
            }
            String count= String.valueOf(bundle.getInt("count",0));
            et_noOfDependent.setText(count);
            tv_title.setText(bundle.getString("title"));
            if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                tv_dependenntType.setText(bundle.getString("Rotarian's"));
            }else {
                tv_dependenntType.setText(bundle.getString("title"));

            }
            if(list.size()>0){
                ib_continue.setText("UPDATE");
            }

        }
    }

    private void init() {
        et_noOfDependent = (EditText) findViewById(R.id.et_noOfDependent);
        ll_decreaseDependent = (LinearLayout)findViewById(R.id.ll_decreaseDependent);

        newList=new ArrayList<>();
        updatedList=new ArrayList<>();
        deletedList=new ArrayList<>();


        ll_decreaseDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no = Integer.parseInt(et_noOfDependent.getText().toString());
                if(no>0){
                    et_noOfDependent.setText(String.valueOf(no-1));
                    final ArrayList<DependentData> adapterList=adapter.getList();
                    if(adapterList.size()>0){
                        DependentData data=adapterList.get(adapterList.size()-1);
                        deletedList.add(data);
                        adapterList.remove(adapterList.size()-1);
                    }

                    adapter.notifyDataSetChanged();

                    rv_dependentDetails.post(new Runnable() {
                        @Override
                        public void run() {
                            if(adapterList.size()>0){
                                rv_dependentDetails.smoothScrollToPosition(adapterList.size()-1);
                            }
                        }
                    });
                }

            }
        });

        ll_increaseDependent = (LinearLayout)findViewById(R.id.ll_increaseDependent);
        ll_increaseDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no = Integer.parseInt(et_noOfDependent.getText().toString());
                et_noOfDependent.setText(String.valueOf(no+1));
                DependentData data=new DependentData();
                data.setEdited(true);
                data.setMemberID("0");
                if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
                    data.setType(Constant.Dependent.ANNS);
                    data.setAnnsName("");
                    data.setTitle("Anns");
                }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
                    data.setType(Constant.Dependent.ANNETS);
                    data.setAnnetsName("");
                    data.setTitle("Annets");
                }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                    data.setType(Constant.Dependent.VISITORS);
                    data.setVisitorName("");
                    data.setBrought("");
                    data.setTitle("Visitors");
                }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                    data.setType(Constant.Dependent.ROTARIAN);
                    data.setRotarianID("");
                    data.setRotarianName("");
                    data.setClubName("");
                    data.setTitle("Rotarian");
                }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                    data.setType(Constant.Dependent.DELEGETS);
                    data.setDistrictDesignation("");
                    data.setRotarianName("");
                    data.setClubName("");
                    data.setTitle("Delegates");
                }

                final ArrayList<DependentData> adapterList=adapter.getList();
                adapterList.add(data);
                adapter.notifyDataSetChanged();

                rv_dependentDetails.post(new Runnable() {
                    @Override
                    public void run() {
                        rv_dependentDetails.smoothScrollToPosition(adapterList.size()-1);
                    }
                });


            }
        });

        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<DependentData> list=adapter.getList();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",list );
                returnIntent.putExtra("deletedList",deletedList );
                returnIntent.putExtra("count",et_noOfDependent.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }


    @Override
    public void onClick(View view, int position) {
        int no = Integer.parseInt(et_noOfDependent.getText().toString());
        if(no>0){
            et_noOfDependent.setText(String.valueOf(no-1));
            ArrayList<DependentData> adapterList=adapter.getList();

            if(adapterList.size()>0){
                DependentData data=adapterList.get(position);
                deletedList.add(data);
                adapterList.remove(position);
            }

            adapter.notifyDataSetChanged();
        }
    }



}
