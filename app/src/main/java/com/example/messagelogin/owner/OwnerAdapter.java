package com.example.messagelogin.owner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagelogin.customer.UserListener;
import com.example.messagelogin.customer.UserMdl;
import com.example.messagelogin.customer.UsersAdapter;
import com.example.messagelogin.databinding.ItemContainerOwnerBinding;
import com.example.messagelogin.databinding.ItemContainerUserBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.UserViewHolder> {

    private final List<UserMdl> users;
    private final UserListener userListener;

    public OwnerAdapter(List<UserMdl> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerOwnerBinding itemContainerOwnerBinding = ItemContainerOwnerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false);

        return new UserViewHolder(itemContainerOwnerBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerOwnerBinding binding;

        UserViewHolder(ItemContainerOwnerBinding itemContainerOwnerBinding)
        {
            super(itemContainerOwnerBinding.getRoot());
            binding = itemContainerOwnerBinding;

        }

        void setUserData(UserMdl user)
        {

            binding.ownertextFirstName.setText(user.customersFirstname);
            binding.ownertextEmail.setText(user.customersEmail);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

            if (user.image!=null && !user.image.isEmpty()){
                Picasso.get().load(user.image).into(binding.imageProfile);
            }

        }
    }

    private Bitmap getUserImage(String encodedImage) {
        if (encodedImage == null) {
            return null;
        }
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



}
