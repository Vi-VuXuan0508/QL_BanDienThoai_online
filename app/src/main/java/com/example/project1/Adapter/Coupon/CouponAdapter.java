package com.example.project1.Adapter.Coupon;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Coupon;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.work.Cart;
import com.example.project1.work.CouponProduct;
import com.example.project1.work.DetailProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    Activity context;
    ArrayList<Coupon> couponmodels;

    public CouponAdapter(Activity context, ArrayList<Coupon> couponmodels) {
        this.context = context;
        this.couponmodels = couponmodels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_coupon,null);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txttitle.setText(couponmodels.get(position).getTitle());
        holder.txtdate.setText("Hạn Sử Dụng:"+couponmodels.get(position).getTime());
        holder.txtuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Cart.class);
                intent.putExtra("idcoupon",couponmodels.get(position).getIdcoupon());
                intent.putExtra("percent",couponmodels.get(position).getPercent());
                context.startActivity(intent);
            }
        });
        Picasso.get().load(couponmodels.get(position).getImage()).into(holder.imgcoupon);
       // ảnh

    }

    @Override
    public int getItemCount() {
        return couponmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle,txtdate,txtuse;
        ImageView imgcoupon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txt_coupon_title);
            txtdate = itemView.findViewById(R.id.txt_coupon_date);
            txtuse = itemView.findViewById(R.id.txt_coupon_using);
            imgcoupon = itemView.findViewById(R.id.img_coupon_item);

        }
    }
}
