package com.iblue.bluedots;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;        // for calculating the velocity of the touch
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Generate n lines
 * Calculate intersection of all pairs
 * Also note which two lines intersection resulted in that vertex
 * Make set of vertex for each line
 * <p/>
 * Consider each line one by one and sort the vertexes either by x or by y coordinate
 * join the adjacent pairs and insert the edges in the hash set indexed by the two lines which resulted in that vertices
 * <p/>
 * YO That's all !!!!
 */

public class BlueDots extends Activity {

    public static int HEIGHT;
    public static int WIDTH;
    public static int type;
    public static String graph = "";
    public static String vertex = "";
    public static int nodes;

    private GameView arrowGameView;
    private VelocityTracker mVelocityTracker = null;
    private boolean won;
    private int selectednode;
    private int starttime = 0, currenttime = 0;
    private int intersections;
    private int backButtonCount = 0;
    private long backButtonPreviousTime = 0;


    private SoundPool sp;
    private int four, one, done, sol, scm, btnbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        one = sp.load(this, R.raw.one, 1);
        four = sp.load(this, R.raw.four, 1);
        done = sp.load(this, R.raw.done, 1);
        sol = sp.load(this, R.raw.sol, 1);
        scm = sp.load(this, R.raw.scm, 1);
        btnbtn = sp.load(this, R.raw.btnbtn, 1);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WIDTH = size.x;
        HEIGHT = size.y;

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        graph = intent.getStringExtra("graph");

        if (graph.length() > 0) {
            String[] str = graph.split(" ");
            nodes = Integer.parseInt(str[0]);
            graph = str[1];
            if (str.length == 3) {
                vertex = str[2];
            } else {
                vertex = "";
            }
        } else {
            nodes = 0;
            graph = "";
        }

        startgame();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        int nodewidth = arrowGameView.mynodes[0].getBitmap().getWidth();
        int nodeheight = arrowGameView.mynodes[0].getBitmap().getHeight();

