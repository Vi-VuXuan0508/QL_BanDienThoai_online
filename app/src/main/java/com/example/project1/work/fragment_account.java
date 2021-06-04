package com.example.project1.work;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.Main;
import com.example.project1.Model.ProductModel;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.example.project1.kien.Login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class  fragment_account extends Fragment {
    TextView txt_username,txt_email;
    CardView card_coupon,card_manage,card_product,card_history,card_user, card_logout;
    ImageView img_user;
    private FirebaseAuth mAuth;
    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__account, container, false);
        txt_username = view.findViewById(R.id.txt_user_name);
        txt_email = view.findViewById(R.id.txt_user_mail);
        card_logout = view.findViewById(R.id.card_user_logout);
        img_user = view.findViewById(R.id.img_user);
        card_user = view.findViewById(R.id.card_user_more);
        card_coupon = view.findViewById(R.id.card_user_coupon_more);
        card_manage = view.findViewById(R.id.card_user_manage_more);
        card_product = view.findViewById(R.id.card_user_product_more);
        card_history = view.findViewById(R.id.card_user_history_more);
        card_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),personal_information.class);
                startActivity(intent);
            }
        });
        card_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),CouponProduct.class);
                startActivity(intent);
            }
        });
        card_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Manager.class);
                startActivity(intent);
            }
        });
        card_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),ProDuctFinal.class);
                startActivity(intent);
            }
        });
        card_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),History.class);
                startActivity(intent);
            }
        });
        card_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance()!=null)
                {
                    Main.iduser =null;

                    FirebaseAuth.getInstance().signOut();
                    GoogleSignIn.getClient(getActivity(),new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                            .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Sign Out Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Main.iduser =null;
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
//
            }
        });
        database.child("user").child(Main.iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    UserModel model = snapshot.getValue(UserModel.class);
                    txt_username.setText(model.getFullname());
                    txt_email.setText(model.getEmail());
                    Picasso.get().load(model.getImage()).into(img_user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}