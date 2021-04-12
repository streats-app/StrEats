package uk.ac.ucl.streats.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.ucl.streats.R;

import java.util.ArrayList;

public class HorizontalCardAdapter extends RecyclerView.Adapter<HorizontalCardAdapter.MostViewedViewHolder> {

    ArrayList<CardHelperClass> mostViewedLocations;

    public HorizontalCardAdapter(ArrayList<CardHelperClass> mostViewedLocations) {
        this.mostViewedLocations = mostViewedLocations;
    }

    @NonNull
    @Override
    public MostViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_card_design, parent, false);
        return new MostViewedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewedViewHolder holder, int position) {
        CardHelperClass helperClass = mostViewedLocations.get(position);

        holder.id = helperClass.getId();
        helperClass.getInfoTask().addOnSuccessListener(document -> {
            holder.title.setText(helperClass.getTitle());
            holder.description.setText(helperClass.getDescription());
        });
    }

    @Override
    public int getItemCount() {
        return mostViewedLocations.size();
    }

    public void updateAdapter(ArrayList<CardHelperClass> mostViewedLocations) {
        this.mostViewedLocations = mostViewedLocations;
        notifyDataSetChanged();
    }

    public static class MostViewedViewHolder extends RecyclerView.ViewHolder {

        public String id;
        public TextView title, description;

        public MostViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.most_viewed_title);
            description = itemView.findViewById(R.id.most_viewed_description);

            // open restaurant card on click
            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("restaurantId", id);

                Navigation.findNavController(v).navigate(R.id.action_to_restaurantPageFragment, bundle);
            });
        }

    }
}


