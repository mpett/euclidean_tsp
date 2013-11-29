/**
 * Created with IntelliJ IDEA.
 * User: martinpettersson
 * Date: 2013-11-28
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
import java.util.ArrayList;
public class EuclideanTSP {
    Kattio io;
    ArrayList<Pair<Double, Double>> inputNodes = new ArrayList<Pair<Double, Double>>();

    public static void main(String[] args) {
        new EuclideanTSP();
    }

    EuclideanTSP() {
        io = new Kattio(System.in, System.out);
        handleInput();
        //printInput();
        int[] res = greedyTour();
        int[] res2 = twoOpt();
        printTour(res);
        System.out.println("Total length: " + getTotalLength(res));
        System.out.println("---------------");
        printTour(res2);
        System.out.println("Total length: " + getTotalLength(res2));
        io.close();
    }

    void handleInput() {
        int numberOfNodes = io.getInt();
        for(int i = 0; i < numberOfNodes; i++) {
            Pair<Double, Double> node = new Pair<Double, Double>(io.getDouble(), io.getDouble());
            inputNodes.add(i,node);
        }
    }

    void printTour(int[] tour) {
        for(int i = 0; i < tour.length; i++)
            System.out.println(tour[i]);
    }

    double getTotalLength(int[] tour) {
        double total = 0;
        for(int i = 1; i < tour.length; i++)
            total += dist(tour[i-1], tour[i]);
        total += dist(tour[tour.length-1], tour[0]);
        return total;
    }

    void printInput() {
        for(Pair<Double, Double> node : inputNodes)
            System.out.println(node.getL() + " " + node.getR());
    }

    int[] greedyTour() {
        int N = inputNodes.size();
        int[] tour = new int[N];
        boolean[] used = new boolean[N];
        tour[0] = 0;
        used[0] = true;
        for(int i = 1; i < N; i++) {
            int best = -1;
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
        return Math.sqrt(
                Math.pow((inputNodes.get(a).getL()-inputNodes.get(b).getL()),2)+
                Math.pow((inputNodes.get(a).getR()-inputNodes.get(b).getR()),2));
    }

    int[] twoOptSwap(int[] route, int i, int j) {
        int N = inputNodes.size();
        int[] newRoute = new int[N];
        for (int ii = 0; ii < i; ii++)
            newRoute[ii] = route[ii];
        for (int ii = i; ii <= j; ii++)
            newRoute[j-(ii-i)] = route[ii];
        for (int ii = j+1; ii < N; ii++)
            newRoute[ii] = route[ii];
        return newRoute;
    }

    int[] twoOpt() {
        /*
        repeat until no improvement is made {
            start_again:
            best_distance = calculateTotalDistance(existing_route)
            for (i = 0; i < number of nodes eligible to be swapped - 1; i++) {
                for (k = i + 1; k < number of nodes eligible to be swapped; k++) {
                    new_route = 2optSwap(existing_route, i, k)
                    new_distance = calculateTotalDistance(new_route)
                    if (new_distance < best_distance) {
                        existing_route = new_route
                        goto start_again
                    }
                }
            }
        }
        */
        int N = inputNodes.size();
        int[] tour =  new int[N];
        for (int i = 0; i < N; i++) tour[i] = i;
        double bestDist = getTotalLength(tour);
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                int[] newRoute = twoOptSwap(tour, i,j);
                double newDist =  getTotalLength(newRoute);
                if (newDist < bestDist){
                    bestDist = newDist;
                    tour = newRoute;
                }
            }
        }
        return tour;
    }
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
