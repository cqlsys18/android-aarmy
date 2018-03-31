package com.alaryani.aamrny.autocompleteaddress;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alaryani.aamrny.R;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements
		Filterable {
	public ArrayList<String> resultList;

	LayoutInflater mInflater;

	public PlaceAPI mPlaceAPI = new PlaceAPI();
	Context mContext;
	int mResource;

	public PlacesAutoCompleteAdapter(Context context, int resource) {
		super(context, resource);

		mContext = context;
		mResource = resource;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// Last item will be the footer
		return resultList.size();
	}

	@Override
	public String getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_auto_place, null);
			holder.lblPlace = (TextView) convertView
					.findViewById(R.id.lbl_auto_place);
			holder.lblPlace.setSelected(true);
			holder.imgGg = (ImageView) convertView
					.findViewById(R.id.img_gg_logo);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String strPlace = resultList.get(position);
		if (!strPlace.equals("") && strPlace != null) {
			holder.lblPlace.setText(resultList.get(position));
			if (position < resultList.size() - 1) {
				holder.imgGg.setVisibility(View.GONE);
			} else {
				holder.imgGg.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					resultList = mPlaceAPI.autocomplete(constraint.toString());

					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}

				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};

		return filter;
	}

	class Holder {
		TextView lblPlace;
		ImageView imgGg;
	}

}
