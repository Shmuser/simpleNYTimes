package ru.vladroid.NYTimes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewListActivity extends AppCompatActivity {

    public final static String titleKey = "NEWS_LIST_ACTIVITY_TITLE";
    public final static String textKey = "NEWS_LIST_ACTIVITY_TEXT";
    public final static String categoryKey = "NEWS_LIST_ACTIVITY_CATEGORY";
    public final static String timeKey = "NEWS_LIST_ACTIVITY_TIME";
    public final static String imageUrlKey = "NEWS_LIST_ACTIVITY_IMAGE_URL";
    private List<NewsItem> data = new ArrayList<>();
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    DataLoadTask loadTask;

    private final NewsRecyclerAdapter.OnItemClickListener clickListener = new NewsRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(NewsItem news) {
            Intent intent = new Intent(getApplicationContext(), NewsDetailsActivity.class);
            intent.putExtra(titleKey, news.getTitle());
            intent.putExtra(textKey, news.getFullText());
            intent.putExtra(categoryKey, news.getCategory().getName());
            intent.putExtra(timeKey, NewsRecyclerAdapter.dateToString(news.getPublishDate()));
            intent.putExtra(imageUrlKey, news.getImageUrl());
            startActivity(intent);
        }
    };

    class DataLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.clear();
            data.addAll(DataUtils.generateNews());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (recyclerView.getAdapter() != null) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setAdapter(new NewsRecyclerAdapter(this, data, clickListener));
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        loadTask = new DataLoadTask();
        loadTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!loadTask.getStatus().equals(AsyncTask.Status.FINISHED))
            loadTask.cancel(true);
    }
}
