// Brian Becker, 999646986, brbecker@ucdavis.edu
// Daljodh Pannu, 912303549, dpannu@ucdavis.edu

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Comparator;

// A* implementation with a heuristic that works, but could 
// probably have a simpler way of doing it mathematically

public class AStarExp implements AIModule
{
    class PathPoint {
        public Point                p;    // current point
        public double               g_n;  // cost to current point
        public double               h_n;  // heuristic
        public ArrayList<Point>     pat;  // path to current point

        public PathPoint(Point p1, double g, double h, ArrayList<Point> t) {
            p = p1;
            g_n = g;
            h_n = h;
            pat = new ArrayList<Point>(t);
            pat.add(p);
        }
    }

    static class Cmp implements Comparator<PathPoint> {
        public int compare(PathPoint p1, PathPoint p2) {
            return (int)(( (p1.g_n + p1.h_n) - (p2.g_n + p2.h_n) ) * 10000);
            // want differences in ints to be retained through double conversion loss 
        }
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        Cmp c = new Cmp();
        PriorityQueue<PathPoint>    frontier    = new PriorityQueue<PathPoint>(1, c);
        HashMap<String, PathPoint>  infrontier  = new HashMap<String, PathPoint>();
        HashMap<String, Boolean>    explored    = new HashMap<String, Boolean>();


        final Point start = map.getStartPoint();
        final Point end   = map.getEndPoint();

        PathPoint first = new PathPoint(start, 0.0,
            getHeuristic(map, start, end), new ArrayList<Point>());

        frontier.add(first);
        infrontier.put(Integer.toString(start.x) + "," + Integer.toString(start.y), first);

        while (start != end) {
            if (frontier.size() == 0) {
                System.out.println("Something went wrong lol");
            }  // frontier is empty, return failure

            PathPoint next = frontier.poll();
            String nextkey = Integer.toString(next.p.x) + "," + Integer.toString(next.p.y);
            infrontier.remove(nextkey);

            if (next.p.x == end.x && next.p.y == end.y) {
                return next.pat;
            }  // found the end, return

            explored.put(nextkey, true);
            
            for (Point temp : map.getNeighbors(next.p)) {  // for each possible neighbor

                PathPoint child = new PathPoint(temp, map.getCost(next.p, temp) + next.g_n, 
                    getHeuristic(map, temp, end), next.pat);

                String key = Integer.toString(child.p.x) + "," + Integer.toString(child.p.y);
                PathPoint p = infrontier.get(key);

                if (explored.get(key) == null && p == null) {
                    frontier.add(child);
                    infrontier.put(key, child);
                } // not in explored or in the frontier
                else if (p != null && p.g_n + p.h_n > child.g_n + child.h_n) {
                    infrontier.remove(key);
                    infrontier.put(key, child);
                    
                    frontier.remove(p);
                    frontier.add(child);
                } // in the frontier, and what's in the frontier has a higher cost, so replace it
            }
        }

        return new ArrayList<Point>();
    }

    private double getHeuristic(final TerrainMap map, final Point p1, final Point p2) {
        double d =          (double)Math.max(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        double d2 =         (double)(Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y));
        double height =     map.getTile(p2) - map.getTile(p1);
        double h_n =        0.0;

        if (height >= 0) {  // going uphill
            if (d >= height) {
                h_n = (height * 2) + (d - height);
            }
            else if (d < height && d2 > height) {
                h_n = ((height % d) * 4) + ((d - (height % d)) * 2);
            }
            else if (d2 == height) {
                h_n = (height * 2);
            }
            else if (d2 < height) {
                h_n = ((height % d2) * Math.pow(2, Math.ceil(height / d2))) +
                        + ((d2 - (height % d2)) * Math.pow(2, Math.floor(height / d2)));
            }
        }
        else {  // height < 0, going downhill
            height *= -1;
            if (d >= height) {
                h_n = (height * 0.5) + (d - height);
            }
            else if (d < height) {
                h_n = ((height % d) * Math.pow(2, -1 * Math.ceil(height / d)))
                        + ((d - (height % d)) * Math.pow(2, -1 * Math.floor(height / d)));
            }
        }

        return h_n;
    }
}
