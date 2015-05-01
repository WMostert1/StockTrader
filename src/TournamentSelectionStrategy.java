import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */
public class TournamentSelectionStrategy implements SelectionStrategy {
    private int tSize;
    public TournamentSelectionStrategy(int tournament_size){
        tSize = tournament_size;
    }
    @Override
    public Trader select(ArrayList<Trader> population, double[] populationFitness){
        Random r = new Random();
        int size = population.size();

        double [] groupFitness = new double[tSize];
        Trader [] group = new Trader[tSize];

        for(int g = 0; g < tSize; g++){
            int index = r.nextInt(size);
            groupFitness[g] = populationFitness[index];
            group[g] = population.get(index);
        }

        int maxIndex = 0;
        for(int i = 1; i < tSize; i++){
            if(groupFitness[i] > groupFitness[maxIndex])
                maxIndex = i;
        }
        return group[maxIndex];

    }
}
