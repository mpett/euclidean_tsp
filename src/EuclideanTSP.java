/**
 * Created with IntelliJ IDEA.
 * User: martinpettersson
 * Date: 2013-11-28
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList;

public class EuclideanTSP {
    private static Kattio io = new Kattio(System.in, System.out);
    ArrayList<Pair<Double, Double>> inputNodes = new ArrayList<Pair<Double, Double>>();
    private final static long           TIME_LIMIT = 1500000000;
    static long startTime;
    private static double[][] DIST_MATRIX;
    private static int N;

    public static void main(String[] args) {
        startTime = System.nanoTime();
        new EuclideanTSP();
        io.close();
    }

    EuclideanTSP() {
        handleInput();
        DIST_MATRIX = new double[N][N];
        int[] res = greedyTour();
        // System.err.println("----------"+getTotalLength(res)+"-----------");
        res = twoPointFiveOpt(res);
        //    System.err.println("----------"+getTotalLength(res)+"-----------");
        printTour(res);
    }

    void handleInput() {
        N = io.getInt();
        for(int i = 0; i < N; i++) {
            Pair<Double, Double> node = new Pair<Double, Double>(io.getDouble(), io.getDouble());
            inputNodes.add(i,node);
        }
    }

    void printTour(int[] tour) {
        for(int i = 0; i < N; i++)
            System.out.println(tour[i]);
    }

    double getTotalLength(int[] tour) {
        double total = 0;
        for(int i = 1; i < tour.length; i++)
            total += dist(tour[i-1], tour[i]);
        total += dist(tour[tour.length-1], tour[0]);
        return total;
    }

    int[] greedyTour() {
        int[] tour = new int[N];
        boolean[] used = new boolean[N];
        tour[0] = 0;
        used[0] = true;
        int best;
        for(int i = 1; i < N; i++) {
            best = -1;
            for(int j = 0; j < N; j++) {
                if(!used[j] && (best == -1 || dist(tour[i-1],j) < dist(tour[i-1],best))) {
                    best = j;
                }
            }
            tour[i] = best;
            used[best] = true;
        }
        return tour;
    }

    double dist(int a, int b) {
        double result;
        if(DIST_MATRIX[a][b] != 0) {
            result = DIST_MATRIX[a][b];
        } else {
            result = Math.sqrt(
                    Math.pow((inputNodes.get(a).getL()-inputNodes.get(b).getL()),2)+
                            Math.pow((inputNodes.get(a).getR()-inputNodes.get(b).getR()),2));
            DIST_MATRIX[a][b] = result;

        }
        return result;
    }

    int[] twoPointFiveOpt(int[] tour) {
        int N = inputNodes.size();
        boolean found = false;
        int leftIndex, rightIndex, leftValue, rightValue;

        do {
            found = false;
            for(int i = 0; i < N; i++) {
                for(int k = i+1; k < N-2; k++) {
                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                    double oldDist = dist(tour[i == 0 ? N-1 : i-1], tour[i]) + dist(tour[k], tour[k == N-1 ? 0 : k+1]);
                    double newDist = dist(tour[i == 0 ? N-1 : i-1], tour[k]) + dist(tour[i], tour[k == N-1 ? 0 : k+1]);
                    if (newDist < oldDist) {
                        for (int index = 0; index <= (k-i)/2; index++) {
                            leftIndex = i+index;
                            rightIndex = k-index;
                            leftValue = tour[leftIndex];
                            rightValue = tour[rightIndex];
                            tour[leftIndex] = rightValue;
                            tour[rightIndex] = leftValue;
                            //moreVisData(tour);
                        }
                        found = true;
                        // break;
                    }
                }
            }

            for(int i = 0; i < N; i++) {
                for(int k = 0; k < N; k++) {
                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }

                    if(k >= i+1) {
                        //  if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                        double oldDist = dist(tour[i == 0 ? N-1 : i-1], tour[i]) + dist(tour[k], tour[k == N-1 ? 0 : k+1]);
                        double newDist = dist(tour[i == 0 ? N-1 : i-1], tour[k]) + dist(tour[i], tour[k == N-1 ? 0 : k+1]);
                        if (newDist < oldDist) {
                            for (int index = 0; index <= (k-i)/2; index++) {
                                if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                                leftIndex = i+index;
                                rightIndex = k-index;
                                leftValue = tour[leftIndex];
                                rightValue = tour[rightIndex];
                                tour[leftIndex] = rightValue;
                                tour[rightIndex] = leftValue;
                            }
                            found = true;
                            //    break;
                        }
                    }

                    int v = i;
                    int vPrev =     (i == 0) ? N-1 : i-1;
                    int vNext =     (i == N-1) ? 0 : i+1;
                    int eLeft   = k;
                    int eRight  = (k == N-1) ? 0 : k+1;
                    if (tour[v] == tour[eLeft] || tour[v] == tour[eRight])
                        continue;
                    double oldDist = dist(tour[vPrev], tour[v]) + dist(tour[v], tour[vNext]) + dist(tour[eLeft], tour[eRight]);
                    double newDist = dist(tour[vPrev], tour[vNext]) + dist(tour[eLeft], tour[v]) + dist(tour[v], tour[eRight]);
                    if (newDist < oldDist) {
                        int current = tour[v];
                        if (v < eLeft) {
                            if (eLeft == N-1) {
                                for (int index = v; index < N-1; index++) {
                                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                                    tour[index] = tour[index + 1];
                                }
                                tour[eLeft] = current;
                            } else {

                                for (int index = v; index < eRight; index++) {
                                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                                    tour[index] = tour[index + 1];
                                }
                                tour[eLeft] = current;
                            }
                            found = true;
                            // break;
                        } else if (v > eRight) {
                            if (eRight == 0) {
                                for (int index = v; index > 0; index--) {
                                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                                    tour[index] = tour[index - 1];
                                }
                                tour[eRight] = current;
                            } else {
                                for (int index = v; index > eLeft; index--) {
                                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                                    tour[index] = tour[index - 1];
                                }
                                tour[eRight] = current;
                            }
                            found = true;
                            //   break;
                        }

                    }
                }
            }
        } while (found);
        return tour;
    }

    class Pair<L,R> {
        private L l;
        private R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }
        public L getL(){ return l; }
        public R getR(){ return r; }
        public void setL(L l){ this.l = l; }
        public void setR(R r){ this.r = r; }
    }

}

