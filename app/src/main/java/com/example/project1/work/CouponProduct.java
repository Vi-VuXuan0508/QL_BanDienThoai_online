package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.project1.Adapter.Coupon.CouponAdapter;
import com.example.project1.Adapter.ProDuctDetailAdapter;
import com.example.project1.Main;
import com.example.project1.Model.Coupon;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CouponProduct extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Coupon> couponArrayList;
    CouponAdapter couponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_product);
        toolbar = (Toolbar) findViewById(R.id.toolbarcouponproduct);
        recyclerView = findViewById(R.id.rcv_coupon);
        actionBar= getSupportActionBar();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        couponArrayList = new ArrayList<>();
        toolbar.setTitle("Khuyến Mãi");
        Query query = database.child("coupon").orderByChild("iduser").equalTo(Main.iduser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    Coupon model = next.getValue(Coupon.class);
                    couponArrayList.add(model);
                }
                couponAdapter = new CouponAdapter(CouponProduct.this,couponArrayList);
                recyclerView.setAdapter(couponAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        MenuItem item =  menu.findItem(R.id.action_search);
        item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_cart:
            {
                Intent i = new Intent(CouponProduct.this,Cart.class);
                startActivity(i);
                break;
            }

            case android.R.id.home:
            {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}