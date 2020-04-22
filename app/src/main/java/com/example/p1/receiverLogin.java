package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class receiverLogin extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText name, phoneNumber, emailID, password;
    Button btnUserSignUp , btnAlreadySignIn,btnSignOut;
    FirebaseFirestore fStore;
    String riderID;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_login);
        btnSignOut= findViewById(R.id.button3);
        emailID = findViewById(R.id.editText3);
        password= findViewById(R.id.editText4);
        name=findViewById(R.id.editText6);
        phoneNumber = findViewById(R.id.editText5);
        fStore= FirebaseFirestore.getInstance();
        btnUserSignUp = findViewById(R.id.button4);
        btnAlreadySignIn = findViewById(R.id.button7);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(receiverLogin.this, MainActivity.class);
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
                    Toast.makeText(receiverLogin.this,"Fields are empty!" , Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty()&&pwd.isEmpty())) {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(receiverLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(receiverLogin.this,"Sign Up unsuccessful " , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                riderID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=fStore.collection("riders").document(riderID);
                                Map<String,Object> rider =new HashMap<>();
                                rider.put("name",nam);
                                rider.put("phone",phn);
                                rider.put("email",email);
                                documentReference.set(rider).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess:rider profile is created for rider ID "+riderID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+e.toString());
                                    }
                                });

                                Intent intent = new Intent(receiverLogin.this, homeReceiver.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(receiverLogin.this,"some error occured , please try again" , Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnAlreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(receiverLogin.this, receiverSignIn.class);
                startActivity(intent);
            }
        });



    }
}
