package com.sun.pullzoomviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sun.pullzoom.okpullzoom.PullZoomScrollView;

public class ScrollPullActivity extends AppCompatActivity {
    PullZoomScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_pull);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.scroll_detail, null);
        RelativeLayout zoom_container =
                (RelativeLayout) view.findViewById(R.id.zoom_container);
        ImageView zoom_view = (ImageView) view.findViewById(R.id.zoom_view);
        scrollView = (PullZoomScrollView) findViewById(R.id.scrollView);
        scrollView.addView(view);
        scrollView.setZoomView(zoom_view);
        scrollView.setHeaderContainer(zoom_container);
    }
}
