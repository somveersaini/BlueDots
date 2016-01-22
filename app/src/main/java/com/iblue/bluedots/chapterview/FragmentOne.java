package com.iblue.bluedots.chapterview;


import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.iblue.bluedots.BlueDots;
import com.iblue.bluedots.GameAdapter;
import com.iblue.bluedots.LevelGraphs;
import com.iblue.bluedots.R;
import com.iblue.bluedots.Settings;

/**
 * Created by Samsaini on 12/16/2015.
 */
public class FragmentOne extends Fragment
{
    private String[] num ;
    private LevelGraphs levelGraphs;
    GameAdapter adapter;
    SoundPool sp;
    int  btnbtn;
    GridView grid;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        grid = (GridView) v.findViewById(R.id.gridView);
        adapter = new GameAdapter(getContext(), num,1);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                run(position );
            }
        });
        levelGraphs = new LevelGraphs(getContext(), 1);
        return v;
    }

    public void onResume(){
        super.onResume();
        Settings.load(PreferenceManager.getDefaultSharedPreferences(getContext()));
        adapter.notifyDataSetChanged();
        grid.setAdapter(adapter);
        if(Settings.next && Settings.level == 1){
            Settings.next = false;
            if(Settings.pos < 36){
                Settings.pos++;
                run(Settings.pos);
            }
        }
    }
    public void run(int pos){
        if(Settings.sfx){sp.play(btnbtn, 1, 1, 0, 0, 1);}
        Settings.level = 1;
        Settings.pos = pos;
        String graph = levelGraphs.get(pos);
        Intent intent = new Intent(getContext(), BlueDots.class);
        intent.putExtra("type",Settings.level);
        intent.putExtra("graph", graph);
        startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        num = new String[36];
        for(int i = 1; i < 37; i++){
            num[i-1] = i+"";
        }
        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        btnbtn = sp.load(getContext(), R.raw.btnbtn, 1);
    }

}
