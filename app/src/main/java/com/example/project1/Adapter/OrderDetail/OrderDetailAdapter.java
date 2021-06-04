package com.example.project1.Adapter.OrderDetail;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Main;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.Coupon;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.work.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Activity context;
    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<CartModel>    cartModels = new ArrayList<>();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public OrderDetailAdapter(Activity context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_orderdetail,null);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txttitle.setText(productModels.get(position).getTitle());
        holder.txtprice.setText(String.valueOf(productModels.get(position).getPrice())+" VND");
        getcount(Main.iduser,productModels.get(position).getIdproduct(),holder);

       // ảnh
         Picasso.get().load(productModels.get(position).getImage()).into(holder.imgordertail);
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle,txtprice,txtcount;
        ImageView imgordertail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txt_orderdetail_title);
            txtprice = itemView.findViewById(R.id.txt_orderdetail_price);
            txtcount = itemView.findViewById(R.id.txt_orderdetail_count);
            imgordertail = itemView.findViewById(R.id.img_orderdetail_item);

        }
    }
    private void getcount(String iduser, final String idproduct, final ViewHolder holder)
    {
        Query query = database.child("cart").orderByChild("iduser").equalTo(iduser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                cartModels.clear();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    if (model.getIdproduct().equals(idproduct))
                    {
                        holder.txtcount.setText("Số Lượng Sản Phẩm:"+String.valueOf(model.getCout()));
                    }

                    cartModels.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
