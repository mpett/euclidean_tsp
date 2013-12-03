/**
 * Created with IntelliJ IDEA.
 * User: martinpettersson
 * Date: 2013-11-28
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.PrintWriter;

public class EuclideanTSP {
    Kattio io = new Kattio(System.in, System.out);
    ArrayList<Pair<Double, Double>> inputNodes = new ArrayList<Pair<Double, Double>>();
    private static boolean visualize = false;
    private static final String FILE_NAME = "tour.vis";
    private static PrintWriter writer;
    private final static long           TIME_LIMIT = 1500000000;
    private final static int            GENERATIONS = 3;

    static long startTime;

    public static void main(String[] args) throws FileNotFoundException {


        startTime = System.nanoTime();

        writer = new PrintWriter(FILE_NAME);
        if(args.length != 0) {
            if(args[0].equals("visualize")) visualize = true;
        }
        new EuclideanTSP();
        writer.close();
    }

    EuclideanTSP() throws FileNotFoundException {

        /*
        int[] a = new int[]{1, 2, 3, 4, 5, 6};
        int[] bla = twoOptSwap(a, 2, 4);
        for (int i = 0; i < bla.length; i++) {
            System.out.println("" + bla[i]);
        }
        */



        handleInput();
        //generateVisData();

        int[] res = greedyTour();
        System.err.println("----------"+getTotalLength(res)+"-----------");
        res = twoOpt(res);
        System.err.println("----------"+getTotalLength(res)+"-----------");


        printTour(res);


    }

    void generateVisData() throws FileNotFoundException {
        writer.print("points = [");
        for(Pair<Double, Double> node : inputNodes) {
            writer.print("(" + node.getL() + ", " + node.getR() + ")");
            if(!node.equals(inputNodes.get(inputNodes.size()-1)))
                writer.print(",");
        }
        writer.println("]");
        writer.println("vis = Visualiser(points)");
    }

    void moreVisData(int[] tour) throws FileNotFoundException {
        writer.print("Visualiser.update([");
        for(int i = 0; i < tour.length; i++) {
            writer.print(tour[i]);
            if(i != tour.length -1)
                writer.print(", ");
        }
        writer.println("])");
        writer.println("sleep(secs)");
    }

    void handleInput() {
        int numberOfNodes = io.getInt();
        for(int i = 0; i < numberOfNodes; i++) {
            Pair<Double, Double> node = new Pair<Double, Double>(io.getDouble(), io.getDouble());
            inputNodes.add(i,node);
        }
    }

    void handleInputJapan() {
        int numberOfNodes = io.getInt();
        for(int i = 0; i < numberOfNodes; i++) {
            int tmp = io.getInt();
            Pair<Double, Double> node = new Pair<Double, Double>(io.getDouble(), io.getDouble());
            inputNodes.add(i,node);
        }
        System.err.println("DONE READING");
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

    int[] greedyTour() throws FileNotFoundException {
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

    int[] twoOpt(int[] tour) throws  FileNotFoundException{
        int N = inputNodes.size();
        double bestDistance = getTotalLength(tour);

        boolean found = false;
        do {
            for(int i = 0; i < N; i++) {
                for(int k = i+1; k < N-2; k++) {
                    if(System.nanoTime() - startTime > TIME_LIMIT) { return tour; }
                    //int[] newTour = twoOptSwap(tour, i, k);

                    double oldDist = dist(tour[i == 0 ? N-1 : i-1], tour[i]) + dist(tour[k], tour[k == N-1 ? 0 : k+1]);
                    double newDist = dist(tour[i == 0 ? N-1 : i-1], tour[k]) + dist(tour[i], tour[k == N-1 ? 0 : k+1]);


                    if (newDist < oldDist) {

                        for (int index = 0; index <= (k-i)/2; index++) {
                            int leftIndex = i+index;
                            int rightIndex = k-index;
                            int leftValue = tour[leftIndex];
                            int rightValue = tour[rightIndex];
                            tour[leftIndex] = rightValue;
                            tour[rightIndex] = leftValue;
                        }

                        found = true;
                        break;
                    }
                }
            }
        } while (found);

        return tour;
        }

    int[] twoOptSwap(int[] tour, int i, int k) {
        int[] newTour = new int[tour.length];
        for(int j = 0; j < tour.length; j++) {
            newTour[j] = tour[j];
        }


        for (int index = 0; index <= k-i; index++) {
            int leftIndex = i+index;
            int rightIndex = k-index;
            int leftValue = newTour[leftIndex];
            int rightValue = newTour[rightIndex];

            newTour[leftIndex] = rightValue;
            newTour[rightIndex] = leftValue;
        }

        return newTour;
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

