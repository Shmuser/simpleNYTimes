package ru.vladroid.NYTimes.ui.list;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.vladroid.NYTimes.DTO.Result;
import ru.vladroid.NYTimes.ui.details.NewsDetailsWebActivity;
import ru.vladroid.NYTimes.R;

public class NewsListActivity extends AppCompatActivity implements Subscribable<List<Result>> {

    public final static String TAG = "NewsListActivity";
    public final static String NEWS_URL = "NEWS_URL";

    private List<Result> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button reloadButton;
    private Spinner spinner;
    private NewsLoadTask loadTask;

    private final NewsRecyclerAdapter.OnItemClickListener clickListener = new NewsRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Result news) {
            Intent intent = new Intent(getApplicationContext(), NewsDetailsWebActivity.class);
            intent.putExtra(NEWS_URL, news.getUrl());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        MenuItem menuItem = menu.findItem(R.id.section_spinner);

        spinner = (Spinner) menuItem.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_section_array, R.layout.spinner_theme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTask = createNewLoadingTask((String)spinner.getSelectedItem());
                runLoading();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do
            }
        });

        loadTask = createNewLoadingTask((String) spinner.getSelectedItem());
        runLoading();
        return true;
    }

    @Override
    public void onNewsLoad(final List<Result> news) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.addAll(news);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoadingFailed(Exception e) {
        Log.e(TAG, "Error occurred:", e);
    }

    @Override
    public void onStateChanged(LoadingState state) {
        showState(state);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!loadTask.getStatus().equals(AsyncTask.Status.FINISHED))
            loadTask.cancel(true);
    }

    private void initViews() {
        bindViews();
        initList();
        configureReloadButton();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        reloadButton = findViewById(R.id.reload_button);
    }

    private void initList() {
        recyclerView.setAdapter(new NewsRecyclerAdapter(this, data, clickListener));
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void configureReloadButton() {
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadTask.getStatus().equals(AsyncTask.Status.FINISHED)){
                    loadTask.cancel(true);
                }
                loadTask = createNewLoadingTask((String)spinner.getSelectedItem());
                runLoading();
            }
        });
    }

    private void runLoading() {
        loadTask.execute();
    }

    private NewsLoadTask createNewLoadingTask(String section) {
        NewsLoadTask task = new NewsLoadTask(section);
        task.subscribe(this);
        return task;
    }

    private void showState(@NonNull LoadingState state) {
        switch (state) {
            case DataLoaded:
                progressBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;

            case Loading:
                progressBar.setVisibility(View.VISIBLE);
                reloadButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                break;

            case NetworkError:
                progressBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                break;
        }
    }

    private void setVisible(@Nullable View view, boolean show) {
        if (view == null) return;
        int visibility = show ? View.VISIBLE : View.GONE;
        view.setVisibility(visibility);
    }
}
