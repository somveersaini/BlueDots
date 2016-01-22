package com.iblue.bluedots.chapterview;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.iblue.bluedots.BlueDots;
import com.iblue.bluedots.R;
import com.iblue.bluedots.Settings;

public class Chapters extends FragmentActivity {
    ViewPager pager;
    SoundPool sp;
    int btnbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chapters);


        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        btnbtn = sp.load(getApplicationContext(), R.raw.btnbtn, 1);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

    }


    public void buttonHandler(View view) {
        int id = view.getId();
        if (id == R.id.circle) {
            run(1);
        }
        if (id == R.id.heart) {
            run(2);
        }
        if (id == R.id.random) {
            run(3);
        }
    }

    public void run(int type) {
        if (Settings.sfx) {
            sp.play(btnbtn, 1, 1, 0, 0, 1);
        }
        Intent intent = new Intent(getApplicationContext(), BlueDots.class);
        intent.putExtra("type", type);
        intent.putExtra("graph", "");
        startActivity(intent);
    }
}
