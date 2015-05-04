import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * The Random Selection Strategy simply selectes any random Trader from the population
 */
public class RandomSelectionStrategy implements SelectionStrategy {
    /**
     * Returns a Trader determined by Random selection
     * @param population            A collection of Traders
     * @param populationFitness     A collection of the Trader's fitness
     * @return A randomly selected Trader in the population
     */
    @Override
    public Trader select(ArrayList<Trader> population, double [] populationFitness) {
        return population.get((new Random()).nextInt(population.size()));
    }
}
