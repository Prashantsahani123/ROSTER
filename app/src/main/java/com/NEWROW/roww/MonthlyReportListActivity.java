package com.NEWROW.row;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.NEWROW.row.Adapter.MonthlyReportListAdapter;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthlyReportListActivity extends AppCompatActivity {

    private Context ctx;
    private RecyclerView recyclerViewMonths;
    MonthlyReportListAdapter adapter;
    private ArrayList<String> montStringArrayList = new ArrayList<>();
    TextView tv_title;
    ImageView iv_backbutton, iv_actionbtn;
    String moduleName = "";
    private String grpID = "0";
    private LinearLayoutManager layoutManager;
    private String memberProfileID = "0";
    private String moduleId;
    String title="",year="";
    Spinner sp_year;
    private String fromYear, toYear;
    ArrayList<String> filtertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_report_list);

        recyclerViewMonths = (RecyclerView) findViewById(R.id.rvMonths);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        sp_year = (Spinner) findViewById(R.id.sp_year);

        title = getIntent().getExtras().getString("moduleName", "Club Monthly Report");

        ctx = MonthlyReportListActivity.this;

        layoutManager = new LinearLayoutManager(ctx);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMonths.setLayoutManager(layoutManager);

        tv_title.setText(title);

        init();

        makeMonthList();

        /*sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedYear = sp_year.getSelectedItem().toString();
                String array[] = selectedYear.split("-");

                fromYear = array[0];
                toYear = array[1];

                year = sp_year.getSelectedItem().toString();

                Calendar mCalendar = Calendar.getInstance();
                int currYear =  mCalendar.get(Calendar.YEAR);
                int currentMonth =  mCalendar.get(Calendar.MONTH);
                String currentYear = String.valueOf(currYear);

                Utils.log(fromYear + " - " + toYear +" device year ="+currentYear+" month="+currentMonth);

                if(fromYear.equalsIgnoreCase(currentYear)) {
                    makeMonthList();
                } else if(toYear.equalsIgnoreCase(currentYear)){

                    if(currentMonth<6) {
                        makeMonthList();
                    }else{
                        makeMonthListForOtherYear(Integer.parseInt(fromYear),Integer.parseInt(toYear));
                    }

                } else {
                    makeMonthListForOtherYear(Integer.parseInt(fromYear),Integer.parseInt(toYear));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });*/

    }

    private void init(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        Utils.log("Month: " + month + " year : " + year);

        if (month > 6) {
            fromYear = String.valueOf(year);
            toYear = String.valueOf(year + 1);
        } else {
            toYear = String.valueOf(year);
            fromYear = String.valueOf(year - 1);
        }

        filtertype = new ArrayList<>();

        int flag = 0;

        for (int i = year; i >= 2015; i--) {

            String filterYear;

            if (month > 6 && i == year) {
                filterYear = (i) + "-" + (i + 1);
                flag=1;
            } else if (month <= 6 && i == year) {
                filterYear = (i-1) + "-" + (i);
                flag=2;
            } else {
                if(flag==1){
                    filterYear = (i) + "-" + (i+1);
                }else {
                    filterYear = (i-1) + "-" + (i);
                }
            }

            filtertype.add(filterYear);
        }

        if(flag!=1){
            filtertype.remove(filtertype.size()-1);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);

    }

