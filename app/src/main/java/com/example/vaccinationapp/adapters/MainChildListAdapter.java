package com.example.vaccinationapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.GenerateReportActivity;
import com.example.vaccinationapp.R;
import com.example.vaccinationapp.clicklisteners.ChildClickListener;
import com.example.vaccinationapp.clicklisteners.GivenStatusListener;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.ScheduleModel;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainChildListAdapter extends RecyclerView.Adapter<MainChildListAdapter.MyViewHolder>{

    Context mContext;
    String Type;
    ChildClickListener childClickListener;
    GivenStatusListener givenStatusListener;
    ArrayList<ScheduleModel.Item> items;
    String DOB;

    public MainChildListAdapter(Context mContext, String type, ChildClickListener childClickListener, GivenStatusListener givenStatusListener, ArrayList<ScheduleModel.Item> items, String DOB) {
        this.mContext = mContext;
        Type = type;
        this.childClickListener = childClickListener;
        this.givenStatusListener = givenStatusListener;
        this.items = items;
        this.DOB = DOB;
    }

    @NonNull
    @Override
    public MainChildListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_mainchildlist,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainChildListAdapter.MyViewHolder holder, int position) {
        if (position == 0) {
            holder.mTextAge.setText(MessageFormat.format("Age : {0} Months", items.get(position).getDueMonths()));
            holder.mTextAge.setVisibility(View.VISIBLE);
        } else {
            if (items.get(position).getDueMonths().equals(items.get(position - 1).getDueMonths())) {
                holder.mTextAge.setVisibility(View.GONE);
            } else {
                holder.mTextAge.setText(MessageFormat.format("Age : {0} Months", items.get(position).getDueMonths()));
                holder.mTextAge.setVisibility(View.VISIBLE);
            }
        }

        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(new Date(Long.parseLong(DOB)));
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newFormatter  = new SimpleDateFormat("dd/MM/yyyy");
        String dateStringToDisplay = newFormatter.format(Constants.addDays(date, Integer.parseInt(items.get(position).getDueDays())));

        holder.mTextDueDate.setText(String.format("Due Date : %s", dateStringToDisplay));
        holder.mTextVaccineName.setText(items.get(position).getName());

        holder.mImageReceipt.setOnClickListener(v -> childClickListener.onItemClick(items.get(position),
                dateStringToDisplay));

        if (items.get(position).getStatus().equals("false")) {
            holder.mSwitchGivenOrNot.setChecked(false);
            holder.mImageReceipt.setVisibility(View.GONE);
        }

        holder.mSwitchGivenOrNot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                givenStatusListener.onItemClick(holder.getAdapterPosition(), items.get(position), "POST");
                holder.mImageReceipt.setVisibility(View.VISIBLE);
            } else {
                givenStatusListener.onItemClick(holder.getAdapterPosition(), items.get(position), "DELETE");
                holder.mImageReceipt.setVisibility(View.GONE);
            }
        });

        holder.mSubmitRequest.setVisibility(View.VISIBLE);
        holder.mSubmitRequest.setOnClickListener( v-> getDocList(items.get(position)));

    }

    protected void getDocList(ScheduleModel.Item item) {
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextVaccineName, mTextDueDate;
        Switch mSwitchGivenOrNot;
        ImageView mImageReceipt;
        TextView mTextAge, mSubmitRequest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextVaccineName = itemView.findViewById(R.id.vaccine_name);
            mTextDueDate = itemView.findViewById(R.id.due_date);

            mSwitchGivenOrNot = itemView.findViewById(R.id.given_switch);

            mImageReceipt = itemView.findViewById(R.id.print_receipt);
            mTextAge = itemView.findViewById(R.id.age);
            mSubmitRequest = itemView.findViewById(R.id.submit_request);
        }
    }
}