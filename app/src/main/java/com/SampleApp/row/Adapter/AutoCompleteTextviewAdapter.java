package com.SampleApp.row.Adapter;

/**
 * Created by Admin on 22-03-2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by Admin on 25-01-2018.
 */

public class AutoCompleteTextviewAdapter extends ArrayAdapter<AlbumData>{

    private ArrayList<AlbumData> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private ArrayList<AlbumData> dataListAllItems;
    int resource;


    public AutoCompleteTextviewAdapter(Context context, int resource,ArrayList<AlbumData> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Nullable
    @Override
    public AlbumData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_item, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.sp_textView);
        strName.setText(dataList.get(position).getDistrict_Name());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<AlbumData> matchValues = new ArrayList<>();

                for (AlbumData dataItem : dataListAllItems) {
                    if (dataItem.getDistrict_Name().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<AlbumData>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
