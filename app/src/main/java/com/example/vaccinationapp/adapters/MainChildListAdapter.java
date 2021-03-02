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
import androidx.core.content.ContextCompat;
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

    /**
     * Constructor parameters
     */
    Context mContext;
    String Type;
    ChildClickListener childClickListener;
    GivenStatusListener givenStatusListener;
    ArrayList<ScheduleModel.Item> items;
    String DOB;

    /**
     * Declaring constructor for Recycler Adapter for calling this adapting we'll use this constructor with below params
     *
     * @param mContext
     * @param type
     * @param childClickListener
     * @param givenStatusListener
     * @param items
     * @param DOB
     */
    public MainChildListAdapter(Context mContext, String type, ChildClickListener childClickListener, GivenStatusListener givenStatusListener, ArrayList<ScheduleModel.Item> items, String DOB) {
        this.mContext = mContext;
        Type = type;
        this.childClickListener = childClickListener;
        this.givenStatusListener = givenStatusListener;
        this.items = items;
        this.DOB = DOB;
        this.setHasStableIds(true);
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
    public MainChildListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_mainchildlist,parent,false);
        return new MyViewHolder(mView);
    }

    /**
     * This will act as a for loop
     * binds data to the given layout, position wise from the array passed in constructor while calling from activity or fragment class
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MainChildListAdapter.MyViewHolder holder, int position) {
        /**
         * comment this line if you're facing data binding problem
         */
//        holder.setIsRecyclable(false);

        /**
         * for the very first position we'll show time period of vaccination and age
         */
        if (position == 0) {
            holder.mTextAge.setText(MessageFormat.format("Age : {0} Months", items.get(position).getDueMonths()));
            holder.mTextAge.setVisibility(View.VISIBLE);
        } else {
            /**
             * if the previous time period is same as current iterated/position period than no need of displaying
             */
            if (items.get(position).getDueMonths().equals(items.get(position - 1).getDueMonths())) {
                holder.mTextAge.setVisibility(View.GONE);
            }
            /**
             * else show the new time period for vaccine
             */
            else {
                holder.mTextAge.setText(MessageFormat.format("Age : {0} Months", items.get(position).getDueMonths()));
                holder.mTextAge.setVisibility(View.VISIBLE);
            }
        }

        /**
         * this formatter will parse the long type dob or timestamp dob for ex:16000232564 into this 2021/05/11
         * this will store it in a date format for calculating next due date for the vaccination
         */
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

        holder.mImageReceipt.setVisibility(View.GONE);

        if (items.get(position).getStatus().equals("true")) {
            holder.mSwitchGivenOrNot.setChecked(true);
            holder.mImageReceipt.setVisibility(View.VISIBLE);
        }

        /**
         * This will track is given switch is checked or not
         * if checked then will changes the vaccination status to given
         * else unchecked will remove the given status from db
         */
        holder.mSwitchGivenOrNot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                givenStatusListener.onItemClick(holder.getAdapterPosition(), items.get(position), "POST");
                holder.mImageReceipt.setVisibility(View.VISIBLE);
            } else {
                givenStatusListener.onItemClick(holder.getAdapterPosition(), items.get(position), "DELETE");
                holder.mImageReceipt.setVisibility(View.GONE);
            }
        });

        /**
         * This will track whether doctor has accepted the request or not
         * according to that it will display the UI
         */
        if (items.get(position).getDocStatus().equals("false")) {
            holder.mSubmitRequest.setVisibility(View.VISIBLE);
            holder.mSubmitRequest.setText("Pending Request");
            holder.mSubmitRequest.setTextColor(ContextCompat.getColor(mContext, R.color.rejected));
            holder.mSubmitRequest.setClickable(false);
        } else if (items.get(position).getDocStatus().equals("true")) {
            holder.mSubmitRequest.setVisibility(View.VISIBLE);
            holder.mSubmitRequest.setText("Request Accepted");
            holder.mSubmitRequest.setTextColor(ContextCompat.getColor(mContext, R.color.accepted));
            holder.mSubmitRequest.setClickable(false);
        } else {
            holder.mSubmitRequest.setVisibility(View.VISIBLE);
            holder.mSubmitRequest.setOnClickListener( v-> {
                getDocList(items.get(position));
                holder.mSubmitRequest.setText("Pending Request");
                holder.mSubmitRequest.setTextColor(ContextCompat.getColor(mContext, R.color.rejected));
                holder.mSubmitRequest.setClickable(false);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected void getDocList(ScheduleModel.Item item) {
    }

    /**
     * This will return size of the array list, telling the bindViewHolder to iterate or bind the data till this size.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Defined layout View using view holder for dataBinding
     */
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
