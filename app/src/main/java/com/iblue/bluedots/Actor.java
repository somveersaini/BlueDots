package com.iblue.bluedots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable; // read about it


public class Actor {
    private Context mContext;
    private int x;            // Location of actor
    private int y;
    private int costume;    // refers to drawable resources
    private int currentCostume;

    private BitmapDrawable[] graphic = new BitmapDrawable[10];

    public Actor(Context context, int xSet, int ySet, int outfit) {
        x = xSet;
        y = ySet;
        costume = outfit;
        currentCostume = 0;
        mContext = context;
        graphic[0] = (BitmapDrawable) mContext.getResources().getDrawable(costume);

    }

    public void goTo(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCostume() {
        return costume;
    }

    public Bitmap getBitmap() {
        return graphic[currentCostume].getBitmap();
    }

}
