package com.indestructibles.workoutbudd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String WORKOUTBUDD_FIREBASEDATABASE = "https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/";
    private Cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;

    private String currentUId;

    private DatabaseReference usersDb;

    private FirebaseAuth mAuth;

    ListView listView;
    List<Cards> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance(WORKOUTBUDD_FIREBASEDATABASE).getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        checkUserGender();



        //Ici c'est juste une liste des noms de cartes pour l'instant
        rowItems = new ArrayList<Cards>();
        //C'est là qu'on met les cartes

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //Ça va enlever la carte une fois swipé
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                Cards object = (Cards) dataObject;
                String userId =  object.getUserId();
                usersDb.child(oppositeUserGender).child(userId).child("connections").child("no").child(currentUId).setValue(true);
                //Ça va juste envoyer un message quand il est swipé à gauche
                Toast.makeText(MainActivity.this,"Left!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards object = (Cards) dataObject;
                String userId =  object.getUserId();
                usersDb.child(oppositeUserGender).child(userId).child("connections").child("yes").child(currentUId).setValue(true);
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

        DatabaseReference maleDb = FirebaseDatabase.getInstance(WORKOUTBUDD_FIREBASEDATABASE).getReference().child("Users").child("Male");
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

        DatabaseReference femaleDb = FirebaseDatabase.getInstance(WORKOUTBUDD_FIREBASEDATABASE).getReference().child("Users").child("Female");
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
        DatabaseReference oppositeGenderDb = FirebaseDatabase.getInstance(WORKOUTBUDD_FIREBASEDATABASE).getReference().child("Users").child(oppositeUserGender);
        oppositeGenderDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                //On regarde si la personne n'a pas encore été swiper à droite ou à gauche par lu currentUserId
                if(snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUId) && !snapshot.child("connections").child("yes").hasChild(currentUId)){

                    Cards item = new Cards (snapshot.getKey(),snapshot.child("Name").getValue().toString());
                    rowItems.add(item);
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