package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Announcement;
import com.SampleApp.row.Celebration;
import com.SampleApp.row.Directory;
import com.SampleApp.row.Documents;
import com.SampleApp.row.E_Bulletin;
import com.SampleApp.row.Events;
import com.SampleApp.row.R;

/**
 * Created by user on 06-01-2016.
 */
public class GroupDashboadAdapter extends BaseAdapter {

    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public GroupDashboadAdapter(Context cntx, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=cntx;
        imageId=prgmImages;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.groupdashboardgrid_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text);
        holder.img=(ImageView) rowView.findViewById(R.id.picture);

        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                if (result[position] == "Directory") {

                    Intent i = new Intent(context,Directory.class);
                  //  context.startActivity(i);
                } else if (result[position] == "e-bulletin") {
                    Intent i = new Intent(context,E_Bulletin.class);
                  //  context.startActivity(i);
                }else if (result[position] == "Announcement") {
                    Intent i = new Intent(context,Announcement.class);
                  //  context.startActivity(i);
                }else if (result[position] == "Events") {
                    Intent i = new Intent(context,Events.class);
                    context.startActivity(i);
                }else if (result[position] == "Document Safe") {
                    Intent i = new Intent(context,Documents.class);
                   // context.startActivity(i);
                }else if (result[position] == "Celebrations") {
                    Intent i = new Intent(context,Celebration.class);
                    context.startActivity(i);
                }
            }
        });

        return rowView;
    }

}
