package ru.vladroid.NYTimes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class NewListActivity extends AppCompatActivity {

    public final static String titleKey = "NEWS_LIST_ACTIVITY_TITLE";
    public final static String textKey = "NEWS_LIST_ACTIVITY_TEXT";
    public final static String categoryKey = "NEWS_LIST_ACTIVITY_CATEGORY";
    public final static String timeKey = "NEWS_LIST_ACTIVITY_TIME";
    public final static String imageUrlKey = "NEWS_LIST_ACTIVITY_IMAGE_URL";

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
           // Toast.makeText(NewListActivity.this, news.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new NewsRecyclerAdapter(this, DataUtils.generateNews(), clickListener));
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.option_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
