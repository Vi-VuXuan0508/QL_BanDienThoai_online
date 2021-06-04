package com.example.project1.nhan.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.ProductInformation;
import com.example.project1.R;
import com.example.project1.nhan.DAO.ProductInfoDAO;
import com.example.project1.nhan.adapter.ProductInfoAdapter;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductInfoFragment extends Fragment {
    RecyclerView rcv;
    String id;

    public ProductInfoFragment(String id_po) {
        this.id=id_po;
    }

    public ProductInfoFragment() {

    }

    public static ProductInfoFragment newInstance() {
        return new ProductInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_info_fragment, container, false);

        rcv=view.findViewById(R.id.rcvProductInfo);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        new ShowAsyncTask(getContext(),rcv,id).execute(3);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }


}