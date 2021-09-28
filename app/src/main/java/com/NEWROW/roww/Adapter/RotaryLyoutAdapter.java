package com.NEWROW.row.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.R;

public class RotaryLyoutAdapter extends BaseAdapter {
    private Context context;
    private String[]appName;
    private Integer[]appImg;
    private LayoutInflater inflater;

    public RotaryLyoutAdapter(Context context, String[] appName, Integer[] appImg) {
        this.context = context;
        this.appName = appName;
        this.appImg = appImg;
    }

    @Override
    public int getCount() {
        return appName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater==null){
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView==null){
            convertView=inflater.inflate(R.layout.rotarylayout_item, null);
        }
        ImageView imageView=convertView.findViewById(R.id.image_view);
        TextView textView=convertView.findViewById(R.id.text_view);
        imageView.setImageResource(appImg[position]);
        textView.setText(appName[position]);
        return convertView;
    }
}
