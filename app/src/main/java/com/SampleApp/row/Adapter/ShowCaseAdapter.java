package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.R;
import com.SampleApp.row.holders.ShowCaseCatHolder;

import java.util.ArrayList;

/**
 * Created by Admin on 10-04-2018.
 */

public class ShowCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<AlbumData> list=new ArrayList<>();
    String categories="0";
    public Context context;
    private static LayoutInflater inflater = null;
    boolean isSelectedAll;
    public  ShowCaseAdapter(ArrayList<AlbumData> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cat_item, parent, false);
        ShowCaseCatHolder holder = new ShowCaseCatHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final AlbumData data=list.get(position);
        final ShowCaseCatHolder catHolder= (ShowCaseCatHolder) holder;

        catHolder.txt_title.setText(data.getCat_name());

        if(position==0){
            catHolder.cbCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = catHolder.cbCat.isChecked();
                    if(isChecked) {
                        selectAll(true);
                    }else {
                        selectAll(false);
                    }
                }
            } );
        }else{

            catHolder.cbCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = catHolder.cbCat.isChecked();

                    if(!isChecked){
                        list.get(0).setSelected(false);
                        list.get(position).setSelected(false);
                        notifyDataSetChanged();
                    }else {
                        int count = 0;
                        list.get(position).setSelected(true);
                        for(int i=1;i<list.size();i++){
                            AlbumData data1 = list.get(i);
                            if(!data1.isSelected()){
                                count++;
                            }
                        }

                        if(count==0){
                            list.get(0).setSelected(true);

                            //list.get(position).setSelected(false);
                            notifyDataSetChanged();
                        }else{
                            list.get(0).setSelected(false);
                            //list.get(position).setSelected(false);
                            notifyDataSetChanged();
                        }
                    }
                }
            });

        }


        if (!data.isSelected()){
            catHolder.cbCat.setChecked(false);
            catHolder.cbCat.setButtonDrawable(context.getResources().getDrawable(R.drawable.circle_checkbox_unchecked));
        }
        else{
            catHolder.cbCat.setChecked(true);
            catHolder.cbCat.setButtonDrawable(context.getResources().getDrawable(R.drawable.circle_checkbox_checked));
        }


//        catHolder.ll_catitem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, Gallery.class);
//                i.putExtra("moduleName", data.getCat_name());
//                i.putExtra("catid", data.getCat_id());
//                context.startActivity(i);
//            }
//        });

    }

    public String getCategories(){
        return  categories;
    }

    public ArrayList<AlbumData> getCategoryList(){
        return list;
    }
    public void selectAll(boolean check){
        Log.e("onClickSelectAll","yes");
        categories="0";
        for (AlbumData data : list){
            data.setSelected(check);
        }
        //isSelectedAll=check;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
