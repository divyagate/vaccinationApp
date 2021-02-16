package com.example.vaccinationapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.R;
import com.example.vaccinationapp.models.UserModel;

import java.util.ArrayList;

public class AdminHomeAdapter extends RecyclerView.Adapter<AdminHomeAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<UserModel.Item> itemArrayList;

    public AdminHomeAdapter(Context mContext, ArrayList<UserModel.Item> itemArrayList) {
        this.mContext = mContext;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public AdminHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_adminhome,parent,false);
        return new AdminHomeAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminHomeAdapter.MyViewHolder holder, int position) {
        holder.mCardLayout.setOnClickListener(v -> {
            onClick(itemArrayList.get(position));
        });
        holder.mTextParentName.setText(String.format("%s %s", itemArrayList.get(position).getFirstname(), itemArrayList.get(position).getLastname()));
    }

    protected void onClick(UserModel.Item item) {
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardLayout;
        TextView mTextParentName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardLayout = itemView.findViewById(R.id.card_home_admin);
            mTextParentName = itemView.findViewById(R.id.parent_name);
        }
    }
}