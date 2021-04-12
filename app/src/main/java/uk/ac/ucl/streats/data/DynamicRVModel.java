package uk.ac.ucl.streats.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DynamicRVModel {

    String id, name, details, category;
    private FirebaseFirestore db;
    private Task<DocumentSnapshot> infoTask;

    public DynamicRVModel(String id) {
        this.id = id;
        name = "";
        details = "";

        db = FirebaseFirestore.getInstance();

        infoTask = db.collection("restaurants").document(String.valueOf(id)).get();

        infoTask.addOnSuccessListener(document -> {
            name = document.getString("name");
            details = document.getString("location");
            category = document.getString("cuisine");
        });
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Task<DocumentSnapshot> getInfoTask() {
        return infoTask;
    }
}
