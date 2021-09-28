package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 24-04-2017.
 */

public class FindRotatrianActivity extends Activity {
    private TextView tv_title,tv_search;
    private ImageView iv_backbutton;
    EditText edtname,edt_classification,edt_city,edt_districtNo,edt_mobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findrotarian);
        actionbarfunction();
        init();
    }

    private void actionbarfunction(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Find a Rotarian");
    }

    private void init(){
        tv_search = (TextView)findViewById(R.id.search);
        edtname = (EditText)findViewById(R.id.et_name);
        edt_classification = (EditText)findViewById(R.id.edt_classification);
        edt_city = (EditText)findViewById(R.id.edt_city);
        edt_districtNo = (EditText)findViewById(R.id.edt_districtNo);
        //Add by Gaurav
        edt_mobileno = (EditText)findViewById(R.id.edt_mobileno);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){
                    Intent i = new Intent(FindRotatrianActivity.this,RotarianList.class);
                    i.putExtra("name",edtname.getText().toString());
                    i.putExtra("classification",edt_classification.getText().toString());
                    i.putExtra("club",edt_city.getText().toString());
                    i.putExtra("district_number",edt_districtNo.getText().toString());
                    i.putExtra("mobile_number",edt_mobileno.getText().toString());

                    startActivity(i);
                    // method to clear form data
                    clear();
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill Atleast One Search Criteria", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public boolean validation(){
        if(edtname.getText().toString().equalsIgnoreCase("")&& edt_classification.getText().toString().equalsIgnoreCase("") && edt_city.getText().toString().equalsIgnoreCase("") && edt_districtNo.getText().toString().equalsIgnoreCase("") && edt_mobileno.getText().toString().equalsIgnoreCase("")&& edt_mobileno.getText().toString().length()<10){
            return false;
        }
        return true;
    }

    public void clear(){
        edtname.setText("");
        edt_classification.setText("");
        edt_city.setText("");
        edt_districtNo.setText("");
        edt_mobileno.setText("");
    }

}
