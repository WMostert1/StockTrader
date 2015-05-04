import java.util.ArrayList;

/**
 * Created by wernermostert on 2015/04/30.
 */

/**
 * Interface for pluggable Selection Strategies
 */
public interface SelectionStrategy {
    public Trader select(ArrayList<Trader> population, double [] populationFitness);
}