/*    private void makeMonthList() {
        Calendar mCalendar = Calendar.getInstance();
        int currentMonth =  mCalendar.get(Calendar.MONTH);
        int currentYear =  mCalendar.get(Calendar.YEAR);
        int previousYear = currentYear-1;
        int nextYear = currentYear+1;

        if(currentMonth<6) {

            title = title + " ("+previousYear+"-"+currentYear+")";

            montStringArrayList.add("July "+previousYear);
            montStringArrayList.add("August "+previousYear);
            montStringArrayList.add("September "+previousYear);
            montStringArrayList.add("October "+previousYear);
            montStringArrayList.add("November "+previousYear);
            montStringArrayList.add("December "+previousYear);
            montStringArrayList.add("January "+currentYear);
            montStringArrayList.add("February "+currentYear);
            montStringArrayList.add("March "+currentYear);
            montStringArrayList.add("April "+currentYear);
            montStringArrayList.add("May "+currentYear);
            montStringArrayList.add("June "+currentYear);

        } else {

            title = title + " ("+currentYear+"-"+nextYear+")";

            montStringArrayList.add("July "+currentYear);
            montStringArrayList.add("August "+currentYear);
            montStringArrayList.add("September "+currentYear);
            montStringArrayList.add("October "+currentYear);
            montStringArrayList.add("November "+currentYear);
            montStringArrayList.add("December "+currentYear);
            montStringArrayList.add("January "+nextYear);
            montStringArrayList.add("February "+nextYear);
            montStringArrayList.add("March "+nextYear);
            montStringArrayList.add("April "+nextYear);
            montStringArrayList.add("May "+nextYear);
            montStringArrayList.add("June "+nextYear);
        }

        tv_title.setText(title);

        adapter = new MonthlyReportListAdapter(ctx,montStringArrayList);

        recyclerViewMonths.setAdapter(adapter);

    }*/

