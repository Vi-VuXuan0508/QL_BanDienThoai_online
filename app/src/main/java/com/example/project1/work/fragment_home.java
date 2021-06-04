package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.project1.Adapter.Category.CategoryHomeAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctNokiaAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctIphoneAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctOppoAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctSamSumAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctVsmartAdapter;
import com.example.project1.Adapter.ProDuct.ProDuctXiaomiAdapter;
import com.example.project1.DAO.ProDuctDAO;
import com.example.project1.Main;
import com.example.project1.Model.Category;
import com.example.project1.Model.Coupon;
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

public class fragment_home extends Fragment {
    RecyclerView rcvhome,rcviphone,rcvoppo,rcvvsmart,rcvnokia,rcvxiaomi,rcvsamsum;
    TextView txtiphone,txtoppo,txtvsmart,txtnokia,txtxiaomi,txtsamsum;
    ImageView imgcoupon,imgwelcome,imgcouponend;
    AnimationDrawable rocketAnimation;
    ViewFlipper viewFlipper;
    ProDuctDAO proDuctDAO;
    ArrayList<ProductModel> productModelsIphone;
    ArrayList<ProductModel> productModelsSamSum;
    ArrayList<ProductModel> productModelsOppo;
    ArrayList<ProductModel> productModelsNokia;
    ArrayList<ProductModel> productModelsVsmart;
    ArrayList<ProductModel> productModelsXiaomao;
    ArrayList<Category> categoriesmodel;
    ProDuctIphoneAdapter adapterIphone;
    ProDuctNokiaAdapter adapterNokia;
    ProDuctOppoAdapter adapterOppo;
    ProDuctVsmartAdapter adapterVsmart;
    ProDuctSamSumAdapter adaptersamSum;
    ProDuctXiaomiAdapter adapterxiaomi;
    CategoryHomeAdapter categoryHomeAdapter;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewFlipper = view.findViewById(R.id.imghomewellcome);
        imgcoupon = view.findViewById(R.id.imghomecoupon);

        imgcouponend = view.findViewById(R.id.imghomecouponend);
        // tham chiếu rcv
        rcviphone = view.findViewById(R.id.rcvhomeproductIphone);
        rcvoppo = view.findViewById(R.id.rcvhomeproductOppo);
        rcvvsmart = view.findViewById(R.id.rcvhomeproductVsmart);
        rcvnokia = view.findViewById(R.id.rcvhomeproductNokia);
        rcvxiaomi = view.findViewById(R.id.rcvhomeproductXiaomi);
        rcvsamsum = view.findViewById(R.id.rcvhomeproductSamsum);
        rcvhome = view.findViewById(R.id.rcvhomecategory);
        //tham chieu textview
        txtiphone= view.findViewById(R.id.txthomeproductIphone);
        txtnokia= view.findViewById(R.id.txthomeproductNokia);
        txtoppo= view.findViewById(R.id.txthomeproductOppo);
        txtsamsum= view.findViewById(R.id.txthomeproductSamsum);
        txtvsmart= view.findViewById(R.id.txthomeproductVsmart);
        txtxiaomi= view.findViewById(R.id.txthomeproductXiaomi);
        // set layout cho rcv
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);

        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager manager4 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager manager5 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager manager6 = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rcviphone.setLayoutManager(manager);
        rcvoppo.setLayoutManager(manager1);
        rcvvsmart.setLayoutManager(manager2);
        rcvnokia.setLayoutManager(manager3);
        rcvxiaomi.setLayoutManager(manager4);
        rcvsamsum.setLayoutManager(manager5);
        rcvhome.setLayoutManager(manager6);
        //welcome
        int images[] = {R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i6};
        for (int i = 0; i < images.length; i++){
            fillperImages(images[i]);
        }
        //goi phuong thuc ArrayList
        productModelsIphone = new ArrayList<>();
        productModelsNokia = new ArrayList<>();
        productModelsOppo  = new ArrayList<>();
        productModelsSamSum = new ArrayList<>();
        productModelsVsmart = new ArrayList<>();
        productModelsXiaomao  = new ArrayList<>();
        categoriesmodel = new ArrayList<>();
        proDuctDAO = new ProDuctDAO(getContext());
        setHasOptionsMenu(true);
