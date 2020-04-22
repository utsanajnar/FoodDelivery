package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userSignin extends AppCompatActivity {

    EditText  emailID, password;
    Button btnUserSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signin);

        emailID = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnUserSignIn = findViewById(R.id.button5);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!= null){
                   // Toast.makeText(userSignin.this,"You are logged in " , Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(userSignin.this, homeUser.class);
                   // startActivity(intent);
                }
                else{
                    Toast.makeText(userSignin.this,"Please login " , Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnUserSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailID.setError("please enter a valid email ID");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("please enter a valid password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(userSignin.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(userSignin.this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!(task.isSuccessful())) {
                                Toast.makeText(userSignin.this, "Login Error , please login again", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(userSignin.this, homeUser.class);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Toast.makeText(userSignin.this, "some error occured , please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}
