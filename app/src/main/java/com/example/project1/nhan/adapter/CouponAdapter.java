package com.example.project1.nhan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.example.project1.Model.Coupon;
import com.example.project1.R;
import com.example.project1.nhan.DAO.CouponDAO;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.DAO.ProductDAO;
import com.example.project1.nhan.Main2Activity;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder>{
    Context context;
    ArrayList<Coupon>list;
    RecyclerView rcv;
    public CouponAdapter(Context context, ArrayList<Coupon> list, RecyclerView rcv)
    {
        this.context=context;
        this.list=list;
        this.rcv=rcv;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_listview_v_c,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvTittle.setText(list.get(position).getTitle());
        Picasso.get().load(Uri.parse(list.get(position).getImage())).into(holder.imv);
        holder.tvTime.setText("Hạn sử dụng: "+list.get(position).getTime());
        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogXacNhan(list.get(position));
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    public void dialogXacNhan(final Coupon c)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_xac_nhan,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();
        Button btnXacNhan=view.findViewById(R.id.btnXacNhanDialog);
        Button btnHuy=view.findViewById(R.id.btnHuyDialog);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponDAO.deleteCoupon(context, c.getSeri());
                ImageDAO.deleteImage(c.getImage());
                new ShowAsyncTask(context,rcv,null).execute(4);
                dialog.dismiss();
            }
        });
    }

    public void dialogUpdate(final Coupon c)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.activity_cap_nhat_v_c,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText etTitle=view.findViewById(R.id.txtTittleCouponUpdate);
        final EditText etPercent=view.findViewById(R.id.txtPercentCouponUpdate);
        final EditText etTime=view.findViewById(R.id.txtTimeCouponUpdate);
        ImageView imv=view.findViewById(R.id.ivHinhCouponUpdate);
        Picasso.get().load(Uri.parse(c.getImage())).into(imv);

        etTitle.setText(c.getTitle());
        etPercent.setText(c.getPercent());
        etTime.setText(c.getTime());

        Button btnThem=view.findViewById(R.id.btnUpdateCoupon);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=etTitle.getText().toString();
                int percent=Integer.parseInt(etPercent.getText().toString());
                String time=etTime.getText().toString();
                Coupon coupon=new Coupon(null,null,c.getImage(),null,title,time,percent,c.getSeri());
                CouponDAO.updateCoupon(context,c.getIdcoupon(),coupon);
                new ShowAsyncTask(context,rcv,null).execute(4);
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
        TextView tvTittle, tvTime;
        ImageView imv, imvDelete;
        public MyViewHolder(View view)
        {
            super(view);
            item=view.findViewById(R.id.itemCoupon);
            imv=view.findViewById(R.id.ivCoupon);
            tvTittle=view.findViewById(R.id.tvTitleCoupon);
            tvTime=view.findViewById(R.id.tvTimeCoupon);
            imvDelete=view.findViewById(R.id.ivDeleteCoupon);
        }
    }
}
