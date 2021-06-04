package com.example.project1.kien.Login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.project1.BuildConfig;
import com.example.project1.FlashActivity;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    EditText etUser, etPass, etResetpass, etFullName, etEmail;
    Button btnSignUp,btnCancle;
    DatabaseReference reference   = FirebaseDatabase.getInstance().getReference("user");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ImageView img_signup;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-f57c4.appspot.com/");
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etUser = findViewById(R.id.txtUserSignup);
        etPass = findViewById(R.id.txtPassSignup);
        etResetpass =findViewById(R.id.txtResetPassSignup);
        etFullName = findViewById(R.id.txtFullNameSignup);
        etEmail = findViewById(R.id.txtEmailSignup);
        btnSignUp = findViewById(R.id.btnSignUp);
        img_signup = findViewById(R.id.img_signup);
        img_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = etUser.getText().toString();
                final String passWord = etPass.getText().toString();
                final String resetPass = etResetpass.getText().toString();
                final String fullName = etFullName.getText().toString();
                final String email = etEmail.getText().toString();

                reference.child("/user");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (userName.isEmpty()) {
                            etUser.setError("Please enter your name");
                            etUser.requestFocus();
                        } else if (passWord.isEmpty()) {
                            etPass.setError("Please enter your pass");
                            etPass.requestFocus();
                        } else if (fullName.isEmpty()) {
                            etFullName.setError("Please enter your fullname");
                            etFullName.requestFocus();
                        } else if (email.isEmpty()) {
                            etEmail.setError("Please enter your email");
                            etEmail.requestFocus();
                        } else if (!passWord.equals(resetPass)) {
                            Toast.makeText(SignUpActivity.this, "Fail!!", Toast.LENGTH_SHORT).show();
                        }else {
                            Calendar calendar = Calendar.getInstance();
                            final StorageReference mountainsRef = storageRef.child("images"+ calendar.getTimeInMillis()+".png");
                            img_signup.setDrawingCacheEnabled(true);
                            img_signup.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) img_signup.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] data = baos.toByteArray();

                            final UploadTask uploadTask = mountainsRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Log.i("Er","ABCd");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                    mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String id = reference.push().getKey();
                                            UserModel modelUser = new UserModel(id, userName, passWord,"khach hang",fullName,email,"",uri.toString(),"",0);
                                            reference.child(id).setValue(modelUser);
                                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            Toast.makeText(SignUpActivity.this, "Successfull!!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
    public void Pushimage() {



    }
    public void selectImage() {
        try {
            PackageManager pm = SignUpActivity.this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, SignUpActivity.this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Chụp Ảnh", "Thư Viện Ảnh", "Hủy"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#172737'>Tùy Chọn</font>"));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Chụp Ảnh")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                            //chỗ code này để lưu ảnh vào bộ nhớ
                            try {
                                uri = FileProvider.getUriForFile(SignUpActivity.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                            startActivityForResult(intent, PICK_IMAGE_CAMERA);

                        } else if (options[item].equals("Thư Viện Ảnh")) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Hủy")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(SignUpActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Poly Bookstore");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }
    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(SignUpActivity.this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = SignUpActivity.this.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(SignUpActivity.this.getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(SignUpActivity.this.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = MediaStore.Images.Media.getBitmap(SignUpActivity.this.getContentResolver(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                img_signup.setImageBitmap(bitmap);
                saveImageToExternalStorage(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(SignUpActivity.this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath);
                bitmap = decodeUri(selectedImage, 500);
                img_signup.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    }