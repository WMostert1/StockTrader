import java.util.ArrayList;

/**
 * Created by wernermostert on 2015/04/30.
 */
public abstract class CrossoverStrategy {
    protected SelectionStrategy selectionStrategy;
    protected double crossoverRate;
    public CrossoverStrategy(double crossoverRate,SelectionStrategy selectionStrategy){
        this.crossoverRate = crossoverRate;
        this.selectionStrategy = selectionStrategy;
    }
    public abstract ArrayList<Trader> crossover(ArrayList<Trader> population);
}
