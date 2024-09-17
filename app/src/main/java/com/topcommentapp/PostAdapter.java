package com.topcommentapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.authorTextView.setText(post.getAuthor());
        holder.commentsTextView.setText(String.valueOf(post.getNumComments()));
        holder.titleTextView.setText(post.getTitle());

        if (post.getThumbnail() != null && !post.getThumbnail().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getThumbnail())
                    .placeholder(R.drawable.placeholder) // Відображаємо заміну, поки завантажується
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);  // Якщо зображення немає
        }

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), FullscreenImageActivity.class);
            intent.putExtra("imageUrl", post.getImageUrl());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView, titleTextView, commentsTextView;
        ImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.post_author);
            titleTextView = itemView.findViewById(R.id.post_title);
            commentsTextView = itemView.findViewById(R.id.post_comments);
            imageView = itemView.findViewById(R.id.post_image);
        }
    }
}