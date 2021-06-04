package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.project1.Adapter.History.HistoryAdapter;
import com.example.project1.Main;
import com.example.project1.Model.OrderDetail;
import com.example.project1.Model.PaymentModel;
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

public class History extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    ArrayList<ProductModel> productModels;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_history);
        toolbar = (Toolbar) findViewById(R.id.toolbarHistory);
        actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.rcv_history);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        productModels = new ArrayList<>();
        getpayment(Main.iduser);
        toolbar.setTitle("Lịch Sử Mua Hàng");
    }
    private void getpayment(String iduser)
    {
        database.child("payment").orderByChild("iduser").equalTo(iduser).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    PaymentModel model = next.getValue(PaymentModel.class);
                    getorderdetail(model.getIdorder());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getorderdetail(String idorder)
    {
        Query query = database.child("orderdetail").orderByChild("idorder").equalTo(idorder);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    OrderDetail model = next.getValue(OrderDetail.class);
                    getproduct(model.getIdproduct());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getproduct(String idproduct)
    {
        Query query = database.child("product").child(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    ProductModel model = snapshot.getValue(ProductModel.class);
                    productModels.add(model);

                adapter = new HistoryAdapter(History.this,productModels);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}