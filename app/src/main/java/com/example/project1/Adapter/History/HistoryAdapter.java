package com.example.project1.Adapter.History;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Coupon;
import com.example.project1.Model.OrderDetail;
import com.example.project1.Model.PaymentModel;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.work.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Activity context;
    ArrayList<ProductModel> productModels;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public HistoryAdapter(Activity context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_history,null);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txttitle.setText(productModels.get(position).getTitle());
        holder.txtprice.setText(String.valueOf(productModels.get(position).getPrice())+" VND");
        getcount(productModels.get(position).getIdproduct(),holder);

       // ảnh

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle,txtprice,txtcount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txt_history_title);
            txtprice = itemView.findViewById(R.id.txt_history_price);
            txtcount = itemView.findViewById(R.id.txt_history_count);


        }
    }
    private void getcount(String idproduct, final ViewHolder holder)
    {
        database.child("orderdetail").orderByChild("idproduct").equalTo(idproduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    OrderDetail model = next.getValue(OrderDetail.class);
                    holder.txtcount.setText(model.getCount()+" Sản Phẩm | Đã Mua Thành Công");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
