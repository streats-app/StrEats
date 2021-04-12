package uk.ac.ucl.streats.data;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ucl.streats.R;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {

    private ArrayList<StaticRvModel> items;
    int row_index = -1;
    UpdateRecyclerView updateRecyclerView;
    Activity activity;
    HashMap<String, ArrayList<String>> restaurantCategories;
    boolean check = true;
    boolean select = true;
    public final int AMERICAN = 0;
    public final String AMERICAN_CUISINE = "American";
    public final int BRITISH = 1;
    public final String BRITISH_CUISINE = "British";
    public final int CHINESE = 2;
    public final String CHINESE_CUISINE = "Chinese";
    public final int DESSERT = 3;
    public final String DESSERT_CUISINE = "Dessert";
    public final int INDIAN = 4;
    public final String INDIAN_CUISINE = "Indian";
    public final int ITALIAN = 5;
    public final String ITALIAN_CUISINE = "Italian";
    public final int JAPANESE = 6;
    public final String JAPANESE_CUISINE = "Japanese";
    public final int KOREAN = 7;
    public final String KOREAN_CUISINE = "Korean";
    public final int MALAYSIAN = 8;
    public final String MALAYSIAN_CUISINE = "Malaysian";
    public final int VEGETARIAN = 9;
    public final String VEGETARIAN_CUISINE = "Vegetarian";


    public StaticRvAdapter(ArrayList<StaticRvModel> items, Activity activity, HashMap<String, ArrayList<String>> restaurantCategories, UpdateRecyclerView updateRecyclerView) {
        this.items = items;
        this.activity = activity;
        this.updateRecyclerView = updateRecyclerView;
        this.restaurantCategories = restaurantCategories;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, final int category) {
        StaticRvModel currentItem = items.get(category);

        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        if (check) {
            ArrayList<DynamicRVModel> items = getCategoryElements(category);
            updateRecyclerView.callback(category, items);
            check = false;
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = category;
                notifyDataSetChanged();
                updateRecyclerView.callback(category, getCategoryElements(category));
            }
        });

        if (select) {
            if (category == 0)
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            select = false;
        } else {
            if (row_index == category) {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            } else {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
            }
        }
    }

    private ArrayList<DynamicRVModel> getCategoryElements(int category) {
        ArrayList<DynamicRVModel> items = new ArrayList<>();
        String cuisine;
        switch (category) {
            case AMERICAN:
                cuisine = AMERICAN_CUISINE;
                break;
            case BRITISH:
                cuisine = BRITISH_CUISINE;
                break;
            case CHINESE:
                cuisine = CHINESE_CUISINE;
                break;
            case DESSERT:
                cuisine = DESSERT_CUISINE;
                break;
            case INDIAN:
                cuisine = INDIAN_CUISINE;
                break;
            case ITALIAN:
                cuisine = ITALIAN_CUISINE;
                break;
            case JAPANESE:
                cuisine = JAPANESE_CUISINE;
                break;
            case KOREAN:
                cuisine = KOREAN_CUISINE;
                break;
            case MALAYSIAN:
                cuisine = MALAYSIAN_CUISINE;
                break;
            case VEGETARIAN:
                cuisine = VEGETARIAN_CUISINE;
                break;
            default:
                cuisine = AMERICAN_CUISINE;
        }

        for (String id : restaurantCategories.get(cuisine))
            items.add(new DynamicRVModel(id));

        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder {

        public String id;
        public TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dynamic_card_image);
            textView = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);

            // open restaurant card on click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("restaurantId", id);

                    Navigation.findNavController(v).navigate(R.id.action_to_restaurantPageFragment, bundle);
                }
            });
        }
    }
}