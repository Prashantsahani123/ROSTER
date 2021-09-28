package com.NEWROW.row;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Utils.Constant;

public class EditDependentActivity extends AppCompatActivity {

    TextView txt_title,txt_total;
    EditText et_count;
    ImageView iv_delete,iv_add;
    RecyclerView rv_editDependentList;
    Context context;
    String module="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dependent);
        context=this;

        txt_title=(TextView)findViewById(R.id.txt_title);
        txt_total=(TextView)findViewById(R.id.txt_total);

        et_count=(EditText)findViewById(R.id.et_count);

        iv_add=(ImageView)findViewById(R.id.iv_add);
        iv_delete=(ImageView)findViewById(R.id.iv_delete);

        rv_editDependentList=(RecyclerView)findViewById(R.id.rv_editDependentList);
        rv_editDependentList.setLayoutManager(new LinearLayoutManager(context));

        Bundle b=getIntent().getExtras();
        if(b!=null){
            Intent intent=getIntent();
            if(intent.hasExtra("dependent")){
                module=intent.getStringExtra("dependent");
            }
        }
        init();
    }

    private void init(){



        if(module.equalsIgnoreCase(Constant.Dependent.ANNS)){
            txt_title.append("Anns");

        }else if(module.equalsIgnoreCase(Constant.Dependent.ANNETS)){
            txt_title.append("Annets");
        }else if(module.equalsIgnoreCase(Constant.Dependent.VISITORS)){
            txt_title.append("Visitors");
        }else if(module.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
            txt_title.append("Rotarian's");
        }else if(module.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
            txt_title.append("Delegates");
        }

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
