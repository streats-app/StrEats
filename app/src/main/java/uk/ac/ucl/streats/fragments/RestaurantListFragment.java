package uk.ac.ucl.streats.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;
import uk.ac.ucl.streats.data.CardHelperClass;
import uk.ac.ucl.streats.data.HorizontalCardAdapter;

public class RestaurantListFragment extends Fragment {

    private String title;
    RecyclerView restaurantListRecycler;
    HorizontalCardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_restaurant_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = getArguments().getString("title");

        TextView titleView = getActivity().findViewById(R.id.restaurant_list_title);
        titleView.setText(title);

        restaurantListRecycler = getActivity().findViewById(R.id.restaurant_list_recycler);
        restaurantListRecycler();
    }

    private void restaurantListRecycler() {
        restaurantListRecycler.setHasFixedSize(true);
        restaurantListRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        HomeActivity homeActivity = (HomeActivity) getActivity();
        ArrayList<CardHelperClass> restaurantList = new ArrayList<>();
        adapter = new HorizontalCardAdapter(restaurantList);
        restaurantListRecycler.setAdapter(adapter);

        homeActivity.getProfileTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (title.equals("Favourites")) {
                    for (String restaurantId : homeActivity.getFavourites()) {
                        restaurantList.add(new CardHelperClass(restaurantId));
                    }
                }
                else if (title.equals("Bookmarks")) {
                    for (String restaurantId : homeActivity.getBookmarks()) {
                        restaurantList.add(new CardHelperClass(restaurantId));
                    }
                }
                adapter.updateAdapter(restaurantList);
            }
        });

    }

}