//        ButterKnife.bind(this, view);
        getimagecoupon();
        getimagecouponend();
        // gọi phương thức data
        database.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesmodel.clear();
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    final Category model = next.getValue(Category.class);
                    categoriesmodel.add(model);
                    if (model.getName().trim().equalsIgnoreCase("iphone"))
                    {
                        txtiphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });

                        getproductiphone(model.getIdcategory());
                    }
                    else if (model.getName().trim().equalsIgnoreCase("oppo"))
                    {
                        txtoppo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });
                        getproductoppo(model.getIdcategory());
                    }
                    else if ((model.getName().trim().equalsIgnoreCase("nokia")))
                    {
                        txtnokia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });
                        getproductnokia(model.getIdcategory());
                    }
                    else if (model.getName().trim().equalsIgnoreCase("samsung"))
                    {
                        txtsamsum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });
                        getproductsamsum(model.getIdcategory());
                    }
                    else if ((model.getName().trim().equalsIgnoreCase("vsmart")))
                    {
                        txtvsmart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });
                        getproductvsmart(model.getIdcategory());
                    }
                    else if (model.getName().trim().equalsIgnoreCase("xiaomi"))
                    {
                        txtxiaomi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ProDuctCategory.class);
                                intent.putExtra("idcategory",model.getIdcategory());
                                Log.i("id",""+model.getIdcategory());
                                startActivity(intent);
                            }
                        });
                        getproductxiaomi(model.getIdcategory());
                    }
                }
                categoryHomeAdapter = new CategoryHomeAdapter(getActivity(),categoriesmodel);
                rcvhome.setAdapter(categoryHomeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








//
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar,menu);
        MenuItem item =  menu.findItem(R.id.action_search);
         item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_cart:
            {
                Intent i = new Intent(getContext(), Cart.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void fillperImages(int image){
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
    }
    public void getproductoppo(String idcategory)
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
                    productModelsOppo.add(model);
                }

                adapterOppo = new ProDuctOppoAdapter(getActivity(),productModelsOppo);
                rcvoppo.setAdapter(adapterOppo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getproductiphone(String idcategory)
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
                    productModelsIphone.add(model);
                }
                adapterIphone = new ProDuctIphoneAdapter(getActivity(),productModelsIphone);
                rcviphone.setAdapter(adapterIphone);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproductvsmart(String idcategory)
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
                    productModelsVsmart.add(model);
                }
                adapterVsmart = new ProDuctVsmartAdapter(getActivity(),productModelsVsmart);
                rcvvsmart.setAdapter(adapterVsmart);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproductnokia(String idcategory)
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
                    productModelsNokia.add(model);
                }
                adapterNokia = new ProDuctNokiaAdapter(getActivity(),productModelsNokia);
                rcvnokia.setAdapter(adapterNokia);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproductxiaomi(String idcategory)
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
                    productModelsXiaomao.add(model);
                }
                adapterxiaomi = new ProDuctXiaomiAdapter(getActivity(),productModelsXiaomao);
                rcvxiaomi.setAdapter(adapterxiaomi);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getproductsamsum(String idcategory)
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
                    productModelsSamSum.add(model);
                }
                adaptersamSum = new ProDuctSamSumAdapter(getActivity(),productModelsSamSum);
                rcvsamsum.setAdapter(adaptersamSum);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getimagecoupon()
    {
        Query query = database.child("coupon").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    Coupon model = next.getValue(Coupon.class);
                    Picasso.get().load(model.getImage()).into(imgcoupon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getimagecouponend()
    {
        Query query = database.child("coupon").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    Coupon model = next.getValue(Coupon.class);
                    Picasso.get().load(model.getImage()).into(imgcouponend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}