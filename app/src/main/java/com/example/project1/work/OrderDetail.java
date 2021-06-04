package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project1.Adapter.Cart.CartAdapter;
import com.example.project1.Adapter.Coupon.CouponAdapter;
import com.example.project1.Adapter.OrderDetail.OrderDetailAdapter;
import com.example.project1.Main;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.Coupon;
import com.example.project1.Model.PaymentModel;
import com.example.project1.Model.ProductModel;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderDetail extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    OrderDetailAdapter orderDetailAdapter;
    ArrayList<ProductModel> productModels;
    TextView txt_reset,txt_name,txt_address,txt_phone,txt_pricefinal,txt_orderdetail_final;
    LinearLayout btn_orderdetail;
    private String idorder = null;
    private String idpayment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_order_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbarorderdetail);
        recyclerView = findViewById(R.id.rcv_orderdetail);
        actionBar= getSupportActionBar();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productModels = new ArrayList<>();
        txt_reset = findViewById(R.id.txt_orderdetail_resetuser);
        txt_name = findViewById(R.id.txt_orderdetail_name);
        txt_address = findViewById(R.id.txt_orderdetail_address);
        txt_phone = findViewById(R.id.txt_orderdetail_phone);
        txt_pricefinal = findViewById(R.id.txt_orderdetail_price);
        txt_orderdetail_final = findViewById(R.id.txt_orderdetail_final);
        btn_orderdetail = findViewById(R.id.btn_orderdetail);
        final double price = getIntent().getDoubleExtra("price",0.0);
        txt_orderdetail_final.setText(String.valueOf(price));
        toolbar.setTitle("Chi Tiết Đóa Đơn");
//        database.child("user").
//                child("-MMZBHIBKIy7Lse1cQpW")
//                .setValue(new UserModel
//                        ("-MMZBHIBKIy7Lse1cQpW","admin","admin","admin","Nguyễn Văn Hoài","hoainvps13805@fpt.edut.vn","1379 Hồng Hoa","https://firebasestorage.googleapis.com/v0/b/project1-f57c4.appspot.com/o/images1605272180138.png?alt=media&token=e4ab2222-d988-4e1e-9813-3ea834bf6eba",true,0342134021));
        getuser(Main.iduser);
        getidcart(Main.iduser);
        btn_orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idorder = getIntent().getStringExtra("idorder");
                idpayment = database.push().getKey();
                database.child("payment").child(idpayment).
                        setValue(new PaymentModel(idpayment,Main.iduser,idorder,price));
                Query query = database.child("cart").orderByChild("iduser").equalTo(Main.iduser);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                        while (iterator.hasNext())
                        {
                            DataSnapshot next = iterator.next();
                            CartModel model = next.getValue(CartModel.class);
                            database.child("cart").child(model.getIdcart()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                LayoutInflater layoutInflater = OrderDetail.this.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.dialog_payment,null);
                builder.setView(view);
                final AlertDialog alertDialog  = builder.create();
                alertDialog.show();
                Button button = view.findViewById(R.id.btn_payment);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderDetail.this, Main.class);
                        intent.putExtra("iduser",Main.iduser);
                        startActivity(intent);
                    }
                });

            }
        });
    }
    private void getuser(String user)
    {
        Query query = database.child("user").orderByChild("iduser").equalTo(user);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                UserModel model = null;
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    model = next.getValue(UserModel.class);
                    txt_name.setText(model.getFullname());
                    txt_address.setText(model.getAddress());
                    txt_phone.setText(String.valueOf(model.getPhone()));
                    Log.i("modelll",""+model.getIduser());
                }
                Log.i("modelll421",""+model.getIduser());
                final UserModel finalModel = model;
                Log.i("modelll421",""+finalModel.getIduser());
                txt_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                        LayoutInflater layoutInflater = OrderDetail.this.getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.dialog_orderdetail_reset,null);
                        builder.setView(view);
                        final AlertDialog alertDialog  = builder.create();
                        alertDialog.show();
                        final TextInputEditText txt_fullname = view.findViewById(R.id.txt_dialog_fullname);
                        final TextInputEditText txt_dialog_address = view.findViewById(R.id.txt_dialog_address);
                        final TextInputEditText txt_dialog_phone = view.findViewById(R.id.txt_dialog_phone);
                        ImageView imageView = view.findViewById(R.id.img_dialog);
                        Button btn_dialog = view.findViewById(R.id.btn_dialog_orderdetail);
                        txt_fullname.setText(txt_name.getText().toString());
                        txt_dialog_address.setText(txt_address.getText().toString());
                        txt_dialog_phone.setText(txt_phone.getText().toString());
                        btn_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                database.child("user").child(finalModel.getIduser())
                                        .setValue(new UserModel(finalModel.getIduser(),finalModel.getUsername(),finalModel.getPassword(),finalModel.getStatus(),txt_fullname.getText().toString(),finalModel.getEmail(),txt_dialog_address.getText().toString(),finalModel.getImage(),finalModel.getDate(),Integer.parseInt(txt_dialog_phone.getText().toString())));
                                alertDialog.dismiss();
                            }
                        });
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });



                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getidcart(String iduser)
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

                orderDetailAdapter = new OrderDetailAdapter(OrderDetail.this,productModels);
                recyclerView.setAdapter(orderDetailAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onStop() {
        if (idorder == null)
        {
            final String idorder1 = getIntent().getStringExtra("idorder");
            database.child("order").child(idorder1).removeValue();
            database.child("orderdetail").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                    while (iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        com.example.project1.Model.OrderDetail model = next.getValue(
                                com.example.project1.Model.OrderDetail.class);
                        if (model.getIdorder().equals(idorder1))
                        {
                            database.child("orderdetail").child(model.getIdorderdetail()).removeValue();
                        }
                        else
                        {
                            // not dothing
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });


        }
        else
        {
//            not doing
        }
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        if (idpayment!=null)
        {
            database.child("payment").child(idpayment).removeValue();
        }

        super.onBackPressed();
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