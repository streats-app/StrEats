package uk.ac.ucl.streats.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.activities.HomeActivity;
import uk.ac.ucl.streats.data.CardHelperClass;
import uk.ac.ucl.streats.data.DynamicRVAdapter;
import uk.ac.ucl.streats.data.DynamicRVModel;
import uk.ac.ucl.streats.data.StaticRvAdapter;
import uk.ac.ucl.streats.data.StaticRvModel;
import uk.ac.ucl.streats.data.UpdateRecyclerView;
import uk.ac.ucl.streats.data.VerticalCardAdapter;

public class SearchViewFragment extends Fragment implements UpdateRecyclerView {
    private RecyclerView recyclerView, recyclerView2;
    private StaticRvAdapter staticRvAdapter;
    SearchView searchView;

    ArrayList<DynamicRVModel> items = new ArrayList();
    DynamicRVAdapter dynamicRVAdapter;
    int pos;

    public SearchViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searchview, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.american_icon,"American"));
        item.add(new StaticRvModel(R.drawable.british_icon,"British"));
        item.add(new StaticRvModel(R.drawable.chinese_icon,"Chinese"));
        item.add(new StaticRvModel(R.drawable.dessert_icon,"Dessert"));
        item.add(new StaticRvModel(R.drawable.indian_icon,"Indian"));
        item.add(new StaticRvModel(R.drawable.italian_icon, "Italian"));
        item.add(new StaticRvModel(R.drawable.japanese_icon,"Japanese"));
        item.add(new StaticRvModel(R.drawable.korean_icon,"Korean"));
        item.add(new StaticRvModel(R.drawable.malaysian_icon,"Malaysian"));
        item.add(new StaticRvModel(R.drawable.vegetarian_icon,"Vegetarian"));

        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.getRestaurantCategoriesTask().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recyclerView = getView().findViewById(R.id.rv_1);
                staticRvAdapter = new StaticRvAdapter(item, getActivity(), homeActivity.getRestaurantCategories(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(staticRvAdapter);
            }
        });


        items = new ArrayList<>();

        recyclerView2 = getView().findViewById(R.id.rv_2);
        dynamicRVAdapter = new DynamicRVAdapter(items);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(dynamicRVAdapter);

        //setUpSearchBar();
    }

    @Override
    public void callback(int position, ArrayList<DynamicRVModel> items) {
        dynamicRVAdapter = new DynamicRVAdapter(items);
        dynamicRVAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(dynamicRVAdapter);
    }

    public void setUpSearchBar(){
        searchView = getView().findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        ArrayList<DynamicRVModel> filteredlist = new ArrayList<>();

        for (DynamicRVModel item : items) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(getContext(), "Not found!", Toast.LENGTH_SHORT).show();
        } else {
            dynamicRVAdapter.filterList(filteredlist);
        }
    }

}