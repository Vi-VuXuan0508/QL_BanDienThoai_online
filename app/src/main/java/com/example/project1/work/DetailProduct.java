package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Adapter.ProDuct.ProDuctOppoAdapter;
import com.example.project1.Adapter.ProDuctDetailAdapter;
import com.example.project1.Main;
import com.example.project1.Model.CartModel;
import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class DetailProduct extends AppCompatActivity {
    ImageView img;
    TextView txttitleproduct,txtprice,txtpricecoupon,
            txtscreen,txtcamera,txtcameraselfie,txtram,txtrom,txtcpu,
            txtgpu,txtpin,txthedieuhanh,txtorigin,txtyear,txtsim;
    RecyclerView rcvdetail;
    Toolbar toolbar;
    ActionBar actionBar;
    ProDuctDetailAdapter adapter;
    ArrayList<ProductModel> productModels;
    ArrayList<ProductInformation> productInformations;
    ArrayList<CartModel> cartModels;
    RelativeLayout btninsertcart;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_detail_product);
        toolbar = (Toolbar) findViewById(R.id.toolbarproductdetail);
        actionBar= getSupportActionBar();
        rcvdetail = findViewById(R.id.rcv_product_detail);
        img = findViewById(R.id.img_detail);

        txttitleproduct = findViewById(R.id.txt_producttitle_detail);
        txtprice = findViewById(R.id.txt_productprice_detail);
        txtpricecoupon = findViewById(R.id.txt_productpricecoupon_detail);
        txtscreen = findViewById(R.id.txt_detail_srceen);
        txtcamera = findViewById(R.id.txt_detail_camera);
        txtcameraselfie = findViewById(R.id.txt_detail_cameraselfie);
        txtram = findViewById(R.id.txt_detail_ram);
        txtrom = findViewById(R.id.txt_detail_rom);
        txtcpu = findViewById(R.id.txt_detail_cpu);
        txtgpu = findViewById(R.id.txt_detail_gpu);
        txtpin = findViewById(R.id.txt_detail_pin);
        txthedieuhanh = findViewById(R.id.txt_detail_hedieuhanh);
        txtorigin = findViewById(R.id.txt_detail_origin);
        txtyear = findViewById(R.id.txt_detail_year);
        txtsim = findViewById(R.id.txt_detail_sim);
        btninsertcart = findViewById(R.id.btn_detail_insert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcvdetail.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(DetailProduct.this,RecyclerView.HORIZONTAL,false);
        rcvdetail.setLayoutManager(manager);

        cartModels = new ArrayList<>();
        productInformations= new ArrayList<>();
        productModels = new ArrayList<>();
        final String idproduct = getIntent().getStringExtra("idproduct");
        final String iduser = Main.iduser;
        Log.i("Fsae",iduser);
        toolbar.setTitle("Chi Tiết Sản Phẩm");
        setToolbar();
        getdetail(idproduct);
        btninsertcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idcart = database.push().getKey();
                Query query = database.child("cart").orderByChild("idproduct").equalTo(idproduct);
                cartModels.clear();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                        while (iterator.hasNext())
                        {
                            DataSnapshot next = iterator.next();
                            CartModel model = next.getValue(CartModel.class);
                            cartModels.add(model);
                        }
                        if (cartModels.size()==0)
                        {
                            Log.i("Fsae",Main.iduser);
                            database.child("cart").child(idcart).setValue(new CartModel(idcart,idproduct,Main.iduser,1));
                            Toast.makeText(DetailProduct.this, "Thêm Vào Giỏ Hàng Thành Công", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(DetailProduct.this, "Thêm Vào Giỏ Hàng Thành Công",
 Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }

    public void getproductinfor(String idproduct)
    {
        Query query = database.child("productinfomation").orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                ProductInformation model = new ProductInformation();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    model = next.getValue(ProductInformation.class);

                }
                Log.i("fesafesagesa",""+model.getIdproductInformation());
                txtscreen.setText(model.getScreen());
                txtcamera.setText(model.getCamera());
                txtcameraselfie.setText(model.getCameraselfie());
                txtcpu.setText(model.getCpu());
                txthedieuhanh.setText(model.getSystem());
                txtorigin.setText(model.getOrigin());
                txtpin.setText(model.getPin());
                txtram.setText(model.getRam());
                txtrom.setText(model.getRom());
                txtyear.setText(model.getYearofmanufacture());
                txtgpu.setText(model.getGpu());
                txtsim.setText(model.getSim());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getdetail(String idproduct)
    {
        Query query = database.child("product").
                orderByChild("idproduct").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                ProductModel model = null;
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    model = next.getValue(ProductModel.class);

                }
                Picasso.get().load(model.getImage()).into(img);
                txttitleproduct.setText(model.getTitle());
                txtprice.setText(String.valueOf(model.getPrice()));
                txtpricecoupon.setText(String.valueOf(model.getPricecoupon()));
                getproductinfor(model.getIdproduct());
                getproduct(model.getIdcategory(),model.getIdproduct());
//                    txtprice.setText(String.valueOf(model.get()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproduct(String idcategory, final String idproduct)
    {
        Query query = database.child("product").
                orderByChild("idcategory").equalTo(idcategory).limitToFirst(3);
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
                for (int i = 0; i <productModels.size() ; i++) {
                    if (productModels.get(i).getIdproduct().equals(idproduct))
                    {
                        productModels.remove(i);
                    }
                }

                adapter = new ProDuctDetailAdapter(DetailProduct.this,productModels);
                rcvdetail.setAdapter(adapter);
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
                Intent i = new Intent(DetailProduct.this,Cart.class);
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

    private void setToolbar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = DetailProduct.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        ((AppCompatActivity)DetailProduct.this).setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

}