package com.example.project1.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ForgetActivity extends AppCompatActivity {
    TextInputEditText txt_username,txt_email,txt_phone;
    Button btn_forget;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        txt_username = findViewById(R.id.txtUser_forget);
        txt_email= findViewById(R.id.txtEmail_forget);
        txt_phone = findViewById(R.id.txtPhone_forget);
        btn_forget= findViewById(R.id.btnForget);
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("/user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Iterable<DataSnapshot> iterable = snapshot.getChildren();
                            Iterator<DataSnapshot> iterator = iterable.iterator();
                            while (iterator.hasNext()) {
                                DataSnapshot next = iterator.next();
                                final UserModel user = next.getValue(UserModel.class);
                                if (txt_username.getText().toString().equals(user.getUsername())&&txt_email.getText().toString().equals(user.getEmail())&&txt_phone.getText().toString().equals(String.valueOf(user.getPhone())))
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgetActivity.this);
                                    View view = getLayoutInflater().inflate(R.layout.dialog_forget_resetpass,null);
                                    builder.setView(view);
                                    final TextInputEditText txtnewpass = view.findViewById(R.id.txt_dialog_forget_newpass);
                                    final TextInputEditText txtpass = view.findViewById(R.id.txt_dialog_forget_pass);
                                    Button btnforger_reset = view.findViewById(R.id.btn_dialog_forget);
                                    btnforger_reset.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (txtnewpass.getText().toString().equals(txtpass.getText().toString()))
                                            {
                                                reference.child("/user").child(user.getIduser())
                                                        .setValue(new UserModel(user.getIduser(),user.getUsername(),txtpass.getText().toString(),user.getStatus(),user.getFullname(),user.getEmail(),user.getAddress(),user.getImage(),user.getDate(),user.getPhone()));
                                                Toast.makeText(ForgetActivity.this, "Thanh Cong", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(ForgetActivity.this, "Mat Khau Ko Trung Nhau", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    final Dialog dialog = builder.create();
                                    dialog.show();
                                }
                                else
                                {
                                    Toast.makeText(ForgetActivity.this, "Tai Khoan Ko ton tai", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}