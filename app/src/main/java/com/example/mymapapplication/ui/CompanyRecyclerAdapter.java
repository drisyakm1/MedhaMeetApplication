package com.example.mymapapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymapapplication.R;
import com.example.mymapapplication.models.Company;

import java.util.ArrayList;

public class CompanyRecyclerAdapter extends RecyclerView.Adapter<CompanyRecyclerAdapter.CompanyViewHolder> {
    private Context mContext;
    private ArrayList<Company> companyArrayList = new ArrayList<>();

    public CompanyRecyclerAdapter(Context context, ArrayList<Company> companies) {
        mContext = context;
        companyArrayList = companies;
    }

    public void updateList(ArrayList<Company> companies) {
        companyArrayList.clear();
        companyArrayList.addAll(companies);
    }


    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        CompanyViewHolder viewHolder = new CompanyViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {

        holder.name.setText(companyArrayList.get(position).getCompanyName());
        holder.description.setText(companyArrayList.get(position).getCompanyDescription());
        holder.rating.setText(String.valueOf(companyArrayList.get(position).getAvgRating()));
        Glide.with(mContext).load(companyArrayList.get(position).getCompanyImageUrl()).into(holder.img);
        holder.itemRatingBar.setRating(companyArrayList.get(position).getAvgRating());

    }

    @Override
    public int getItemCount() {
        return companyArrayList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView rating;
        TextView description;
        ImageView img;
        RatingBar itemRatingBar;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            rating = itemView.findViewById(R.id.item_rating);
            description = itemView.findViewById(R.id.item_description);
            img = itemView.findViewById(R.id.item_img);
            itemRatingBar = itemView.findViewById(R.id.item_ratingBar);
        }
    }
}
