package com.example.project1.hoai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.project1.Adapter.Payment.PaymentAdapter;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.PaymentModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class admin_quanlyhoadon extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<PaymentModel> paymentModels;
    PaymentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quanlyhoadon);
        toolbar = (Toolbar) findViewById(R.id.toolbar_admin_quanlydonhang);
        actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quản Lý Hóa Đơn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        recyclerView = findViewById(R.id.rcv_admin_quanlydonhang);
        paymentModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        reference.child("payment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paymentModels.clear();
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    PaymentModel model = next.getValue(PaymentModel.class);
                    paymentModels.add(model);
                }
                adapter = new PaymentAdapter(admin_quanlyhoadon.this,paymentModels);
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}