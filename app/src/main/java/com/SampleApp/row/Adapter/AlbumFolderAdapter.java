package com.SampleApp.row.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Data.AlbumFolder;
import com.SampleApp.row.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 14-09-2016.
 */
public class AlbumFolderAdapter extends BaseAdapter {

    ArrayList<AlbumFolder> listAlbum = new ArrayList<>();
    private static LayoutInflater inflater = null;
    String editflag; //0-edit grps . 1- normal listing
    Context context;


    public AlbumFolderAdapter(Context cnt, ArrayList<AlbumFolder> list, String edit) {
        context = cnt;
        listAlbum = list;
        editflag = edit;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.layout_albumfolder_adapter, null);
            holder.img = (ImageView) rowView.findViewById(R.id.picture);
            holder.text = (TextView) rowView.findViewById(R.id.text);
            holder.framelayout = (FrameLayout) rowView.findViewById(R.id.framelayout);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }
        holder = (Holder) rowView.getTag();

        if(listAlbum.get(position).getId()!= null) {
            if (listAlbum.get(position).getId().trim().length() == 0 || listAlbum.get(position).getId() == null || listAlbum.get(position).getId().isEmpty()) {
                holder.img.setImageResource(R.drawable.dashboardplaceholder);
            } else {
                Uri uri = Uri.fromFile(new File(listAlbum.get(position).getFilepath()));

                Picasso.with(context)
                        .load(uri)
                        .centerCrop()
                        .resize(200, 200)
                        .placeholder(R.drawable.dashboardplaceholder)
                        .into(holder.img);
            /*Picasso.with(context).load(listAlbum.get(position).get())
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(holder.img);
*/


                holder.img.setImageBitmap(listAlbum.get(position).getBitmap());


            }
        }

        holder.text.setText(listAlbum.get(position).getName());


        return rowView;

    }

    public class Holder {

        ImageView img;
        TextView text;
        FrameLayout framelayout;

    }
}
