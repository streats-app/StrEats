package uk.ac.ucl.streats.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;
import uk.ac.ucl.streats.data.GlideApp;

public class ProfilePageFragment extends Fragment {

    private TextView name, username, bio;
    private ImageView profilePicture, profileHeader;
    private Uri selectedImageUri;
    private FirebaseAuth fAuth;
    private FirebaseStorage firebaseStorage;
    private final int PROFILE = 0;
    private final int HEADER = 1;

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profilepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        name = getActivity().findViewById(R.id.user_profile_name);
        username = getActivity().findViewById(R.id.user_profile_username);
        bio = getActivity().findViewById(R.id.user_profile_bio);
        profilePicture = getActivity().findViewById(R.id.profile_picture);
        profileHeader = getActivity().findViewById(R.id.profile_header);


        setUpEditProfileButton();
        setUpFavouritesButton();
        setUpBookmarksButton();
        setUpChangeAvatarButton();
        setUpChangeHeaderButton();

        HomeActivity homeActivity = (HomeActivity) getActivity();
        updateUserProfilePageInfo(homeActivity.getName(), homeActivity.getUsername(), homeActivity.getBio());
        updateProfilePicture();
        updateProfileHeader();
    }

    private void updateProfilePicture() {
        StorageReference storageRef = firebaseStorage.getReference().child("images/users/" + fAuth.getUid() + "/profile");
        storageRef.getMetadata().addOnSuccessListener(storageMetadata -> GlideApp.with(getActivity())
                .load(storageRef)
                .signature(new MediaStoreSignature(storageMetadata.getContentType(),
                        storageMetadata.getCreationTimeMillis(),
                        0))
                .into(profilePicture));
    }

    private void updateProfileHeader() {
        StorageReference storageRef = firebaseStorage.getReference().child("images/users/" + fAuth.getUid() + "/header");
        storageRef.getMetadata().addOnSuccessListener(storageMetadata -> GlideApp.with(getActivity())
                .load(storageRef)
                .signature(new MediaStoreSignature(storageMetadata.getContentType(),
                        storageMetadata.getCreationTimeMillis(),
                        0))
                .into(profileHeader));
    }

    public void setUpEditProfileButton() {
        Button editProfileButton = getActivity().findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_profilePage_to_editProfile));
    }

    private void setUpFavouritesButton() {
        Button favourites = getActivity().findViewById(R.id.favourites_button);
        favourites.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", "Favourites");
            Navigation.findNavController(v).navigate(R.id.action_profilePage_to_restaurantListFragment, bundle);
        });
    }

    private void setUpBookmarksButton() {
        Button bookmarks = getActivity().findViewById(R.id.bookmarks_button);
        bookmarks.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", "Bookmarks");
            Navigation.findNavController(v).navigate(R.id.action_profilePage_to_restaurantListFragment, bundle);
        });
    }
    private void setUpChangeAvatarButton() {
        Button uploadProfilePictureButton = getActivity().findViewById(R.id.upload_profile_picture);
        uploadProfilePictureButton.setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        PROFILE);
            } catch (Exception e) {
                Toast.makeText(getContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        });
    }

    private void setUpChangeHeaderButton() {
        Button uploadProfilePictureButton = getActivity().findViewById(R.id.upload_header_picture);
        uploadProfilePictureButton.setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        HEADER);
            } catch (Exception e) {
                Toast.makeText(getContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        });
    }

    public void updateUserProfilePageInfo(String name, String username, String bio) {
        this.name.setText(name);
        this.username.setText(username);
        this.bio.setText(bio);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectedImageUri = null;
        if (resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            launchUploadActivity(requestCode);
        }
    }

    private void launchUploadActivity(int requestCode){
        if (selectedImageUri.getPath() != null) {
            uploadPhoto(requestCode);
        } else {
            Toast.makeText(getActivity(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadPhoto(int requestCode) {
        StorageReference storageRef  = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = fAuth.getCurrentUser();
        String userID = user.getUid();
        String uploadPath = null;
        if (requestCode == PROFILE){
            uploadPath = "images/users/" + userID + "/profile";
        }
        else if (requestCode == HEADER) {
            uploadPath = "images/users/" + userID + "/header";
        }
        StorageReference storageReference = storageRef.child(uploadPath);
        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getActivity(),"Successful Upload", Toast.LENGTH_SHORT).show();
            if (requestCode == PROFILE){
                profilePicture.setImageURI(selectedImageUri);
            }
            else if (requestCode == HEADER) {
                profileHeader.setImageURI(selectedImageUri);
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(),"Error uploading.", Toast.LENGTH_SHORT).show());
    }
}