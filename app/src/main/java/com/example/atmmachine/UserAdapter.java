package com.example.atmmachine;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.atmmachine.databinding.ItemUserBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserItemViewHolder>  {
    private List<UserData> userList;

    public UserAdapter(List<UserData> userList) {
        this.userList = userList;

    }



    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserItemViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        UserData user = userList.get(position);
        holder.mBinding.showUserName.setText(user.getUserName());
        holder.mBinding.showEmail.setText(user.getEmail());
        holder.mBinding.showBalance.setText(String.valueOf( user.getBalance()));




    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
