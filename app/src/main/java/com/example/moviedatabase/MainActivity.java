package com.example.moviedatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMovies);
        tvError = findViewById(R.id.tvError);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadMovieData();
    }

    private void loadMovieData() {
        JsonUtils.MovieLoadResult result = JsonUtils.loadMoviesFromJson(this);
        if (!result.getMovies().isEmpty()) {
            adapter.updateMovies(result.getMovies());
            recyclerView.setVisibility(View.VISIBLE);
            if (result.getErrorMessage().isEmpty()) {
                tvError.setVisibility(View.GONE);
            } else {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(result.getErrorMessage());
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            if (result.getErrorMessage().isEmpty()) {
                tvError.setText("No valid movies found.");
            } else {
                tvError.setText(result.getErrorMessage());
            }
        }
    }
}