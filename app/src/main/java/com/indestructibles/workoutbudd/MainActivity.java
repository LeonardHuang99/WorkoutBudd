package com.indestructibles.workoutbudd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        checkUserGender();



        //Ici c'est juste une liste des noms de cartes pour l'instant
        al = new ArrayList<>();

        //C'est là qu'on met les cartes
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //Ça va enlever la carte une fois swipé
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Ça va juste envoyer un message quand il est swipé à gauche
                Toast.makeText(MainActivity.this,"Left!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //Ça va juste envoyer un message quand il est swipé à droite
                Toast.makeText(MainActivity.this,"Right!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Ça va juste envoyer un message quand on clique sur les cartes
                Toast.makeText(MainActivity.this,"Clicked!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,HomePageActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private String userGender;
    private String oppositeUserGender;
    public void checkUserGender(){
        final FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb = FirebaseDatabase.getInstance("https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                if(snapshot.getKey().equals(user.getUid())){
                    userGender = "Male";
                    oppositeUserGender = "Female";
                    getOppositeGenderOfUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        DatabaseReference femaleDb = FirebaseDatabase.getInstance("https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                if(snapshot.getKey().equals(user.getUid())){
                    userGender = "Female";
                    oppositeUserGender = "Male";
                    getOppositeGenderOfUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    public void getOppositeGenderOfUsers(){
        DatabaseReference oppositeGenderDb = FirebaseDatabase.getInstance("https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(oppositeUserGender);
        oppositeGenderDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.exists()){
                    al.add(snapshot.child("Name").getValue().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
}