package uk.ac.ucl.streats.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;
import uk.ac.ucl.streats.data.VerticalCardAdapter;
import uk.ac.ucl.streats.data.HorizontalCardAdapter;
import uk.ac.ucl.streats.data.CardHelperClass;

public class HomepageFragment extends Fragment {

    private RecyclerView featuredRecycler;
    private RecyclerView mostViewedRecycler;
    private RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        featuredRecycler = getView().findViewById(R.id.featured_recycler);
        mostViewedRecycler = getView().findViewById(R.id.most_viewed_recycler);

        //Functions will be executed automatically when this activity will be created
        featuredRecycler();
        mostViewedRecycler();
    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        HomeActivity homeActivity = (HomeActivity) getActivity();

        homeActivity.getMostViewedTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CardHelperClass> mostViewedLocations = new ArrayList<>();
                for (String id : homeActivity.getMostViewedIds()) {
                    mostViewedLocations.add(new CardHelperClass(id));
                }

                adapter = new HorizontalCardAdapter(mostViewedLocations, getActivity());
                mostViewedRecycler.setAdapter(adapter);
            }
        });
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.getFeaturedTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CardHelperClass> featuredLocations = new ArrayList<>();
                for (String id : homeActivity.getFeaturedIds()) {
                    featuredLocations.add(new CardHelperClass(id));
                }

                adapter = new VerticalCardAdapter(featuredLocations, getActivity());
                featuredRecycler.setAdapter(adapter);
            }
        });
    }

}

