package com.example.silidcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileView extends AppCompatActivity {
    FirebaseUser user;
    String uname,email,acctype,contactnum;
    TextView _email,_uname,_acctype,_phone;
    FloatingActionButton btn_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("accounts");
        user = FirebaseAuth.getInstance().getCurrentUser();
        _email = findViewById(R.id.prof_email);
        _uname = findViewById(R.id.prof_username);
        _phone = findViewById(R.id.prof_phone);
        _acctype = findViewById(R.id.prof_acctype);
        btn_edit = findViewById(R.id.btn_edit_profile);
        btn_edit.setOnClickListener(v -> { Intent i = new Intent(getApplicationContext() , EditProfileq.class);startActivity(i); });

        Toast.makeText(ProfileView.this,"Loading User data. Please wait",Toast.LENGTH_SHORT).show();



        String uid = user.getUid();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uname = snapshot.child(uid).child("username").getValue(String.class);
                email = snapshot.child(uid).child("email").getValue(String.class);
                acctype = snapshot.child(uid).child("accountType").getValue(String.class);
                contactnum = snapshot.child(uid).child("contactNum").getValue(String.class);
                _email.setText(email);
                _uname.setText(uname);
                _phone.setText(acctype);
                _acctype.setText(contactnum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileView.this,"Error Loading Data! Please Check your Internet Connection.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}