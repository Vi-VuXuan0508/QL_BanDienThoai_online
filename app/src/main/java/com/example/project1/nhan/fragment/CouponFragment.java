package com.example.project1.nhan.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Coupon;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.example.project1.nhan.DAO.CouponDAO;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.adapter.CouponAdapter;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CouponFragment extends Fragment {
    RecyclerView rcv;
    FloatingActionButton btn;
    FirebaseAuth mAuth;
    Uri filePath;
    String link=null;
    ImageView imv;
    private final int PICK_IMAGE_REQUEST = 22;

    public static CouponFragment newInstance() {
        return new CouponFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coupon_fragment, container, false);

        rcv=view.findViewById(R.id.rcvCoupon);
        btn=view.findViewById(R.id.btnThemCoupon);
        mAuth = FirebaseAuth.getInstance();

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        new ShowAsyncTask(getContext(),rcv,null).execute(4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThem();
            }
        });

        return view;
    }

    public void dialogThem()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
        View view= inflater.inflate(R.layout.activity_them_v_c,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText etTitle=view.findViewById(R.id.txtTittleCouponThem);
        final EditText etPercent=view.findViewById(R.id.txtPercentCouponThem);
        final EditText etTime=view.findViewById(R.id.txtTimeCouponThem);
        imv=view.findViewById(R.id.ivHinhCouponThem);

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        Button btnThem=view.findViewById(R.id.btnCouponThem);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (link!=null)
                {
                    ImageDAO.deleteImage(link);
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getText().toString().length()!=0&&etPercent.getText().toString().length()!=0&&etTime.getText().toString().length()!=0)
                {
                    if (link!=null)
                    {
                        String image= link;
                        String title=etTitle.getText().toString();
                        int percent=Integer.parseInt(etPercent.getText().toString());
                        String time=etTime.getText().toString();

                        DatabaseReference dbreff;
                        dbreff= FirebaseDatabase.getInstance().getReference().child("user");
                        String seri=dbreff.push().getKey();
                        final Coupon coupon=new Coupon(null,null,image,null,title,time,percent,seri);
                        dbreff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                                {
                                    UserModel userModel=snapshot.getValue(UserModel.class);
                                    coupon.setIduser(userModel.getIduser());
                                    CouponDAO.createCoupon(getContext(),coupon);
                                }
                                new ShowAsyncTask(getContext(),rcv,null).execute(4);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Chưa chọn hình",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Chưa Nhập Đầy Đủ Thông Tin",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(getActivity(), new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    // Override onActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Get the Uri of data
            filePath = data.getData();
            try {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference  = storage.getReference();
                if (filePath != null) {
                    final ProgressDialog progressDialog
                            = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    // Defining the child of storageReference
                    final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    link =uri.toString();
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    // Error, Image not uploaded
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        // Progress Listener for loading
                                        // percentage on the dialog box
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            double progress
                                                    = (100.0
                                                    * taskSnapshot.getBytesTransferred()
                                                    / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage(
                                                    "Uploaded "
                                                            + (int)progress + "%");
                                        }
                                    });
                }

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imv.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}