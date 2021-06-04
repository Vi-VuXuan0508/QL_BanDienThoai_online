package com.example.project1.kien.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
   Activity activity;
   ArrayList<UserModel> list;

    public UserAdapter(Activity activity, ArrayList<UserModel> list) {
        this.activity = activity;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)activity).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.tvStatus.setText(" "+list.get(position).getStatus());
        holder.tvphone.setText(" "+list.get(position).getPhone());
        holder.tvFullName.setText(" "+list.get(position).getFullname());
        holder.tvEmail.setText(" "+list.get(position).getEmail());
        Picasso.get().load(list.get(position).getImage()).into(holder.ivAvatar);

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view.findViewById(R.id.delUser),"Chắc Chưa??",5000)
                        .setActionTextColor(Color.RED)
                        .setAction("OK!!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user").child(list.get(position).getIduser());
                                myRef.removeValue();
                                Toast.makeText(activity, "Deleta Success!!", Toast.LENGTH_LONG).show();
                                list.clear();
                            }
                        })
                        .show();

            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditUser(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item;
        ImageView ivDel,ivAvatar;
        TextView tvphone,tvFullName,tvEmail,tvStatus;
        public ViewHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item_user);
            ivDel = view.findViewById(R.id.delUser);
            ivAvatar = view.findViewById(R.id.ivAvatar);
            tvStatus = view.findViewById(R.id.txtStatusItem);
            tvphone = view.findViewById(R.id.txtPhoneItem);
            tvFullName = view.findViewById(R.id.txtFullNameItem);
            tvEmail = view.findViewById(R.id.txtEmailItem);
        }
    }


    private void dialogEditUser(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);
        final EditText etUser = view.findViewById(R.id.txtEditUser);
        final EditText etPhone = view.findViewById(R.id.txtEditPhone);
        final EditText etFullName = view.findViewById(R.id.txtEditFullName);
        final EditText etEmail = view.findViewById(R.id.txtEditEmail);
        final EditText etAddress = view.findViewById(R.id.txtEditAddress);
        ImageView imageView = view.findViewById(R.id.img_user_edit);
        Button btnEdit = view.findViewById(R.id.btnEditUser);
        Picasso.get().load(list.get(position).getImage()).into(imageView);
        etUser.setText(list.get(position).getUsername());
        etPhone.setText(String.valueOf(list.get(position).getPhone()));
        etFullName.setText(list.get(position).getFullname());
        etEmail.setText(list.get(position).getEmail());
        etAddress.setText(list.get(position).getAddress());


        final Dialog dialog = builder.create();
        dialog.show();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = etUser.getText().toString();
                int phone = Integer.parseInt(etPhone.getText().toString());
                String fullName = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String address = etAddress.getText().toString();

                UserModel user=new UserModel(list.get(position).getIduser(),userName,list.get(position).getPassword(),list.get(position).getStatus(),fullName,email,address,list.get(position).getImage(),list.get(position).getDate(),phone);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user").child(list.get(position).getIduser());
                myRef.setValue(user);


                Toast.makeText(activity, "Edit Success!!", Toast.LENGTH_LONG).show();

            }
        });
    }



}
