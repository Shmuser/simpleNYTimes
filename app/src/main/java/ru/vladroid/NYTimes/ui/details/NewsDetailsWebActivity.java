package ru.vladroid.NYTimes.ui.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.vladroid.NYTimes.R;
import ru.vladroid.NYTimes.ui.list.NewsListActivity;

public class NewsDetailsWebActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_web);
        webView = findViewById(R.id.web_view);
        Intent intent = getIntent();

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);
        if (intent != null)
            webView.loadUrl(intent.getStringExtra(NewsListActivity.NEWS_URL));
    }
}
