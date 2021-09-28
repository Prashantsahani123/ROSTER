package com.NEWROW.row.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.NEWROW.row.Data.PictureInsideAlbumData;
import com.NEWROW.row.Data.SimplePhotoData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.ImageCompression;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 18-10-2016.
 */
public class PictureInsideAlbumAdapter extends BaseAdapter {

    ArrayList<PictureInsideAlbumData> listAlbum = new ArrayList<>();
    private static LayoutInflater inflater = null;
    String editflag; //0-edit grps . 1- normal listing
    Context context;
    ImageCompression compressImage = new ImageCompression();
    CheckBox chkBox;
    String isloadmore;
    ArrayList<SimplePhotoData> existinglist;
    Boolean isFirstTime = true;

    int noOfImagesSelected = 0;


    public PictureInsideAlbumAdapter(Context cnt, ArrayList<PictureInsideAlbumData> list, String edit, String flag, ArrayList<SimplePhotoData> existingphotolist) {
        context = cnt;
        listAlbum = list;
        editflag = edit;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        isloadmore = flag;
        existinglist = existingphotolist;


    }

    PictureInsideAlbumData getAlbum(int position) {
        return ((PictureInsideAlbumData) getItem(position));
    }

    @Override
    public int getCount() {
        return listAlbum.size();
    }

    @Override
    public Object getItem(int position) {
        return listAlbum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getImageSelectedCount() {
        return noOfImagesSelected;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView = convertView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.layout_picture_insidealbum_adapter, null);
            holder.img = (ImageView) rowView.findViewById(R.id.picture);
            holder.framelayout = (FrameLayout) rowView.findViewById(R.id.framelayout);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }
        holder = (Holder) rowView.getTag();

        final PictureInsideAlbumData p = getMember(position);
        final CheckBox cb_select = (CheckBox) rowView.findViewById(R.id.cb_select);
        cb_select.setOnCheckedChangeListener(myCheckChangList);
        cb_select.setTag(position);
        cb_select.setChecked(p.box);

        if (isFirstTime) {
            getData();
        }

        if (editflag.equals("0")) {

            cb_select.setVisibility(View.VISIBLE);


            //  cb_select.setTag(listgrp.get(position).getGrpProfileId());
            holder.framelayout.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View v) {
                    int size = listAlbum.size();

                    if (cb_select.isChecked() == true) {
                        cb_select.setChecked(false);
                    } else
                        cb_select.setChecked(true);

                }

            });
        }


        Uri uri = Uri.fromFile(new File(listAlbum.get(position).getFilepath()));


        //holder.img.setImageURI(uri);

        Picasso.with(context).load(uri)
                .centerCrop()
                .resize(200, 200)
                .placeholder(R.drawable.placeholder_new)
                .into(holder.img);
        // }


        return rowView;


    }

    PictureInsideAlbumData getMember(int position) {
        return ((PictureInsideAlbumData) getItem(position));
    }

    public ArrayList<PictureInsideAlbumData> getBox() {
        ArrayList<PictureInsideAlbumData> box = new ArrayList<PictureInsideAlbumData>();
        for (PictureInsideAlbumData p : listAlbum) {
            if (p.box)
                box.add(p);
        }
        return box;
    }


    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isFirstTime = false;
            int size = getBox().size();
            if (getBox().size() > 4) {
                getMember((Integer) buttonView.getTag()).box = isChecked;
                buttonView.setVisibility(View.GONE);

            } else {
                buttonView.setVisibility(View.VISIBLE);
                getMember((Integer) buttonView.getTag()).box = isChecked;
            }

            notifyDataSetChanged();

        }
    };


    public class Holder {

        ImageView img;
        ImageView selectedImage;
        FrameLayout framelayout;
    }

    public void getData() {
        if (isloadmore.equalsIgnoreCase("") || isloadmore.equalsIgnoreCase("null")) {

        } else {
            for (int j = 0; j < listAlbum.size(); j++) {
                for (int i = 0; i < existinglist.size(); i++) {
                    String path = existinglist.get(i).getUrl();
                    if (listAlbum.get(j).getFilepath().equalsIgnoreCase(path)) {
                        listAlbum.get(j).setBox(true);
                        listAlbum.get(j).setDesc(existinglist.get(i).getDescription());
                    }

                }
            }
        }
    }


}










