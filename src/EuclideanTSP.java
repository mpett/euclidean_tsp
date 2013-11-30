import java.util.ArrayList;

public class EuclideanTSP {
    private Kattio io;
    private ArrayList<Pair<Double, Double>> inputNodes = new ArrayList<Pair<Double, Double>>();

    public static void main(String[] args) {
        new EuclideanTSP();
    }

    EuclideanTSP() {
        io = new Kattio(System.in, System.out);
        handleInput();
 
        /*
        inputNodes.add(new Pair(95.0129, 61.5432));
        inputNodes.add(new Pair(23.1139, 79.1937));
        inputNodes.add(new Pair(60.6843, 92.1813));
        inputNodes.add(new Pair(48.5982, 73.8207));
        inputNodes.add(new Pair(89.1299, 17.6266));
        inputNodes.add(new Pair(76.2097, 40.5706));
        inputNodes.add(new Pair(45.6468, 93.5470));
        inputNodes.add(new Pair(1.8504, 91.6904));
        inputNodes.add(new Pair(82.1407, 41.0270));
        inputNodes.add(new Pair(44.4703, 89.3650));
        */

        //printInput();
        //int[] res = greedyTour();
        int[] res = twoOpt();

        printTour(res);
        //System.out.println("Total length: " + getTotalLength(res));

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
        for(Pair<Double, Double> node : inputNodes) {
            System.out.println(node.getL() + " " + node.getR());
        }
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
        int[] tour =  greedyTour();

        double bestDist = getTotalLength(tour);
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                int oldi = tour[i];
                int oldj = tour[j];

                tour[i] = oldj;
                tour[j] = oldi;
                double newDist =  getTotalLength(tour);

                if (newDist < bestDist){
                    bestDist = newDist;
                    //System.out.println("-");
                } else { // Revert...
                    tour[i] = oldi;
                    tour[j] = oldj;


                    return tour;
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