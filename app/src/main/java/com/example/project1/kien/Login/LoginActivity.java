package com.example.project1.kien.Login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Login.ForgetActivity;
import com.example.project1.Login.loginfb;
import com.example.project1.Main;
import com.example.project1.MainActivity;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.example.project1.nhan.AdminBeginActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignup, tvChange;
    EditText etUser, etPass;
    Button btnLogin;
    LoginButton btn_loginfb;
    SignInButton btn_logingg;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    TextView txtForget;
    ArrayList<UserModel> list;
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = findViewById(R.id.txtUser);
        etPass = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.txtSignUp);
        btn_loginfb= findViewById(R.id.btnloginfb);
        btn_logingg = findViewById(R.id.btnlogingoogle);
        txtForget = findViewById(R.id.txtForget);

        list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();


        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
        btn_loginfb.setReadPermissions("email", "public_profile");
        btn_loginfb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("36563115356-v061l29qdrmr6bvhqomdevvsitke8ud1.apps.googleusercontent.com")
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this,gso);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        btn_logingg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signingg();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = etUser.getText().toString();
                final String pass = etPass.getText().toString();

                reference.child("/user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            Iterable<DataSnapshot> iterable = snapshot.getChildren();
                            Iterator<DataSnapshot> iterator = iterable.iterator();
                            while (iterator.hasNext()) {
                                DataSnapshot next = iterator.next();
                                UserModel user1 = next.getValue(UserModel.class);
                                list.add(user1);
                            }

                            for (UserModel user2 : list) {
                                if (user.equals(user2.getUsername()) && pass.equals(user2.getPassword())) {

                                    if (user2.getStatus().equalsIgnoreCase("Admin")){
                                        Intent i = new Intent(LoginActivity.this, AdminBeginActivity.class);
                                        i.putExtra("iduser",user2.getIduser());
                                        startActivity(i);
                                    }else if(user2.getStatus().equalsIgnoreCase("Khach Hang")){
                                        Intent i =new Intent(LoginActivity.this,Main.class);
                                        i.putExtra("iduser",user2.getIduser());
                                        startActivity(i);
                                    }

                                    Toast.makeText(LoginActivity.this, "Thành Công!!", Toast.LENGTH_LONG).show();
                                    break;
                                } else
                                    {
                                    Toast.makeText(LoginActivity.this, "Thất Bại!!", Toast.LENGTH_LONG).show();

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

    private void signingg() {
        Intent SignInintent=   signInClient.getSignInIntent();
        startActivityForResult(SignInintent,GOOGLE_SIGN_IN_CODE);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(final FirebaseUser user) {
        if (firebaseAuth.getCurrentUser()!=null)
        {
            reference.child("/user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Iterable<DataSnapshot> iterable = snapshot.getChildren();
                        Iterator<DataSnapshot> iterator = iterable.iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot next = iterator.next();
                            UserModel user1 = next.getValue(UserModel.class);
                            if (user1.getEmail().equalsIgnoreCase(user.getEmail()))
                            {
                                Intent i =new Intent(LoginActivity.this, Main.class);
                                i.putExtra("iduser",user1.getIduser());
                                startActivity(i);
                                break;
                            }
                            else
                            {
                                String iduser = reference.push().getKey();
                                    reference.child("/user").child(iduser).
                                            setValue(new UserModel(iduser,user.getUid(),"","Khach Hang",user.getDisplayName(),user.getEmail(),"",String.valueOf(user.getPhotoUrl()),"",0));
                                Intent i =new Intent(LoginActivity.this, Main.class);
                                i.putExtra("iduser",iduser);
                                startActivity(i);
                                break;
                            }
                        }

                    }
                    else
                    {
                        String iduser = reference.push().getKey();
                        reference.child("/user").child(iduser).
                                setValue(new UserModel(iduser,user.getUid(),"","Khach Hang",user.getDisplayName(),user.getEmail(),"",String.valueOf(user.getPhotoUrl()),"",0));
                        Intent i =new Intent(LoginActivity.this, Main.class);
                        i.putExtra("iduser",iduser);
                        startActivity(i);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_CODE)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                final GoogleSignInAccount signInAcc =task.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);
                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("auth",""+firebaseAuth.getCurrentUser());
                        Toast.makeText(getApplicationContext(), "Your Google Account is Connected to Our Application ", Toast.LENGTH_SHORT).show();
                        firebaseAuthWithGoogle(signInAcc.getIdToken());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("efsa",""+task.isSuccessful());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                              updateUI(null);
                        }

                        // ...
                    }
                });
    }
}