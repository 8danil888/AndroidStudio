package com.topcommentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<List<Post>> pages = new ArrayList<>();
    private String after = null;
    private PagerAdapter pagerAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        Button buttonPrevious = findViewById(R.id.button_previous);
        Button buttonNext = findViewById(R.id.button_next);

        buttonPrevious.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            } else {
                Toast.makeText(MainActivity.this, "No previous page", Toast.LENGTH_SHORT).show();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < pages.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                Toast.makeText(MainActivity.this, "No more pages", Toast.LENGTH_SHORT).show();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonStates();
            }
        });

        loadTopPosts();
    }

    private void loadTopPosts() {
        if (isLoading) return;
        isLoading = true;

        RedditApiService redditApiService = RetrofitClient.getRedditApiService();
        redditApiService.getPopularPosts(100, "day", after).enqueue(new Callback<RedditResponse>() {
            @Override
            public void onResponse(Call<RedditResponse> call, Response<RedditResponse> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    RedditResponse.Data data = response.body().getData();
                    after = data.getAfter();

                    List<Post> allPosts = data.getChildrenPosts();
                    if (allPosts != null) {
                        List<List<Post>> newPages = new ArrayList<>();
                        for (int i = 0; i < allPosts.size(); i += 10) {
                            newPages.add(allPosts.subList(i, Math.min(i + 10, allPosts.size())));
                        }

                        Log.d("MainActivity", "New pages count: " + newPages.size());

                        if (newPages.size() > 0) {
                            pages.addAll(newPages);
                            pagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(pages.size() - newPages.size(), false);
                            updateButtonStates();
                        } else {
                            Toast.makeText(MainActivity.this, "No more posts available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No posts available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RedditResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(MainActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateButtonStates() {
        Button buttonPrevious = findViewById(R.id.button_previous);
        Button buttonNext = findViewById(R.id.button_next);

        int currentItem = viewPager.getCurrentItem();
        buttonPrevious.setEnabled(currentItem > 0);
        buttonNext.setEnabled(currentItem < pages.size() - 1);
    }

    private class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PageViewHolder> {
        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
            return new PageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            List<Post> currentPagePosts = pages.get(position);
            PostAdapter postAdapter = new PostAdapter(currentPagePosts);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            holder.recyclerView.setAdapter(postAdapter);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;

            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                recyclerView = itemView.findViewById(R.id.recyclerView);
            }
        }
    }
}