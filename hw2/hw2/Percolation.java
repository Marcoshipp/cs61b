package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSites;
    private final int size;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;
    private final boolean[][] sites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.openSites = 0;
        this.size = N;
        this.uf = new WeightedQuickUnionUF(N * N + 2);
        this.uf2 = new WeightedQuickUnionUF(N * N + 1);
        this.sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.sites[i][j] = false;
            }
        }
    }

    private void checkIndexError(int row, int col) {
        if (row < 0 || row > this.sites.length || col < 0 || col > this.sites.length) {
            throw new IndexOutOfBoundsException("row: " + row + ", col: " + col);
        }
    }

    private int rcToIndex(int row, int col) {
        return row * this.size + col + 1;
    }

    /**
     * Open the row and the column
     * @param row the row
     * @param col the column
     */
    public void open(int row, int col) {
        checkIndexError(row, col);
        if (isOpen(row, col)) {
            return;
        }
        this.sites[row][col] = true;
        this.openSites += 1;
        if (row == 0) {
            this.uf.union(rcToIndex(row, col), 0);
            this.uf2.union(rcToIndex(row, col), 0);
            if (isOpen(row + 1, col)) {
                this.uf.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                this.uf2.union(rcToIndex(row, col), rcToIndex(row + 1, col));
            }
            // if in top left
            if (col == 0) {
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
            }
            // if in top right
            else if (col == this.size - 1) {
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
            // else
            else {
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
        }
        else if (row == this.size - 1) {
            this.uf.union(rcToIndex(row, col), this.size * this.size + 1);
            if (isOpen(row - 1, col)) {
                this.uf.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                this.uf2.union(rcToIndex(row, col), rcToIndex(row - 1, col));
            }
            // if in bottom left
            if (col == 0) {
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
            }
            // if in bottom right
            else if (col == this.size - 1) {
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
            // else
            else {
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
        }
        else {
            if (col == 0) {
                if (isOpen(row + 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                }
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
            }
            else if (col == this.size - 1) {
                if (isOpen(row + 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                }
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
            else {
                if (isOpen(row + 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row - 1, col));
                }
                if (isOpen(row, col + 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col + 1));
                }
                if (isOpen(row, col - 1)) {
                    this.uf.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                    this.uf2.union(rcToIndex(row, col), rcToIndex(row, col - 1));
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        checkIndexError(row, col);
        return this.sites[row][col];
    }

    public boolean isFull(int row, int col) {
        checkIndexError(row, col);
        int index = rcToIndex(row, col);
        return this.uf2.connected(0, index);
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return this.uf.connected(0, this.size * this.size + 1);
    }
}
