package com.example.project1.nhan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Category;
import com.example.project1.R;
import com.example.project1.nhan.DAO.CategoryDAO;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.Main2Activity;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    Context context;
    ArrayList<Category>list;
    RecyclerView rcv;
    public CategoryAdapter(Context context, ArrayList<Category> list, RecyclerView rcv)
    {
        this.context=context;
        this.list=list;
        this.rcv=rcv;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_category,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getName());
       // holder.imv.setImageURI(Uri.parse(list.get(position).getImage()));
        Picasso.get().load(Uri.parse(list.get(position).getImage())).into(holder.imv);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Main2Activity.class);
                intent.putExtra("code","product");
                intent.putExtra("idCaterogy",list.get(position).getIdcategory());
                intent.putExtra("nameCategory",list.get(position).getName());
                context.startActivity(intent);
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialogUpdate(list.get(position));
                return false;
            }
        });
        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogXacNhan(list.get(position));
            }
        });


    }
    public void dialogXacNhan(final Category c)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        builder.setMessage("Xác Nhận Xóa?").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CategoryDAO.deleteCategory(context, c.getIdcategory());
                CategoryDAO.deleteAllProduct(context, c.getIdcategory());
                ImageDAO.deleteImage(c.getImage());
                new ShowAsyncTask(context,rcv,null).execute(1);
                /*
                Intent intent=new Intent(context, Main2Activity.class);
                intent.putExtra("code","category");
                context.startActivity(intent);*/
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//        View view= inflater.inflate(R.layout.dialog_xac_nhan,null);
//        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();
//        Button btnXacNhan=view.findViewById(R.id.btnXacNhanDialog);
//        Button btnHuy=view.findViewById(R.id.btnHuyDialog);
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnXacNhan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    public void dialogUpdate(final Category c)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_update_category,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText etName=view.findViewById(R.id.txtNameCategoryUpdate);
        ImageView imv=view.findViewById(R.id.ivHinhCategoryUpdate);
        Picasso.get().load(Uri.parse(c.getImage())).into(imv);
        etName.setText(c.getName());
        Button btnThem=view.findViewById(R.id.btnCategoryUpdate);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                Category category=new Category(name,c.getImage());
                CategoryDAO.updateCategory(context,c.getIdcategory(),category);
                new ShowAsyncTask(context,rcv,null).execute(1);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView item;
        TextView tvName;
        ImageView imv, imvDelete;
        public MyViewHolder(View view)
        {
            super(view);
            item=view.findViewById(R.id.category_item);
            imv=view.findViewById(R.id.imvCategory);
            tvName=view.findViewById(R.id.tvNameCategory);
            imvDelete=view.findViewById(R.id.imvDeleteCategory);
        }
    }
}
