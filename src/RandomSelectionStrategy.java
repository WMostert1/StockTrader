import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wernermostert on 2015/04/30.
 */
public class RandomSelectionStrategy implements SelectionStrategy {
    @Override
    public Trader select(ArrayList<Trader> population, double [] populationFitness) {
        return population.get((new Random()).nextInt(population.size()));
    }
}
