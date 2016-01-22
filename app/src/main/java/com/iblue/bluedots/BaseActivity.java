package com.iblue.bluedots;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

public class BaseActivity extends Activity {
    private FrameLayout gFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onCreate();
    }

    public void onCreate() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gFrameLayout = new FrameLayout(this);
        setContentView(gFrameLayout);
    }

    protected View addView(View view) {
        gFrameLayout.addView(view);
        return view;
    }

    protected View addView(int resource) {
        final View view = getLayoutInflater().inflate(resource, null);
        return addView(view);
    }
}
