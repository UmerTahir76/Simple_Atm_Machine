package com.example.atmmachine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atmmachine.databinding.ItemUserBinding;


public class UserItemViewHolder extends RecyclerView.ViewHolder {
    public ItemUserBinding mBinding;
    public UserItemViewHolder(@NonNull ItemUserBinding Binding) {
        super(Binding.getRoot());
        this.mBinding = Binding;
    }
}
