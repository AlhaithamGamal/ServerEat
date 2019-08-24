package com.example.servereat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import com.example.servereat.common.Common;
import com.example.servereat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
EditText edtPhone,edtPassword;
Button btnSignIn;
FirebaseDatabase db;
DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = findViewById(R.id.edtpassword);
        edtPhone = findViewById(R.id.edtphone);
        btnSignIn = findViewById(R.id.btnSignin);
        db = FirebaseDatabase.getInstance();
        users = db.getReference();
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(Common.isConnectToInternet(getBaseContext())) {
                    signIn(edtPhone.getText().toString(), edtPassword.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signIn(String phone, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();
        final String localPhone = phone;
        final String localPassword = password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child("currentUserId").child(localPhone).exists()){

                    progressDialog.dismiss();
                    User user = dataSnapshot.child("User").child("currentUserId").child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsStaff())){
                            if(user.getPassword().equals(localPassword)){
                                Intent login = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(login);
                                finish();


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"wrong password",Toast.LENGTH_LONG).show();
                            }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"please login with staff account ",Toast.LENGTH_LONG).show();



                    }


                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"user not exists in our database",Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
