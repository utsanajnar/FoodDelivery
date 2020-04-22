package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userLogin extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText name, phoneNumber, emailID, password;
    Button btnUserSignUp , btnAlreadySignIn;
    Button btnSignOut;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);



        btnSignOut = findViewById(R.id.button8);

        emailID = findViewById(R.id.editText);
        password= findViewById(R.id.editText2);
        name=findViewById(R.id.editText7);
        phoneNumber = findViewById(R.id.editText8);
        fStore= FirebaseFirestore.getInstance();
        btnUserSignUp = findViewById(R.id.button5);
        btnAlreadySignIn = findViewById(R.id.button6);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(userLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                final String nam = name.getText().toString();
                final String phn = phoneNumber.getText().toString();
                if(email.isEmpty()){
                    emailID.setError("please enter a valid email ID");
                    emailID.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("please enter a valid password");
                    password.requestFocus();
                }
                else if((email.isEmpty()&&pwd.isEmpty())||phn.isEmpty()||nam.isEmpty()){
                    Toast.makeText(userLogin.this,"Fields are empty!" , Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty()&&pwd.isEmpty())) {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(userLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(userLogin.this,"Sign Up unsuccessful " , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=fStore.collection("users").document(userID);
                                Map<String,Object> user =new HashMap<>();
                                user.put("name",nam);
                                user.put("phone",phn);
                                user.put("email",email);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess:user profile is created  for"+userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+e.toString());
                                    }
                                });


                                Intent intent = new Intent(userLogin.this, homeUser.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(userLogin.this,"some error occured , please try again" , Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnAlreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLogin.this, userSignin.class);
                startActivity(intent);
            }
        });

    }


}
