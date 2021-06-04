package com.example.project1.kien.Login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.example.project1.kien.Adapter.UserAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListUser_KH_Activity extends AppCompatActivity {
    RecyclerView list;
    ArrayList<UserModel> arrayList;
    UserAdapter adapter;
    FloatingActionButton fltAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_rcv_user_kh);

        list = findViewById(R.id.lv_User);
        fltAdd = findViewById(R.id.flt_User);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        fltAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                dialogAddUser();
            }
        });

        arrayList = new ArrayList<>();
        GetData();

    }

    private void GetData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    UserModel user = data.getValue(UserModel.class);

                        arrayList.add(user);
                        Log.i("abs",""+arrayList.size());
                }
                adapter = new UserAdapter(ListUser_KH_Activity.this, arrayList);
                list.setAdapter(adapter);
                Toast.makeText(ListUser_KH_Activity.this, "Load Data Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListUser_KH_Activity.this, "Load Data Failed" + error.toString(), Toast.LENGTH_LONG).show();
                Log.d("MYTAG", "onCancelled: " + error.toString());
            }
        });
    }

    private void dialogAddUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListUser_KH_Activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_user, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        dialog.setTitle("Add User");
        final EditText etUser = view.findViewById(R.id.txtAddUser);
        final EditText etPass = view.findViewById(R.id.txtAddPass);
        final EditText etResetPass = view.findViewById(R.id.txtResetAddPass);
        final EditText etFullName = view.findViewById(R.id.txtAddFullName);
        Spinner spinner = (Spinner) view.findViewById(R.id.spnStatus);
        final EditText etEmail =view.findViewById(R.id.txtAddEmail);

        List<String> ctg = new ArrayList<String>();
        ctg.add("Khach Hang");
        ctg.add("Admin");

        ArrayAdapter<String> data = new ArrayAdapter<String>(ListUser_KH_Activity.this, android.R.layout.simple_spinner_item,ctg);

        data.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spinner.setAdapter(data);


        final Button btnAdd = view.findViewById(R.id.btnAddUser);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String status = adapterView.getSelectedItem().toString();
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userName = etUser.getText().toString();
                        String passWord = etPass.getText().toString();
                        String resetPass = etResetPass.getText().toString();
                        String fullName = etFullName.getText().toString();
                        String email = etEmail.getText().toString();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("user");

                        if (!passWord.equals(resetPass)) {
                            Toast.makeText(ListUser_KH_Activity.this, "Fail!!", Toast.LENGTH_SHORT).show();
                        } else {
                            String id = myRef.push().getKey();
                            UserModel modelUser = new UserModel(id, userName, passWord,status,fullName,email,"","","",0);
                            myRef.child(id).setValue(modelUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ListUser_KH_Activity.this, "Add Success", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

}