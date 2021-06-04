package com.example.project1.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;

public class ProDuctDAO {
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage;
//    private StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-f57c4.appspot.com/");
    private Context context;
    ArrayList<ProductModel> listProduct = new ArrayList<>();
    public ProDuctDAO(Context context) {
        this.context = context;
    }
    public void update(ArrayList<ProductModel> listProduct)
    {
        this.listProduct = listProduct;
    }
    public void GetProDuctCategory(String idcategory)
    {
         DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Query query = database.child("product").
                orderByChild("idcategory").equalTo(idcategory);
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
                    Log.i("fdafe",""+list.size());
                    update(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("datalist213",""+listProduct.size());
    }
    public ArrayList<ProductModel> getcategory(String idcategory)
    {
        GetProDuctCategory(idcategory);
        Log.i("esafesa",""+listProduct.size());
        return listProduct;
    }

    public ArrayList<ProductModel> getAll()
    {
        final ArrayList<ProductModel> list = new ArrayList<>();
        database.child("product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductModel model = next.getValue(ProductModel.class);
                    list.add(model);
                }
                Log.i("darefdsa",""+list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }
    public static ArrayList<ProductInformation> getall(String idproduct)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayList<ProductInformation> informations = new ArrayList<>();
        Query query = database.child("productinformation").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductInformation model = next.getValue(ProductInformation.class);
                    informations.add(model);
                }
                Log.i("darefdsa",""+informations.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return informations;
    }
}
