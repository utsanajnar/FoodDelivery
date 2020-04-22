package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class orderFoodU extends AppCompatActivity {
    TextView textView , cost;
    Button btnClick;
    CheckBox  cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10;
    RadioButton rb1,rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_u);
        textView = findViewById(R.id.textView);

        btnClick = findViewById(R.id.button12);

        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);
        cb7 = findViewById(R.id.checkBox7);
        cb8 = findViewById(R.id.checkBox8);
        cb9 = findViewById(R.id.checkBox9);
        cb10 = findViewById(R.id.checkBox10);
        rb1=findViewById(R.id.radioButton);
        rb2=findViewById(R.id.radioButton2);

        final Intent intent = getIntent();
        final String value = intent.getStringExtra("key");
        Double d = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("#.##");
        final String formatted = df.format(d);
        String value1 = "Delivery Charges = "+formatted;
        textView.setText(value1);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(orderFoodU.this, phoneAuthUser.class);
                int s1=0,s2=0,s = 0 ,s4=0,s5=0,s6=0,s7=0,s8=0,s9=0,s10=0;
                if(cb1.isChecked()){
                    s1 = s1+ Integer.parseInt(cb1.getText().toString());
                }
                if(cb2.isChecked()){
                    s2 = s2+ Integer.parseInt(cb2.getText().toString());
                }
                if(cb3.isChecked()){
                    s = s + Integer.parseInt(cb3.getText().toString());
                }
                if(cb4.isChecked()){
                    s4 = s4+ Integer.parseInt(cb4.getText().toString());
                }  if(cb5.isChecked()){
                    s5 = s5+ Integer.parseInt(cb5.getText().toString());
                }  if(cb6.isChecked()){
                    s6 = s6+ Integer.parseInt(cb6.getText().toString());
                }  if(cb7.isChecked()){
                    s7 = s7+ Integer.parseInt(cb7.getText().toString());
                }  if(cb3.isChecked()){
                    s8 = s8+ Integer.parseInt(cb8.getText().toString());
                }  if(cb9.isChecked()){
                    s9 = s9+ Integer.parseInt(cb9.getText().toString());
                }  if(cb10.isChecked()){
                    s10 = s10+ Integer.parseInt(cb10.getText().toString());
                }

                double sum=s1+s2+ s +s4+s5+s6+s7+s8+s9+s10;
                if(rb1.isChecked())
                    sum=sum*0.8;


                String foodCost = Double.toString(sum);

                Double val= Double.parseDouble(value);
                 double total = sum+val;
                DecimalFormat df = new DecimalFormat("#.##");
                String totalCost = df.format(total);


                final  String disp = "Total cost = "+formatted+" + "+foodCost+" = "+totalCost;
                i.putExtra("key2",disp);
                i.putExtra("key1",totalCost);
                startActivity(i);
            }
        });
    }
}
