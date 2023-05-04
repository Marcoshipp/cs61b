package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;
public class Board implements WorldState {
    private int[][] tiles;
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }
    public int size() {
        return this.tiles.length;
    }
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }
    public int hamming() {
        int x = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != toNum(i, j)) {
                    x++;
                }
            }
        }
        return x;
    }

    private int toNum(int i, int j) {
        return i * size() + j + 1;
    }

    private int[] numToPos(int x) {
        if (x == 0) {
            int[] arr = {size() - 1, size() - 1};
            return arr;
        }
        x--;
        int i = x / size();
        int j = x % size();
        int[] arr = {i, j};
        return arr;
    }


    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int[] pos = numToPos(tiles[i][j]);
                int x = pos[0];
                int y = pos[1];
                sum += (Math.abs(x - i) + Math.abs(y - j));
            }
        }
        return sum;
    }
    public int estimatedDistanceToGoal() {
        return this.manhattan();
    }
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (other.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] bd = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board b = new Board(bd);
        Solver solver = new Solver(b);
        for (WorldState ws : solver.solution()) {
            System.out.println(ws);
        }
    }
}
