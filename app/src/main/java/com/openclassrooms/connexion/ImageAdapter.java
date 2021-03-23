package com.openclassrooms.connexion;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ImageAdapter extends FirebaseRecyclerAdapter<Telechargement, ImageAdapter.ViewHolder> {

    public ImageAdapter(@NonNull FirebaseRecyclerOptions<Telechargement> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Telechargement tele) {

        holder.textViewDescription.setText(tele.getDescription());
        Glide.with(holder.imageView.getContext()).load(tele.getImageUrl()).into(holder.imageView);
        holder.textViewName.setText(tele.getNom());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewDescription;

        public ViewHolder( @NonNull View itemView) {
            super(itemView);
            textViewDescription = (TextView) itemView.findViewById(R.id.text_view_description);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_telecharger);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_name);
        }
    }

}
