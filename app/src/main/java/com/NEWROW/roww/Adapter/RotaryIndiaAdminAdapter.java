package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.ModuleData.RotaryIndiaAdminModule;
import com.NEWROW.row.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.NEWROW.row.FragmentALL.notificationCountDatas;

public class RotaryIndiaAdminAdapter extends RecyclerView.Adapter<RotaryIndiaAdminAdapter.RotaryAdminHolder> {

    private Context context;
    private ArrayList<RotaryIndiaAdminModule> arrayList;

    public RotaryIndiaAdminAdapter(Context context, ArrayList<RotaryIndiaAdminModule> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RotaryAdminHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rotarylayout_item, viewGroup, false);
        RotaryAdminHolder viewHolder = new RotaryAdminHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RotaryAdminHolder rotaryAdminHolder, final int position) {

        RotaryAdminHolder rHolder = (RotaryAdminHolder) rotaryAdminHolder;
        RotaryIndiaAdminModule data = (RotaryIndiaAdminModule) arrayList.get(position);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Resources r = context.getResources();

        int sp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                r.getDimension(R.dimen.text_size_3_sp),
                r.getDisplayMetrics()
        );

        if(arrayList.size() % 2 == 0) {
            if(position % 2 == 0) {
                layoutParams.setMargins(0,0,sp,sp);
            }else {
                layoutParams.setMargins(0,0,0,sp);
            }
        }else {
            if(position == arrayList.size()-1) {
                layoutParams.setMargins(sp,0,sp,0);
            }else {
                if(position % 2 == 0) {
                    layoutParams.setMargins(sp,0,sp,sp);
                }else {
                    layoutParams.setMargins(0,0,sp,sp);
                }
            }
        }

        rHolder.ll_holder.setLayoutParams(layoutParams);


        rHolder.tv_title.setText(data.getModuleName());

        if (data.getImage().trim().length() == 0 || data.getImage() == null || data.getImage().isEmpty()) {
            // gHolder.iv_img.setImageResource(R.drawable.app_icon_big);
            rHolder.iv_img.setImageResource(R.drawable.defaultlogo);
        } else {
            Picasso.with(context).load(data.getImage())
                    .placeholder(R.drawable.defaultlogo)
                    .resize(150, 150)
                    .into(rHolder.iv_img);
        }

        //This is added By Gaurav for notification coming

        int count = notificationCountDatas.getGroupCount(arrayList.get(position).getGroupId());

        if (count == 0) {
            rHolder.tv_group_count.setVisibility(View.GONE);
        } else {
            rHolder.tv_group_count.setVisibility(View.VISIBLE);
            rHolder.tv_group_count.setText("" + notificationCountDatas.getGroupCount(arrayList.get(position).getGroupId()));
            rHolder.tv_group_count.setBackgroundResource(R.drawable.notification_count);
        }




        rHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGroupSelectedListener != null) {
                    onGroupSelectedListener.onGroupSelected(position);
                }
            }
        });

    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 2)
//            IS_THIRD_POSITION = 1;
//        return super.getItemViewType(position);
        return 0;
    }

    public class RotaryAdminHolder extends RecyclerView.ViewHolder{
        ImageView iv_img;
        TextView tv_title,tv_group_count;
        LinearLayout ll_holder;

        public RotaryAdminHolder(@NonNull View itemView) {
            super(itemView);
            iv_img=itemView.findViewById(R.id.image_view);
            tv_title=(TextView) itemView.findViewById(R.id.text_view);
            tv_group_count=(TextView) itemView.findViewById(R.id.group_count);
            ll_holder=(LinearLayout) itemView.findViewById(R.id.ll_holder);
        }
    }

    public void setOnGroupSelectedListener(RotaryIndiaAdminAdapter.OnGroupSelectedListener onGroupSelectedListener) {
        this.onGroupSelectedListener = onGroupSelectedListener;
    }


    private RotaryIndiaAdminAdapter.OnGroupSelectedListener onGroupSelectedListener;

    public interface OnGroupSelectedListener {
        void onGroupSelected(int position);
    }

}