/*    private void makeMonthListOld() {

        Calendar mCalendar = Calendar.getInstance();
        int currentMonth =  mCalendar.get(Calendar.MONTH);
        int currentYear =  mCalendar.get(Calendar.YEAR);
        int previousYear = currentYear-1;

          switch (currentMonth){

                case 0:
                    montStringArrayList.add("January "+previousYear);
                    montStringArrayList.add("February "+previousYear);
                    montStringArrayList.add("March "+previousYear);
                    montStringArrayList.add("April "+previousYear);
                    montStringArrayList.add("May "+previousYear);
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    break;
                case 1:
                    montStringArrayList.add("February "+previousYear);
                    montStringArrayList.add("March "+previousYear);
                    montStringArrayList.add("April "+previousYear);
                    montStringArrayList.add("May "+previousYear);
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);

                    break;
                case 2:
                    montStringArrayList.add("March "+previousYear);
                    montStringArrayList.add("April "+previousYear);
                    montStringArrayList.add("May "+previousYear);
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);

                    break;
                case 3:
                    montStringArrayList.add("April "+previousYear);
                    montStringArrayList.add("May "+previousYear);
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    break;
                case 4:
                    montStringArrayList.add("May "+previousYear);
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    break;
                case 5:
                    montStringArrayList.add("June "+previousYear);
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    break;

                case 6:
                    montStringArrayList.add("July "+previousYear);
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    break;
                case 7:
                    montStringArrayList.add("August "+previousYear);
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    montStringArrayList.add("July "+currentYear);
                    break;
                case 8:
                    montStringArrayList.add("September "+previousYear);
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    montStringArrayList.add("July "+currentYear);
                    montStringArrayList.add("August "+currentYear);
                    break;
                case 9:
                    montStringArrayList.add("October "+previousYear);
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    montStringArrayList.add("July "+currentYear);
                    montStringArrayList.add("August "+currentYear);
                    montStringArrayList.add("September "+currentYear);
                    break;
                case 10:
                    montStringArrayList.add("November "+previousYear);
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    montStringArrayList.add("July "+currentYear);
                    montStringArrayList.add("August "+currentYear);
                    montStringArrayList.add("September "+currentYear);
                    montStringArrayList.add("October "+currentYear);
                    break;
                case 11:
                    montStringArrayList.add("December "+previousYear);
                    montStringArrayList.add("January "+currentYear);
                    montStringArrayList.add("February "+currentYear);
                    montStringArrayList.add("March "+currentYear);
                    montStringArrayList.add("April "+currentYear);
                    montStringArrayList.add("May "+currentYear);
                    montStringArrayList.add("June "+currentYear);
                    montStringArrayList.add("July "+currentYear);
                    montStringArrayList.add("August "+currentYear);
                    montStringArrayList.add("September "+currentYear);
                    montStringArrayList.add("October "+currentYear);
                    montStringArrayList.add("November "+currentYear);
                    break;

            }



        Collections.reverse(montStringArrayList);
        adapter = new MonthlyReportListAdapter(ctx,montStringArrayList);
        recyclerViewMonths.setAdapter(adapter);

    }*/

      private void makeMonthListForOtherYear(int currentYear, int nextYear){

          montStringArrayList.clear();

          montStringArrayList.add("July "+currentYear);
          montStringArrayList.add("August "+currentYear);
          montStringArrayList.add("September "+currentYear);
          montStringArrayList.add("October "+currentYear);
          montStringArrayList.add("November "+currentYear);
          montStringArrayList.add("December "+currentYear);
          montStringArrayList.add("January "+nextYear);
          montStringArrayList.add("February "+nextYear);
          montStringArrayList.add("March "+nextYear);
          montStringArrayList.add("April "+nextYear);
          montStringArrayList.add("May "+nextYear);
          montStringArrayList.add("June "+nextYear);

          adapter = new MonthlyReportListAdapter(ctx,montStringArrayList);
          recyclerViewMonths.setAdapter(adapter);

      }

       private void makeMonthList(){

           montStringArrayList.clear();

           Calendar mCalendar = Calendar.getInstance();
           int currentMonth =  mCalendar.get(Calendar.MONTH);
           int currentYear =  mCalendar.get(Calendar.YEAR);
           int previousYear = currentYear-1;

           if(currentMonth<6) {

               montStringArrayList.add("July "+previousYear);
               montStringArrayList.add("August "+previousYear);
               montStringArrayList.add("September "+previousYear);
               montStringArrayList.add("October "+previousYear);
               montStringArrayList.add("November "+previousYear);
               montStringArrayList.add("December "+previousYear);

               switch (currentMonth) {

                   case 0:
                       montStringArrayList.add("January "+currentYear);
                       break;
                   case 1:
                       montStringArrayList.add("January "+currentYear);
                       montStringArrayList.add("February "+currentYear);
                       break;
                   case 2:
                       montStringArrayList.add("January "+currentYear);
                       montStringArrayList.add("February "+currentYear);
                       montStringArrayList.add("March "+currentYear);
                       break;
                   case 3:
                       montStringArrayList.add("January "+currentYear);
                       montStringArrayList.add("February "+currentYear);
                       montStringArrayList.add("March "+currentYear);
                       montStringArrayList.add("April "+currentYear);
                       break;
                   case 4:
                       montStringArrayList.add("January "+currentYear);
                       montStringArrayList.add("February "+currentYear);
                       montStringArrayList.add("March "+currentYear);
                       montStringArrayList.add("April "+currentYear);
                       montStringArrayList.add("May "+currentYear);
                       break;
                   case 5:
                       montStringArrayList.add("January "+currentYear);
                       montStringArrayList.add("February "+currentYear);
                       montStringArrayList.add("March "+currentYear);
                       montStringArrayList.add("April "+currentYear);
                       montStringArrayList.add("May "+currentYear);
                       montStringArrayList.add("June "+currentYear);
                       break;

               }

           } else {

               switch (currentMonth){

                   case 6:
                       montStringArrayList.add("July "+currentYear);
                       break;
                   case 7:
                       montStringArrayList.add("July "+currentYear);
                       montStringArrayList.add("August "+currentYear);
                       break;
                   case 8:
                       montStringArrayList.add("July "+currentYear);
                       montStringArrayList.add("August "+currentYear);
                       montStringArrayList.add("September "+currentYear);
                       break;
                   case 9:
                       montStringArrayList.add("July "+currentYear);
                       montStringArrayList.add("August "+currentYear);
                       montStringArrayList.add("September "+currentYear);
                       montStringArrayList.add("October "+currentYear);
                       break;
                   case 10:
                       montStringArrayList.add("July "+currentYear);
                       montStringArrayList.add("August "+currentYear);
                       montStringArrayList.add("September "+currentYear);
                       montStringArrayList.add("October "+currentYear);
                       montStringArrayList.add("November "+currentYear);
                       break;
                   case 11:
                       montStringArrayList.add("July "+currentYear);
                       montStringArrayList.add("August "+currentYear);
                       montStringArrayList.add("September "+currentYear);
                       montStringArrayList.add("October "+currentYear);
                       montStringArrayList.add("November "+currentYear);
                       montStringArrayList.add("December "+currentYear);
                       break;
               }

           }

//           Collections.reverse(montStringArrayList);
           adapter = new MonthlyReportListAdapter(ctx,montStringArrayList);
           recyclerViewMonths.setAdapter(adapter);

       }

}
