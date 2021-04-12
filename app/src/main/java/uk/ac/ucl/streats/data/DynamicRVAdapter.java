package uk.ac.ucl.streats.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.ucl.streats.R;

public class DynamicRVAdapter extends RecyclerView.Adapter<DynamicRVAdapter.DynamicRvHolder>{

    public ArrayList<DynamicRVModel> dynamicRVModels;
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner mListner){
        this.mListner = mListner;
    }

    public DynamicRVAdapter(ArrayList<DynamicRVModel> dynamicRVModels){
        this.dynamicRVModels = dynamicRVModels;
    }

    public void filterList(ArrayList<DynamicRVModel> filterllist) {
        dynamicRVModels = filterllist;
        notifyDataSetChanged();
    }

    public class DynamicRvHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title;
        public TextView details;
        ConstraintLayout constraintLayout;

        public DynamicRvHolder(@NonNull View itemView, final OnItemClickListner mListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);

            itemView.setOnClickListener(v -> {
                if (mListner!=null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mListner.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public DynamicRVAdapter.DynamicRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout,parent,false);
        return new DynamicRvHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicRVAdapter.DynamicRvHolder holder, int position) {
        DynamicRVModel currentItem = dynamicRVModels.get(position);

        currentItem.getInfoTask().addOnSuccessListener(document -> {
            holder.title.setText(currentItem.getName());
            holder.details.setText(currentItem.getDetails());
        });
    }

    @Override
    public int getItemCount() {
        return dynamicRVModels.size();
    }
}



