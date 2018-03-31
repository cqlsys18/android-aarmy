package com.alaryani.aamrny.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryPhotoAdapter extends BaseAdapter{
	private Context context;
	private int imgIds[] =null;
	

	public GalleryPhotoAdapter(Context context, int[] imgIds) {
		this.context = context;
		this.imgIds = imgIds;
	}

	@Override
	public int getCount() {
		return imgIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView img=new ImageView(context);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setImageResource(imgIds[position]);
		return img;
	}

}
