package com.iblue.bluedots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    public static Vertex myvertex[];        // Set of vertex only x and y coordinates
    public static int intersections;     //no.of intersections
    public static int GameOver = 0;            // 1 means game is over i.e player has succeeded
    public int time = 0;
    public static boolean focus = false;
    private Handler h;                        // for animation sequence timing
    public Actor mynodes[];                    // Array of nodes in the gameplay

    private Paint testPaint;
    private Paint textPaint;                // for painting the text
    private Paint finalPaint;
    private Paint blurPaint;
    private int HEIGHT = 470;
    private int WIDTH = 280;


    private final int FRAME_RATE = 30;
    private int starttimeinsec = 0;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        HEIGHT = BlueDots.HEIGHT / 2 - 20;
        WIDTH = BlueDots.WIDTH / 2 - 40;

        System.out.println("height " + HEIGHT + " " + WIDTH);

        h = new Handler();
        starttimeinsec = (int) System.currentTimeMillis() / 1000;

        // ColorFilter colorFilter = new LightingColorFilter(Color.WHITE, Color.parseColor("#1e90ff"));
        //EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(new float[]{0,0,0},0.5f,10, 3f);

        Shader shader = new RadialGradient(WIDTH, HEIGHT, 350, Color.BLUE, Color.parseColor("#2979ff"), Shader.TileMode.CLAMP);

        testPaint = new Paint();
        testPaint.setColor(Color.MAGENTA);
        testPaint.setAntiAlias(true);
        testPaint.setStrokeWidth(5);
        testPaint.setStrokeJoin(Paint.Join.ROUND);
        testPaint.setStyle(Paint.Style.STROKE);

        finalPaint = new Paint();
        finalPaint.setAntiAlias(true);
        finalPaint.setStrokeWidth(5);
        finalPaint.setStyle(Paint.Style.STROKE);
        finalPaint.setShader(shader);

        blurPaint = new Paint();
        blurPaint.setAntiAlias(true);
        blurPaint.setAlpha(140);
        blurPaint.setStyle(Paint.Style.STROKE);
        blurPaint.setColor(Color.parseColor("#2196fe"));
        blurPaint.setStrokeWidth(6);


        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#76ff03"));
        textPaint.setTextSize(32);
        textPaint.setTypeface(Typeface.SERIF);
        textPaint.setAntiAlias(true);
        textPaint.setShadowLayer(9f, 3, 3, Color.parseColor("#2090ff"));

        switch (Settings.theme) {
            case Constants.MAGNETA:
                testPaint.setColor(Color.MAGENTA);
                textPaint.setColor(Color.MAGENTA);
                this.setBackgroundColor(Color.WHITE);
                break;

            case Constants.WHITE:
                testPaint.setColor(Color.GRAY);
                textPaint.setColor(Color.MAGENTA);
                this.setBackgroundColor(Color.WHITE);
                break;

            case Constants.BLACK:
                testPaint.setColor(Color.WHITE);
                textPaint.setColor(Color.WHITE);
                this.setBackgroundColor(Color.BLACK);
                break;

            case Constants.BLUE:
                testPaint.setColor(Color.WHITE);
                blurPaint.setColor(Color.BLUE);
                textPaint.setColor(Color.WHITE);
                this.setBackgroundColor(Color.parseColor("#2196fe"));
                break;
        }

        Graph.Makegraph(BlueDots.nodes, BlueDots.graph);        // Generate the graph

        mynodes = new Actor[Graph.numnodes];
        myvertex = new Vertex[Graph.numnodes];

        int x = 0, y = 0;

        int maxx = 0, maxy = 0;
        int minx = 9999, miny = 99999;
        double rx = 1, ry = 1, rxn = 1, ryn = 1;

        boolean a = true;
        for (int i = 0; i < Graph.numnodes; ++i) {
            if (BlueDots.type == 4) {
                if (a == true) {
                    a = false;
                    for (int j = 0; j < Graph.numnodes; ++j) {
                        int x1 = Graph.totalvertex.get(j).x;
                        int y1 = Graph.totalvertex.get(j).y;
                        if (x1 > maxx) {
                            maxx = x1;
                        }
                        if (y1 > maxy) {
                            maxy = y1;
                        }
                        if (x1 < minx) {
                            minx = x1;
                        }
                        if (y1 < miny) {
                            miny = y1;
                        }
                    }

                    if (maxx > WIDTH) {
                        rx = (WIDTH * 1.0 - 10) / maxx;
                    } else {
                        rx = 1;
                    }
                    if (maxy > HEIGHT) {
                        ry = (HEIGHT * 1.0 - 20) / maxy;
                    } else {
                        ry = 1;
                    }
                    if (minx < -WIDTH) {
                        rxn = (WIDTH * 1.0 - 10) / -minx;
                    } else {
                        rxn = 1;
                    }
                    if (miny < -HEIGHT) {
                        ryn = (HEIGHT * 1.0 - 20) / -miny;
                    } else {
                        ryn = 1;
                    }

                }

                System.out.println(maxx + " " + maxy + " " + rx + " " + ry);
                int tx = Graph.totalvertex.get(i).x;
                int ty = Graph.totalvertex.get(i).y;
                if (tx < 0) {
                    x = (int) (tx * rxn) + WIDTH;
                } else {
                    x = (int) (tx * rx) + WIDTH;
                }
                if (ty < 0) {
                    y = (int) (ty * ryn) + HEIGHT;
                } else {
                    y = (int) (ty * ry) + HEIGHT;
                }

                System.out.println(tx + " " + ty + " " + x + " " + y);
            }
            if (BlueDots.type == 2) {
                double t = 6.28 * i / Graph.numnodes;
                x = (int) (WIDTH / 17 * (16 * Math.sin(t) * Math.sin(t) * Math.sin(t))) + WIDTH;
                y = (int) -(WIDTH / 17 * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t))) + HEIGHT;
            }
            if (BlueDots.type == 3) {
                x = Graph.randInt(35, WIDTH * 2 - 32);
                y = Graph.randInt(120, HEIGHT * 2 - 40);
            }
            if (BlueDots.type == 1) {
                x = (int) (WIDTH * Math.cos(6.28 * i / Graph.numnodes)) + WIDTH + 8;
                y = (int) (WIDTH * Math.sin(6.28 * i / Graph.numnodes)) + HEIGHT;
            }
            mynodes[i] = new Actor(context, x, y, R.drawable.bdotglow_48);
            myvertex[i] = new Vertex(x, y);
        }
    }

    private Runnable r = new Runnable() {

        public void run() {
            invalidate();
        }
    };

    public static int CheckGraphIntersection() {
        intersections = 0;
        for (Edge e : Graph.totaledge) {
            e.intersection = false;
        }
        for (int i = 0; i < Graph.totaledge.size() - 1; ++i) {
            for (int j = i + 1; j < Graph.totaledge.size(); ++j) {
                Edge e1 = Graph.totaledge.get(i);
                Edge e2 = Graph.totaledge.get(j);

                if (e1.v1 == e2.v1 || e1.v1 == e2.v2 || e1.v2 == e2.v1 || e1.v2 == e2.v2) {
                    continue;
                }

                if (Graph.doIntersect(myvertex[e1.v1], myvertex[e1.v2], myvertex[e2.v1], myvertex[e2.v2])) {
                    // System.out.println("INTERSECTION BETWEEN  EDGES " + i + " AND " + j + " intersects " + intersections);
                    e1.intersection = true;
                    e2.intersection = true;
                    intersections++;
                }
            }
        }
        return (intersections > 0) ? 1 : 0;
    }

    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    protected void onDraw(Canvas c) {

        int ctime = (int) System.currentTimeMillis() / 1000;
        int diff = ctime - starttimeinsec;
        starttimeinsec = ctime;
        if(focus){
            time += diff;
        }
        int min = time / 60;
        int sec = time % 60;

        int twidth = mynodes[0].getBitmap().getWidth() / 2;
        int theight = mynodes[0].getBitmap().getHeight() / 2;

        for (int i = 0; i < Graph.numedges; ++i) {
            int v1 = Graph.totaledge.get(i).v1;
            int v2 = Graph.totaledge.get(i).v2;
            boolean intersect = Graph.totaledge.get(i).intersection;
            if (!intersect)
                c.drawLine(mynodes[v1].getX() + twidth, mynodes[v1].getY() - theight, mynodes[v2].getX() + twidth, mynodes[v2].getY() - theight, blurPaint);
            c.drawLine(mynodes[v1].getX() + twidth, mynodes[v1].getY() - theight, mynodes[v2].getX() + twidth, mynodes[v2].getY() - theight, (!intersect) ? finalPaint : testPaint);
        }

        for (int i = 0; i < Graph.numnodes; ++i) {
            // c.drawText(Integer.toString(i), mynodes[i].getX(), mynodes[i].getY(), textPaint);
            c.drawBitmap(mynodes[i].getBitmap(), mynodes[i].getX(), mynodes[i].getY() - twidth * 2, null);
        }

        c.drawText("TIME  " + Integer.toString(min / 10) + Integer.toString(min % 10) + ":" + Integer.toString(sec / 10) + Integer.toString(sec % 10), 25, 55, textPaint);
        c.drawText("CUTS  " + Integer.toString(intersections), WIDTH - 30, 55, textPaint);
        c.drawText("SCORE  " + Integer.toString(Settings.score), 2 * WIDTH - 100, 55, textPaint);

        h.postDelayed(r, FRAME_RATE);
    }
    public void stopHandler(){
        h.removeCallbacks(r);
    }
}
