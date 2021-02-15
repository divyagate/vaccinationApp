package com.example.vaccinationapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccinationapp.R;
import com.example.vaccinationapp.clicklisteners.DeleteMemberClickListener;
import com.example.vaccinationapp.models.UserModel;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

    Context mContext;
    String Type;
    DeleteMemberClickListener deleteMemberClickListener;
    ArrayList<UserModel.Item> itemArrayList;

    public MemberAdapter(Context mContext, String type, DeleteMemberClickListener deleteMemberClickListener, ArrayList<UserModel.Item> itemArrayList) {
        this.mContext = mContext;
        Type = type;
        this.deleteMemberClickListener = deleteMemberClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout_members,parent,false);
        return new MemberAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.mDeleteMember.setOnClickListener(v -> deleteMemberClickListener.onItemClick(holder.getAdapterPosition(), itemArrayList.get(position)));

        holder.mTextMemberName.setText(String.format("%s %s", itemArrayList.get(position).getFirstname(), itemArrayList.get(position).getLastname()));

        if (Type.equals("Schedule")) {
            holder.mDeleteMember.setVisibility(View.GONE);
            holder.mTextMemberName.setOnClickListener(v -> {
                selectDoc(itemArrayList.get(position));
            });

            holder.mTextCity.setVisibility(View.VISIBLE);
            holder.mTextCity.setText(String.format("City :%s", itemArrayList.get(position).getCity()));
        }
    }

    protected void selectDoc(UserModel.Item itemDoc) {
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextMemberName, mTextCity;
        ImageView mDeleteMember;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextMemberName = itemView.findViewById(R.id.member_name);
            mDeleteMember = itemView.findViewById(R.id.remove_member);
            mTextCity = itemView.findViewById(R.id.member_city);
        }
    }
}