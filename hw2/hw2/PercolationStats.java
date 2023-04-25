package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import java.util.Random;

public class PercolationStats {
    private final int size;
    private final int T;
    private final int[] opens;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = N;
        this.opens = new int[T];
        this.T = T;
        for (int times = 0; times < T; times++) {
            Percolation pc = new Percolation(size);
            while (!pc.percolates()) {
                int row = StdRandom.uniform(0, size);
                int col = StdRandom.uniform(0, size);
                pc.open(row, col);
            }
            this.opens[times] = pc.numberOfOpenSites();
        }
    }
    public double mean() {
        return StdStats.mean(opens);
    }
    public double stddev() {
        return StdStats.stddev(opens);
    }
    public double confidenceLow() {
        return this.mean() - (1.96 * this.stddev()) / Math.sqrt(this.T);
    }
    public double confidenceHigh() {
        return this.mean() + (1.96 * this.stddev()) / Math.sqrt(this.T);
    }
}
