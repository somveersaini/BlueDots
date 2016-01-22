package com.iblue.bluedots;

/**
 * Created by Samsaini on 12/16/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class GameSolView extends View {

    public static Vertex myvertex[];

    private Vertex totalvertex[];

    private Handler h;                        // for animation sequence timing
    public Actor mynodes[];                    // Array of nodes in the gameplay
    private int numedges;
    private int numnodes;
               // for painting the text
    private Paint finalPaint;
    private int HEIGHT = 470;
    private int WIDTH = 280;


    private final int FRAME_RATE = 30;
    private ArrayList<Edge> totaledge;

    public GameSolView(Context context, AttributeSet attrs) {
        super(context, attrs);

        HEIGHT = Solution.HEIGHT / 2 - 20;
        WIDTH = Solution.WIDTH / 2 - 40;

        System.out.println("height " + HEIGHT + " " + WIDTH);

        h = new Handler();

        totaledge = new ArrayList<Edge>();


        Shader shader = new RadialGradient(WIDTH,HEIGHT,350, Color.BLUE,Color.parseColor("#2979ff"), Shader.TileMode.CLAMP);


        finalPaint = new Paint();
        finalPaint.setAntiAlias(true);
        finalPaint.setStrokeWidth(5);
        finalPaint.setStyle(Paint.Style.STROKE);
        finalPaint.setColor(Color.BLUE);


        switch (Settings.theme){
            case Constants.MAGNETA:
                this.setBackgroundColor(Color.WHITE);
                break;

            case Constants.WHITE:
                this.setBackgroundColor(Color.WHITE);
                break;

            case Constants.BLACK:
                this.setBackgroundColor(Color.BLACK);
                break;
            case Constants.BLUE:
                this.setBackgroundColor(Color.parseColor("#2196fe"));
                break;
        }

       // Generate the graph
        if (Solution.graph.length() != 0) {
            String[] edge = Solution.graph.split(",");
            for (int i = 0; i < edge.length; ++i) {
                String[] e = edge[i].split("-");
                int ev1 = Integer.parseInt(e[0]);
                int ev2 = Integer.parseInt(e[1]);
                Edge e1 = new Edge(ev1, ev2, 0);
                System.out.println("EDGE: V1 = " + e1.v1 + "  V2 = " + e1.v2);
                totaledge.add(e1);
            }
            numedges = totaledge.size();
        }

        String[] node = Solution.nodes.split(",");
        numnodes = node.length;
        mynodes = new Actor[numnodes];
        myvertex = new Vertex[numnodes];
        totalvertex = new Vertex[numnodes];



        int x = 0;
        int y = 0;
        int maxx = 0, maxy = 0;
        double rx = 1, ry = 1;

        for (int i = 0; i < numnodes; ++i) {
            String[] e = node[i].split("-");
            int tx = Integer.parseInt(e[0]);
            int ty = Integer.parseInt(e[1]);

           // if (tx > WIDTH * 2) tx = WIDTH * 2;
           // if (ty > HEIGHT * 2) ty = HEIGHT * 2;
            x = tx;
            y = ty;

            if (x > maxx) {
                maxx = x;
            }
            if (y > maxy) {
                maxy = y;
            }
            System.out.println(x+ " " + y);
            myvertex[i] = new Vertex(x, y);
        }
        if(maxx > WIDTH * 2) {
            rx = (WIDTH*2.0  - 10) / maxx;
        }else {
            rx = 1;
        }
        if(maxy > HEIGHT*2) {
            ry = (HEIGHT*2.0 - 20)/maxy;
        }else {
            ry = 1;
        }
        for (int j = 0; j < Graph.numnodes; ++j) {
            int x1 = myvertex[j].x;
            int y1 = myvertex[j].y;
            x1 = (int) (x1*rx);
            y1 = (int) (y1*ry);
            mynodes[j] = new Actor(context, x1, y1, R.drawable.bdotglow32);
            myvertex[j] = new Vertex(x1, y1);
        }



    }

    private Runnable r = new Runnable() {

        public void run() {
            invalidate();
        }
    };

    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    protected void onDraw(Canvas c) {


        //System.out.println("EDGE: V1 = " );
        int twidth = mynodes[0].getBitmap().getWidth() / 2;
        int theight = mynodes[0].getBitmap().getHeight() / 2;

        for (int i = 0; i < numedges; ++i) {
            int v1 = totaledge.get(i).v1;
            int v2 = totaledge.get(i).v2;
            c.drawLine(mynodes[v1].getX()+twidth, mynodes[v1].getY() - theight, mynodes[v2].getX() + twidth, mynodes[v2].getY() - theight, finalPaint);
        }

        for (int i = 0; i < numnodes; ++i) {
            c.drawBitmap(mynodes[i].getBitmap(), mynodes[i].getX(), mynodes[i].getY()-twidth*2, null);
        }

        h.postDelayed(r, FRAME_RATE);
    }
}
