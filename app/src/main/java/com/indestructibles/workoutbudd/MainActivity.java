package com.indestructibles.workoutbudd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indestructibles.workoutbudd.Cards.Cards;
import com.indestructibles.workoutbudd.Cards.arrayAdapter;
import com.indestructibles.workoutbudd.Matches.MatchesActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String WORKOUTBUDD_FIREBASEDATABASE = "https://workoutbudd-default-rtdb.europe-west1.firebasedatabase.app/";
    private Cards cards_data[];
    private com.indestructibles.workoutbudd.Cards.arrayAdapter arrayAdapter;
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
                //On créer ici une connexion entre le CurrentUser et la personne qui a été swipé
                Cards object = (Cards) dataObject;
                String userId =  object.getUserId();
                //Ici, la personne à été swipé à gauche et donc il y a une connexion "no" qui est crée
                usersDb.child(userId).child("connections").child("no").child(currentUId).setValue(true);
                //Ça va juste envoyer un message quand il est swipé à gauche
                Toast.makeText(MainActivity.this,"Left!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //On créer ici une connexion entre le CurrentUser et la personne qui a été swipé
                Cards object = (Cards) dataObject;
                String userId =  object.getUserId();
                //Ici, la personne à été swipé à droite et donc il y a une connexion "yes" qui est crée
                usersDb.child(userId).child("connections").child("yes").child(currentUId).setValue(true);
                //Ça va juste envoyer un message quand il est swipé à droite
                isConnectionMatch(userId);
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

    private void isConnectionMatch(String userId) {
        //On va ici juste aller dans la database et regardé s'il y a déjà eu un yes d'un côté ou pas
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yes").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //On regarde si le "yes" exist
                if (snapshot.exists()){
                    //Si oui, on va créer un nouveau child dans la database pour ajouter l'info du match chez les 2 users
                    Toast.makeText(MainActivity.this,"New Match !",Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance(WORKOUTBUDD_FIREBASEDATABASE).getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("chatId").setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
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

        DatabaseReference userDb = usersDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if (snapshot.child("gender") != null){
                        userGender = snapshot.child("gender").getValue().toString();
                        switch (userGender){
                            case "Male":
                                oppositeUserGender = "Female";
                                break;
                            case "Female":
                                oppositeUserGender = "Male";
                                break;
                        }
                        getOppositeGenderOfUsers();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    public void getOppositeGenderOfUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                //On regarde si la personne n'a pas encore été swiper à droite ou à gauche par lu currentUserId
                if (snapshot.child("gender").getValue() != null){
                    if(snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUId) && !snapshot.child("connections").child("yes").hasChild(currentUId) && snapshot.child("gender").getValue().toString().equals(oppositeUserGender)){

                        String profileImageUrl = "default";
                        if(snapshot.child("profileImageUrl").getValue() != null) {

                            if (!snapshot.child("profileImageUrl").getValue().equals("default")) {

                                profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                            }
                        }
                        Cards item = new Cards (snapshot.getKey(), Objects.requireNonNull(snapshot.child("Name").getValue()).toString(), profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
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

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
}