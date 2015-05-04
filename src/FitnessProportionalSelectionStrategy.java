import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * Fitness Proportional selection chooses a Trader in the population, making a Trader more likely to be selected
 * based on its actual fitness value in comparison with other Trader in the population.
 */
public class FitnessProportionalSelectionStrategy implements SelectionStrategy {
    /**
     * Selects a Trader from the population using the Fitness Proportional (or Roulette Wheel) algorithm.
     * @param population    A collection of traders
     * @param populationFitness A collection of fitnesses of the population
     * @return A selected Trader
     */
    @Override
    public Trader select(ArrayList<Trader> population, double[] populationFitness) {
        double totalFitness = 0.0;
        double [] cumulativeFitness = new double[populationFitness.length];
        double totalCumulativeFitness =0.0;
        for(int i = 0; i < populationFitness.length; i++){
            totalFitness+= populationFitness[i];
            cumulativeFitness[i] = totalFitness;
            totalCumulativeFitness += totalFitness;
        }

            cumulativeFitness[0] = cumulativeFitness[0]/totalCumulativeFitness;
        for(int i = 1; i < populationFitness.length; i++){
            cumulativeFitness[i] = cumulativeFitness[i-1]+cumulativeFitness[i]/totalCumulativeFitness;
        }

        Random r = new Random();
        double pointer = r.nextDouble();
        if(pointer <= cumulativeFitness[0]) return population.get(0);
        for(int i = 0; i < populationFitness.length-1; i++){
            if(pointer > cumulativeFitness[i] && pointer <= cumulativeFitness[i+1])
                return population.get(i);
        }
        return null;
    }
}
