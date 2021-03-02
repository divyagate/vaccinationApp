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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.R;
import com.example.vaccinationapp.clicklisteners.HomeClickListener;
import com.example.vaccinationapp.models.ChildModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    /**
     * Constructor parameters
     */
    Context mContext;
    String Type;
    HomeClickListener homeClickListener;
    ArrayList<ChildModel.Item> itemArrayList;


    /**
     * Declaring constructor for Recycler Adapter for calling this adapting we'll use this constructor with below params
     *
     * @param mContext
     * @param type
     * @param homeClickListener
     * @param itemArrayList
     */
    public HomeAdapter(Context mContext, String type, HomeClickListener homeClickListener, ArrayList<ChildModel.Item> itemArrayList) {
        this.mContext = mContext;
        Type = type;
        this.homeClickListener = homeClickListener;
        this.itemArrayList = itemArrayList;
    }

    /**
     * When this adapter is called first the view is going to created with defined layout
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_home,parent,false);
        return new HomeAdapter.MyViewHolder(mView);
    }

    /**
     * This will act as a for loop
     * binds data to the given layout, position wise from the array passed in constructor while calling from activity or fragment class
     *
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.mTextChildName.setText(itemArrayList.get(position).getChildName());

        if (itemArrayList.get(position).getGender().equals("Female")) {
            holder.mImageGender.setImageResource(R.drawable.female);
        }

        /**
         * If doctor logs in
         */
        if (Type.equals("Doctor")) {
            holder.mTextVaccineName.setVisibility(View.VISIBLE);
            holder.mSwitchRequest.setVisibility(View.VISIBLE);
            holder.mTextRequestStatus.setVisibility(View.VISIBLE);

            holder.mTextVaccineName.setText(itemArrayList.get(position).getVaccineName());

            holder.mSwitchRequest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (holder.mSwitchRequest.isChecked()) {
                    submitRequest(itemArrayList.get(position), "true");
                    holder.mTextRequestStatus.setText("Request Accepted");
                    holder.mTextRequestStatus.setTextColor(ContextCompat.getColor(mContext, R.color.accepted));
                } else {
                    submitRequest(itemArrayList.get(position), "false");
                    holder.mTextRequestStatus.setText("Accept Request");
                    holder.mTextRequestStatus.setTextColor(ContextCompat.getColor(mContext, R.color.textBlack));
                }
            });

            if (itemArrayList.get(position).getDocStatus().equals("true")) {
                holder.mSwitchRequest.setChecked(true);
                holder.mTextRequestStatus.setText("Request Accepted");
                holder.mTextRequestStatus.setTextColor(ContextCompat.getColor(mContext, R.color.accepted));
            }

            /**
             * if admin logs in or parent both of them having the same access
             */
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

    /**
     * This will return size of the array list, telling the bindViewHolder to iterate or bind the data till this size.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    /**
     * Defined layout View using view holder for dataBinding
     */
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