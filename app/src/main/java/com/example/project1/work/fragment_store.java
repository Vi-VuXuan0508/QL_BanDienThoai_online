package com.example.project1.work;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1.Adapter.ProDuct.ProDuctStoreAdapter;
import com.example.project1.Model.PaymentModel;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class fragment_store extends Fragment {
    RecyclerView recyclerView;
    ProDuctStoreAdapter adapter;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ProductModel> productModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView = view.findViewById(R.id.rcv_store);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        productModels = new ArrayList<>();
        setHasOptionsMenu(true);
        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModels.clear();
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    ProductModel model = next.getValue(ProductModel.class);
                    productModels.add(model);
                }
                adapter = new ProDuctStoreAdapter(getActivity(),productModels);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar,menu);
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
}