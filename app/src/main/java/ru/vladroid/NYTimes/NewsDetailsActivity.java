package ru.vladroid.NYTimes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Intent intent = getIntent();
        if (intent != null) {
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setTitle(intent.getStringExtra(NewListActivity.categoryKey));
            }
            TextView text = findViewById(R.id.details_text);
            text.setText(intent.getStringExtra(NewListActivity.textKey));

            TextView title = findViewById(R.id.details_title);
            title.setText(intent.getStringExtra(NewListActivity.titleKey));

            TextView date = findViewById(R.id.details_date);
            date.setText(intent.getStringExtra(NewListActivity.timeKey));

            ImageView image = findViewById(R.id.details_image);
            Glide.with(this).load(intent.getStringExtra(NewListActivity.imageUrlKey)).into(image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
