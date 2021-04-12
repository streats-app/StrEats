package uk.ac.ucl.streats.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;

public class HomeActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private String userID, name, username, bio;
    private ArrayList<String> favourites, bookmarks;
    private ArrayList<String> featuredIds, mostViewedIds;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private Task<DocumentSnapshot> profileTask, featuredTask, mostViewedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            // do nothing
        });


        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = "";
        username = "";
        bio = "";
        favourites = new ArrayList<>();
        bookmarks = new ArrayList<>();

        userID = fAuth.getUid();
        profileTask = db.collection("profiles").document(userID).get();
        profileTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                name = document.getString("name");
                username = "@".concat(document.getString("username"));
                bio = document.getString("bio");
                favourites = (ArrayList<String>) document.get("favourites");
                bookmarks = (ArrayList<String>) document.get("bookmarks");
            }
        });

        featuredTask = db.collection("restaurantCards").document("featured").get();
        featuredTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                featuredIds = (ArrayList<String>) document.get("ids");
            }
        });

        mostViewedTask = db.collection("restaurantCards").document("mostViewed").get();
        mostViewedTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                mostViewedIds = (ArrayList<String>) document.get("ids");
            }
        });

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //call super
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Task<DocumentSnapshot> getProfileTask() {
        return profileTask;
    }

    public Task<DocumentSnapshot> getFeaturedTask() {
        return featuredTask;
    }

    public Task<DocumentSnapshot> getMostViewedTask() {
        return mostViewedTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<String> getFeaturedIds() {
        return featuredIds;
    }

    public ArrayList<String> getMostViewedIds() {
        return mostViewedIds;
    }

    public ArrayList<String> getFavourites() {
        return favourites;
    }

    public void removeFromFavourites(String restaurantID) {
        favourites.remove(restaurantID);

        db.collection("profiles").document(userID).get().addOnCompleteListener(task -> db.collection("profiles").document(userID).update("favourites", FieldValue.arrayRemove(restaurantID)));
    }

    public void addToFavourites(String restaurantID) {
        favourites.add(restaurantID);

        db.collection("profiles").document(userID).get().addOnCompleteListener(task -> db.collection("profiles").document(userID).update("favourites", FieldValue.arrayUnion(restaurantID)));
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void removeFromBookmarks(String restaurantID) {
        bookmarks.remove(restaurantID);

        db.collection("profiles").document(userID).get().addOnCompleteListener(task -> db.collection("profiles").document(userID).update("bookmarks", FieldValue.arrayRemove(restaurantID)));
    }

    public void addToBookmarks(String restaurantID) {
        bookmarks.add(restaurantID);

        db.collection("profiles").document(userID).get().addOnCompleteListener(task -> db.collection("profiles").document(userID).update("bookmarks", FieldValue.arrayUnion(restaurantID)));
    }
}