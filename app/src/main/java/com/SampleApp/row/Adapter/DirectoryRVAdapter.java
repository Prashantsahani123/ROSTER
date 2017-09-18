package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.ProfileMasterHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by USER1 on 25-03-2017.
 */
public class DirectoryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ProfileMasterData> list;
    String flag; // 0-Directroy 1-globalsearch --> To hide the contents


    public DirectoryRVAdapter(Context context, ArrayList<ProfileMasterData> list, String flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.directory_item, parent, false);
            ProfileMasterHolder holder = new ProfileMasterHolder(view);
            return holder;
        } catch(NullPointerException npe) {
            Utils.log("Error is : "+npe);
            npe.printStackTrace();
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindProfileData(holder, position);
    }

    public void bindProfileData(RecyclerView.ViewHolder holder, final int position) {
        if ( list.get(position) instanceof ProfileMasterData) {
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

            if ( data.getIsPersonalDetVisible().equals("yes")) {
                pHolder.getTvMobileNo().setText(data.getMemberMobile());
            } else {
                pHolder.getTvMobileNo().setText("");
            }

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
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getProfileId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnMemberSelectedListener{
        public void onMemberSelected(ProfileMasterData data, int position);
    }

    public void setOnMemberSelectedListener(OnMemberSelectedListener onMemberSelectedListener) {
        this.onMemberSelectedListener = onMemberSelectedListener;
    }

    OnMemberSelectedListener onMemberSelectedListener;
}
