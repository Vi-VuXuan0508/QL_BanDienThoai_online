package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.project1.DAO.ProDuctDAO;
import com.example.project1.work.Cart;
import com.example.project1.work.DetailProduct;
import com.example.project1.work.fragment_account;
import com.example.project1.work.fragment_home;
import com.example.project1.work.fragment_store;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {
    private ActionBar actionBar;
    Toolbar toolbar;
    Fragment fragment;
    FirebaseAuth firebaseAuth;
    BottomNavigationView navigation;
    ProDuctDAO proDuctDAO;
    public static String iduser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );

        toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        proDuctDAO = new ProDuctDAO(Main.this);
        Main.iduser = getIntent().getStringExtra("iduser");
        Log.i("iduser",iduser);
        toolbar.setTitle("Trang Chủ");
        setToolbar();
        fragment = new fragment_home();
        loadFragment(fragment);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                {
                    toolbar.setTitle("Trang Chủ");
                    fragment = new fragment_home();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.navigation_store:
                    {
                    fragment = new fragment_store();
                    toolbar.setTitle("Cửa Hàng");
                    loadFragment(fragment);
                    return true;
                     }
                case R.id.navigation_user:
                    {
                    fragment = new fragment_account();
                    toolbar.setTitle("Cá Nhân");
                    loadFragment(fragment);
                    return true;
                }
            }
            return false;
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar,menu);
//        MenuItem item =  menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.action_cart:
//            {
//                Intent i = new Intent(Main.this, Cart.class);
//                startActivity(i);
//
//                break;
//            }
//            case R.id.action_search:
//            {
//                Toast.makeText(this, "Open", Toast.LENGTH_LONG).show();
//                break;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setToolbar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Main.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        ((AppCompatActivity)Main.this).setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(0xFFFFFFFF);
    }
}