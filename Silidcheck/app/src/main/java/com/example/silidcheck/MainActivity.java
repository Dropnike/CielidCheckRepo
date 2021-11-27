package com.example.silidcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button gotosignup;
    Button signin;
    EditText email,psw;
    String emailpattern = "[a-zA-Z0-9._-]+@tup+\\.+edu+\\.+ph+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotosignup = findViewById(R.id.button);
        email = findViewById(R.id.login_email);
        psw = findViewById(R.id.login_password);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        signin = findViewById(R.id.button3);
        gotosignup.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Register.class)));
        signin.setOnClickListener(view -> loginfunc());
    }

    private void loginfunc() {
        String smail = email.getText().toString();
        String password = psw.getText().toString();
        if (!smail.matches(emailpattern)){
            email.setError("Enter Valid email.");
        }else if (password.isEmpty()||password.length()<6){
            psw.setError("Password should be longer than 6 character.");
        }else{
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Logging in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(smail,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Log in Successful!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext() , Home_activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else  {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}