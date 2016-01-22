package com.iblue.bluedots;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;


/**
 * Created by Samsaini on 12/16/2015.
 */
public class Settings extends Activity{
    private static final String TAG = "SettingsActivity" ;
    public static boolean sfx;
    public static int theme;
    public static int[] l1 = new int[36];
    public static int[] l2 = new int[36];
    public static int[] l3 = new int[36];
    public static int level = 20;
    public static int pos;
    public static int score;
    public static int currentScore;
    public static boolean next = false;


    Switch  ssfx;
    SharedPreferences settings;
    SoundPool sp;
    int  btnbtn, scs, sce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.settings);

        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        sce = sp.load(this, R.raw.sce, 1);
        btnbtn = sp.load(this, R.raw.btnbtn, 1);
        scs = sp.load(this, R.raw.scs, 1);


        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        ssfx = (Switch) findViewById(R.id.switch2);
        if (sfx) {
            ssfx.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void buttonsettings(View view) {
        final int id = view.getId();



        if(view instanceof Switch) {
            if(sfx){sp.play(sce, 1, 1, 0, 0, 1);}
            Switch s = (Switch) view;
            boolean isChecked = s.isChecked();
            if (id == R.id.switch2) {
                sfx = isChecked;
            }
        }
        if(view instanceof Button){
            if(sfx){sp.play(btnbtn, 1, 1, 0, 0, 1);}
            if(id == R.id.magneta){
                theme = Constants.MAGNETA;
            }
            if(id == R.id.black){
                theme = Constants.BLACK;
            }
            if(id == R.id.white){
                theme = Constants.WHITE;
            }
            if(id == R.id.bluetheme){
                theme = Constants.BLUE;
            }
            this.finish();
        }
        save();
    }
    private  void save(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("sfx", sfx);
        editor.putInt("theme", theme);
        editor.putInt("score", score);
        editor.commit();
    }
    public static void savelevels(SharedPreferences set){
        SharedPreferences.Editor editor = set.edit();

        if(level == 1){
            if(l1[pos] < currentScore)
            l1[pos] = currentScore;
        }
        if(level == 2){
            if(l2[pos] < currentScore)
            l2[pos] = currentScore;
        }
        if(level == 3){
            if(l3[pos] < currentScore)
            l3[pos] = currentScore;
        }

        score = 0;
        for(int i=0;i<36;i++){
            editor.putInt("l1"+i, l1[i]);
            editor.putInt("l2"+i, l2[i]);
            editor.putInt("l3"+i, l3[i]);
            score += l1[i] + l2[i] + l3[i];
        }

        editor.putInt("score", score);
        editor.commit();
    }
    //loadState
    public static void load(SharedPreferences set){
        sfx = set.getBoolean("sfx", true);
        theme = set.getInt("theme", Constants.WHITE);
        for(int i=0;i<36;i++){
            l1[i] = set.getInt("l1"+i,0);
            l2[i] = set.getInt("l2"+i,0);
            l3[i] = set.getInt("l3"+i,0);
        }
        score = set.getInt("score", 0);

    }

}
