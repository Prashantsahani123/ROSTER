package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.NEWROW.row.Data.profiledata.ProfileMasterData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.ProfileMasterHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 22-01-2018.
 */

public class DistrictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    ArrayList<ProfileMasterData> list;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private String module="";

    public DistrictAdapter(Context context, ArrayList<ProfileMasterData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try {

            if(viewType==VIEW_TYPE_ITEM){
                module="ITEM";
                View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.directory_item, parent, false);
                ProfileMasterHolder holder = new ProfileMasterHolder(view);
                return holder;
            } else {
                module="LOAD";
                View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_item, parent, false);
                ProfileMasterHolder holder = new ProfileMasterHolder(view);
                return holder;
            }

        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (module.equalsIgnoreCase("ITEM")) {

            if (list.get(position) instanceof ProfileMasterData) {

                ProfileMasterHolder pHolder = (ProfileMasterHolder) holder;

                final ProfileMasterData data = list.get(position);

                if (data.getProfilePic().trim().length() == 0 || data.getProfilePic().equals("") || data.getProfilePic() == null || data.getProfilePic().isEmpty()) {
                    pHolder.getIvProfilePic().setImageResource(R.drawable.profile_pic);
                } else {
                    Picasso.with(context).load(data.getProfilePic())
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.profile_pic)
                            .into(pHolder.getIvProfilePic());
                }

                pHolder.getTvMemberName().setText(data.getMemberName());

                pHolder.getTvMobileNo().setText(data.getMemberMobile());

                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if ( onMemberSelectedListener != null ) {
                            onMemberSelectedListener.onMemberSelected(data, position);
                        } else {
                            Toast.makeText(context, "Event not registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        } else {
//            ProfileMasterHolder pHolder = (ProfileMasterHolder) holder;
//            pHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public ArrayList<ProfileMasterData> getArrayList(){
        return list;
    }

    public interface OnMemberSelectedListener{
        public void onMemberSelected(ProfileMasterData data, int position);
    }

    public void setOnMemberSelectedListener(OnMemberSelectedListener onMemberSelectedListener) {
        this.onMemberSelectedListener = onMemberSelectedListener;
    }

    OnMemberSelectedListener onMemberSelectedListener;
}
