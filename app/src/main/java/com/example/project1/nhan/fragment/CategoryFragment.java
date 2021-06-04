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

import com.example.project1.Model.Category;
import com.example.project1.R;
import com.example.project1.nhan.DAO.CategoryDAO;
import com.example.project1.nhan.DAO.ImageDAO;
import com.example.project1.nhan.adapter.CategoryAdapter;
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

public class CategoryFragment extends Fragment {
    RecyclerView rcv;
    FloatingActionButton btn;
    FirebaseAuth mAuth;
    Uri filePath;
    String link=null;
    private final int PICK_IMAGE_REQUEST = 22;
    ImageView imv;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        rcv=view.findViewById(R.id.rcvCategory);
        btn=view.findViewById(R.id.btnThemCategory);
        mAuth = FirebaseAuth.getInstance();

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        rcv.setLayoutManager(manager);
        new ShowAsyncTask(getContext(),rcv,null).execute(1);
        btn.setOnClickListener(new View.OnClickListener() {
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
    public void dialogThem()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_category_them,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText etName=view.findViewById(R.id.txtNameCategoryThem);
        imv=view.findViewById(R.id.ivHinhCategoryThem);

        imv.setOnClickListener(new View.OnClickListener() {
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

        Button btnThem=view.findViewById(R.id.btnCategoryThem);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().length()!=0)
                {
                    if (link!=null)
                    {
                        String image= link;
                        String name=etName.getText().toString();
                        Category category=new Category(name,image);
                        CategoryDAO.createCategory(getContext(),category);
                        new ShowAsyncTask(getContext(),rcv,null).execute(1);
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
