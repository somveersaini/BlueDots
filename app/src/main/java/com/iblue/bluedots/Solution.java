package com.iblue.bluedots;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class Solution extends Activity {

    private GameSolView gameSolView;
    GestureDetector gestureDetector;

    public static int HEIGHT = 470;
    public static int WIDTH = 280;
    public static String graph = "0-1,1-2,2-3,3-1,1-4";
    public static String nodes = "150-70,150-150,250-70,250-150,450-70";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WIDTH = size.x;
        HEIGHT = size.y;

        Intent intent = getIntent();
        graph = intent.getStringExtra("graph");
        nodes = intent.getStringExtra("vertex");

       // graph = "0-1,1-2,2-3,3-1,1-4";
       // nodes = "150-70,150-150,250-70,250-150,450-70";


        setContentView(R.layout.activity_solution);
        gameSolView = (GameSolView) findViewById(R.id.GameSolView);
        gestureDetector = new GestureDetector(this, gestureListener);
    }

    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }
    public void onDestroy() {
        super.onDestroy();
    }


    SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        public boolean onDoubleTap(MotionEvent e) {
            finishthis();
            return true;
        }


    };


    public void finishthis(){
        this.finish();
    }
}

