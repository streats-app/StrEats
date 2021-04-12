package uk.ac.ucl.streats.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;


public class RestaurantPageFragment extends Fragment {

    private String id;
    private TextView restaurant_name, restaurant_location, restaurant_operating_hours,
            restaurant_website, restaurant_cuisine, restaurant_description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("restaurantId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity fa = getActivity();

        restaurant_name = fa.findViewById(R.id.restaurant_name);
        restaurant_location = fa.findViewById(R.id.restaurant_location);
        restaurant_operating_hours = fa.findViewById(R.id.restaurant_operating_hours);
        restaurant_website = fa.findViewById(R.id.restaurant_website);
        restaurant_cuisine = fa.findViewById(R.id.restaurant_cuisine);
        restaurant_description = fa.findViewById(R.id.restaurant_description);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("restaurants").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                restaurant_name.setText(document.getString("name"));
                restaurant_location.setText(document.getString("location"));
                restaurant_operating_hours.setText(document.getString("openingHours"));
                restaurant_website.setText(document.getString("website"));
                restaurant_cuisine.setText(document.getString("cuisine"));
                restaurant_description.setText(document.getString("description"));
            }
        });

        setUpFavouriteButton();
        setUpBookmarkButton();
    }

    public void setUpFavouriteButton() {
        ImageButton favouriteButton = getActivity().findViewById(R.id.favouriteButton);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        favouriteButton.setOnClickListener(v -> {
            if (favouriteButton.getTag().equals("true")) {
                homeActivity.removeFromFavourites(id);
                favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_40);
                favouriteButton.setTag("false");
            }
            else {
                homeActivity.addToFavourites(id);
                favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_40);
                favouriteButton.setTag("true");
            }
        });

        homeActivity.getProfileTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (homeActivity.getFavourites().contains(id)) {
                    favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_40);
                    favouriteButton.setTag("true");

                }
            }
        });
    }

    public void setUpBookmarkButton() {
        ImageButton bookmarkButton = getActivity().findViewById(R.id.bookmarkButton);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        bookmarkButton.setOnClickListener(v -> {
            if (bookmarkButton.getTag().equals("true")) {
                homeActivity.removeFromBookmarks(id);
                bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_40);
                bookmarkButton.setTag("false");
            }
            else {
                homeActivity.addToBookmarks(id);
                bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_40);
                bookmarkButton.setTag("true");
            }
        });

        homeActivity.getProfileTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (homeActivity.getBookmarks().contains(id)) {
                    bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_40);
                    bookmarkButton.setTag("true");
                }
            }
        });
    }
}