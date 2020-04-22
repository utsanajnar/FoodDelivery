package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.CharsetEncoder;
import java.util.Random;

import javax.annotation.Nullable;

public class ReceiverConfirmation extends AppCompatActivity {
        Button checkOrderStatus,confirmOrder;
        EditText phoneNo;
        TextView indication;
        FirebaseFirestore db;
        String phnUser,phnReceiver,boolIndication;
       // FirebaseAuth fAuth;
       // FirebaseFirestore fStore;
       // String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_confirmation);

        phoneNo= findViewById(R.id.editText11);
        checkOrderStatus = findViewById(R.id.button9);
        confirmOrder= findViewById(R.id.button13);
        indication = findViewById(R.id.textView10);
        phoneNo.setText(GlobalVariable.phoneUser);
        db=FirebaseFirestore.getInstance();
/*
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phoneNo.setText(documentSnapshot.getString("phone"));
            }
        });

*/
        checkOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("bool")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //this code will execute when data is completely extracted.
                                if(task.isSuccessful()){
                                    //Let's make our database final:
                                    for(QueryDocumentSnapshot document:task.getResult()){

                                        GlobalVariable.indication=document.getString("bool");
                                    }
                                }
                            }
                        });

                if(GlobalVariable.indication.equals("true")){
                    String ya= "Customer here!!!";
                    indication.setText(ya);
                }
                else{
                    String ya= "No orders!";
                    indication.setText(ya);
                }



            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting intent and PendingIntent instance

                db.collection("usersPhone")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //this code will execute when data is completely extracted.
                                if(task.isSuccessful()){
                                    //Let's make our database final:
                                    QuerySnapshot document = task.getResult();
                                    if(!document.isEmpty()){
                                        GlobalVariable.phoneUser=document.getDocuments().toString();
                                    }



                                }
                            }
                        });
                db.collection("ridersPhone")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //this code will execute when data is completely extracted.
                                if(task.isSuccessful()){
                                    //Let's make our database final:
                                    QuerySnapshot document = task.getResult();
                                    if(!document.isEmpty()){
                                        GlobalVariable.phoneRider=document.getDocuments().toString();
                                    }


                                }
                            }
                        });


                String orderID=getAlphaNumericString(10);
                String msg = "Order confirm with OrderID: "+orderID+" Rider's phone number: "+GlobalVariable.phoneRider ;
                String msgReceiver = "Order confirm with OrderID: "+orderID+" Customer's phone number: "+ GlobalVariable.phoneUser;
                if(ActivityCompat.checkSelfPermission(ReceiverConfirmation.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ReceiverConfirmation.this,new String[]
                            {Manifest.permission.SEND_SMS},1);

                }
                else {
                    // Intent intent = new Intent(getApplicationContext(), ReceiverConfirmation.class);
                    //  PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    //Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(GlobalVariable.phoneUser, null, msg, null, null);
                   sms.sendTextMessage(GlobalVariable.phoneRider, null, msgReceiver, null, null);

                    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ReceiverConfirmation.this, Main2ActivityReceiver.class);
                    startActivity(i);

                }

            }
        });



    }
/*
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }*/

    public static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
