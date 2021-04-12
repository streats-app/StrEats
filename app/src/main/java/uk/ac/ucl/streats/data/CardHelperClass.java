package uk.ac.ucl.streats.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CardHelperClass {

    private String id, title, description;
    private FirebaseFirestore db;
    private Task<DocumentSnapshot> infoTask;

    public CardHelperClass(String id) {
        this.id = id;
        title = "";
        description = "";

        db = FirebaseFirestore.getInstance();

        infoTask = db.collection("restaurants").document(String.valueOf(id)).get();

        infoTask.addOnSuccessListener(document -> {
            title = document.getString("name");
            description = document.getString("description");
        });
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Task<DocumentSnapshot> getInfoTask() {
        return infoTask;
    }
}
