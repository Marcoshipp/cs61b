package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    private SearchNode n;
    private class SearchNode implements Comparable {
        public int movesFromOrigin;
        public WorldState ws;
        public ArrayList<WorldState> worldStates;
        public WorldState last;

        public SearchNode (WorldState initial, WorldState last, int x, ArrayList<WorldState> worldStates) {
            this.last = last;
            movesFromOrigin = x;
            ws = initial;
            this.worldStates = worldStates;
        }


        @Override
        public int compareTo(Object o) {
            SearchNode other = (SearchNode) o;
            return Integer.compare(this.movesFromOrigin + this.ws.estimatedDistanceToGoal(), other.movesFromOrigin + other.ws.estimatedDistanceToGoal());
        }
    }
    public Solver(WorldState initial) {
        SearchNode node = new SearchNode(initial, null, 0, new ArrayList<WorldState>());
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(node);
        while (!pq.isEmpty()) {
            SearchNode bms = pq.delMin();
            if (bms.ws.isGoal()) {
                bms.worldStates.add(bms.ws);
                this.n = bms;
                break;
            }
            bms.worldStates.add(bms.ws);
            for (WorldState neighbor : bms.ws.neighbors()) {
                // Check if the neighbor is already in the current path
                if (neighbor.equals(bms.last)) {
                    continue;
                }
                SearchNode newNode = new SearchNode(neighbor, bms.ws, bms.movesFromOrigin + 1, new ArrayList<>(bms.worldStates));
                pq.insert(newNode);
            }
        }
    }
    public int moves() {
        return this.n.movesFromOrigin;
    }
    public Iterable<WorldState> solution() {
        return this.n.worldStates;
    }
}
