import java.util.ArrayList;
import java.util.Random;

public class EuclideanTSP {
    private Kattio io;
    private ArrayList<Pair<Double, Double>> inputNodes = new ArrayList<Pair<Double, Double>>();
		private float mutationRisk = 0.1f;
		private boolean verbousePrint = false;			
		private int generations = 10;			
		private Random random;
		private long timeLimit = 1500;

    public static void main(String[] args) {
        new EuclideanTSP();
    }

    EuclideanTSP() {
				random = new Random();
				random.setSeed(999);

        io = new Kattio(System.in, System.out);
        handleInput();
        //int[] res = twoOpt();
				int[] res = evolutionaryGeedyTwoOpt();
				printTour(res);
				if (verbousePrint) {
					System.out.println("------ " + getTotalLength(res) + "------");
				}
        io.close();
    }
		
		int[] evolutionaryGeedyTwoOpt() {

		long startTime = System.currentTimeMillis();

			int[] bestPath = twoOpt();
			double bestPathLength = getTotalLength(bestPath); 
			//for (int i = 0; i < generations; i++) {
				while (System.currentTimeMillis()- startTime < timeLimit) {
					int[] currentPath = twoOpt();;
					double currentPathLength = getTotalLength(currentPath); 
					
					if (currentPathLength < bestPathLength) {
							bestPath = currentPath;
							bestPathLength = currentPathLength;
					}
			//}
			}
			return bestPath;
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
					if(!used[j]) {
						if (random.nextFloat() < mutationRisk) {
							best = j;
							continue;
						}
						if (best == -1 || dist(tour[i-1],j) < dist(tour[i-1],best)) {
							best = j;
					}
                }
            }
            tour[i] = best;
            used[best] = true;
        }
        return tour;
    }


    int[] twoOpt() {
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    double dist(int a, int b) {
        return Math.sqrt(
                Math.pow((inputNodes.get(a).getL()-inputNodes.get(b).getL()),2)+
                        Math.pow((inputNodes.get(a).getR()-inputNodes.get(b).getR()),2));
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
