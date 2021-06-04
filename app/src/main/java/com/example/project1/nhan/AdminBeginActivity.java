package com.example.project1.nhan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project1.Login.ForgetActivity;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.example.project1.hoai.admin_quanlyhoadon;
import com.example.project1.kien.Login.ListUserActivity;
import com.example.project1.kien.Login.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class AdminBeginActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    ImageView imvdt,imvvc,imnd,imhd;
    private String iduser = null;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_begin);
        toolbar = (Toolbar) findViewById(R.id.toolbar_Main);
        actionBar= getSupportActionBar();
        toolbar.setTitle("QUẢN LÝ ĐIỆN THOẠI");
        iduser = getIntent().getStringExtra("iduser");
        imhd = findViewById(R.id.img_qlhd);
        imnd = findViewById(R.id.img_qlnd);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );

        imvdt=findViewById(R.id.imvQLDT);
        imvvc=findViewById(R.id.imvQLVC);
        imhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminBeginActivity.this, admin_quanlyhoadon.class);
                startActivity(i);
            }
        });
        imnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminBeginActivity.this,ListUserActivity.class);
                startActivity(i);
            }
        });
        imvdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminBeginActivity.this,Main2Activity.class);
                i.putExtra("code","category");
                startActivity(i);
            }
        });
        imvvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminBeginActivity.this,Main2Activity.class);
                i.putExtra("code","coupon");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_resetpassword:
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminBeginActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_reset_pass,null);
                builder.setView(view);
                final TextInputEditText txtoldpass = view.findViewById(R.id.txt_dialog_reset_oldpass);
                final TextInputEditText txtnewpass = view.findViewById(R.id.txt_dialog_reset_newpass);
                final TextInputEditText txtpass = view.findViewById(R.id.txt_dialog_reset_pass);
                final Button btnforger_reset = view.findViewById(R.id.btn_dialog_reset);
                final Dialog dialog = builder.create();
                dialog.show();
                reference.child("/user").child(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final UserModel user = snapshot.getValue(UserModel.class);
                                btnforger_reset.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (txtoldpass.getText().toString().equals(user.getPassword()))
                                        {
                                            if (txtnewpass.length()==0||txtpass.length()==0)
                                            {
                                                Toast.makeText(AdminBeginActivity.this, "Vui Lòng Nhập Mật Khẩu Mới", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                if (txtnewpass.getText().toString().equals(txtpass.getText().toString()))
                                                {
                                                    reference.child("/user").child(user.getIduser())
                                                            .setValue(new UserModel(user.getIduser(),user.getUsername(),txtpass.getText().toString(),user.getStatus(),user.getFullname(),user.getEmail(),user.getAddress(),user.getImage(),user.getDate(),user.getPhone()));
                                                    dialog.dismiss();
                                                    Toast.makeText(AdminBeginActivity.this, "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(AdminBeginActivity.this, "Mật Khẩu không Trùng Khớp", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        }
                                        else
                                        {
                                            Toast.makeText(AdminBeginActivity.this, "Sai Mật Khẩu", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }





                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            }

            case R.id.action_logout:
            {
                iduser = null;
                Intent intent = new Intent(AdminBeginActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}