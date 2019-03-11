package com.example.beerapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beerapp.networking.Beer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.MyViewHolder> {

    private List<Beer> currentList = new ArrayList<>();

    public BeerAdapter(List<Beer> beerList) {
        currentList.clear();
        currentList.addAll(beerList);
        notifyDataSetChanged();
    }

    public void updateData(List<Beer> newList) {
        currentList.clear();
        currentList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.displayItem(currentList.get(position));
    }

    @Override
    public int getItemCount() {
        return currentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView beerImage;
        TextView beerTitle;
        TextView beerDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            beerImage = itemView.findViewById(R.id.beerImage);
            beerTitle = itemView.findViewById(R.id.beerTitle);
            beerDesc = itemView.findViewById(R.id.beerDesc);
        }

        public void displayItem(Beer beer) {

            Log.d("adapter", "@@@imagePath=" + beer.getImageUrl());
            if (beer.getImageUrl() != null && !beer.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(beer.getImageUrl())
                        .resize(50, 50)
                        .centerCrop()
                        .into(beerImage);
            }
            beerTitle.setText(beer.getName());
            beerDesc.setText(beer.getDesc());
        }
    }
}
