package com.example.project1.nhan.DAO;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.project1.Model.Coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CouponDAO {

    public static void createCoupon(Context context, Coupon coupon)
    {
        DatabaseReference reff;
        reff= FirebaseDatabase.getInstance().getReference().child("coupon");
        String id= reff.push().getKey();
        coupon.setIdcoupon(id);
        reff.child(id).setValue(coupon);
    }

    public static void updateCoupon(Context context, String id, final Coupon c)
    {
        /*DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Coupon").child(id);
        coupon.setId_coupon(id);
        reff.setValue(coupon);*/
        Query reff;
        reff= FirebaseDatabase.getInstance().getReference().child("coupon").orderByChild("seri").equalTo(id);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Coupon coupon=snapshot.getValue(Coupon.class);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("coupon").child(coupon.getIdcoupon());
                    c.setIdcoupon(coupon.getIdcoupon());
                    ref.setValue(c);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void deleteCoupon(final Context context, String id)
    {
        //DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Coupon").child(id);
        //reff.removeValue();
        Query reff;
        reff= FirebaseDatabase.getInstance().getReference().child("coupon").orderByChild("seri").equalTo(id);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Coupon coupon=snapshot.getValue(Coupon.class);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("coupon").child(coupon.getIdcoupon());
                    ref.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
