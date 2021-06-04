package com.example.project1.nhan.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Category;
import com.example.project1.Model.Coupon;
import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.nhan.DAO.ProductInfoDAO;
import com.example.project1.nhan.adapter.CategoryAdapter;
import com.example.project1.nhan.adapter.CouponAdapter;
import com.example.project1.nhan.adapter.ProductAdapter;
import com.example.project1.nhan.adapter.ProductInfoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class ShowAsyncTask extends AsyncTask<Integer,Void,Void> {
    Context context;
    RecyclerView rcv;
    String id;
    TextView tv;

    public ShowAsyncTask(Context context, TextView tv, String id)
    {
        this.context=context;
        this.tv=tv;
        this.id=id;
    }

    public ShowAsyncTask(Context context, String id)
    {
        this.context=context;
        this.id=id;
    }

    public ShowAsyncTask(Context context, RecyclerView rcv, String id)
    {
        this.context=context;
        this.rcv=rcv;
        this.id=id;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        if (integers[0]==1)
        {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("category");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                    ArrayList<Category> list = new ArrayList<>();
                    while (iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        Category model = next.getValue(Category.class);
                        list.add(model);
                    }
                    CategoryAdapter adapter=new CategoryAdapter(context, list, rcv);
                    rcv.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (integers[0]==2)
        {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("product").orderByChild("idcategory").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                    ArrayList<ProductModel> list = new ArrayList<>();
                    while (iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        ProductModel model = next.getValue(ProductModel.class);
                        list.add(model);
                    }
                    ProductAdapter adapter=new ProductAdapter(context, list, rcv);
                    rcv.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (integers[0]==3)
        {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("productinfomation").orderByChild("idproduct").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                    ArrayList<ProductInformation> list = new ArrayList<>();
                    while (iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        ProductInformation model = next.getValue(ProductInformation.class);
                        list.add(model);
                    }
                    ProductInfoAdapter adapter=new ProductInfoAdapter(context, list, rcv);
                    rcv.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (integers[0]==4)
        {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("coupon");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                    ArrayList<Coupon> list = new ArrayList<>();
                    String a = "";
                    while (iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        Coupon model = next.getValue(Coupon.class);
                        if (!a.equals(model.getSeri()))
                        {
                            list.add(model);
                            a=model.getSeri();
                        }

                    }
                    CouponAdapter adapter=new CouponAdapter(context, list, rcv);
                    rcv.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (integers[0]==5)
        {
            Query ref= FirebaseDatabase.getInstance().getReference().child("productinfomation").orderByChild("idproduct").equalTo(id);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProductInformation info= dataSnapshot.getChildren().iterator().next().getValue(ProductInformation.class);
                    tv.setText(info.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (integers[0]==6)
        {
            Query reff= FirebaseDatabase.getInstance().getReference().child("category").orderByChild("idcategory").equalTo(id);
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Category cate= dataSnapshot.getChildren().iterator().next().getValue(Category.class);
                    tv.setText(cate.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (integers[0]==7)
        {
            Query ref= FirebaseDatabase.getInstance().getReference().child("productinfomation").orderByChild("idproduct").equalTo(id);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProductInformation info= dataSnapshot.getChildren().iterator().next().getValue(ProductInformation.class);
                    ProductInfoDAO.deleteInfo(context, info.getIdproductInformation());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Log.d("TAG", "doInBackground: Lỗi ShowAsyncTask");
        }

        return null;
    }
}