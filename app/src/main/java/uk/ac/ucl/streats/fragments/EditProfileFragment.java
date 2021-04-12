package uk.ac.ucl.streats.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;

public class EditProfileFragment extends Fragment {

    private EditText editTextName, editTextUsername, editTextBio;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = getActivity().findViewById(R.id.edit_profile_name);
        editTextUsername = getActivity().findViewById(R.id.edit_profile_username);
        editTextBio = getActivity().findViewById(R.id.edit_profile_bio);

        Button editProfileSaveButton = getActivity().findViewById(R.id.edit_profile_save_button);
        editProfileSaveButton.setOnClickListener(v -> {

            String userID = fAuth.getUid();
            db.collection("profiles").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                String name = editTextName.getText().toString();
                String username = editTextUsername.getText().toString();
                String bio = editTextBio.getText().toString();

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> data = new HashMap<>();
                        if (!name.isEmpty()) {
                            data.put("name", name);
                            homeActivity.setName(name);
                        }
                        if (!username.isEmpty()) {
                            data.put("username", username);
                            homeActivity.setUsername(username);
                        }
                        if (!bio.isEmpty()) {
                            data.put("bio", bio);
                            homeActivity.setBio(bio);
                        }

                        db.collection("profiles").document(userID).set(data, SetOptions.merge());
                    }
                }
            });

            Navigation.findNavController(v).popBackStack();
        });
    }
}