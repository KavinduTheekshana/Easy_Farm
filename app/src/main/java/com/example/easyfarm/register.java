package com.example.easyfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyfarm.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;

    Button reg;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText1=(EditText)findViewById(R.id.Uname);
        editText2=(EditText)findViewById(R.id.Remail);
        editText3=(EditText)findViewById(R.id.password);
        editText4=(EditText)findViewById(R.id.conpass);

        reg=(Button)findViewById(R.id.register);
        textView = findViewById(R.id.login);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(register.this,login.class);
                startActivity(homeIntent);
                finish();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = editText1.getText().toString();
                String email = editText2.getText().toString();
                String password = editText3.getText().toString();
                String conpassword = editText4.getText().toString();

                if(username.isEmpty()){
                    editText1.setError("User name required!");
                }else if(email.isEmpty()){
                    editText2.setError("Email is required!");
                }else if (password.isEmpty()){
                    editText3.setError("Password is required!");
                }else if (conpassword.isEmpty()){
                    editText4.setError("Re enter password!");
                }else if (password == conpassword){
                    Toast.makeText(register.this, "Check password again", Toast.LENGTH_SHORT).show();
                }else{
                    reference = FirebaseDatabase.getInstance().getReference("User");

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userid = user.getUid();

                                        Users members = new Users(userid,username,email,password);
                                        reference.child(userid).setValue(members);

                                        System.out.println(members);
                                        Toast.makeText(register.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(register.this, Dashboard.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(register.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        });
    }
}