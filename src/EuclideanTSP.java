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
        printTour(greedyTour());
        //printTotalLengthTour(greedyTour());
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

    void printTotalLengthTour(int[] tour) {
        double total = 0;
        for(int i = 1; i < tour.length; i++)
            total += dist(tour[i-1], tour[i]);
        System.out.println("Total tour length: " + total);
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
        //double v1 = inputNodes.get(a).getL();
        //double v2 = inputNodes.get(b).getR();
        //double dist = v1 - v2;
        //dist = Math.abs(dist);
        //return dist;
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
