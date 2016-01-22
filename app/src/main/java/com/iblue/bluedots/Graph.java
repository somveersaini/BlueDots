package com.iblue.bluedots;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Graph {

    public static Comparator<Vertex> mycomp;
    public static ArrayList<Vertex>[] vertexlist;    // Array List of vertex for each line
    public static ArrayList<Vertex> totalvertex;    // Array List of all the vertices present in the graph
    public static ArrayList<Edge> totaledge;    // Array List of all the edges present in the graph

    public static int numlines = 7;
    public static int intersectx;
    public static int intersecty;
    public static int numnodes;
    public static int numedges;
    public static int[][] lineset;

    public static boolean onSegment(Vertex p, Vertex q, Vertex r) {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
            return true;

        return false;
    }

    /**
     * find out the orientation for Vertex q with respect to Vertex p and r
     * @param p
     * @param q
     * @param r
     * @return 0 for colinear 1 for clockwise 2 for counterclockwise
     */
    public static int orientation(Vertex p, Vertex q, Vertex r) {
        // See 10th slides from following link for derivation of the formula
        // http://www.dcs.gla.ac.uk/~pat/52233/slides/Geometry1x1.pdf
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;  // colinear

        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    /**
     * The main function for finding intersection that returns true if line segment 'p1q1' and 'p2q2' intersect.
     * @param p1  Vertex
     * @param q1
     * @param p2
     * @param q2
     * @return true if intersect otherwise false
     */
    public static boolean doIntersect(Vertex p1, Vertex q1, Vertex p2, Vertex q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        //if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        //if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        //if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        //if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    /**
     * returns a random int between (min, max)
     * @param min int
     * @param max int
     * @return a random int
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int randInt(int max) {
        Random rand = new Random();
        return rand.nextInt((max) + 1);
    }

    /**
     * To find the point of intersection of two line and update the list intersectx and intersecty
     * @param x11
     * @param y11
     * @param x12
     * @param y12
     * @param x21
     * @param y21
     * @param x22
     * @param y22
     */
    public static void getintersection(int x11, int y11, int x12, int y12, int x21, int y21, int x22, int y22) {

        //A = y2-y1
        //B = x1-x2
        //C = A*x1+B*y1
        //double det = A1*B2 - A2*B1

        int A1 = y12 - y11;
        int B1 = x11 - x12;
        int C1 = A1 * x11 + B1 * y11;

        int A2 = y22 - y21;
        int B2 = x21 - x22;
        int C2 = A2 * x21 + B2 * y21;

        float det = A1 * B2 - A2 * B1;
        if (det == 0) {
            intersectx = 5555;    // lines are parallel
            intersecty = 5000;
        } else {
            intersectx = (int) ((B2 * C1 - B1 * C2) / det);
            intersecty = (int) ((A1 * C2 - A2 * C1) / det);
            return;
        }
        return;
    }

    /**
     * for making the graph totalvertex and totaledge
     * @param checkv
     * @param edges
     */
    public static void Makegraph(int checkv, String edges) {

        totalvertex = new ArrayList<Vertex>();
        totaledge = new ArrayList<Edge>();
        numlines = randInt(7, 11);
        vertexlist = new ArrayList[numlines];

        int edgecount = 0;

        if (checkv != 0) {
            String[] edgelist = edges.split(",");
            for (String anEdge : edgelist) {
                String[] e = anEdge.split("-");
                int ev1 = Integer.parseInt(e[0]);
                int ev2 = Integer.parseInt(e[1]);
                Edge e1 = new Edge(ev1, ev2, edgecount);
                System.out.println("EDGE: V1 = " + e1.v1 + "  V2 = " + e1.v2);
                totaledge.add(e1);
                ++edgecount;
            }

            numedges = totaledge.size();
            numnodes = checkv;
            return;

        }

        //Generating the set of lines
        lineset = new int[Graph.numlines][4];
        for (int i = 0; i < Graph.numlines; ++i) {
            int t1x = randInt(480);
            int t1y = randInt(920);
            int t2x = randInt(480);
            int t2y = randInt(920);
            Log.d(" line ", t1x + " " + t1y + " " + t2x + " " + t2y);
            lineset[i][0] = t1x;
            lineset[i][1] = t1y;
            lineset[i][2] = t2x;
            lineset[i][3] = t2y;
            vertexlist[i] = new ArrayList<Vertex>();
        }

        int vertexcount = 0;

        // Getting intersection
        for (int i = 0; i < numlines; ++i) {
            for (int j = i + 1; j < numlines; ++j) {
                getintersection(lineset[i][0], lineset[i][1], lineset[i][2], lineset[i][3], lineset[j][0], lineset[j][1], lineset[j][2], lineset[j][3]);
                int tempx = intersectx;
                int tempy = intersecty;
                if (tempx < 4000 && tempx > -4000 & tempy > -4500 && tempy < 4500) {
                    Vertex t1 = new Vertex(tempx, tempy, i, j, vertexcount);
                    //< WIDTH && tempx > -1 && tempy < HEIGHT  && tempy > -1
                    vertexlist[i].add(t1);        // add the vertex in the list of both the lines set
                    vertexlist[j].add(t1);
                    totalvertex.add(t1);        // set of all vertices
                    ++vertexcount;
                }
            }
        }


        mycomp = new Comparator<Vertex>() {
            public int compare(Vertex v1, Vertex v2) {
                return v1.x - v2.x;
            }
        };

        edgecount = 0;
        for (int i = 0; i < Graph.numlines; ++i) {
            Collections.sort(vertexlist[i], mycomp);    // sort all the vertices for each line
            System.out.println(vertexlist[i].size());
            for (int j = 0; j < vertexlist[i].size() - 1; ++j) {
                Edge e1 = new Edge(vertexlist[i].get(j).id, vertexlist[i].get(j + 1).id, edgecount);
                //System.out.println("EDGE: V1 = " + e1.v1 + "  V2 = " + e1.v2);
                totaledge.add(e1);
                ++edgecount;
            }
        }

        numedges = totaledge.size();
        numnodes = totalvertex.size();
        // savedata
        String data = numnodes + " ";
 /*        for (int i = 0; i < totaledge.size(); i++) {
            Edge e = totaledge.get(i);
            data = data + e.v1 + "-" + e.v2;
            if(i < totaledge.size()-1){
                data += ",";
            }
        }
        data += " ";
        for (int i = 0; i < totalvertex.size(); i++) {
            Vertex v = totalvertex.get(i);
            data = data + v.x + "-" + v.y;
            if(i < totalvertex.size()-1){
                data += ",";
            }
        }

        data += "\n";
        System.out.println(data);
        File file;
        FileOutputStream fos = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "test2");
            fos = new FileOutputStream(file, true);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        return;
    }
}
