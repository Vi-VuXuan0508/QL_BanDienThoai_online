package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.project1.Adapter.ProDuct.ProDuctCategoryAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctOppoAdapter;
import com.example.project1.Main;
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

public class ProDuctCategory extends AppCompatActivity {
    private ActionBar actionBar;
    Toolbar toolbar;
    RecyclerView rcvproductcategory;
    ArrayList<ProductModel> productModels;
    ProDuctCategoryAdapter adapter;


    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_pro_duct_category);

        toolbar = (Toolbar) findViewById(R.id.toolbarproductcategory);
        actionBar= getSupportActionBar();
        rcvproductcategory = findViewById(R.id.rcv_product_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcvproductcategory.setHasFixedSize(true);
        rcvproductcategory.setLayoutManager(new GridLayoutManager(ProDuctCategory.this,2));
        productModels = new ArrayList<>();
        String idcategory = getIntent().getStringExtra("idcategory");
        toolbar.setTitle("Sản Phẩm Theo Loại");
        getproduct(idcategory);
    }

    public void getproduct(String idcategory)
    {
        Query query = database.child("product").
                orderByChild("idcategory").equalTo(idcategory);
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

                adapter = new ProDuctCategoryAdapter(ProDuctCategory.this,productModels);

                rcvproductcategory.setAdapter(adapter);
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

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_cart:
            {
                Intent i = new Intent(ProDuctCategory.this,Cart.class);
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