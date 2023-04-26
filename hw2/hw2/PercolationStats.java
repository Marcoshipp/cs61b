package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final int T;
    private final double[] opens;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.opens = new double[T];
        this.T = T;
        for (int times = 0; times < T; times++) {
            Percolation pc = pf.make(N);
            while (!pc.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                pc.open(row, col);
            }
            this.opens[times] = (double) pc.numberOfOpenSites() / (N * N);
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
