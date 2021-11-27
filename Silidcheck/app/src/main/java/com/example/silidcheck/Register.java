package com.example.silidcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    RadioGroup acctype;
    RadioButton radioButton,temprad,temprad2;
    TextView gotosignin;
    EditText email,uname,psw,cpsw,phone;
    Button btnreg;
    String emailpattern = "[a-zA-Z0-9._-]+@tup+\\.+edu+\\.+ph+";
    String unamepattern = "[a-zA-Z0-9]+";
    String phonepattern = "09+[0-9]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gotosignin = findViewById(R.id.textView);
        email = findViewById(R.id.input_email);
        uname = findViewById(R.id.input_username);
        psw = findViewById(R.id.input_pass);
        phone = findViewById(R.id.input_phone);
        cpsw = findViewById(R.id.input_confirmpass);
        btnreg = findViewById(R.id.btnreg);
        acctype = findViewById(R.id.accountType);
        temprad = findViewById(R.id.rad_student);
        temprad2 = findViewById(R.id.rad_staff);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        gotosignin.setOnClickListener(v -> { Intent i = new Intent(getApplicationContext() , MainActivity.class);startActivity(i); });

        btnreg.setOnClickListener(v -> PerformAuth()); }

    private void PerformAuth() {
        String semail = email.getText().toString();
        String username = uname.getText().toString();
        String pass = psw.getText().toString();
        String conpass = cpsw.getText().toString();
        String phonenum = phone.getText().toString();
        int radioid = acctype.getCheckedRadioButtonId();
        if (!semail.matches(emailpattern)){
            email.setError("Input your TUP email.");
            email.requestFocus();
        }else if (!username.matches(unamepattern)){
            uname.setError("No Special Characters allowed.");
            uname.requestFocus();
        }else if (pass.isEmpty()||pass.length()<6){
            psw.setError("Password should be longer than 6 character.");
            psw.requestFocus();
        }else if (!pass.equals(conpass)){
            cpsw.setError("Password not matched.");
            cpsw.requestFocus();
        }else if (!temprad.isChecked() && !temprad2.isChecked()){
            temprad.setError("Please Select Account Type");
            temprad.requestFocus();
        }else if (phonenum.isEmpty()||phonenum.length()>11||phonenum.length()<11||!phonenum.matches(phonepattern)){
            phone.setError("Enter Contact Number");
            phone.requestFocus();
        }else{
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Signing up");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(semail,pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                  progressDialog.dismiss();

                    radioButton = findViewById(radioid);

                    UserAccounts user = new UserAccounts(username,semail,pass,radioButton.getText().toString(),phonenum,"off");
                    database.getReference("accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"Account Uploaded!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(Register.this,"Registration Successful!",Toast.LENGTH_SHORT).show();
                    Intent changeintent = new Intent(getApplicationContext() , MainActivity.class);
                    changeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(changeintent);
                }else  {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this,""+task.getException(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void checked(View v){

    }

}