package com.alaryani.aamrny.adapters;

/**
 * Created by Administrator on 4/8/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alaryani.aamrny.R;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.widget.TextViewRaleway;

import java.util.List;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.MyViewHolder> {

    private List<CarType> moviesList;
    private static ClickListener clickListener;
    int positionCheck = 0;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextViewRaleway title;
        public RelativeLayout llTypeCar;
        public View viewBorder;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextViewRaleway) view.findViewById(R.id.txtTitle);
            llTypeCar = (RelativeLayout) view.findViewById(R.id.llTypeCar);
            viewBorder = (View) view.findViewById(R.id.view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public CarTypeAdapter(List<CarType> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cartype, parent, false);
        int itemWidth = parent.getWidth() / 3;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = itemWidth;
        itemView.setLayoutParams(layoutParams);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CarType movie = moviesList.get(position);
        holder.title.setText(movie.getName());
        if (position == positionCheck) {
            holder.viewBorder.setVisibility(View.VISIBLE);
        } else {
            holder.viewBorder.setVisibility(View.GONE);
        }
        holder.llTypeCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position, v);
                holder.viewBorder.setVisibility(View.VISIBLE);
                positionCheck = position;
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public int getPositionCheck() {
        return positionCheck;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}