package com.example.vaccinationapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.R;
import com.example.vaccinationapp.clicklisteners.VaccinationListClickListener;
import com.example.vaccinationapp.models.VaccineListModel;

import java.util.ArrayList;

public class VaccinationListAdapter extends RecyclerView.Adapter<VaccinationListAdapter.MyViewHolder> {

    Context mContext;
    String Type;
    VaccinationListClickListener vaccinationListClickListener;
    ArrayList<VaccineListModel.Item> itemArrayList;

    public VaccinationListAdapter(Context mContext, String type, VaccinationListClickListener vaccinationListClickListener, ArrayList<VaccineListModel.Item> itemArrayList) {
        this.mContext = mContext;
        Type = type;
        this.vaccinationListClickListener = vaccinationListClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_vaccinationlist,parent,false);
        return new VaccinationListAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        if (Type.equals("add")) {
            //Add Child Activity Binding
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mImageNext.setVisibility(View.GONE);
        } else {
            //Vaccination Info Activity Binding
            holder.mCheckBox.setVisibility(View.GONE);
            holder.mImageNext.setVisibility(View.VISIBLE);

            holder.mLinearVaccine.setOnClickListener(v -> vaccinationListClickListener.onItemClick(holder.getAdapterPosition(), itemArrayList.get(position)));

            holder.mVaccinationName.setText(itemArrayList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mVaccinationName;
        Switch mCheckBox;
        ImageView mImageNext;
        LinearLayout mLinearVaccine;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mVaccinationName = itemView.findViewById(R.id.vaccination_name);
            mCheckBox = itemView.findViewById(R.id.check_box);
            mImageNext = itemView.findViewById(R.id.image_right);
            mLinearVaccine = itemView.findViewById(R.id.linear_vaccine);
        }
    }
}
