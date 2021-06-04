package com.example.project1.nhan.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.project1.Model.Category;
import com.example.project1.Model.Coupon;
import com.example.project1.Model.ProductModel;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDAO {


    public static void createCategory(Context context, Category category)
    {
        DatabaseReference reff;
        reff= FirebaseDatabase.getInstance().getReference().child("category");
        String id= reff.push().getKey();
        category.setIdcategory(id);
        reff.child(id).setValue(category);
    }

    public static void updateCategory(Context context, String id, Category category)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("category").child(id);
        category.setIdcategory(id);
        reff.setValue(category);
    }

    public static void deleteCategory(Context context, String id)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("category").child(id);
        reff.removeValue();
    }

    public static void deleteAllProduct(final Context context, String id)
    {
        Query reff;
        reff= FirebaseDatabase.getInstance().getReference().child("product").orderByChild("idcategory").equalTo(id);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    ProductModel product=snapshot.getValue(ProductModel.class);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("product").child(product.getIdproduct());
                    ref.removeValue();
                    new ShowAsyncTask(context,product.getIdproduct()).execute(7);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<Category> getAll(Context context)
    {
        final ArrayList<Category> list=new ArrayList<Category>();
        DatabaseReference reff;
        reff= FirebaseDatabase.getInstance().getReference().child("category");
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Category category=snapshot.getValue(Category.class);
                    list.add(category);
                    Log.d("TAG", "onDataChange: "+category.getIdcategory());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reff.addListenerForSingleValueEvent(eventListener);

        return list;
    }

}
