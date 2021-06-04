package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-f57c4.appspot.com/");
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // Create a reference to "mountains.jpg"


    Uri ImageUri;
    ImageView view;
    Button save,logout;
    int REQUEST_CODE_IMAGE = 1;
    ListView listView;
    EditText txtname;
//    HinhAnhAdapter adapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =findViewById(R.id.lvview);
        view = findViewById(R.id.imgview);
        save = findViewById(R.id.btnsave);
        txtname = findViewById(R.id.txtten);
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
//
//        database.child("product").removeValue();
//        database.child("category").removeValue();
//        for (int i = 0; i < 6 ; i++) {
//            String idcategory = database.push().getKey();
//            for (int j = 0; j < 3; j++) {
//                String idproduct = database.push().getKey();
//
//                String idproductInfor = database.push().getKey();
//                database.child("product").child(idproduct).setValue(new
//                        ProductModel(idproduct,"image"+i,"titleproduct"+i,idcategory,idproductInfor,i,Math.pow(i,5)));
//                database.child("productinformation").child(idproductInfor).setValue(new
//                        ProductInformation(idproduct,idproductInfor,"titleproduct"+i));
//
//            }
//               database.child("category").child(idcategory).setValue(new Category(idcategory,"","image"+i));
//
//        }
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        Log.d("tag","Oncreate"+firebaseAuth.getCurrentUser().getEmail());
//        Log.d("tag","Oncreate"+firebaseAuth.getCurrentUser().getDisplayName());
//        Log.d("tag","Oncreate"+firebaseAuth.getCurrentUser().getUid());
//        Log.d("tag","Oncreate"+firebaseAuth.getCurrentUser().getPhoneNumber());
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(logout);
//            }
//        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,REQUEST_CODE_IMAGE);
//            }
//        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the data from an ImageView as bytes
//                // Create a reference to "mountains.jpg"
//
//
//                // Create a reference to 'images/mountains.jpg'
//                Calendar calendar = Calendar.getInstance();
//                final StorageReference mountainsRef = storageRef.child("images"+ calendar.getTimeInMillis()+".png");
//
//                view.setDrawingCacheEnabled(true);
//                view.buildDrawingCache();
//                Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] data = baos.toByteArray();
//
//                final UploadTask uploadTask = mountainsRef.putBytes(data);m
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        Log.i("Er","ABCd");
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                        // ...
//                        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Log.i("Download:",""+uri.toString());
//                                Image image = new Image(txtname.getText().toString(),uri.toString());
//                                database.child("Image").push().setValue(image);
//                            }
//                        });
//                    }
//                });
//
//
//            }
//
//        });
//        database.child("Image").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.exists())
//                {
//                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
//                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
//                    while (iterator.hasNext())
//                    {
//                        DataSnapshot next = iterator.next();
//                        Image model = next.getValue(Image.class);
//                        list.add(model);
//                        Log.i("data",""+list.size());
//                    }
//                    listView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                }
//                // lấy dự liễu lên firebase
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
//
//        adapter = new HinhAnhAdapter(MainActivity.this,list);
//    }
//    public void logout(final View view)
//    {
//        FirebaseAuth.getInstance().signOut();
//        GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
//                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                startActivity(new Intent(view.getContext(),Login.class));
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Sign Out Failed", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void  test()
    {



        // nhập dự liễu lên firebase

//        database.child("/adfsa/fea").set();
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("esfesfa","rfesafsa");
//        hashMap.put("es21fesfa","rfesafsa");
//        hashMap.put("esfe1sfa","rfesafsa");
//
//        database.child("SinhVien").setValue(new UserModel("fdfadssa","redsfaaw","eras"));
//        database.child("SinhVien/iduser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("data",""+snapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        DatabaseReference myRef = database.getReference("SinhVien");
//        myRef.setValue("Hello, World!");
//        UserModel model = new UserModel("a","b","c");
//        database.child("/SinhVien/iduser").removeValue();
//        database.child("/SinhVien").removeValue();

//        database.child("SinhVien").push().setValue(new UserModel("dsa","grea","ydfs"));
//        database.child("SinhVien").push().setValue(new UserModel("d21sa","gr2ea","yd1fs"));
//        database.child("SinhVien").push().setValue(new UserModel("21dsa","21grea","ydf21s"));
//        database.child("SinhVien").child("iduser").setValue("fdsafda");
//        database.child("/").removeValue();
//        final HashMap<String,Object> hocHashMap= new HashMap<>();
//            hocHashMap.put("maLop","431241fdsafes");
//              hocHashMap.put("tensv","");

//
//        for (int i = 0; i <3 ; i++) {
//            String push = database.child("LopHoc").push().getKey();
//
//            database.child("LopHoc/"+push).setValue(new LopHoc(push,"2432113"));
//            Log.i("push",""+push);
//        }


//        database.child("LopHoc/").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//               String alo = snapshot.getKey();
//               Log.i("daws",""+alo);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        database.child("LopHoc").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.exists())
//                {
//                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
//                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
//                    while (iterator.hasNext())
//                    {
//                        DataSnapshot next = iterator.next();
//                        LopHoc model = next.getValue(LopHoc.class);
//                        list.add(model);
//                        Log.i("data",""+list.size());
//
//                    }
//                    for (int i = 0;i<list.size();i++)
//                    {
//
//
//                            Log.i("data","fesacxzczv21321");
//                            database.child("/LopHoc/-MLqzMQCN3ld4HOHdH0W").setValue(hocHashMap);
//
//
////                        Log.i("aloo",""+database.child("LopHoc/").getKey());
//                        Log.i("iduser",""+list.get(i).getMaLop());
//                        Log.i("pass",""+list.get(i).getTensv());
//
//                    }
////                    Log.i("iduser",""+list.get(1).getIduser());
//////                    Log.i("iduser",""+list.get(1).getPassword());
//////                    Log.i("iduser",""+list.get(1).getUsername());
//                }
//                // lấy dự liễu lên firebase
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {


//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        view.setImageBitmap(bitmap);
        super.onActivityResult(requestCode, resultCode, data);

    }
}