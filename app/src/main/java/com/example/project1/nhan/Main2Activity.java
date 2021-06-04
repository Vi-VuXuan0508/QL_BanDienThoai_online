package com.example.project1.nhan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.project1.R;
import com.example.project1.nhan.fragment.CategoryFragment;
import com.example.project1.nhan.fragment.CouponFragment;
import com.example.project1.nhan.fragment.ProductFragment;
import com.example.project1.nhan.fragment.ProductInfoFragment;


public class Main2Activity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);
        toolbar = (Toolbar) findViewById(R.id.toolbarmainfragment);
        toolbar.setTitle("Quản Lý Điện Thoại");
        actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment;
        Intent intent=this.getIntent();
        String code=intent.getStringExtra("code");
        String id_ca=intent.getStringExtra("idCaterogy");
        String id_po=intent.getStringExtra("idProduct");
        String name=intent.getStringExtra("nameCategory");
        if (code==null||code.equalsIgnoreCase("category"))
        {
            fragment=new CategoryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,fragment).commit();

        }
        else if (code.equalsIgnoreCase("product"))
        {
            fragment=new ProductFragment(id_ca,name);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,fragment).commit();
        }
        else if (code.equalsIgnoreCase("info"))
        {
            fragment=new ProductInfoFragment(id_po);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,fragment).commit();
            toolbar.setTitle("Thông Tin Chi Tiết");
        }
        else if (code.equalsIgnoreCase("coupon"))
        {
            fragment=new CouponFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,fragment).commit();
            toolbar.setTitle("Quản Lý Voucher");
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (context != null && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;

                }

            }

        }

        return true;

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
