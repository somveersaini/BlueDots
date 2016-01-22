package com.iblue.bluedots;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {

    SharedPreferences settings;
    SoundPool sp;
    int btnbtn, scb, scs;

    private boolean sfx;
    Switch  ssfx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        btnbtn = sp.load(this, R.raw.btnbtn, 1);
        scb = sp.load(this, R.raw.scb, 1);
        scs = sp.load(this, R.raw.scs, 1);

        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        boolean start = settings.getBoolean("start", true);

        if(start){
            starter();
            start = false;
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("start", start);
            editor.commit();
        }

    }

    public void buttonHandler(View view) {
        int id = view.getId();
        if(Settings.sfx){sp.play(btnbtn, 1, 1, 0, 0, 1);}
        if(id == R.id.play_button){
            Intent intent = new Intent(this, com.iblue.bluedots.chapterview.Chapters.class);
            startActivity(intent);
        }
        if(id == R.id.settings){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            //settings();
        }
        if(id == R.id.how_button){
            Intent intent = new Intent(this, HowToPlay.class);
            startActivity(intent);
        }
        if(id == R.id.about_button){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }

    public void settings() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings, null);
        ssfx = (Switch) view.findViewById(R.id.switch2);
        if (Settings.sfx) {
            ssfx.setChecked(true);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        //  alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        alertDialog.show();

    }

    @Override
    public void onResume(){
        super.onResume();
        load();

    }
    @Override
    public void onStop(){
        super.onStop();
    }
    public void onPause(){
        super.onPause();
    }
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exit, null);
        Button button = (Button) view.findViewById(R.id.ok);
        Button button1 = (Button) view.findViewById(R.id.cancel);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.sfx){sp.play(btnbtn, 1, 1, 0, 0, 1);}
                quitgame();
                alertDialog.cancel();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.sfx){sp.play(btnbtn, 1, 1, 0, 0, 1);}
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }
    public void load(){
        Settings.load(settings);
        sfx = Settings.sfx;
    }

    private void quitgame(){
        this.finish();
    }

    public void starter(){
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.welcomedialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final TextView logs = (TextView) promptsView
                .findViewById(R.id.thanku);
        final TextView hh = (TextView) promptsView
                .findViewById(R.id.ht);

        hh.setText(R.string.htht);
        final TextView ll = (TextView) promptsView
                .findViewById(R.id.lt);


        final AlertDialog alertDialog = alertDialogBuilder.create();
        logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

}
