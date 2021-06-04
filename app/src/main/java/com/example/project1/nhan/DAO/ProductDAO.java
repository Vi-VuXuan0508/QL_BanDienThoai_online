package com.example.project1.nhan.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.project1.Model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDAO {

    public static void createProduct(Context context, ProductModel product)
    {
        DatabaseReference reff;
        reff= FirebaseDatabase.getInstance().getReference().child("product");
        String id= reff.push().getKey();
        product.setIdproduct(id);
        reff.child(id).setValue(product);
    }

    public static void updateProduct(Context context, String id, ProductModel product)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("product").child(id);
        product.setIdproduct(id);
        reff.setValue(product);
    }

    public static void deleteProduct(Context context, String id)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("product").child(id);
        reff.removeValue();
    }

    public static ArrayList<ProductModel> getAll(Context context, String id_category)
    {
        final ArrayList<ProductModel> list=new ArrayList<ProductModel>();
        Query reff;
        reff= FirebaseDatabase.getInstance().getReference().child("product").orderByChild("id_category").equalTo(id_category);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    ProductModel product=snapshot.getValue(ProductModel.class);
                    list.add(product);
                    Log.d("TAG", "onDataChange: "+product.getIdproduct());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return list;
    }
}
