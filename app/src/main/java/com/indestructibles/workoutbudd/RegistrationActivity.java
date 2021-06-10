package com.indestructibles.workoutbudd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText  mEmail, mPassword, mName;

    private RadioGroup mRadioGroup;

    private CheckBox mCheckBoxTCU;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @org.jetbrains.annotations.NotNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // On regarde ici si l'utilisateur est déjà connecté ou pas
                if(user!=null){
                    // s'il est déjà connecté, il peut aller sur la MainActivity
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mRegister = (Button) findViewById(R.id.register);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mCheckBoxTCU = (CheckBox) findViewById(R.id.checkBoxTCU);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectId);
                // On regarde ici si la personne a coché un bouton ou non
                if(radioButton.getText() ==  null){
                    //Si non, on arrête tous
                    return;
                }

                if (! mCheckBoxTCU.isChecked()){
                    return;
                }

                //On récupère l'email et le password pour créer un nouvel utilisateur
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        //Si pour une raison ou une autre, y'a un problème lors de l'inscription
                        if(!task.isSuccessful()){
                            //On affiche un message à l'utilisateur
                            Toast.makeText(RegistrationActivity.this, "Registration Error, Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // On va juste insérer quelques infos dans la database
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance("https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("Name", name);
                            userInfo.put("gender", radioButton.getText().toString());
                            userInfo.put("profileImageUrl", "default");

                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }

    //On start le Listener pour justement appeler le programme qui
    //permet de voir si l'utilisateur est co ou pas. Sinon, il s'active pas.
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireBaseAuthStateListener);
    }

    //On l'arrête ici quand le Listener pour voir s'il est co ou pas
    //est appelé 1 fois (Puisqu'on saura qu'il n'est pas co)
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireBaseAuthStateListener);
    }
}