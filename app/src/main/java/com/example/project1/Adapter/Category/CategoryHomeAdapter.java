package com.example.project1.Adapter.Category;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.Category;
import com.example.project1.R;
import com.example.project1.work.ProDuctCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>{
    Activity activity;
    ArrayList<Category> categories;

    public CategoryHomeAdapter(Activity activity, ArrayList<Category> categories) {
        this.activity = activity;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_category_home,null);
        return (new ViewHolder(view));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(categories.get(position).getImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProDuctCategory.class);
                intent.putExtra("idcategory",categories.get(position).getIdcategory());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_category_item);
        }
    }
}
