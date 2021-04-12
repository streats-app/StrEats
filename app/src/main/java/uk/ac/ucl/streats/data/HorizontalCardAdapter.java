package uk.ac.ucl.streats.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.ac.ucl.streats.R;

import java.util.ArrayList;

public class HorizontalCardAdapter extends RecyclerView.Adapter<HorizontalCardAdapter.MostViewedViewHolder> {

    ArrayList<CardHelperClass> mostViewedLocations;
    FragmentActivity activity;

    public HorizontalCardAdapter(ArrayList<CardHelperClass> mostViewedLocations, FragmentActivity activity) {
        this.mostViewedLocations = mostViewedLocations;
        this.activity = activity;
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

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference().child("images/restaurants/"+holder.id);
            storageRef.getMetadata().addOnSuccessListener(storageMetadata -> GlideApp.with(activity)
                    .load(storageRef)
                    .signature(new MediaStoreSignature(storageMetadata.getContentType(),
                            storageMetadata.getCreationTimeMillis(),
                            0))
                    .into(holder.image));
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
        public ImageView image;

        public MostViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.most_viewed_title);
            description = itemView.findViewById(R.id.most_viewed_description);
            image = itemView.findViewById(R.id.horizontal_card_restaurant_photo);

            // open restaurant card on click
            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("restaurantId", id);

                Navigation.findNavController(v).navigate(R.id.action_to_restaurantPageFragment, bundle);
            });
        }

    }
}


