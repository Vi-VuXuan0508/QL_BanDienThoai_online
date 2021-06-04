package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Adapter.Cart.CartAdapter;
import com.example.project1.Main;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.OrderModel;
import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Cart extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    ArrayList<OrderModel> orderModels;
    ArrayList<CartModel> cartModels;
    ArrayList<ProductModel> productModels;
    ArrayList<ProductInformation> productInformations;
    TextView txt_coupon,txt_pricetotal,txt_pricesee,txt_pricecoupon,txt_priceship,txt_pricefinal,txt_seecoupon;
    Button btn_coupon,btn_cart;
     private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
    final ArrayList<Integer> countlist = new ArrayList<>();
    private static final int REQUEST_CODE_EXAMPLE = 19345;
     private double moneytotal = 0 ;
    private double moneycouponproduct = 0 ;
    private double moneyfinal = 0 ;
    private double moneycoupon = 0 ;
    private int precent =0;
    private String idcoupon = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbarcart);
        actionBar= getSupportActionBar();
        orderModels = new ArrayList<>();
        cartModels = new ArrayList<>();
        productModels = new ArrayList<>();
        productInformations = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_cart);
        txt_coupon =  findViewById(R.id.txt_cart_coupon);
        txt_pricesee = findViewById(R.id.txt_cart_pricedown);
        txt_pricecoupon=findViewById(R.id.txt_cart_pricecoupon);
        txt_seecoupon = findViewById(R.id.txt_cart_see_coupon);
        txt_priceship = findViewById(R.id.txt_cart_priceship);
        txt_pricefinal = findViewById(R.id.txt_cart_pricefinal);
        txt_pricetotal = findViewById(R.id.txt_cart_pricetotal);
        btn_coupon = findViewById(R.id.btn_cart_coupon);
        btn_cart = findViewById(R.id.btn_cart_final);
        toolbar.setTitle("Giỏ Hàng");
        cartAdapter = new CartAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        txt_pricetotal.setText(String.valueOf(cartAdapter.getmoney()));
        getidcart(Main.iduser);
        txt_seecoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this,CouponProduct.class);
                startActivity(intent);
            }
        });
        idcoupon = getIntent().getStringExtra("idcoupon");
        precent = getIntent().getIntExtra("percent",0);
        txt_coupon.setText(idcoupon);
        txt_pricecoupon.setText("0");
        txt_priceship.setText("0");
        Query query = database.child("cart").orderByChild("iduser").equalTo(Main.iduser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                countlist.clear();
                moneytotal=0;
                moneycouponproduct = 0;
                moneyfinal = 0 ;
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    CartModel model = next.getValue(CartModel.class);
                    getproductmoney(model.getIdproduct(),model.getCout());
                    Log.i("idproduct",""+model.getIdproduct()+"cout:"+model.getCout());
                    countlist.add(model.getCout());
                }

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

                cartAdapter = new CartAdapter(Cart.this,productModels);
                recyclerView.setAdapter(cartAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproductmoney(final String idproduct, final int count)
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
                    moneytotal += model.getPrice()*count;
                    moneycouponproduct+=model.getPricecoupon()*count;

                }
                btn_coupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (precent<=100)
                        {
                            moneycoupon = moneytotal/precent;
                            txt_pricecoupon.setText(String.valueOf(moneycoupon));
                            moneyfinal=(moneytotal-moneycouponproduct)-moneycoupon;
                            txt_pricefinal.setText(String.valueOf(moneyfinal));
                        }
                        else
                        {
                            Log.d("precent", "onDataChange:"+precent);
                            Log.d("moneytotal", "onDataChange:"+moneytotal);
                            txt_pricecoupon.setText(String.valueOf(precent));
                            moneyfinal=(moneytotal-moneycouponproduct)-precent;
                            txt_pricefinal.setText(String.valueOf(moneyfinal));
                        }
                    }
                });
                txt_pricecoupon.setText("0");
                moneyfinal=(moneytotal-moneycouponproduct);
                txt_pricefinal.setText(String.valueOf(moneyfinal));
                txt_pricetotal.setText(String.valueOf(moneytotal));
                txt_pricesee.setText(String.valueOf(moneycouponproduct));

                //order
                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String idorder = database.push().getKey();

                        Date today = new Date();
                        String now = simpleDateFormat.format(today);
                        if (idcoupon!=null)
                        {
                            database.child("coupon").child(idcoupon).removeValue();
                        }
                        database.child("order").child(idorder)
                                .setValue(new OrderModel(idorder,now,"Chưa Thanh Toán",moneytotal,moneycoupon+moneycouponproduct,Double.parseDouble(txt_priceship.getText().toString()),moneyfinal));

                        for (int i = 0; i <productModels.size() ; i++)
                        {
                            String idorderdetail = database.push().getKey();
                            database.child("orderdetail").child(idorderdetail).setValue(new com.example.project1.Model.OrderDetail(idorderdetail,idorder,productModels.get(i).getIdproduct(),countlist.get(i).intValue()));

                        }
                        Intent intent = new Intent(Cart.this, com.example.project1.work.OrderDetail.class);
                        intent.putExtra("idorder",idorder);
                        intent.putExtra("price",moneyfinal);

                        startActivityForResult(intent,REQUEST_CODE_EXAMPLE);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQUEST_CODE_EXAMPLE) {
        }
    }

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