import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * The Rank Based selection strategy allows a more fit Trader to be more likely to be selected
 * based on its rank in the population according to its fitness value.
 */
public class RankBasedSelectionStrategy implements  SelectionStrategy {
    /**
     * Finds a Trader in the population according to the Rank Based Selection algorithm
     * @param population    A collection of traders
     * @param populationFitness A collection of the traders' fitness
     * @return A trader selected using the algorithm
     */
    @Override
    public Trader select(ArrayList<Trader> population, double[] populationFitness) {
        int size = populationFitness.length;
        Trader [] populationArr = new Trader[size];

        for(int i = 0; i < size;i++)
            populationArr[i] = population.get(i);


        //Do some sorting of the original population
        for(int i = 0; i < size-1; i++){
            for(int j = i+1; j < size; j++){
                if(populationFitness[j] < populationFitness[i]){
                    Trader tempTrader = populationArr[j];
                    double tempFitness= populationFitness[j];

                    populationFitness[j] = populationFitness[i];
                    populationFitness[i] = tempFitness;

                    populationArr[j] = populationArr[i];
                    populationArr[i] = tempTrader;
                }
            }
        }

        double totalRelativeWeights = 0.0;
        double [] relativeWeights = new double[size];
        for(int i = 0; i< size; i++){
            relativeWeights[i] = (i+1.0)/(size+1.0);
            totalRelativeWeights += relativeWeights[i];
        }


        double [] cumulativeRelativeWeights = new double[size];
        cumulativeRelativeWeights[0] = relativeWeights[0];
        for(int i = 1; i< size; i++){
            cumulativeRelativeWeights[i] = relativeWeights[i] + cumulativeRelativeWeights[i-1];
        }

        Random r = new Random();
        double pointer = r.nextDouble()*totalRelativeWeights;
        if(pointer <= cumulativeRelativeWeights[0]) return populationArr[0];
        for(int i = 0; i< size-1; i++){
            if(pointer > cumulativeRelativeWeights[i] && pointer <= cumulativeRelativeWeights[i+1])
                return populationArr[i];
        }

        return null;
    }
}
