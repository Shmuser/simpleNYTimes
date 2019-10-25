package ru.vladroid.NYTimes.ui.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.vladroid.NYTimes.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    EditText messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.message_text);
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Someone");
        RelativeLayout innerRLayout = findViewById(R.id.inner_relative_layout);
        TextView label = new TextView(this);
        label.setText("2019 someone");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        params.bottomMargin = 8;
        innerRLayout.addView(label, params);
        ImageView iconVk = findViewById(R.id.iconVk);
        iconVk.setOnClickListener(this);
        ImageView iconSO = findViewById(R.id.iconSO);
        iconSO.setOnClickListener(this);
        ImageView iconInst = findViewById(R.id.iconInst);
        iconInst.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_button:
                String message = messageView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@email.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "app test");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No email app found", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iconInst:
                Uri uri = Uri.parse("http://instagram.com/_u/instagram");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/instagram")));
                }
                break;
            case R.id.iconSO:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://stackoverflow.com")));
                break;
            case R.id.iconVk:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://vk.com")));
                break;


            default:
                break;

        }
    }
}
