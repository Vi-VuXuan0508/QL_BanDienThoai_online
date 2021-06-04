package com.example.project1.Adapter.ProDuct;

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

import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.work.DetailProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class ProDuctOppoAdapter extends RecyclerView.Adapter<ProDuctOppoAdapter.ViewHolder> {
    Activity context;
    ArrayList<ProductModel> productModels;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public ProDuctOppoAdapter(Activity context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_home_rcv,null);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txttitle.setText(productModels.get(position).getTitle());
        getproductinfor(productModels.get(position).getIdproduct(),holder);
        holder.txtpricecoupon.setText(String.valueOf(productModels.get(position).getPrice()*0.2));

        holder.txtprice.setText(String.valueOf(productModels.get(position).getPrice()));
        holder.cardViewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProduct.class);
                intent.putExtra("idproduct",productModels.get(position).getIdproduct());

                context.startActivity(intent);
            }
        });
        Picasso.get().load(productModels.get(position).getImage()).into(holder.imgproduct);
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle,txtprice,txtpricecoupon,txtprouctinfor;
        ImageView imgproduct;
        CardView cardViewproduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txt_producttitle_item);
            txtprice = itemView.findViewById(R.id.txt_productprice_item);
            txtpricecoupon = itemView.findViewById(R.id.txt_productcoupon_item);
            txtprouctinfor = itemView.findViewById(R.id.txt_productInfor_item);
            imgproduct = itemView.findViewById(R.id.img_product_item);
            cardViewproduct= itemView.findViewById(R.id.cardView_product_home);
        }
    }
    private void getproductinfor(String idproduct, final ViewHolder viewHolder)
    {
        Query query = database.child("productinfomation").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductInformation information = next.getValue(ProductInformation.class);
                    viewHolder.txtprouctinfor.setText("Screen: "+information.getScreen()+" Camera: "+information.getCamera()+" Camera Selfie: "+information.getCameraselfie()+" CPU: "+information.getCpu()+" Ram: " +information.getRam()+" Rom: "+information.getRom());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
