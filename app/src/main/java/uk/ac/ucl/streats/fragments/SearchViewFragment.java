package uk.ac.ucl.streats.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.data.DynamicRVAdapter;
import uk.ac.ucl.streats.data.DynamicRVModel;
import uk.ac.ucl.streats.data.StaticRvAdapter;
import uk.ac.ucl.streats.data.StaticRvModel;
import uk.ac.ucl.streats.data.UpdateRecyclerView;

public class SearchViewFragment extends Fragment implements UpdateRecyclerView {
    private RecyclerView recyclerView2;

    ArrayList<DynamicRVModel> items = new ArrayList();
    DynamicRVAdapter dynamicRVAdapter;

    public SearchViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searchview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.american_icon,"American"));
        item.add(new StaticRvModel(R.drawable.british_icon,"British"));
        item.add(new StaticRvModel(R.drawable.chinese_icon,"Chinese"));
        item.add(new StaticRvModel(R.drawable.italian_icon, "Italian"));
        item.add(new StaticRvModel(R.drawable.dessert_icon,"Dessert"));

        RecyclerView recyclerView = getView().findViewById(R.id.rv_1);
        StaticRvAdapter staticRvAdapter = new StaticRvAdapter(item, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        items = new ArrayList<>();

        recyclerView2 = getView().findViewById(R.id.rv_2);
        dynamicRVAdapter = new DynamicRVAdapter(items);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(dynamicRVAdapter);
    }

    @Override
    public void callback(int position, ArrayList<DynamicRVModel> items) {
        dynamicRVAdapter = new DynamicRVAdapter(items);
        dynamicRVAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(dynamicRVAdapter);
    }

}