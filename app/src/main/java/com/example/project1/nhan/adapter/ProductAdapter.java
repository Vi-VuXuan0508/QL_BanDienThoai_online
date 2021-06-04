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
import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.nhan.DAO.CategoryDAO;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.DAO.ProductDAO;
import com.example.project1.nhan.DAO.ProductInfoDAO;
import com.example.project1.nhan.Main2Activity;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{
    Context context;
    ArrayList<ProductModel> list;
    RecyclerView rcv;
    public ProductAdapter(Context context, ArrayList<ProductModel> list, RecyclerView rcv)
    {
        this.context=context;
        this.list=list;
        this.rcv=rcv;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.list_san_pham_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getTitle());
        holder.tvgiamgia.setText(String.valueOf(list.get(position).getPricecoupon()));
        holder.tvPrice.setText(String.valueOf(list.get(position).getPrice()));
        holder.tvCount.setText(String.valueOf(list.get(position).getCount()));
        Picasso.get().load(Uri.parse(list.get(position).getImage())).into(holder.imvProduct);
        new ShowAsyncTask(context,holder.tvCategory,list.get(position).getIdcategory()).execute(6);
        new ShowAsyncTask(context,holder.decription,list.get(position).getIdproduct()).execute(5);

        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogXacNhan(list.get(position));
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Main2Activity.class);
                intent.putExtra("code","info");
                intent.putExtra("idProduct",list.get(position).getIdproduct());
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

    }
    public void dialogXacNhan(final ProductModel p)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
//        View view= inflater.inflate(R.layout.dialog_xac_nhan,null);
//        builder.setView(view);
        builder.setMessage("Xác Nhận Xóa?").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProductDAO.deleteProduct(context, p.getIdproduct());
                ImageDAO.deleteImage(p.getImage());
                new ShowAsyncTask(context,p.getIdproduct()).execute(7);
                new ShowAsyncTask(context,rcv,p.getIdcategory()).execute(2);
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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

    public void dialogUpdate(final ProductModel p)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.activity_capnhatqldt,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText etName=view.findViewById(R.id.txtTSPUpdate);
        final EditText etPrice=view.findViewById(R.id.txtGiaUpdate);
        final EditText etCount=view.findViewById(R.id.txtSLUpdate);
        final EditText etGiamGia=view.findViewById(R.id.txtGGUpdate);
        ImageView imv=view.findViewById(R.id.ivHinhProductUpdate);

        etName.setText(p.getTitle());
        etCount.setText(String.valueOf(p.getCount()));
        etPrice.setText(String.valueOf(p.getPrice()));
        etGiamGia.setText(String.valueOf(p.getPricecoupon()));
        Picasso.get().load(Uri.parse(p.getImage())).into(imv);

        Button btnThem=view.findViewById(R.id.btnCapNhatSanPham);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                Float giamgia=Float.parseFloat(etGiamGia.getText().toString());
                float price=Float.parseFloat(etPrice.getText().toString());
                int count=Integer.parseInt(etCount.getText().toString());
                ProductModel product=new ProductModel(p.getImage(),name,p.getIdcategory(),count,price,giamgia);
                ProductDAO.updateProduct(context,p.getIdproduct(),product);
                new ShowAsyncTask(context,rcv,p.getIdcategory()).execute(2);
                /*
                Intent intent=new Intent(context, Main2Activity.class);
                intent.putExtra("code","product");
                context.startActivity(intent);*/
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
        TextView tvPrice,tvName,tvCount,tvCategory,decription,tvgiamgia;
        ImageView imvProduct, imvDelete;
        public MyViewHolder(View view)
        {
            super(view);
            item=view.findViewById(R.id.product_item);
            tvCategory=view.findViewById(R.id.tvApple);
            tvName=view.findViewById(R.id.tvNameProduct);
            imvProduct=view.findViewById(R.id.ivavata);
            imvDelete=view.findViewById(R.id.imvDeleteProduct);
            tvCount=view.findViewById(R.id.tvsoluong);
            tvPrice=view.findViewById(R.id.tvGia);
            tvgiamgia=view.findViewById(R.id.tvGiaGiam);
            decription=view.findViewById(R.id.decreptionProduct);
        }
    }
}
