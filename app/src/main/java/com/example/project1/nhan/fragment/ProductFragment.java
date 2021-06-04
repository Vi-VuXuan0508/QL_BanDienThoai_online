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

import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.R;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.DAO.ProductDAO;
import com.example.project1.nhan.DAO.ProductInfoDAO;
import com.example.project1.nhan.adapter.ProductAdapter;
import com.example.project1.nhan.asynctask.ShowAsyncTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ProductFragment extends Fragment {
    String id,name;
    RecyclerView rcv;
    FirebaseAuth mAuth;
    Uri filePath;
    String link=null;
    private final int PICK_IMAGE_REQUEST = 22;
    FloatingActionButton btnThem;
    ImageView imageView;

    public ProductFragment(String id, String name) {
        this.id=id;
        this.name=name;
    }

    public ProductFragment() {
    }

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_fragment, container, false);
        rcv=view.findViewById(R.id.rcvProduct);
        btnThem=view.findViewById(R.id.btnThemProduct);
        mAuth = FirebaseAuth.getInstance();
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        new ShowAsyncTask(getContext(),rcv,id).execute(2);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThem();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void dialogThem()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
        View view= inflater.inflate(R.layout.activity_themqldt,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();
        final EditText etName=view.findViewById(R.id.txtNameProductThem);
        final EditText etPrice=view.findViewById(R.id.txtPriceProductThem);
        final EditText etCount=view.findViewById(R.id.txtCountProductThem);
        final EditText etGiamGia=view.findViewById(R.id.txtGiamGiaProductThem);
        imageView=view.findViewById(R.id.ivHinhProductThem);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (link!=null)
                {
                    ImageDAO.deleteImage(link);
                }
            }
        });
        Button btnThem=view.findViewById(R.id.btnThemProductDialog);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length()!=0&&etCount.getText().toString().length()!=0&&etGiamGia.getText().toString().length()!=0&&etPrice.getText().toString().length()!=0)
                {
                    if (link!=null)
                    {
                        String image= link;
                        float giamgia=Float.parseFloat(etGiamGia.getText().toString());
                        String nameProduct=etName.getText().toString();
                        float price=Float.parseFloat(etPrice.getText().toString());
                        int count=Integer.parseInt(etCount.getText().toString());
                        String id_category=id;
                        ProductModel product=new ProductModel(image,nameProduct,id_category,count,price,giamgia);
                        ProductDAO.createProduct(getContext(),product);
                        dialogThemInfo(product.getIdproduct());
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Chưa Chọn Hình",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Chưa Nhập Đầy Đủ Thông Tin",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    public void dialogThemInfo(final String idpro)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_product_info_them,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText info1=view.findViewById(R.id.txtInfo1);
        final EditText info2=view.findViewById(R.id.txtInfo2);
        final EditText info3=view.findViewById(R.id.txtInfo3);
        final EditText info4=view.findViewById(R.id.txtInfo4);
        final EditText info5=view.findViewById(R.id.txtInfo5);
        final EditText info6=view.findViewById(R.id.txtInfo6);
        final EditText info7=view.findViewById(R.id.txtInfo7);
        final EditText info8=view.findViewById(R.id.txtInfo8);
        final EditText info9=view.findViewById(R.id.txtInfo9);
        final EditText info10=view.findViewById(R.id.txtInfo10);
        final EditText info11=view.findViewById(R.id.txtInfo11);
        final EditText info12=view.findViewById(R.id.txtInfo12);

        Button btnThem=view.findViewById(R.id.btnThemInfo);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ProductDAO.deleteProduct(getContext(), idpro);
                if (link!=null)
                {
                    ImageDAO.deleteImage(link);
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screen=info1.getText().toString();
                String camera=info2.getText().toString();
                String cameraselfie=info3.getText().toString();
                String ram=info4.getText().toString();
                String rom=info5.getText().toString();
                String cpu=info6.getText().toString();
                String gpu=info7.getText().toString();
                String pin=info8.getText().toString();
                String sim=info9.getText().toString();
                String system=info10.getText().toString();
                String origin=info11.getText().toString();
                String yearofmanufacture=info12.getText().toString();
                ProductInformation info=new ProductInformation(idpro,screen,camera,cameraselfie,ram,rom,cpu,gpu,pin,sim,system,origin,yearofmanufacture);
                ProductInfoDAO.createInfo(getContext(),info);
                new ShowAsyncTask(getContext(),rcv,id).execute(2);

                dialog.dismiss();
            }
        });

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
                imageView.setImageBitmap(bitmap);

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
