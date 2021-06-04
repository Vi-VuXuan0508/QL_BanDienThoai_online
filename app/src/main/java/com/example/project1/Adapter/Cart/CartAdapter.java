package com.example.project1.Adapter.Cart;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Main;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.ProductInformation;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Activity context;
    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<ProductInformation> productInformations = new ArrayList<>();
    ArrayList<CartModel> cartModels  = new ArrayList<>();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public CartAdapter() {
    }
    public CartAdapter(Activity context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_cart,null);
        return (new ViewHolder(view));
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        holder.img_item
        getidcart1(Main.iduser,holder,position,productModels.get(position).getIdproduct());
        holder.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carthandledown(productModels.get(position).getIdproduct(),holder);
            }

        });
        holder.btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carthandleup(productModels.get(position).getIdproduct(),holder);
            }
        });
        holder.img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carthandleremove(productModels.get(position).getIdproduct());

                productModels.remove(position);
                notifyItemRemoved(position);


            }
        });
        Picasso.get().load(productModels.get(position).getImage()).into(holder.img_item);
        Query query1 = database.child("productinfomation").orderByChild("idproduct").
                equalTo(productModels.get(position).getIdproduct());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
//                productInformations.clear();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductInformation model = next.getValue(ProductInformation.class);
                    holder.txt_productinfor.setText(
                            "Màn Hình:"+model.getScreen()
                                    +"\nCamera:"+model.getCamera()+"   Ram: "+model.getRam());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txt_title.setText(productModels.get(position).getTitle());
        holder.txt_price.setText(String.valueOf(productModels.get(position).getPrice()));

//        holder.txt_productinfor.setText();
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item,img_clear;
        TextView txt_title,txt_productinfor,txt_price;
        Button btn_down,btn_count,btn_up;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_clear = itemView.findViewById(R.id.img_cart_clear);
            img_item = itemView.findViewById(R.id.img_cart_item);
            txt_price = itemView.findViewById(R.id.txt_price_cart_item);
            txt_productinfor = itemView.findViewById(R.id.txt_cart_infor_item);
            txt_title = itemView.findViewById(R.id.txt_title_cart_item);
            btn_down = itemView.findViewById(R.id.btn_cart_down);
            btn_up = itemView.findViewById(R.id.btn_cart_up);
            btn_count = itemView.findViewById(R.id.btn_cart_count);
        }
    }
    public void getidcart1(String iduser, final ViewHolder holder, final int position, final String idproduct)
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
                        holder.btn_count.setText(String.valueOf(model.getCout()));
                    }

                    cartModels.add(model);
                }
                Log.i("cartcount","" +cartModels.get(position).getCout());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getidcartremove(String iduser)
    {
        Query query = database.child("cart").orderByChild("iduser").equalTo(iduser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    getproduct(model.getIdproduct());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproduct(final String idproduct)
    {

        Query  query   = database.child("product").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductModel model = next.getValue(ProductModel.class);
                    productModels.add(model);
                }
                Log.i("productid231",""+productModels.size());
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void carthandledown(String idproduct, final ViewHolder holder)
    {
        Query query = database.child("cart").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    database.child("cart").child(model.getIdcart()).setValue(new CartModel(model.getIdcart(),model.getIdproduct(),model.getIduser(),model.getCout()-1));
                    holder.btn_count.setText(String.valueOf(model.getCout()-1));

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void carthandleup(String idproduct,final ViewHolder holder)
    {
        Query query = database.child("cart").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    database.child("cart").child(model.getIdcart()).setValue(new CartModel(model.getIdcart(),model.getIdproduct(),model.getIduser(),model.getCout()+1));
                    holder.btn_count.setText(String.valueOf(model.getCout()+1));

//
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void carthandleremove(String idproduct)
    {
        Query query = database.child("cart").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    Log.i("fesadsa","model"+model.getIdcart());
                    database.child("cart").child(model.getIdcart()).removeValue();
                    productModels.clear();
                }
                Log.i("productid",""+productModels.size());
                Log.i("productid",""+productModels.size());
                getidcartremove(Main.iduser);

                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
