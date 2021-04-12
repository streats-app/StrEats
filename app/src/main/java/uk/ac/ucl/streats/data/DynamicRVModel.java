package uk.ac.ucl.streats.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DynamicRVModel {

    String id, name, details;
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
        });
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public Task<DocumentSnapshot> getInfoTask() {
        return infoTask;
    }
}
