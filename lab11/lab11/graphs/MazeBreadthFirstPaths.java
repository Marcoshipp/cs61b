package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q = new LinkedList<>();
        marked[s] = true;
        q.add(s);
        announce();

        while (!q.isEmpty()) {
            // remove the first item in the queue
            // go through its neigbors and add them to the queue
            int vertex = q.remove();
            if (vertex == t) {
                return;
            }
            for (int neighbor: maze.adj(vertex)) {
                if (!marked[neighbor]) {
                    edgeTo[neighbor] = vertex;
                    marked[neighbor] = true;
                    distTo[neighbor] = distTo[vertex] + 1;
                    q.add(neighbor);
                    announce();
                }
            }
        }

    }


    @Override
    public void solve() {
         bfs();
    }
}

