package com.example.javier.MaterialDesignApp.ListViewAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javier.MaterialDesignApp.Model.ListVideoModel;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.R;
import com.squareup.picasso.Picasso;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<VideosModel> navDrawerItems;



	public NavDrawerListAdapter(Context context,
			ArrayList<VideosModel> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;

	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


				convertView = mInflater.inflate(R.layout.item_post,null);


		}

        final TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewItemTitle);
        final TextView textViewContent = (TextView) convertView.findViewById(R.id.textViewItemContent);
        final ImageView imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewImage);
        textViewTitle.setText(navDrawerItems.get(position).getLink());
        textViewContent.setText(navDrawerItems.get(position).getLable());
     //   Picasso.with(context).load(navDrawerItems.get(position).getImages())
             //   .placeholder(convertView.getResources()
              //          .getDrawable(R.drawable.ic_contact_icon)).into(imageViewImage);


		return convertView;
	}
}
