package com.iblue.bluedots;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Samsaini on 12/10/2015.
 */

public class LevelGraphs {
    final static String tag = "LevelGraphs";
    public ArrayList<String> graphList;
    AssetManager mngr;
    Context mcontext;
    int currentLevel;

    public LevelGraphs(Context myContext, int level) {
        currentLevel = level;
        mcontext = myContext;
        graphList = new ArrayList<String>();
        mngr = mcontext.getAssets();
        init();
    }

    void init() {
        try {
            InputStream is = mngr.open("level" + currentLevel);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                graphList.add(line);
            }
            is.close();
        } catch (IOException ex) {
            return;
        }
    }

    public String get(int i) {
        if (i < graphList.size()) {
            return graphList.get(i);
        } else
            return graphList.get(0);
    }

}
