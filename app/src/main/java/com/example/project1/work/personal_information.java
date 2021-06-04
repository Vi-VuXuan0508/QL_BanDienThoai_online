package com.example.project1.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project1.Main;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class personal_information extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    LinearLayout linearLayout;
    CheckBox ckb_pass;
    TextInputEditText txt_email,txt_fullname,txt_phone,txt_date,txt_oldpass,txt_newpass,txt_pass;
    Button btn_reset;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
    final Calendar calendar = Calendar.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);
        actionBar= getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Thông Tin Cá Nhân");
        btn_reset = findViewById(R.id.btn_user_reset);
        txt_email = findViewById(R.id.txt_user_email);
        txt_fullname = findViewById(R.id.txt_user_fullname);
        txt_phone = findViewById(R.id.txt_user_phone);
        txt_date = findViewById(R.id.txt_user_date);
        txt_oldpass = findViewById(R.id.txt_user_oldpassword);
        txt_newpass = findViewById(R.id.txt_user_newpassword);
        txt_pass = findViewById(R.id.txt_user_password);
        ckb_pass = findViewById(R.id.ckb_user_resetpass);
        linearLayout = findViewById(R.id.LL_user_vb);
        ckb_pass.setChecked(false);
        linearLayout.setVisibility(View.GONE);
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(calendar.YEAR);
                DatePickerDialog datePickerDialog =new DatePickerDialog(personal_information.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(  year,  month,  dayOfMonth);
                        txt_date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        database.child("user").child(Main.iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final UserModel model = snapshot.getValue(UserModel.class);
                txt_email.setText(model.getEmail());
                txt_fullname.setText(model.getFullname());
                txt_date.setText(model.getDate());
                txt_phone.setText(String.valueOf(model.getPhone()));
                ckb_pass.setChecked(false);
                linearLayout.setVisibility(View.GONE);
                txt_newpass.setText("");
                txt_oldpass.setText("");
                txt_pass.setText("");
                ckb_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            btn_reset.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                            if ((txt_oldpass.getText().toString()).equals(model.getPassword()))
                            {
                                if ((txt_newpass.getText().toString()).length()!=0&&(txt_pass.getText().toString()).length()!=0)
                                {
                                    if ((txt_newpass.getText().toString()).equals(txt_pass.getText().toString()))
                                    {
                                        database.child("user")
                                                .child(model.getIduser()).setValue(new UserModel(model.getIduser(),
                                                model.getUsername(),txt_pass.getText().toString(),model.getStatus(),
                                                txt_fullname.getText().toString(),txt_email.getText().toString(),model.getAddress(),model.getImage(),
                                                txt_date.getText().toString(),Integer.parseInt(txt_phone.getText().toString())));
                                        Toast.makeText(personal_information.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(personal_information.this, "Mật Khẩu Không Trùng Nhau", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(personal_information.this, "Nhập Mật Khẩu Mới", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if ((txt_oldpass.getText().toString()).length()!=0)
                            {
                                Toast.makeText(personal_information.this, "Sai Mật Khẩu", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(personal_information.this, "Vui Lòng Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
                            }

                                }
                            });
                        }
                        else
                        {
                            linearLayout.setVisibility(View.GONE);
                            btn_reset.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database.child("user")
                                            .child(model.getIduser()).setValue(new UserModel(model.getIduser(),
                                            model.getUsername(),model.getPassword(),model.getStatus(),
                                            txt_fullname.getText().toString(),txt_email.getText().toString(),model.getAddress(),model.getImage(),
                                            txt_date.getText().toString(),Integer.parseInt(txt_phone.getText().toString())));
                                    Toast.makeText(personal_information.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                if (!ckb_pass.isChecked())
                {
                    btn_reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                    database.child("user")
                                            .child(model.getIduser()).setValue(new UserModel(model.getIduser(),
                                            model.getUsername(),model.getPassword(),model.getStatus(),
                                            txt_fullname.getText().toString(),txt_email.getText().toString(),model.getAddress(),model.getImage(),
                                            txt_date.getText().toString(),Integer.parseInt(txt_phone.getText().toString())));
                                            Toast.makeText(personal_information.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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