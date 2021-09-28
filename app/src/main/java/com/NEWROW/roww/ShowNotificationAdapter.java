package com.NEWROW.row;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Utils.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowNotificationAdapter extends RecyclerView.Adapter<ShowNotificationAdapter.ShowNotificationViewHolder> {


    private ArrayList<NotificationList> mNotificationList;
    private Context mContext;

    Display display;
    WindowManager wm;


    private static int row_index = -1;

    public ShowNotificationAdapter(ArrayList<NotificationList> mNotificationList, Context mContext) {
        this.mNotificationList = mNotificationList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ShowNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notification_display, parent, false );
        ShowNotificationViewHolder viewHolder = new ShowNotificationViewHolder( v );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowNotificationViewHolder holder, final int position) {

        final NotificationList curentList = mNotificationList.get( position );
       String tt = curentList.getEvent_title();
        String et =  curentList.getEntityname();

        tt = tt.replace("'", "");


        et = et.replace("'", "");

        holder.titletxt.setText(tt);
        // holder.bodytxt.setText( curentList.getEvent_description() );
        holder.bodytxt.setText( curentList.getEvent_description() );


        holder.timelist.setText( curentList.getTime() );


        String dateInMonth = null;
        dateInMonth = convertDateIntoMonth( curentList.getDate() );
        holder.datelist.setText( dateInMonth );

      //  holder.bodytxt.setText( curentList.getEntityname() );
        holder.bodytxt.setText( et );
        if(et == null)
        {
            holder.bodytxt.setVisibility(View.GONE);
        }
        else if(et.isEmpty() || et.equalsIgnoreCase("") )
        {
            holder.bodytxt.setVisibility(View.GONE);
        }
        else
        {
            holder.bodytxt.setVisibility(View.VISIBLE);
        }

        holder.datetxt.setText( dateInMonth + " " + curentList.getTime() );


/*        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateInMonth = convertDateIntoMonth( curentList.getDate() );
            holder.datelist.setText( dateInMonth );

        } else {
            holder.datelist.setText( curentList.getDate() );

        }*/
        // holder.datelist.setText( curentList.getDate() );
        holder.datelist.setText( dateInMonth );
        holder.modulename.setText( curentList.getEntityname() );

        final String currentMessageId = curentList.getMessageId();
        final String flag = curentList.getFlag();

        if (flag.equals( "1" )) {
            //light
            holder.titletxt.setTextColor( Color.parseColor( "#706F6F" ) );//0000FF//a9a9a9
           // holder.read.setText("");

        } else {
            //bold
            holder.titletxt.setTextColor( Color.parseColor( "#000000" ) );
          //  holder.read.setText("unread");
         //   holder.read.setTextColor( Color.parseColor( "#87CEEB" ) );
          //  read

            //87CEEB//000000
        }

        final DatabaseHelper databaseHelpers = new DatabaseHelper( mContext );

        //this is added by Gaurav for Notification Image display
        if (curentList.getType().equalsIgnoreCase( "Event" )) {
            //added By Gaurav for Image
            holder.iv_notification.setImageResource( R.drawable.event );
        } else if (curentList.getType().equalsIgnoreCase( "ann" )) {
            //added By Gaurav for Image annoucement
            holder.iv_notification.setImageResource( R.drawable.bw_annoucement );
        } else if (curentList.getType().equalsIgnoreCase( "Calender" )) {
            //added By Gaurav for Image

            if (curentList.getVenue().equalsIgnoreCase( "B" )) {
                holder.iv_notification.setImageResource( R.drawable.birthday );
            } else if (curentList.getVenue().equalsIgnoreCase( "A" )) {
                holder.iv_notification.setImageResource( R.drawable.anniversary );
            }
        } else if (curentList.getType().equalsIgnoreCase( "Doc" )) {
            //added By Gaurav for Image Document
            holder.iv_notification.setImageResource( R.drawable.bw_document );
        } else if (curentList.getType().equalsIgnoreCase( "Activity" ) || curentList.getType().equalsIgnoreCase( "Gallery" )) {
            // For club
            //added By Gaurav for Image Project

            //  Activity for Club
            //Gallery for District

            if (curentList.getEvent_title().contains( "A new club meeting" ) || curentList.getEvent_title().contains( "A new district event" )) {
                //District Data Image
                holder.iv_notification.setImageResource( R.drawable.bw_club_district_event );

            } else {
                //Club Data Image
                //District and Service Project
                holder.iv_notification.setImageResource( R.drawable.bw_service_district_project );


            }
        } else if (curentList.getType().equalsIgnoreCase( "PopupNoti" )) {
            //added By Gaurav for Image Greetings
            // use BAType as a groupType 1-birthday , 2-Annivasary
            if (curentList.getGrouptype().equalsIgnoreCase( "1" )) {
                holder.iv_notification.setImageResource( R.drawable.birthday );
            } else if (curentList.getGrouptype().equalsIgnoreCase( "2" )) {
                holder.iv_notification.setImageResource( R.drawable.anniversary );

            } else {
                holder.iv_notification.setImageResource( R.drawable.bw_greetings );


            }
        } else if (curentList.getType().equalsIgnoreCase( "Ebulletin" )) {
            //added By Gaurav for Image NewsLetter

            holder.iv_notification.setImageResource( R.drawable.bw_newsletter );

        }


        holder.card.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(),"you have Clicked  " +position,Toast.LENGTH_LONG ).show();

                row_index = position;

                //  notifyDataSetChanged();

                if (row_index == position) {
                    // holder.card.setBackgroundColor(Color.parseColor("#567845"));
                    // holder.titletxt.setTextColor(Color.parseColor("#a9a9a9"));
                    holder.titletxt.setTextColor( Color.parseColor( "#706F6F" ) );//706F6F//a9a9a9


                    //update Data into Database
                    boolean notificationInsert = databaseHelpers.updateData( curentList.getMessageId() );
                    String type = curentList.getType();

                    if (type.equalsIgnoreCase( "Event" )) {

                        Intent intent = new Intent( mContext, EventDetails.class );


                        intent.putExtra("notificationkey","yes");
                        intent.putExtra( "eventid", curentList.getEvent_id() );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        // intent.putExtra("memID", curentList.getMem_id());
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );
                        intent.putExtra( "fromNotification", curentList.getFromnotification() );
                        intent.putExtra( "eventImg", curentList.getEventimg() );
                        intent.putExtra( "eventTitle", curentList.getEvent_title() );
                        intent.putExtra( "eventDesc", curentList.getEvent_description() );
                        intent.putExtra( "venue", curentList.getVenue() );
                        intent.putExtra( "reglink", curentList.getReglink() );
                        intent.putExtra( "eventDate", curentList.getEventdate() );
                        intent.putExtra( "rsvpEnable", curentList.getRsvpenable() );
                        intent.putExtra( "goingCount", curentList.getGoingcount() );
                        intent.putExtra( "maybeCount", curentList.getMaybecount() );
                        intent.putExtra( "notgoingCount", curentList.getNotgoingcount() );
                        intent.putExtra( "totalCount", curentList.getTotalcount() );
                        intent.putExtra( "myResponse", curentList.getMyresponse() );
                        intent.putExtra( "questionType", curentList.getQuestiontype() );
                        intent.putExtra( "questionText", curentList.getQuestiontext() );
                        intent.putExtra( "questionID", curentList.getQuestionid() );
                        intent.putExtra( "isQuesEnable", curentList.getIsquesenable() );
                        intent.putExtra( "option1", curentList.getOption1() );
                        intent.putExtra( "option2", curentList.getOption2() );
                        intent.putExtra( "entity name", curentList.getEntityname() );
                        intent.putExtra( "group_category", curentList.getGroupcategory() );

                        //This code is added by Gaurav
                        //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                        if (curentList.getGroup_id().equalsIgnoreCase( "-1" )) {
                            PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "Yes" );
                            //Consider MasterUId as profile id in Rotary India admin  module
                            Long masterUid = Long.parseLong( PreferenceManager.getPreference( mContext, PreferenceManager.MASTER_USER_ID ) );

                            String profile_id = String.valueOf( PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, String.valueOf( masterUid ) ) );
                            intent.putExtra( "memID", profile_id );
                            intent.putExtra("memIDnew",curentList.getMem_id());


                        } else {
                            intent.putExtra( "memID", curentList.getMem_id() );

                            PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "No" );

                        }
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "ann" )) {


                        Intent intent = new Intent( mContext, Announcement_details.class );

                        intent.putExtra( "announcemet_id", curentList.getEvent_id() );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        intent.putExtra( "memID", curentList.getMem_id() );
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );
                        intent.putExtra( "moduleID", curentList.getModuleid() );
                        intent.putExtra( "fromNotification", curentList.getFromnotification() );
                        intent.putExtra( "ann_img", curentList.getEventimg() );
                        intent.putExtra( "ann_title", curentList.getEvent_title() );
                        intent.putExtra( "Ann_date", curentList.getEventdate() );
                        intent.putExtra( "ann_lnk", curentList.getReglink() );
                        intent.putExtra( "ann_desc", curentList.getEvent_description() );
                        intent.putExtra( "entity name", curentList.getEntityname() );
                        intent.putExtra( "group_category", curentList.getGroupcategory() );

                        try {
                            intent.putExtra( "moduleName", curentList.getModulename() );
                            PreferenceManager.savePreference( mContext, PreferenceManager.MODUEL_NAME, "Announcement" );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "Ebulletin" )) {


                        Intent intent = new Intent( mContext, E_Bulletin.class );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        //This below intent added base on grpID if grpID="-1" then pass memID=profile id

                        //  intent.putExtra("memID", data.get("memID"));
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );

                        try {
                            PreferenceManager.savePreference( mContext, PreferenceManager.MODUEL_NAME, "Newsletters" );
                            //This code is added by Gaurav
                            //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                            if (curentList.getGroup_id().equalsIgnoreCase( "-1" )) {
                                PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "Yes" );
                                //Consider MasterUId as profile id in Rotary India admin  module
                                Long masterUid = Long.parseLong( PreferenceManager.getPreference( mContext, PreferenceManager.MASTER_USER_ID ) );

                                String profile_id = String.valueOf( PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, String.valueOf( masterUid ) ) );
                                intent.putExtra( "memID", profile_id );


                            } else {

                                PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, curentList.getMem_id() );

                                intent.putExtra( "memID", curentList.getMem_id() );

                                PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "No" );

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "AddMember" )) {


                        Intent intent = new Intent( mContext, Profile.class );
                        intent.putExtra( "typeID", curentList.getEvent_id() );
                        intent.putExtra( "memID", curentList.getMem_id() );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "RemoveMember" )) {

                        Intent intent = new Intent( mContext, Splash.class ); // Check it
                        mContext.startActivity( intent );


                    } else if (type.equalsIgnoreCase( "Doc" )) {
                        // holder.iv_notification.setImageResource(R.drawable.defaultlogo);


                        Intent intent = new Intent( mContext, Documents.class );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        //This below intent added base on grpID if grpID="-1" then pass memID=profile id
                        // intent.putExtra("memID", data.get("memID"));
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );

                        try {
                            PreferenceManager.savePreference( mContext, PreferenceManager.MODUEL_NAME, "Documents" );
                            //This code is added by Gaurav
                            //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                            if (curentList.getGroup_id().equalsIgnoreCase( "-1" )) {
                                PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "Yes" );
                                //Consider MasterUId as profile id in Rotary India admin  module
                                Long masterUid = Long.parseLong( PreferenceManager.getPreference( mContext, PreferenceManager.MASTER_USER_ID ) );

                                String profile_id = String.valueOf( PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, String.valueOf( masterUid ) ) );
                                intent.putExtra( "memID", profile_id );


                            } else {
                                PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, curentList.getMem_id() );

                                intent.putExtra( "memID", curentList.getMem_id() );

                                PreferenceManager.savePreference( mContext, PreferenceManager.isRIadminModule, "No" );

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "EditGroup" )) {

                        Intent intent = new Intent( mContext, GroupInfo_New.class );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        intent.putExtra( "memID", curentList.getMem_id() );
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "imp" )) {

                        Intent intent = new Intent( mContext, Improvement_details.class );
                        intent.putExtra( "improvementId", curentList.getEvent_id() );
                        intent.putExtra( "grpID", curentList.getGroup_id() );
                        intent.putExtra( "memID", curentList.getMem_id() );
                        intent.putExtra( "isAdmin", curentList.getIsadmin() );
                        mContext.startActivity( intent );


                    } else if (type.equalsIgnoreCase( "Calender" )) {

//

                        Intent intent = new Intent( mContext, CalenderActivity.class );

                        PreferenceManager.savePreference( mContext, PreferenceManager.GROUP_ID, curentList.getGroup_id() );
                        PreferenceManager.savePreference( mContext, PreferenceManager.GRP_PROFILE_ID, curentList.getMem_id() );
                        PreferenceManager.savePreference( mContext, PreferenceManager.MY_CATEGORY, curentList.getGrouptype() );

                        intent.putExtra( "fromNoti", curentList.fromnotification );
                        intent.putExtra( "date", curentList.getEventdate() );
                        intent.putExtra( "type", curentList.getVenue() );//for celebration type we use venue
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "Activity" )) {// For club

                        Intent intent = new Intent( mContext, GalleryDescription.class );
                        intent.putExtra( "albumID", curentList.getEvent_id() );
                        intent.putExtra( "fromNoti", curentList.getFromnotification() );
                        mContext.startActivity( intent );

                    } else if (type.equalsIgnoreCase( "Gallery" )) {// For district
                        Intent intent = new Intent( mContext, GalleryDescription.class );
                        intent.putExtra( "albumID", curentList.getEvent_id() );
                        intent.putExtra( "fromNoti", curentList.getFromnotification() );
                        mContext.startActivity( intent );
                    } else if (type.equalsIgnoreCase( "PopupNoti" )) {
                        Intent intent = new Intent( mContext, DashboardActivity.class );
                        intent.putExtra( "title", curentList.getEvent_title() );
                        intent.putExtra( "message", curentList.getEvent_description() );
                        intent.putExtra( "fromNotification", curentList.getFromnotification() );
                        intent.putExtra( "BAType", curentList.getGrouptype() );// use BAType as a groupType 1-birthday , 2-Annivasary
                        mContext.startActivity( intent );

                    }


                  /*  //Open Notification class
                    Intent intent=new Intent(mContext,OpenNotification.class);
                    intent.putExtra("title",curentList.getTitle());
                    intent.putExtra("message",curentList.getBody());
                    intent.putExtra("state","");
                    mContext.startActivity(intent);*/

                } else {
                    //  holder.card.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder.titletxt.setTextColor( Color.parseColor( "#000000" ) );
                }


            }
        } );


    }


    private String convertDateIntoMonth(String date) {

        DateFormat inputFormat = new SimpleDateFormat( "dd-MM-yyyy" );
        DateFormat outputFormat = new SimpleDateFormat( "dd MMM yyyy" );
        String inputDateStr = "2013-06-24";
        Date dateInMonth = null;
        String outputDateStr = null;

        try {
            dateInMonth = inputFormat.parse( date );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr = outputFormat.format( dateInMonth );


/*
        String actualDate = date;
        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern( "dd-MM-yyyy", Locale.ENGLISH );
        }
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern( "dd MMM yyyy", Locale.ENGLISH );
        LocalDate ld = LocalDate.parse( actualDate, dtf );
        String month_name_date = dtf2.format( ld );
        System.out.println( month_name_date ); // Mar 2016
        return month_name_date;
*/
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }


    public class ShowNotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titletxt, read;
        TextView bodytxt, datelist, timelist, modulename, datetxt;
        RelativeLayout card;
        ImageView iv_notification;

        public ShowNotificationViewHolder(View itemView) {
            super( itemView );
            titletxt = itemView.findViewById( R.id.notification_title );
          //  read =  itemView.findViewById( R.id.read );
            bodytxt = itemView.findViewById( R.id.notification_message );
            datetxt = itemView.findViewById( R.id.notification_message_date );
            datelist = itemView.findViewById( R.id.datelist );
            timelist = itemView.findViewById( R.id.timelist );
            modulename = itemView.findViewById( R.id.tv_modulename );
            iv_notification = itemView.findViewById( R.id.iv_notification );
            card = itemView.findViewById( R.id.card );
        }
    }
}
