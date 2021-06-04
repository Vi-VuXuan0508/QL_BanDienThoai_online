package com.example.project1.nhan.DAO;

import android.content.Context;

import com.example.project1.Model.ProductInformation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductInfoDAO {

    public static void createInfo(Context context, ProductInformation info)
    {
        DatabaseReference reff;
        reff= FirebaseDatabase.getInstance().getReference().child("productinfomation");
        String id= reff.push().getKey();
        info.setIdproductInformation(id);
        reff.child(id).setValue(info);
    }

    public static void updateInfo(Context context, String id, ProductInformation info)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("productinfomation").child(id);
        info.setIdproductInformation(id);
        reff.setValue(info);
    }

    public static void deleteInfo(Context context, String id)
    {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("productinfomation").child(id);
        reff.removeValue();
    }
}
