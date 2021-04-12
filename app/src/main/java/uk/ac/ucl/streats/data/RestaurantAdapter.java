package uk.ac.ucl.streats.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;
import uk.ac.ucl.streats.data.Restaurant;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private ArrayList<Restaurant> restaurantList;

    public RestaurantAdapter(@NonNull Context context, int resource, ArrayList<Restaurant> restaurantList) {
        super(context, resource);
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        ImageView restaurantImage = convertView.findViewById(R.id.restaurant_imageview);
        TextView restaurantTextView = convertView.findViewById(R.id.title_textview);
        TextView addressTextView = convertView.findViewById(R.id.restaurant_textview);

        restaurantImage.setImageResource(restaurantList.get(position).getRestaurantImageID());
        restaurantTextView.setText(restaurantList.get(position).getRestaurantName());
        addressTextView.setText(restaurantList.get(position).getRestaurantAdd());

        return convertView;
    }
}