        if (action == MotionEvent.ACTION_DOWN) {

            currenttime = (int) Math.abs(System.currentTimeMillis());
            currenttime = Math.abs(currenttime);
            // System.out.println(currenttime + " " + starttime);
            if (Math.abs(currenttime - starttime) < 300) {
                // showsolution();
            }
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
            mVelocityTracker.addMovement(event);

            int y = (int) event.getRawY();
            int x = (int) event.getRawX();

            int min = 10000;
            int mini = -1;

            for (int i = 0; i < Graph.numnodes; ++i) {
                int t1 = y - arrowGameView.mynodes[i].getY() - nodeheight / 2;
                int t2 = x - arrowGameView.mynodes[i].getX() - nodewidth / 2;
                int temp = (int) Math.sqrt(Math.abs(t1 * t1 + t2 * t2));
                if (temp < min) {
                    min = temp;
                    mini = i;
                }
            }

            if (min < 4000) {
                if (Settings.sfx) {
                    sp.play(one, 0.5f, 0.5f, 0, 0, 1);
                }
                if (x > nodewidth / 2 && y > nodeheight + 90 && x < WIDTH - nodewidth / 2 && y < HEIGHT - nodeheight / 2) {

                    arrowGameView.mynodes[mini].goTo(x - nodewidth / 2, y - nodeheight / 2);
                    GameView.myvertex[mini].x = x - nodewidth / 2;
                    GameView.myvertex[mini].y = y - nodeheight / 2;
                    selectednode = mini;
                } else {
                    if (x < nodewidth / 2) {
                        x = nodewidth / 2;
                    }
                    if (x > WIDTH - nodewidth / 2) {
                        x = WIDTH - nodewidth / 2;
                    }
                    if (y < nodeheight + 90) {
                        y = nodeheight + 90;
                    }
                    if (y > HEIGHT - nodeheight / 2) {
                        y = HEIGHT - nodeheight / 2;
                    }
                    arrowGameView.mynodes[mini].goTo(x - nodewidth / 2, y - nodeheight / 2);
                    GameView.myvertex[mini].x = x - nodewidth / 2;
                    GameView.myvertex[mini].y = y - nodeheight / 2;
                    selectednode = mini;
                }
            } else {
                selectednode = -1;
            }
            starttime = currenttime;

        } else if (action == MotionEvent.ACTION_MOVE) {
            mVelocityTracker.addMovement(event);
            mVelocityTracker.computeCurrentVelocity(1000);
            int xvel = (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
            int yvel = (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);

            int vel = (int) Math.sqrt(xvel * xvel + yvel * yvel);

            int y = (int) event.getRawY();
            int x = (int) event.getRawX();
            if (selectednode != -1 && vel < 8000) {
                if (x > nodewidth / 2 && y > nodeheight + 90 && x < WIDTH - nodewidth / 2 && y < HEIGHT - nodeheight / 2) {
                    arrowGameView.mynodes[selectednode].goTo(x - nodewidth / 2, y - nodeheight / 2);
                    GameView.myvertex[selectednode].x = x - nodewidth / 2;
                    GameView.myvertex[selectednode].y = y - nodeheight / 2;
                } else {
                    if (x < nodewidth / 2) {
                        x = nodewidth / 2;
                    }
                    if (x > WIDTH - nodewidth / 2) {
                        x = WIDTH - nodewidth / 2;
                    }
                    if (y < nodeheight + 90) {
                        y = nodeheight + 90;
                    }
                    if (y > HEIGHT - nodeheight / 2) {
                        y = HEIGHT - nodeheight / 2;
                    }
                    arrowGameView.mynodes[selectednode].goTo(x - nodewidth / 2, y - nodeheight / 2);
                    GameView.myvertex[selectednode].x = x - nodewidth / 2;
                    GameView.myvertex[selectednode].y = y - nodeheight / 2;
                }
            } else {
                selectednode = -1;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (Settings.sfx) {
                sp.play(four, 0.5f, 0.5f, 0, 0, 1);
            }
            int flag = GameView.CheckGraphIntersection();
            if (flag == 1) {
                // System.out.println("GRAPH HAS INTERSECTION");
            } else {
                GameView.GameOver = 1;        // game done
                //Toast.makeText(getApplicationContext(), "CONGRATULATIONS", Toast.LENGTH_LONG).show();
                if (!won) {
                    gameOver();
                }
                System.out.println("GRAPH HAS NO INTERSECTION");
                return false;
            }
        }

        return false;
    }

    private void showsolution() {
        if (vertex.length() > 1 && graph.length() > 1) {
            if (Settings.sfx) {
                sp.play(sol, 1, 1, 0, 0, 1);
            }
            Intent intent = new Intent(this, Solution.class);
            intent.putExtra("vertex", vertex);
            intent.putExtra("graph", graph);
            startActivity(intent);
        } else {
            if (Settings.sfx) {
                sp.play(scm, 1, 1, 0, 0, 1);
            }
            Toast.makeText(this, "No solution at this Moment", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonHandler(View view) {
        int id = view.getId();

        if (id == R.id.again) {
            startgame();
        }
        if (id == R.id.next) {
            nextgame();
        }
    }

    public void gameOver() {

        if (Settings.sfx) {
            sp.play(done, 1, 1, 0, 0, 1);
        }
        arrowGameView.focus = false;
        int score = 1;
        if (nodes != 0 && !won) {
            won = true;
            arrowGameView.focus = false;
            int srate = (intersections * (Settings.pos / 3 + 4)) / arrowGameView.time;
            score = (srate >= 5) ? 5 : srate;
            if (score < 0) score = 1;

            Settings.currentScore = score;
            Settings.savelevels(PreferenceManager.getDefaultSharedPreferences(this));
        }
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gameover, null);
        Button button = (Button) view.findViewById(R.id.again);
        Button button1 = (Button) view.findViewById(R.id.next);
        TextView tv = (TextView) view.findViewById(R.id.scorecard);
        tv.setText("Total Score " + Settings.score);


        LinearLayout ll = (LinearLayout) view.findViewById(R.id.topdots);
        ImageView i2 = (ImageView) ll.findViewById(R.id.ic2);
        ImageView i3 = (ImageView) ll.findViewById(R.id.ic3);
        ImageView i4 = (ImageView) ll.findViewById(R.id.ic4);
        ImageView i5 = (ImageView) ll.findViewById(R.id.ic5);

        if (score < 5) {
            i5.setVisibility(View.GONE);
        }
        if (score < 4) {
            i4.setVisibility(View.GONE);
        }
        if (score < 3) {
            i3.setVisibility(View.GONE);
        }
        if (score < 2) {
            i2.setVisibility(View.GONE);
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.sfx) {
                    sp.play(btnbtn, 1, 1, 0, 0, 1);
                }
                startgame();
                alertDialog.cancel();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextgame();
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void startgame() {
        setContentView(R.layout.activity_bluedots);
        arrowGameView = (GameView) findViewById(R.id.arrowGameView);
        GameView.CheckGraphIntersection();
        intersections = arrowGameView.intersections;
        arrowGameView.time = 0;
        won = false;
        arrowGameView.focus = true;
    }

    public void nextgame() {
        // savedata
        String data = Graph.numnodes + " ";
 /*       for (int i = 0; i < Graph.totaledge.size(); i++) {
            Edge e = Graph.totaledge.get(i);
            data = data + e.v1 + "-" + e.v2;
            if (i < Graph.totaledge.size() - 1) {
                data += ",";
            }
        }
        data += " ";
        for (int i = 0; i < GameView.myvertex.length; i++) {
            Vertex v = GameView.myvertex[i];
            data = data + v.x + "-" + v.y;
            if (i < GameView.myvertex.length - 1) {
                data += ",";
            }
        }

        data += "\n";
        System.out.println(data);
        File file;
        FileOutputStream fos = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "test");
            fos = new FileOutputStream(file, true);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        Settings.next = true;
        arrowGameView.willNotDraw();
        arrowGameView.destroyDrawingCache();
        arrowGameView.stopHandler();
        finish();

    }

    private void quitgame() {
        this.finish();
    }

    public void onBackPressed() {
        if (arrowGameView.focus) {
            arrowGameView.focus = false;
        } else {
            arrowGameView.focus = true;
        }
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.resumegame, null);
        Button button = (Button) view.findViewById(R.id.resume);
        Button button1 = (Button) view.findViewById(R.id.quit);
        Button button2 = (Button) view.findViewById(R.id.answer);
        Button button3 = (Button) view.findViewById(R.id.help);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this).setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
                    arrowGameView.focus = true;
                    dialog.cancel();
                    return true;
                }
                return false;
            }
        });
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.sfx) {
                    sp.play(btnbtn, 1, 1, 0, 0, 1);
                }
                alertDialog.cancel();
                arrowGameView.focus = true;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.sfx) {
                    sp.play(btnbtn, 1, 1, 0, 0, 1);
                }
                quitgame();
                alertDialog.cancel();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.sfx) {
                    sp.play(btnbtn, 1, 1, 0, 0, 1);
                }
                showsolution();
                arrowGameView.focus = true;
                alertDialog.cancel();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.sfx) {
                    sp.play(btnbtn, 1, 1, 0, 0, 1);
                }
                Intent intent = new Intent(getApplicationContext(), HowToPlay.class);
                startActivity(intent);
                arrowGameView.focus = true;
                alertDialog.cancel();
            }
        });

        alertDialog.show();

        final long currentTime = System.currentTimeMillis();
        final long timeDiff = currentTime - backButtonPreviousTime;

        backButtonPreviousTime = currentTime;

        if ((timeDiff < Constants.BACK_PRESS_DELAY) || (backButtonCount == 0)) {
            backButtonCount++;
        } else {
            backButtonCount = 1;
        }

        if (backButtonCount >= Constants.BACK_PRESS_COUNT) {
            finish();
        }

    }

}
