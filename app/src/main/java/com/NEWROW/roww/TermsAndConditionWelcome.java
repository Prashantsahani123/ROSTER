package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USER on 15-12-2015.
 */
public class TermsAndConditionWelcome extends Activity {

    TextView tv_terms_conditions,tv_agree_continue;
    TextView tv_title;
    ImageView iv_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.termsandconditionwelcome);

        tv_terms_conditions = (TextView)findViewById(R.id.tv_terms_conditions);
        tv_agree_continue = (TextView)findViewById(R.id.tv_agree_continue);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Welcome");


     //   tv_terms_conditions.setText(Html.fromHtml(getString(R.string.terms_conditions)));
      init();

    }

    private void init() {
        tv_agree_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TermsAndConditionWelcome.this,LoginPage.class);
                startActivity(i);
            }
        });
    }
}
