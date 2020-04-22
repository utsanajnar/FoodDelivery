package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class phoneAuthUser extends AppCompatActivity {
    TextView totalling;
    EditText phoneNo,otp;
    Button btnPhone,btnOtp;
    FirebaseAuth mAuth;
    String codeSent;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_user);
        totalling =findViewById(R.id.textView9);
        phoneNo = findViewById(R.id.editText9);
        otp = findViewById(R.id.editText10);
        btnPhone =findViewById(R.id.button10);
        btnOtp = findViewById(R.id.button11);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String value = intent.getStringExtra("key2");
        totalling.setText(value);
        // lets initialise firebase firestore.
        db=FirebaseFirestore.getInstance();
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
                GlobalVariable.phoneUser=phoneNo.getText().toString();
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  verifySignInCode();

            }
        });
    }

    public void verifySignInCode(){
        String code = otp.getText().toString();
        if(code.isEmpty()){
            otp.setError("please enter the phone number");
            otp.requestFocus();
            return;
        }
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // create new object to store data to firestore.
                            Map<String,Object> user=new HashMap<>();
                            user.put("phoneUSR",phoneNo.getText().toString());

                            //add document with a generated ID:
                            db.collection("usersPhone")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                        //here we will put the code which will be executed when data is stored
                                            Toast.makeText(phoneAuthUser.this, "Order submitted ,please  wait for confirmation message ", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(phoneAuthUser.this, "Error Failed!!! ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //// this data base for indication!!!!
                            Map<String,Object> bool = new HashMap<>();
                            bool.put("bool","true");
                            db.collection("bool")
                                    .add(bool)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //here we will put the code which will be executed when data is stored
                                           // Toast.makeText(phoneAuthUser.this, "Order submitted ,please  wait for confirmation message ", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(phoneAuthUser.this, "Error Failed!!! ", Toast.LENGTH_SHORT).show();
                                }
                            });

                            ///////////// here you can open a new activity ........

                           // GlobalVariable.phoneUser=phoneNo.getText().toString();
                            Intent i = new Intent(phoneAuthUser.this,Main2Activity.class);
                            startActivity(i);


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(phoneAuthUser.this, "invalid otp ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void sendVerificationCode(){

        String phone = phoneNo.getText().toString();
        if(phone.isEmpty()){
            phoneNo.setError("please enter the phone number");
            phoneNo.requestFocus();
            return;
        }

        if(phone.length()<13){                                        ////////////// as we need to enter the phone number along with country code so for india +91 ....
            phoneNo.setError("please enter valid phone number");
            phoneNo.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;            ///////////////////// the code sent to the phone number from app via sms.....
        }
    };
}
