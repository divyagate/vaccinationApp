package com.example.vaccinationapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.R;
import com.example.vaccinationapp.clicklisteners.HomeClickListener;
import com.example.vaccinationapp.models.ChildModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    Context mContext;
    String Type;
    HomeClickListener homeClickListener;
    ArrayList<ChildModel.Item> itemArrayList;

    public HomeAdapter(Context mContext, String type, HomeClickListener homeClickListener, ArrayList<ChildModel.Item> itemArrayList) {
        this.mContext = mContext;
        Type = type;
        this.homeClickListener = homeClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_home,parent,false);
        return new HomeAdapter.MyViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.mTextChildName.setText(itemArrayList.get(position).getChildName());

        if (itemArrayList.get(position).getGender().equals("Female")) {
            holder.mImageGender.setImageResource(R.drawable.female);
        }

        if (Type.equals("Doctor")) {
            holder.mTextVaccineName.setVisibility(View.VISIBLE);
            holder.mSwitchRequest.setVisibility(View.VISIBLE);
            holder.mTextRequestStatus.setVisibility(View.VISIBLE);

            holder.mTextVaccineName.setText(itemArrayList.get(position).getVaccineName());

            holder.mSwitchRequest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (holder.mSwitchRequest.isChecked()) {
                    submitRequest(itemArrayList.get(position), "true");
                    holder.mTextRequestStatus.setText("Request Accepted");
                } else {
                    submitRequest(itemArrayList.get(position), "false");
                    holder.mTextRequestStatus.setText("Accept Request");
                }
            });

        } else if (Type.equals("Admin")){
            holder.mTextVaccineName.setVisibility(View.VISIBLE);
            SimpleDateFormat formatter  = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(Long.parseLong(itemArrayList.get(position).getDob())));
            holder.mTextVaccineName.setText("DOB : " + dateString);

            holder.mCardHome.setOnClickListener(v -> homeClickListener.onItemClick(holder.getAdapterPosition(), itemArrayList.get(position)));
        } else {
            holder.mTextVaccineName.setVisibility(View.VISIBLE);
            SimpleDateFormat formatter  = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(Long.parseLong(itemArrayList.get(position).getDob())));
            holder.mTextVaccineName.setText("DOB : " + dateString);
            holder.mCardHome.setOnClickListener(v -> homeClickListener.onItemClick(holder.getAdapterPosition(), itemArrayList.get(position)));
        }
    }

    protected void submitRequest(ChildModel.Item item, String aTrue) {
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextChildName, mTextVaccineName, mTextRequestStatus;
        ImageView mImageGender;
        CardView mCardHome;
        Switch mSwitchRequest;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextChildName = itemView.findViewById(R.id.child_name);
            mTextVaccineName = itemView.findViewById(R.id.child_vacciane);
            mTextRequestStatus = itemView.findViewById(R.id.status);

            mImageGender = itemView.findViewById(R.id.gender);
            mCardHome = itemView.findViewById(R.id.card_home);
            mSwitchRequest = itemView.findViewById(R.id.request_switch);
        }
    }
}
