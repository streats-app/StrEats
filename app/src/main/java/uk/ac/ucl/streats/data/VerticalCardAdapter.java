package uk.ac.ucl.streats.data;
import uk.ac.ucl.streats.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VerticalCardAdapter extends RecyclerView.Adapter<VerticalCardAdapter.FeaturedViewHolder> {

    ArrayList<CardHelperClass> featuredLocations;

    public VerticalCardAdapter(ArrayList<CardHelperClass> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_card_design, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        CardHelperClass helperClass = featuredLocations.get(position);

        holder.id = helperClass.getId();
        helperClass.getInfoTask().addOnSuccessListener(document -> {
            holder.title.setText(helperClass.getTitle());
            holder.description.setText(helperClass.getDescription());
        });
    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        public String id;
        public TextView title, description;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_description);

            // open restaurant card on click
            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("restaurantId", id);

                Navigation.findNavController(v).navigate(R.id.action_to_restaurantPageFragment, bundle);
            });

        }
    }


}

