package com.iblue.bluedots;

import android.view.View;


public class AboutActivity extends BaseActivity {
    private View gView;

    @Override
    public void onCreate() {
        super.onCreate();
        gView = addView(R.layout.about);
    }

}
