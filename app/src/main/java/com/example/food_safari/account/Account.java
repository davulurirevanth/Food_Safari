package com.example.food_safari.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_safari.HomeScreen;
import com.example.food_safari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Account extends AppCompatActivity {
    EditText nameEt;
    TextView pwdTV,saveChangebtn,close_settings_btn;
    EditText emailet;
    EditText phoneET;
    EditText addresset;
    Button Chgpasswordbtn;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //initializing the firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the reference to account layout
        nameEt = findViewById(R.id.accNameET);
        pwdTV = findViewById(R.id.accPasswordET);
        emailet = findViewById(R.id.accEmailET);
        phoneET = findViewById(R.id.accPhoneET);
        addresset = findViewById(R.id.accAddressTV);
        String user_id = firebaseAuth.getCurrentUser().getUid();
        close_settings_btn = findViewById(R.id.close_settings_btn);
        close_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(Account.this, HomeScreen.class));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                nameEt.setText(name);
                pwdTV.setText(password);
                emailet.setText(email);
                phoneET.setText(phonenumber);
                addresset.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        saveChangebtn = findViewById(R.id.saveChangeBTN);
        saveChangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("entered");
                nameEt = findViewById(R.id.accNameET);
                pwdTV = findViewById(R.id.accPasswordET);
                emailet = findViewById(R.id.accEmailET);
                phoneET = findViewById(R.id.accPhoneET);
                addresset = findViewById(R.id.accAddressTV);
                final String user_id = firebaseAuth.getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = nameEt.getText().toString();
//                        String password = pwdTV.getText().toString();
                        String email = emailet.getText().toString();
                        String phonenumber = phoneET.getText().toString();
                        String address = addresset.getText().toString();
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "/" + dataSnapshot.getKey() + "/" + user_id;
                        HashMap<String, Object> result = new HashMap<>();
                        databaseReference.child("address").setValue(address);
                        databaseReference.child("email").setValue(email);
                        databaseReference.child("name").setValue(name);
                        databaseReference.child("phonenumber").setValue(phonenumber);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("", "User email address updated.");
                                        }
                                    }
                                });
                        Toast.makeText(Account.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        Chgpasswordbtn = findViewById(R.id.ChangePwdBTN);
        Chgpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent b1 = new Intent(Account.this, ForgotPassword.class);
                startActivity(b1);
            }
        });


    }

}