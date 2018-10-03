package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SampleApp.row.CalenderActivity;
import com.SampleApp.row.Data.DashboardData;
import com.SampleApp.row.R;
import com.SampleApp.row.ShowFeedActivity;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import static com.SampleApp.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 *
 */
public class CurrentEventsAdapter extends PagerAdapter {


    private ArrayList<DashboardData> eventList;
    private ArrayList<Integer> colors;
    private LayoutInflater inflater;
    private Context context;
    private DashboardData dashboardData;
    Random rand=new Random();
    public CurrentEventsAdapter(Context context, ArrayList<DashboardData> eventList, ArrayList<Integer> COLORS) {
        this.context = context;
        this.eventList=eventList;
        this.colors = COLORS;
        inflater = LayoutInflater.from(context);
        Utils.log(""+eventList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.current_events, view, false);

        RelativeLayout rl_cont=(RelativeLayout)imageLayout.findViewById(R.id.rl_container);
        assert imageLayout != null;
       final ImageView background  = (ImageView)imageLayout.findViewById(R.id.iv_background);
        final ImageView img_event  = (ImageView)imageLayout.findViewById(R.id.birthdayOrAnniversary);
        final TextView heading = (TextView)imageLayout.findViewById(R.id.tv_heading) ;
        final  TextView tv_name = (TextView)imageLayout.findViewById(R.id.tv_name);
        final  TextView tv_count = (TextView)imageLayout.findViewById(R.id.count);
        final  TextView tv_haveBirthday = (TextView)imageLayout.findViewById(R.id.haveBirthday);
        final  TextView tv_eventTitle = (TextView)imageLayout.findViewById(R.id.tv_event_title);
        final  TextView tv_eventDesc = (TextView)imageLayout.findViewById(R.id.tv_eventDescription);
        final  TextView tv_eventDate = (TextView)imageLayout.findViewById(R.id.tv_eventTimeAndDate);

        final LinearLayout ll_birthday = (LinearLayout) imageLayout.findViewById(R.id.ll_birthday);
        final LinearLayout ll_event = (LinearLayout) imageLayout.findViewById(R.id.ll_event);
        LinearLayout ll_count=(LinearLayout)imageLayout.findViewById(R.id.ll_count);
        dashboardData = new DashboardData();

        heading.setText(eventList.get(position).getClubName().toString());
        final String type=eventList.get(position).getType();
        final String category=eventList.get(position).getClubCategory();
        if(eventList.get(position).getType().equalsIgnoreCase("Birthday")){
                ll_birthday.setVisibility(View.VISIBLE);

                int count= Integer.parseInt(eventList.get(position).getTodaysCount());

                if(count==1){
                    tv_name.setText(eventList.get(position).getTitle());
                    ll_count.setVisibility(View.GONE);
                    tv_haveBirthday.setText("has birthday today.");
                }else if(count == 2) {
                    String name = eventList.get(position).getTitle().replace(","," & ");
                    tv_name.setText(name);
                    ll_count.setVisibility(View.GONE);
                    tv_haveBirthday.setText("have birthday today.");
                }
                else if(count==3) {
                    String[] name = eventList.get(position).getTitle().split(",");
                    tv_name.setText(name[0]);
                    tv_count.setText(String.valueOf(count-1) );
                    tv_haveBirthday.setText("have birthday today.");
                }else{
                    tv_name.setText(eventList.get(position).getTitle());
                    ll_count.setVisibility(View.VISIBLE);
                    tv_count.setText(String.valueOf(count-1) );
                    tv_haveBirthday.setText("have birthday today.");
                }

//                if(eventList.get(position).getClubCategory().equalsIgnoreCase("1")){ //for club
//                    background.setImageDrawable(context.getResources().getDrawable(R.drawable.club_birhday));
//                }else {
//                    background.setImageDrawable(context.getResources().getDrawable(R.drawable.district_birthday));
//                }
        }else if(eventList.get(position).getType().equalsIgnoreCase("Anniversary")){
            ll_birthday.setVisibility(View.VISIBLE);
            img_event.setImageResource(R.drawable.anni);
            tv_name.setText(eventList.get(position).getTitle().toString());

            int count= Integer.parseInt(eventList.get(position).getTodaysCount());

            if(count==1){
                tv_name.setText(eventList.get(position).getTitle());
                ll_count.setVisibility(View.GONE);
                tv_haveBirthday.setText("has anniversary today.");
            }else if(count == 2){
                String name = eventList.get(position).getTitle().replace(","," & ");
                tv_name.setText(name);
                ll_count.setVisibility(View.GONE);
                tv_haveBirthday.setText("have anniversary today.");
            }
            else if(count==3) {
                String[] name = eventList.get(position).getTitle().split(",");
                tv_name.setText(name[0]);
                tv_count.setText(String.valueOf(count-1) );
                tv_haveBirthday.setText("have anniversary today.");
            }else{
                tv_name.setText(eventList.get(position).getTitle());
                ll_count.setVisibility(View.VISIBLE);
                tv_count.setText(String.valueOf(count-1) );
                tv_haveBirthday.setText("have anniversary today.");
            }

//            if(eventList.get(position).getClubCategory().equalsIgnoreCase("1")){ //for club
//                background.setImageDrawable(context.getResources().getDrawable(R.drawable.club_anniversary));
//            }else {
//                background.setImageDrawable(context.getResources().getDrawable(R.drawable.district_anniversary));
//            }
        }else if(eventList.get(position).getType().equalsIgnoreCase("Event")){
            ll_birthday.setVisibility(View.GONE);
            ll_event.setVisibility(View.VISIBLE);
            tv_eventTitle.setText(eventList.get(position).getTitle().toString());
            tv_eventDesc.setText(eventList.get(position).getDescription().toString());
            tv_eventDate.setVisibility(View.VISIBLE);
            tv_eventDate.setText(eventList.get(position).getTodaysCount().toString());
           // int c=rand.nextInt(3);
           // background.setImageResource(colors.get(c));
        }else if(eventList.get(position).getType().equalsIgnoreCase("blog")){
            ll_birthday.setVisibility(View.GONE);
            ll_event.setVisibility(View.VISIBLE);
            tv_eventTitle.setText(eventList.get(position).getDescription().toString());
            tv_eventDesc.setText(eventList.get(position).getDescription().toString());
            tv_eventDate.setVisibility(View.GONE);
            //background.setImageDrawable(context.getResources().getDrawable(R.drawable.blogs));
        }else if(eventList.get(position).getType().equalsIgnoreCase("news")){
            ll_birthday.setVisibility(View.GONE);
            ll_event.setVisibility(View.VISIBLE);
            tv_eventTitle.setText(eventList.get(position).getTitle().toString());
            String title=eventList.get(position).getDescription().toString();

            tv_eventDesc.setText(Html.fromHtml(title.replaceAll("(<(/)img>)|(<img.+?>)", "")));
//            tv_eventDesc.setVisibility(View.GONE);
            tv_eventDate.setVisibility(View.GONE);
           //background.setImageDrawable(context.getResources().getDrawable(R.drawable.news_rss));
        }

        rl_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("Birthday")){

                    Intent i = new Intent(context, CalenderActivity.class);
                    i.putExtra("Dashboard","B");
                    PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, eventList.get(position).getGrpId());
                    PreferenceManager.savePreference(context, PreferenceManager.GRP_PROFILE_ID, eventList.get(position).getGrpProfileID());
                    PreferenceManager.savePreference(context, MY_CATEGORY, eventList.get(position).getClubCategory());
                    PreferenceManager.savePreference(context,IS_GRP_ADMIN,eventList.get(position).getIsAdmin());
                    context.startActivity(i);

                }else if(type.equalsIgnoreCase("Anniversary")){
                    Intent i = new Intent(context, CalenderActivity.class);
                    i.putExtra("Dashboard","A");
                    PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, eventList.get(position).getGrpId());
                    PreferenceManager.savePreference(context, PreferenceManager.GRP_PROFILE_ID, eventList.get(position).getGrpProfileID());
                    PreferenceManager.savePreference(context, MY_CATEGORY, eventList.get(position).getClubCategory());
                    PreferenceManager.savePreference(context,IS_GRP_ADMIN,eventList.get(position).getIsAdmin());
                    context.startActivity(i);
                }else if(type.equalsIgnoreCase("Event")){
                    Intent i = new Intent(context, CalenderActivity.class);
                    i.putExtra("Dashboard","E");
                    PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, eventList.get(position).getGrpId());
                    PreferenceManager.savePreference(context, PreferenceManager.GRP_PROFILE_ID, eventList.get(position).getGrpProfileID());
                    PreferenceManager.savePreference(context, MY_CATEGORY, eventList.get(position).getClubCategory());
                    PreferenceManager.savePreference(context,IS_GRP_ADMIN,eventList.get(position).getIsAdmin());
                    context.startActivity(i);
                }else if(type.equalsIgnoreCase("blog")){
                    Intent i = new Intent(context, ShowFeedActivity.class);

                    i.putExtra("link", eventList.get(position).getLink());
                    i.putExtra("modulename", "Rotary Blog");
                    i.putExtra("description", eventList.get(position).getDescription());

                    context.startActivity(i);
                }else if(type.equalsIgnoreCase("news")){
                    Intent i = new Intent(context, ShowFeedActivity.class);

                    i.putExtra("link", eventList.get(position).getLink());
                    i.putExtra("modulename", "Rotary News & Updates");
                    i.putExtra("description",eventList.get(position).getDescription());

                    context.startActivity(i);
                }
            }
        });



        view.addView(imageLayout);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
