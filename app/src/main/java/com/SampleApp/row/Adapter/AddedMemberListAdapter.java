package com.SampleApp.row.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddedMemberListAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<DirectoryData> objects;

	public AddedMemberListAdapter(Context context, ArrayList<DirectoryData> memberDatas) {
		ctx = context;
		objects = memberDatas;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.added_member_list_item, parent, false);
		}

		final DirectoryData p = getMember(position);


		((TextView) view.findViewById(R.id.tvDescr)).setText(p.getMemberName());
		((TextView) view.findViewById(R.id.tv_mobile)).setText(p.getMembermobile());


		if ( p.getPic() == null || p.getPic().equals("")  || p.getPic().isEmpty()) {
			((ImageView) view.findViewById(R.id.imageView1)).setImageResource(R.drawable.profile_pic);
		} else {
			Picasso.with(ctx).load(p.getPic())
					.transform(new CircleTransform())
					.placeholder(R.drawable.profile_pic)
					.into(((ImageView) view.findViewById(R.id.imageView1)));
		}




		LinearLayout name = (LinearLayout)view.findViewById(R.id.linear_name);

		name.setOnClickListener(new View.OnClickListener() {
			@Override


			public void onClick(View v) {



			}

		});

		return view;
	}

	DirectoryData getMember(int position) {
		 return ((DirectoryData) getItem(position));
	}

	public ArrayList<DirectoryData> getBox() {
		ArrayList<DirectoryData> box = new ArrayList<DirectoryData>();
		for (DirectoryData p : objects)
		{
			if (p.box)
				box.add(p);

		}
		return box;
	}

	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			getMember((Integer) buttonView.getTag()).box = isChecked;

		}
	};

}