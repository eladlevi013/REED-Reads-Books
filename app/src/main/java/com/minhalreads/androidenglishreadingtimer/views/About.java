package com.minhalreads.androidenglishreadingtimer.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.BottomNavigationHelper;

public class About extends AppCompatActivity {
    TextView tvGithubLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // github link
        tvGithubLink = findViewById(R.id.source);
        tvGithubLink.setMovementMethod(LinkMovementMethod.getInstance());
        tvGithubLink.setText(Html.fromHtml(getString(R.string.github_app_creator)));
        tvGithubLink.setLinkTextColor(Color.rgb(82,0, 166));
        tvGithubLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.github_project_link);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        // Bottom navigation init
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.about);
        BottomNavigationHelper.setBottomNavigationListener(bottomNavigationView, this);
    }
}
