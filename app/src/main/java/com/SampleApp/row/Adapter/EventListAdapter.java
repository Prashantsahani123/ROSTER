package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.EventListData;
import com.SampleApp.row.Data.NotificationCountData;
import com.SampleApp.row.EventDetails;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 02-02-2016.
 */
public class EventListAdapter extends ArrayAdapter<EventListData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<EventListData> eventListDatas = new ArrayList<EventListData>();
    MarshMallowPermission marshMallowPermission;// = new MarshMallowPermission(mContext);
    NotificationCountData notificationCountData;

    public static int count_read_events = 0;


    public EventListAdapter(Context mContext, int layoutResourceId, ArrayList<EventListData> eventListDatas) {
        super(mContext, layoutResourceId, eventListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.eventListDatas = eventListDatas;
    }

//    public void setGridData(ArrayList<DirectoryData> directoryDatas) {
//        this.eventListDatas = eventListDatas;
//        notifyDataSetChanged();
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();



            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.iv_cornerimg = (ImageView) row.findViewById(R.id.iv_cornerimg);
            holder.eventTitle = (TextView) row.findViewById(R.id.tv_event_title);
            holder.eventDateTime = (TextView) row.findViewById(R.id.tv_datetime);
            holder.venue = (TextView) row.findViewById(R.id.tv_venue);
            holder.tv_publish = (TextView) row.findViewById(R.id.tv_publish);
            holder.tv_date = (TextView) row.findViewById(R.id.tv_date);
            holder.tv_month = (TextView) row.findViewById(R.id.tv_month);
            holder.tv_time = (TextView) row.findViewById(R.id.tv_time);
            holder.iv_reminder = (ImageView) row.findViewById(R.id.iv_reminder);
            holder.iv_location = (ImageView) row.findViewById(R.id.iv_location);
            holder.iv_reminder.setTag(position);
            row.setTag(holder);


        } else {
            holder = (ViewHolder) row.getTag();
        }

        final EventListData item = eventListDatas.get(position);

        /*holder.textView1.setText(item.getImage());
        //holder.textView1.setText(Html.fromHtml(item.getImage_name()));
        Picasso.with(mContext).load(item.getImage())
                .placeholder(R.drawable.loading_icon)
                        //    .centerCrop()
                .resize(Constant.height, Constant.width)
                .into(holder.imageView);*/


        holder.eventTitle.setText(item.getEventTitle());
        holder.eventDateTime.setText(item.getEventDateTime());
        holder.venue.setText(item.getVenue());
        // tv_publish

        if (item.getFilterType().equals("1")) //1-publish , 2-unpublish, 3-expired
        {
            holder.tv_publish.setText("PUBLISHED");
        } else if (item.getFilterType().equals("2")) {
            holder.tv_publish.setText("UN-PUBLISHED");
        } else if (item.getFilterType().equals("3")) {
            holder.tv_publish.setText("EXPIRED");
        }

        if (item.getMyResponse().equals("0")) {
            holder.iv_cornerimg.setVisibility(View.GONE);
        } else if (item.getMyResponse().equals("1")) {
            holder.iv_cornerimg.setVisibility(View.VISIBLE);
            holder.iv_cornerimg.setImageResource(R.drawable.corner_img);
        } else if (item.getMyResponse().equals("2")) {
            holder.iv_cornerimg.setVisibility(View.VISIBLE);
            holder.iv_cornerimg.setImageResource(R.drawable.corner_img_red);
        } else if (item.getMyResponse().equals("3")) {
            holder.iv_cornerimg.setVisibility(View.VISIBLE);
            holder.iv_cornerimg.setImageResource(R.drawable.corner_img_gray);
        } else {
            holder.iv_cornerimg.setVisibility(View.GONE);
        }

        if (item.getIsRead().equalsIgnoreCase("no") && item.getFilterType().equals(Constant.FILTER_TYPE_PUBLISHED)) {
            holder.eventTitle.setTextColor(mContext.getResources().getColor(R.color.bluecolor));
        } else {
            holder.eventTitle.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        holder.iv_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setreminder(item);
            }
        });
        holder.iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marshMallowPermission = new MarshMallowPermission((Activity)mContext);

                /*Intent i = new Intent(mContext, Map.class);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                bundle.putString("Long", item.getVenueLon());
                bundle.putString("Lat", item.getVenueLat());
                i.putExtras(bundle);*/
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + item.getVenue()));

                if (!marshMallowPermission.checkPermissionForLocation()) {
                    marshMallowPermission.requestPermissionForLocation();
                }else {
                   // mContext.startActivity(i);
                    mContext.startActivity(intent);
                }

               /* if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mContext.startActivity(i);
                }*/
            }
        });


        ///DATE TIME IN THE INDICATOR
        String[] separated = item.getEventDateTime().split(" ");
        holder.tv_date.setText("" + separated[0]);
        holder.tv_month.setText("" + separated[1].toUpperCase());
        holder.tv_time.setText("" + separated[3] + " " + separated[4]);

       /* if (item.getEventImg().equals("") || item.getEventImg() == null || item.getEventImg().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.imageplaceholder);
        } else {
            Picasso.with(mContext).load(item.getEventImg())
                    .placeholder(R.drawable.imageplaceholder)
                    .into(holder.imageView);
        }*/

      /*  holder.eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setreminder();
            }
        });*/
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(mContext)) {
                    Intent i = new Intent(mContext, EventDetails.class);
                    i.putExtra("eventid", item.getEventID());

                    if (!eventListDatas.get(position).getIsRead().equalsIgnoreCase("Yes") ) {
                        if ( !eventListDatas.get(position).getFilterType().equals(Constant.FilterTypes.FILTER_TYPE_EXPIRED))
                            count_read_events++;
                    }

                    ((Activity) mContext).startActivityForResult(i, 1);
                } else {
                    Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return row;
        // return super.getView(position, convertView, parent);
    }

    private void setreminder(EventListData item) {
        //String givenDateString = "16 Mar 2016 10:00 AM";
        String givenDateString = item.getEventDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
      //  Log.d("TOUCHbASE", "DATE -- " + cal);
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", timeInMilliseconds+60*60*1000);
        intent.putExtra("beginTime", timeInMilliseconds+60*60*1000);
        intent.putExtra("title", item.getEventTitle());
        //intent.putExtra("description", "This is a sample description");
        intent.putExtra("eventLocation", item.getVenue());
        mContext.startActivity(intent);
//-----------------------------------
     /*   Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 2);
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Some Test Event");
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);
        intent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                cal.getTime().getTime());
        intent.putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                cal.getTime().getTime() + 600000);
        intent.putExtra(
                Intent.EXTRA_EMAIL,
                "attendee1@yourtest.com, attendee2@yourtest.com");
        mContext.startActivity(intent);*/
        //-----------------------------------
      /*  Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT-1"));
        Date dt = null;
        Date dt1 = null;
        try {
            dt = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2016-04-12 11:11");
            dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2016-04-12 11:15");

            Calendar beginTime = Calendar.getInstance();
            cal.setTime(dt);

            // beginTime.set(2013, 7, 25, 7, 30);
            beginTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE));

            Calendar endTime = Calendar.getInstance();
            cal.setTime(dt1);

            // endTime.set(2013, 7, 25, 14, 30);
            // endTime.set(year, month, day, hourOfDay, minute);
            endTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE));

            ContentResolver cr = this.mContext.getContentResolver();
            ContentValues values = new ContentValues();

            values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
            values.put(CalendarContract.Events.TITLE, "NMAW");
            values.put(CalendarContract.Events.DESCRIPTION, "Party");
            values.put(CalendarContract.Events.CALENDAR_ID, "2");
            // values.put(Events._ID, meeting_id);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.d("TOUCHBASE","@@@@@@:- "+eventID);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("TOUCHBASE","CATGCK:- "+e.toString());
        }*/
    }


    static class ViewHolder {
        TextView eventTitle, eventDateTime, venue, tv_publish,tv_date,tv_month,tv_time;
        ImageView imageView, iv_cornerimg,iv_reminder,iv_location;
    }



}
