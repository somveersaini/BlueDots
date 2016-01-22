package com.iblue.bluedots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



/**
 * Created by samsaini on 12/16/2015.
 */

public class GameAdapter extends BaseAdapter {
    private final static String TAG = "GameAdater";
    private Context context;
    String[] str;
    public int[] stats;
    int level;

    public GameAdapter(Context context, String[] num, int l) {
        this.context = context;
        stats = new int[num.length];
        str = num;
        level = l;

    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;
        TextView text;
        if (convertView == null) {

            if(level == 1){
                 int i = Settings.l1[position];
                if(i == 0) {
                  //context.getResources().getDrawable(R.drawable.select_btn_current).setColorFilter(Color.parseColor("#2091ff"), PorterDuff.Mode.MULTIPLY);
                    view = inflater.inflate(R.layout.key, null);
                }
                else {
                    view = inflater.inflate(R.layout.key_solved, null);

                }
            }
            if(level == 2){
                int i = Settings.l2[position];
                if(i == 0) {
                    view = inflater.inflate(R.layout.key, null);
                }
                else {
                    view = inflater.inflate(R.layout.key_solved, null);

                }
            }
            if(level == 3){
                int i = Settings.l3[position];
                if(i == 0) {
                    view = inflater.inflate(R.layout.key, null);
                }
                else {
                    view = inflater.inflate(R.layout.key_solved, null);

                }
            }
            text = (TextView) view.findViewById(R.id.key1);
            text.setText(str[position]);
        } else { view = (View) convertView; }
        return view;
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public String getItem(int pos) {
        return "sam";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}