package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NEWROW.row.AdminSubModuleActivity;
import com.NEWROW.row.Data.ModuleDataAdmin;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.SquareImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class GroupDashboardAdminAdapter extends RecyclerView.Adapter<GroupDashboardAdminAdapter.MyHolder>{

    private ArrayList<ModuleDataAdmin> list;
    private Context context;
    private String groupId="";

    public GroupDashboardAdminAdapter(ArrayList<ModuleDataAdmin> moduleList,Context ctx,String grId) {
        list = moduleList;
        context = ctx;
        groupId = grId;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       final ModuleDataAdmin moduleDataAdmin = list.get(position);

        holder.text.setText(moduleDataAdmin.getTitle());

        if ((context.getResources().getDisplayMetrics().density) <= 2.0) {

            Picasso.with(context).load(moduleDataAdmin.getImgUrl())
                    .placeholder(R.drawable.placeholder_new)
                    //.resize(150,150)
                    .into(holder.picture);
        } else {

            Picasso.with(context).load(moduleDataAdmin.getImgUrl())
                    .placeholder(R.drawable.placeholder_new)
                    .resize(150, 150)
                    .into(holder.picture);
        }

        holder.picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int moduleId = Integer.parseInt(moduleDataAdmin.getModuleID());

                Intent i = new Intent(context, AdminSubModuleActivity.class);
                i.putExtra("link", moduleDataAdmin.getUrl());
                i.putExtra("modulename", moduleDataAdmin.getTitle());

                context.startActivity(i);

                /*switch (moduleId){
                    case 1: //Directory
                        i = new Intent(context, DirectoryActivity.class);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                    case 2: //Events
                        i = new Intent(context, Events.class);
                        i.putExtra("GroupID", groupId);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                    case 3: //Announcements
                        i = new Intent(context, Announcement.class);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        i.putExtra("GroupID", groupId);
                        i.putExtra("moduleID", ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                    case 4: //Newsletters
                        i = new Intent(context, E_Bulletin.class);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        i.putExtra("GroupID", groupId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                    case 8: // Gallery
                        i = new Intent(context, Gallery.class);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                    case 9: // Documents
                        i = new Intent(context, Documents.class);
                        i.putExtra("moduleName", moduleDataAdmin.getTitle());
                        i.putExtra("GroupID", groupId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, ""+moduleId);
                        PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, moduleDataAdmin.getTitle());
                        break;
                     default:
                         i = new Intent(context, AdminSubModuleActivity.class);
                         i.putExtra("link", moduleDataAdmin.getUrl());
                         i.putExtra("modulename", moduleDataAdmin.getTitle());
                         break;
                }*/
            }
        });

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupdashboardgrid_item,parent,false);
        return new MyHolder(v);
    }

     class MyHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public SquareImageView picture;

        public MyHolder(View v) {
            super(v);
            text = (TextView)v.findViewById(R.id.text);
            picture = (SquareImageView)v.findViewById(R.id.picture);
        }
    }
}
